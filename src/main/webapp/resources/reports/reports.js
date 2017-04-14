/**
 * Created by cshank on 3/23/16.
 */
CustomerServiceApp
    .factory("ReportsService", function ($resource) {
        return $resource("reports/", null, {
            // Net Sales Report
            getNetSales: {
                url: "reports/netsales",
                method: "POST"
            },
            getGrossDemandFile: {
                url: "reports/grossDemand/export",
                method: "GET"
            },
            getGrossDemand: {
                url: "reports/grossDemand",
                method: "POST",
                isArray: true
            },
            getCancellations: {
                url: "reports/cancels",
                method: "POST",
                isArray: true
            },
            getShippingInfo: {
                url: "reports/shipping",
                method: "POST",
                isArray: true
            },
            getReturns: {
                url: "reports/returns",
                method: "POST",
                isArray: true
            },
            getRma: {
                url: "reports/rma",
                method: "POST",
                isArray: true
            },
            getHold: {
                url: "reports/hold",
                method: "POST",
                isArray: true
            },
            getOrderBatch: {
                url: "reports/orderbatch",
                method: "GET",
                isArray: true
            },
            getStyles: {
                url: "reports/styles",
                method: "GET",
                isArray: true
            },
            getInventoryForStyle: {
                url: "reports/inventory",
                method: "POST",
                isArray: true
            }
        });
    })
    .factory("DatePickerService", function($rootScope) {
        return {

        }
    })
    .factory("dtConfig", function (DTOptionsBuilder) {
        return {
            options: DTOptionsBuilder.newOptions().withBootstrap()
        }
    })
    .controller("ReportsController",
        function ($scope, $rootScope, breadcrumbs, $uibModal, $location, $route) {
            $scope.breadcrumbs = breadcrumbs;
            $scope.errors = null;
            log("in ReportsController....")

            // Setup date picker
            $scope.dOptions = {
                minDate: moment(),
                useCurrent: false,
                format: 'MM/DD/YYYY'
            };
            /**
             * check the $rootScope.useDateDefault value...
             * if true, then use default start and end date values
             * if false, then use the current start and end date values
             */
            log('useDateDefault: ', $rootScope.useDateDefault);
            //log('dateObj: ', $rootScope.dateObj);
            $scope.startDate = new Date();
            // set end date to today
            $scope.endDate = new Date();
            if ($rootScope.useDateDefault == true) {
                // set start date based on report type
                var start = new Date();
                $scope.startDate.setDate(start.getDate() - $rootScope.dateDefault);
            }
            else {
                $scope.startDate = $rootScope.dateObj.startDate;
                $scope.endDate = $rootScope.dateObj.endDate;
            }

            // date picker functions
            $scope.open1 = function () {
                $scope.popup1.opened = true;
            };
            $scope.open2 = function () {
                $scope.popup2.opened = true;
            };
            $scope.setDate = function (year, month, day) {
                $scope.dt = new Date(year, month, day);
            };
            $scope.format = 'dd-MMMM-yyyy';

            $scope.popup1 = {
                opened: false
            };
            $scope.popup2 = {
                opened: false
            };

            $scope.generateReport = function () {

                $rootScope.modalInstance.close();
                log($scope.startDate);
                log($scope.endDate);
                $rootScope.dateObj.startDate = $scope.startDate;
                $rootScope.dateObj.endDate = $scope.endDate;
                // set the dates for the report title
                $rootScope.startDateTitle = $scope.startDate;
                $rootScope.endDateTitle = $scope.endDate;
                log("reportType:", $rootScope.reportType)

                // generate report based on type
                switch ($rootScope.reportType) {
                    case 'demand':
                        $location.path("/reports/grossdemand");
                        break;
                    case 'cancel':
                        $location.path("/reports/cancels");
                        break;
                    case 'return':
                        $location.path("/reports/returns");
                        break;
                    case 'rma':
                        $location.path("/reports/rma");
                        break;
                    case 'hold':
                        $location.path("/reports/hold");
                        break;
                    case 'netsales':
                        $location.path("/reports/netsales");
                        break;
                    case 'shipping':
                        if ($rootScope.useDateDefault == true) {
                            $location.path("/reports/shipping");
                        }
                        else {
                            $location.path("/reports/shipping/update");
                        }
                        break;
                }
            }
        })

    .controller("ReportsGrossDemandController",
        function ($scope, $routeParams, $resource, $route, $location, DTOptionsBuilder, $rootScope,
                  DTColumnDefBuilder, ReportsService, breadcrumbs) {
            $scope.breadcrumbs = breadcrumbs;
            $scope.errors = null;
            log("in ReportsGrossDemandController....")
            $scope.dt = {
                options: DTOptionsBuilder.newOptions()
                    .withOption("order", [[0, "desc"]])
                    .withOption("displayLength", 50)
                    .withBootstrap(),
                cols: [
                    // Date
                    DTColumnDefBuilder.newColumnDef(0).withOption('sWidth', '30px'),
                    // Number of Orders
                    DTColumnDefBuilder.newColumnDef(1).withOption('sWidth', '20px'),
                    // Merchandise Total
                    DTColumnDefBuilder.newColumnDef(2).withOption('sWidth', '30px'),
                    // Discount Total
                    DTColumnDefBuilder.newColumnDef(3).withOption('sWidth', '30px'),
                    // ADS Total
                    DTColumnDefBuilder.newColumnDef(4).withOption('sWidth', '30px'),
                    // Shipping Total
                    DTColumnDefBuilder.newColumnDef(5).withOption('sWidth', '30px'),
                    // Tax Total
                    DTColumnDefBuilder.newColumnDef(6).withOption('sWidth', '15px'),
                    DTColumnDefBuilder.newColumnDef(6).notSortable(),
                    // Total Units
                    DTColumnDefBuilder.newColumnDef(7).withOption('sWidth', '15px'),
                    DTColumnDefBuilder.newColumnDef(7).notSortable()
                ]
            }

            $scope.reportGrossDemandList = ReportsService.getGrossDemand({}, $rootScope.dateObj,
                function (response) {
                    if ($scope.reportGrossDemandList.length <= 0) {
                        $rootScope.displayErrorMsg("Unable to retrieve the Gross Demand Report");
                    }
                })


            // function to export to Excel
            $scope.export = function () {
                log("calling createExportFile");
                $scope.createExportFile();
            };

            $scope.createExportFile = function () {
                ReportsService.getGrossDemandFile({},
                    function () {
                        log("after calling createExportFile");
                    })
            }
        })

    .controller("ReportsRmaController",
        function ($scope, $routeParams, $resource, $route, $location, DTOptionsBuilder, $rootScope,
                  DTColumnDefBuilder, ReportsService, breadcrumbs) {
            $scope.breadcrumbs = breadcrumbs;
            $scope.errors = null;
            log("in ReportsRmaController....")
            $scope.dt = {
                options: DTOptionsBuilder.newOptions()
                    .withOption("order", [[0, "desc"]])
                    .withOption("displayLength", 50)
                    .withBootstrap(),
                cols: [
                    // RMA Date
                    DTColumnDefBuilder.newColumnDef(0).withOption('sWidth', '20px'),
                    // Order Date
                    DTColumnDefBuilder.newColumnDef(1).withOption('sWidth', '20px'),
                    // Order number
                    DTColumnDefBuilder.newColumnDef(3).withOption('sWidth', '20px'),
                    // Name
                    DTColumnDefBuilder.newColumnDef(2).withOption('sWidth', '60px'),
                    // RMA number
                    DTColumnDefBuilder.newColumnDef(4).withOption('sWidth', '30px'),
                    // Merchandise Total
                    DTColumnDefBuilder.newColumnDef(5).withOption('sWidth', '30px'),
                    // Status
                    DTColumnDefBuilder.newColumnDef(6).withOption('sWidth', '25px'),
                    // Payment Type
                    DTColumnDefBuilder.newColumnDef(7).withOption('sWidth', '25px')
                ]
            }

            $scope.reportRmaList = ReportsService.getRma({}, $rootScope.dateObj,
                function (response) {
                    if ($scope.reportRmaList.length <= 0) {
                        $rootScope.displayErrorMsg("Unable to retrieve the RMA Report");
                    }
                })
        })

    .controller("ReportsHoldController",
        function ($scope, $routeParams, $resource, $route, $location, DTOptionsBuilder, $rootScope,
                  DTColumnDefBuilder, ReportsService, breadcrumbs) {
            $scope.breadcrumbs = breadcrumbs;
            $scope.errors = null;
            log("in ReportsHoldController....")
            $scope.dt = {
                options: DTOptionsBuilder.newOptions()
                    .withOption("order", [[0, "desc"]])
                    .withOption("displayLength", 50)
                    .withBootstrap(),
                cols: [
                    // ems order no
                    DTColumnDefBuilder.newColumnDef(0).withOption('sWidth', '15px'),
                    // web order no
                    DTColumnDefBuilder.newColumnDef(1).withOption('sWidth', '15px'),
                    // merch total
                    DTColumnDefBuilder.newColumnDef(2).withOption('sWidth', '15px'),
                    // date on hold
                    DTColumnDefBuilder.newColumnDef(3).withOption('sWidth', '15px'),
                    // date off hold
                    DTColumnDefBuilder.newColumnDef(4).withOption('sWidth', '15px'),
                    // on hold reason
                    DTColumnDefBuilder.newColumnDef(5).withOption('sWidth', '250px'),
                    // off hold reason
                    DTColumnDefBuilder.newColumnDef(6).withOption('sWidth', '250px'),
                    // manual hold
                    DTColumnDefBuilder.newColumnDef(7).withOption('sWidth', '15px')
                ]
            }

            $scope.reportHoldList = ReportsService.getHold({}, $rootScope.dateObj,
                function (response) {
                    if ($scope.reportHoldList.length <= 0) {
                        $rootScope.displayErrorMsg("Unable to retrieve the Released from Hold Report");
                    }
                })
        })

    .controller("ReportsShippingController",
        function ($scope, $routeParams, $resource, $route, $location, DTOptionsBuilder, $rootScope,
                  DTColumnDefBuilder, ReportsService, breadcrumbs) {
            $scope.breadcrumbs = breadcrumbs;
            $scope.errors = null;
            log("in ReportsShippingController....");
            $scope.dt = {
                options: DTOptionsBuilder.newOptions()
                    .withOption("order", [[0, "desc"]])
                    .withOption("displayLength", 50)
                    .withBootstrap(),
                cols: [
                    // order no
                    DTColumnDefBuilder.newColumnDef(0).withOption('sWidth', '40px'),
                    // ship information
                    DTColumnDefBuilder.newColumnDef(1).withOption('sWidth', '180px'),
                    // ship amount
                    DTColumnDefBuilder.newColumnDef(2).withOption('sWidth', '80px'),
                    // discount ship amount
                    DTColumnDefBuilder.newColumnDef(3).withOption('sWidth', '80px'),
                    // ship tracking no
                    DTColumnDefBuilder.newColumnDef(4).withOption('sWidth', '120px'),
                    // ship carrier
                    DTColumnDefBuilder.newColumnDef(5).withOption('sWidth', '50px'),
                    // date shipped
                    DTColumnDefBuilder.newColumnDef(6).withOption('sWidth', '120px')
                ]
            }

            // retrieve Shipping Report
            $scope.reportShippingList = ReportsService.getShippingInfo({}, $scope.dateObj,
                function (response) {
                    if ($scope.reportShippingList.length <= 0) {
                        $rootScope.displayErrorMsg("Unable to retrieve the Shipping Report");
                    }
                })

            $scope.rerunReport = function() {
                $rootScope.displayDatePicker('shipping', false);
            }

            /** use for formatting html and displaying with ng-bind-html
            $scope.formatSplitRule = function (t) {
                var ruleList = [];
                if (t.tripSplitRule != null) {
                    var splitString = t.tripSplitRule.split(" ");
                    angular.forEach(splitString, function (y, k) {
                        ruleList[k] = "<label>" + y + "</label>" + ' - ' + TooltipService.getTip(y);
                    })
                }
                return ruleList;
            };
             **/
            // function used to format the shipping name, address, etc
            $scope.formatShippingInfo = function (type, entry) {
                var item = "";
                if (type == 'name') {
                    if (entry.shippingMiddleI != null && entry.shippingMiddleI.length > 1) {
                        item = entry.shippingFirstName + ' ' + entry.shippingMiddleI + ' ' + entry.shippingLastName;
                    }
                    else {
                        item = entry.shippingFirstName + ' ' + entry.shippingLastName;
                    }
                }
                else if (type == 'address') {
                    if (entry.shippingAddress2 != null && entry.shippingAddress2.length > 1) {
                        item = entry.shippingAddress + '  ' + entry.shippingAddress2;
                    }
                    else {
                        item = entry.shippingAddress;
                    }
                }
                return item;
            }

        })

    .controller("ReportsCancelController",
        function ($scope, $routeParams, $resource, $route, $location, DTOptionsBuilder, $rootScope,
                  DTColumnDefBuilder, ReportsService, breadcrumbs) {
            $scope.breadcrumbs = breadcrumbs;
            $scope.errors = null;
            log("in ReportsCancelController....")
            $scope.dt = {
                options: DTOptionsBuilder.newOptions()
                    .withOption("order", [[0, "desc"]])
                    .withOption("displayLength", 50)
                    .withBootstrap(),
                cols: [
                    // Reason
                    DTColumnDefBuilder.newColumnDef(0).withOption('sWidth', '50px'),
                    // Orders
                    DTColumnDefBuilder.newColumnDef(1).withOption('sWidth', '20px'),
                    // Units
                    DTColumnDefBuilder.newColumnDef(2).withOption('sWidth', '10px'),
                    // Value
                    DTColumnDefBuilder.newColumnDef(4).withOption('sWidth', '90px')
                ]
            }

            // retrieve Cancellations Report
            $scope.reportCancelList = ReportsService.getCancellations({}, $scope.dateObj,
                function (response) {
                    if ($scope.reportCancelList.length <= 0) {
                        $rootScope.displayErrorMsg("Unable to retrieve the Cancellations Report");
                    }
                })
        })

    .controller("ReportsReturnsController",
        function ($scope, $routeParams, $resource, $route, $location, DTOptionsBuilder, $rootScope,
                  DTColumnDefBuilder, ReportsService, breadcrumbs) {
            $scope.breadcrumbs = breadcrumbs;
            $scope.errors = null;
            log("in ReportsReturnsController....")
            $scope.dt = {
                options: DTOptionsBuilder.newOptions()
                    .withOption("order", [[0, "desc"]])
                    .withOption("displayLength", 50)
                    .withBootstrap(),
                cols: [
                    // Style
                    DTColumnDefBuilder.newColumnDef(0).withOption('sWidth', '10px'),
                    // Color
                    DTColumnDefBuilder.newColumnDef(1).withOption('sWidth', '90px'),
                    // Quantity
                    DTColumnDefBuilder.newColumnDef(2).withOption('sWidth', '10px'),
                    // Description
                    DTColumnDefBuilder.newColumnDef(3).withOption('sWidth', '10px'),
                    // Return Reason
                    DTColumnDefBuilder.newColumnDef(4).withOption('sWidth', '50px')
                ]
            }

            // retrieve Returns Report
            $scope.reportReturnsList = ReportsService.getReturns({}, $rootScope.dateObj,
                function (response) {
                    if ($scope.reportReturnsList.length <= 0) {
                        $rootScope.displayErrorMsg("Unable to find any returns for the requested date range");
                    }
                })
        })

    .controller("ReportsTransactionsController",
        function ($scope, breadcrumbs, $routeParams, $resource, $timeout, $route, DTOptionsBuilder, DTColumnDefBuilder) {
            $scope.breadcrumbs = breadcrumbs;
            $scope.errors = null;
            log("in ReportsTransactionsController....")

            $scope.dt = {
                options: DTOptionsBuilder.newOptions()
                    .withOption("order", [[1, "desc"]])
                    .withOption("displayLength", 50)
                    .withBootstrap(),
                cols: [
                    // Transaction Number
                    DTColumnDefBuilder.newColumnDef(0).withOption('sWidth', '20px'),
                    // Transaction Date
                    DTColumnDefBuilder.newColumnDef(1).withOption('sWidth', '20px'),
                    // EMS Order Number
                    DTColumnDefBuilder.newColumnDef(2).withOption('sWidth', '20px'),
                    // Transaction Type
                    DTColumnDefBuilder.newColumnDef(3).withOption('sWidth', '20px'),
                    // Transaction Status
                    DTColumnDefBuilder.newColumnDef(4).withOption('sWidth', '20px'),
                    // Transaction Amount
                    DTColumnDefBuilder.newColumnDef(5).withOption('sWidth', '20px'),
                    // Account Number
                    DTColumnDefBuilder.newColumnDef(6).withOption('sWidth', '20px')
                ]
            };

            $scope.dt2 = {
                options: DTOptionsBuilder.newOptions()
                    .withOption("order", [[1, "desc"]])
                    .withOption("displayLength", 50)
                    .withBootstrap(),
                cols: [
                    // Transaction Number
                    DTColumnDefBuilder.newColumnDef(0).withOption('sWidth', '20px'),
                    // Transaction Date
                    DTColumnDefBuilder.newColumnDef(1).withOption('sWidth', '20px'),
                    // EMS Order Number
                    DTColumnDefBuilder.newColumnDef(2).withOption('sWidth', '20px'),
                    // Transaction Type
                    DTColumnDefBuilder.newColumnDef(3).withOption('sWidth', '20px'),
                    // Transaction Status
                    DTColumnDefBuilder.newColumnDef(4).withOption('sWidth', '20px'),
                    // Transaction Amount
                    DTColumnDefBuilder.newColumnDef(5).withOption('sWidth', '20px'),
                    // Account Number
                    DTColumnDefBuilder.newColumnDef(6).withOption('sWidth', '20px')
                ]
            };

            var ReportTransactionChargeService = $resource("reports/transactions/cf");
            $scope.reportTransChargeList = ReportTransactionChargeService.query();

            var ReportTransactionRefundService = $resource("reports/transactions/rf");
            $scope.reportTransRefundList = ReportTransactionRefundService.query();

            $scope.hideAccount = function(accountNum) {

            }
        })

    .controller("ReportsAllowancesController",
        function ($scope, breadcrumbs, $routeParams, $resource, $timeout, $route, DTOptionsBuilder, DTColumnDefBuilder) {
            $scope.breadcrumbs = breadcrumbs;
            $scope.errors = null;
            log("in ReportsAllowancesController....")

            $scope.dt = {
                options: DTOptionsBuilder.newOptions()
                    .withOption("order", [[1, "desc"]])
                    .withOption("displayLength", 50)
                    .withBootstrap(),
                cols: [
                    // Transaction Number
                    DTColumnDefBuilder.newColumnDef(0).withOption('sWidth', '20px'),
                    // Date
                    DTColumnDefBuilder.newColumnDef(1).withOption('sWidth', '20px'),
                    // Order Number
                    DTColumnDefBuilder.newColumnDef(2).withOption('sWidth', '20px'),
                    // Amount
                    DTColumnDefBuilder.newColumnDef(3).withOption('sWidth', '20px'),
                    // Refund Type
                    DTColumnDefBuilder.newColumnDef(4).withOption('sWidth', '20px'),
                    // Comments
                    DTColumnDefBuilder.newColumnDef(5).withOption('sWidth', '190px')
                ]
            };


            var ReportAllowanceService = $resource("reports/allowances");
            $scope.reportAllowancesList = ReportAllowanceService.query();
            //$scope.reportAllowancesList.$promise.then(function () { });
            /**
             $scope.allowanceList = ReportAllowanceService.query();
             $scope.allowanceList.$promise.then(function () {
                $scope.reportAllowancesList = angular.copy($scope.allowanceList);
            });

             $scope.hlc.$promise.then(function () {
                currentHlc = angular.copy($scope.hlc);
            })
             **/
        })

    .controller("ReportsNetSalesController",
        function ($scope, $routeParams, breadcrumbs, $resource, ReportsService) {
            $scope.breadcrumbs = breadcrumbs;
            $scope.errors = null;
            log("in ReportsNetSalesController....")


            $scope.netSales = ReportsService.getNetSales({}, $scope.dateObj,
                function (response) {
                    if ($scope.netSales.length <= 0) {
                        $rootScope.displayErrorMsg("Unable to retrieve the Net Sales Report");
                    }
                })
        })

    .controller("ReportsOrderBatchController",
        function ($scope, $routeParams, breadcrumbs, $resource, ReportsService, DTOptionsBuilder, DTColumnDefBuilder) {
            $scope.breadcrumbs = breadcrumbs;
            $scope.errors = null;
            log("in ReportsOrderBatchController....")
            $scope.dt = {
                options: DTOptionsBuilder.newOptions()
                    .withOption("order", [[0, "desc"]])
                    .withOption("displayLength", 50)
                    .withBootstrap(),
                cols: [
                    // date created
                    DTColumnDefBuilder.newColumnDef(0).withOption('sWidth', '50px'),
                    // # orders
                    DTColumnDefBuilder.newColumnDef(1).withOption('sWidth', '20px'),
                    // # orders not yet processed
                    DTColumnDefBuilder.newColumnDef(2).withOption('sWidth', '20px'),
                    // # next day orders
                    DTColumnDefBuilder.newColumnDef(3).withOption('sWidth', '20px'),
                    // # second day orders
                    DTColumnDefBuilder.newColumnDef(4).withOption('sWidth', '20px'),
                    // # ground orders
                    DTColumnDefBuilder.newColumnDef(5).withOption('sWidth', '20px'),
                    // filename
                    DTColumnDefBuilder.newColumnDef(6).withOption('sWidth', '60px'),
                    // date sent to NFI
                    DTColumnDefBuilder.newColumnDef(7).withOption('sWidth', '50px')
                ]
            };

            $scope.reportOrderBatch = ReportsService.getOrderBatch({},
                function (orderBatchList) {
                    $scope.reportOrderBatch = orderBatchList;
                });

        })

    .controller("ReportsInventoryController",
        function ($scope, $routeParams, $location, $rootScope, $resource, $route, breadcrumbs, ReportsService) {
            $scope.breadcrumbs = breadcrumbs;
            $scope.errors = null;
            log("in InventoryController....")
            $scope.searchType = 'style';
            $scope.inventory = {};

            $scope.setCurrentTab = function (type) {
                $scope.searchType = type;
            }

            $scope.status = {
                isInventoryOpen: false
            }
            // lookup inventory items by style
            $scope.lookupInventory = function () {
                $scope.reportInvList = ReportsService.getInventoryForStyle({style: $scope.inventory.style}, null, function () {
                    $scope.status.isInventoryOpen = true;
                    $scope.isInventoryOpen = true;
                });
            };
        })

    .controller("ReportsStylesController",
        function ($scope, $routeParams, breadcrumbs, $resource, ReportsService, DTOptionsBuilder, DTColumnDefBuilder) {
            $scope.breadcrumbs = breadcrumbs;
            $scope.errors = null;
            log("in ReportsStylesController....")
            $scope.dt = {
                options: DTOptionsBuilder.newOptions()
                    .withOption("order", [[0, "desc"]])
                    .withOption("displayLength", 50)
                    .withBootstrap(),
                cols: [
                    // style
                    DTColumnDefBuilder.newColumnDef(0).withOption('sWidth', '20px'),
                    // description
                    DTColumnDefBuilder.newColumnDef(1).withOption('sWidth', '50px'),
                    // department
                    DTColumnDefBuilder.newColumnDef(2).withOption('sWidth', '30px'),
                    // details
                    DTColumnDefBuilder.newColumnDef(3).withOption('sWidth', '370px')
                ]
            };

            $scope.dt2 = {
                options: DTOptionsBuilder.newOptions()
                    .withOption("paging", false)
                    .withOption("ordering", false)
                    .withOption("searching", false)
                    .withOption("info", false)
                    .withBootstrap(),
                cols: [
                    // sku
                    DTColumnDefBuilder.newColumnDef(0).withOption('sWidth', '20px'),
                    // color
                    DTColumnDefBuilder.newColumnDef(1).withOption('sWidth', '30px'),
                    // size
                    DTColumnDefBuilder.newColumnDef(2).withOption('sWidth', '20px'),
                    // last allocated
                    DTColumnDefBuilder.newColumnDef(3).withOption('sWidth', '80px'),
                    // last received
                    DTColumnDefBuilder.newColumnDef(4).withOption('sWidth', '80px')
                ]
            };

            $scope.reportStyles = ReportsService.getStyles({},
                function (stylesList) {
                    $scope.reportStyles = stylesList;
                });

        })
