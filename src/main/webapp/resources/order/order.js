/**
 * This is the js file for handling the order status/processing functionality
 * Created by Cyndee Shank on 3/23/16.
 */
CustomerServiceApp
    .factory('StatesLookup', function ($resource) {
        var statesLookupService = $resource("lookup/states");
        var statesLookup = statesLookupService.query(
            function (response) {
                statesLookup = response;
            });
        return statesLookup;
    })
    .factory("OrderService", function ($resource) {
        return $resource("orders/new", null, {
            // Single Order
            getOrder: {
                url: "orders/:id",
                method: "GET"
            },
            getOrderStatus: {
                url: "orders/status/:id",
                method: "GET"
            },
            searchOrders: {
                url: "orders/search",
                method: "POST",
                isArray: true
            },
            lookup: {
                url: "orders/lookup",
                method: "POST"
            },
            resubmit: {
                url: "orders/resubmit",
                method: "POST"
            }
        });
    })
    /**
     * Controller for the Order Search Page
     */
    .controller("OrderController",
        function ($scope, $routeParams, breadcrumbs, $resource, $location, $rootScope, OrderService, StatesLookup) {
            $scope.breadcrumbs = breadcrumbs;
            $scope.errors = null;
            $scope.states = StatesLookup;
            $scope.searchType = 'number';
            $scope.order = {};
            $rootScope.orderList = {};
            $scope.placeholder = "Web Order # / Invoice # / Order # / Gift Card # / Loyalty #";
            //$scope.maxLength = 16;
            $scope.selectedData= {
                'Type':{
                    'MaxLength':16,
                    'Placeholder':"Web Order # / Invoice # / Order # / Gift Card # / Loyalty #"
                }
            }

            $scope.orderListTitle = "Current New Orders";

            log("in OrderController....")

            // Get/set duration
            $scope.setDuration = function (duration) {
                $location.search("duration", duration).replace();
            };
            $scope.duration = $routeParams.duration ? $routeParams.duration : $scope.setDuration("3m");
            
            // Get/set search type
            $scope.setType = function (type) {
                $location.search("type", type).replace();
            };
            $scope.type = $routeParams.type ? $routeParams.type : $scope.setType("billing");
            
            // Get/set search match
            $scope.setMatch = function (match) {
                $location.search("match", match).replace();
            };
            $scope.match = $routeParams.match ? $routeParams.match : $scope.setMatch("exact");

            // Get/set search order number type
            $scope.setNumber = function (number) {
                // if selection is giftcard, set maxlength of field = 16
                // if selection is loyalty, set maxlength of field = 12
                // if selection is invoice, set maxlength of field = 7
                if (number == 'giftcard') {
                    $scope.maxLength = 16;
                    $scope.placeholder = "Gift Card Number";
                }
                else if (number == 'loyalty') {
                    $scope.maxLength = 12;
                    $scope.placeholder = "Loyalty Card Number";
                }
                else if (number == 'invoice') {
                    $scope.maxLength = 7;
                    $scope.placeholder = "Invoice Number";
                }
                else if (number == 'web') {
                    $scope.maxLength = 8;
                    $scope.placeholder = "Web Order Number";
                }
                else if (number == 'order') {
                    $scope.maxLength = 8;
                    $scope.placeholder = "EMS Order Number";
                }
                $location.search("number", number).replace();
                log("number:", number);
                log("maxLength:", $scope.maxLength);
                log("placeholder:", $scope.placeholder);
                //$scope.apply();
            };
            $scope.number = $routeParams.number ? $routeParams.number : $scope.setNumber("web");

            $scope.isFormDirty = function () {
                return $scope.orderSearchForm.$dirty;
            }

            $scope.setCurrentTab = function (type) {
                $scope.searchType = type;
                log("$scope.searchType", $scope.searchType);
            }
            // search by specific fields
            $scope.searchOrders = function() {
                console.log("in searchOrders");
                $scope.order.searchType = $scope.searchType;
                $scope.order.numberType = $scope.number;
                $scope.order.addressType = $scope.type;
                $scope.order.addressMatchType = 'all';
                $scope.order.matchType = $scope.match;
                $rootScope.orderList = OrderService.searchOrders({}, $scope.order,
                    function (response) {
                        // if none are returned, display 'not found' message
                        if ($rootScope.orderList.length == 0) {
                            $rootScope.displayWarningMsg("Unable to find a match for entered search criteria");
                        }
                        // if only 1 is returned, then display detail page, else display list page
                        else if ($rootScope.orderList.length == 1) {
                            angular.copy($rootScope.orderList[0], $rootScope.orderDetail);
                            //$rootScope.loyaltyMatch = true;
                            $location.path("/order/" + $rootScope.orderList[0].orderNo);
                        }
                        else {
                            $location.path("/orders/list");
                        }
                    })
            };

            // search by order status
            $scope.orderSearch = function (searchType) {
                $scope.orderSearchType = searchType;
                log("setting scope.orderSearch to: ", searchType);
                var OrderListService;

                switch (searchType) {
                    case 'new':
                        OrderListService = $resource("orders/new");
                        $rootScope.orderListTitle = "Current New Orders";
                        break;
                    case 'dist':
                        OrderListService = $resource("orders/dist");
                        $rootScope.orderListTitle = "Current Orders Sent to Distribution";
                        break;
                    case 'ship':
                        OrderListService = $resource("orders/ship");
                        $rootScope.orderListTitle = "Current Shipped Orders";
                        break;
                    case 'auto':
                        OrderListService = $resource("orders/auto");
                        $rootScope.orderListTitle = "Current Auto-On-Hold Orders";
                        break;
                    case 'manual':
                        OrderListService = $resource("orders/manual");
                        $rootScope.orderListTitle = "Current Manual-On-Hold Orders";
                        break;
                    case 'cancel':
                        OrderListService = $resource("orders/cancel");
                        $rootScope.orderListTitle = "Cancelled Orders";
                        break;
                }
                $rootScope.orderList = OrderListService.query();
                $rootScope.orderList.$promise.then(function() {
                    log("$rootScope.orderList.length")
                    log($rootScope.orderList.length)
                    if ($rootScope.orderList.length > 0) {
                        $location.path("/orders/list")
                    }
                    else {
                        // display error message
                        $rootScope.displayErrorMsg("No orders found matching selected search criteria");
                    }
                })
                // save current data in case of cancel
                //$scope.hlc.$promise.then(function () {
                  //  currentHlc = angular.copy($scope.hlc);
                //});
            }
        })

    /**
     * Controller for the Order Listing Page
     */
    .controller("OrderListController",
        function ($scope, $rootScope, $routeParams, breadcrumbs, $resource, $route, OrderService, DTOptionsBuilder, DTColumnDefBuilder) {
            $scope.breadcrumbs = breadcrumbs;
            $scope.errors = null;

            log("in OrderListController....")
            $scope.dt = {
                options: DTOptionsBuilder.newOptions()
                    .withOption("order", [[0, "desc"]])
                    .withOption("displayLength", 25),
                cols: [
                    // order no
                    DTColumnDefBuilder.newColumnDef(0).withOption('sWidth', '30px'),
                    // web order no
                    DTColumnDefBuilder.newColumnDef(1).withOption('sWidth', '30px'),
                    // billing first/last
                    DTColumnDefBuilder.newColumnDef(2).withOption('sWidth', '95px'),
                    DTColumnDefBuilder.newColumnDef(2).notSortable(),
                    // shipping first/last
                    DTColumnDefBuilder.newColumnDef(3).withOption('sWidth', '95px'),
                    DTColumnDefBuilder.newColumnDef(3).notSortable(),
                    // amount
                    DTColumnDefBuilder.newColumnDef(4).withOption('sWidth', '45px'),
                    DTColumnDefBuilder.newColumnDef(4).notSortable(),
                    // ship type
                    DTColumnDefBuilder.newColumnDef(5).withOption('sWidth', '15px'),
                    DTColumnDefBuilder.newColumnDef(5).notSortable(),
                    // order date
                    DTColumnDefBuilder.newColumnDef(6).withOption('sWidth', '45px'),
                    DTColumnDefBuilder.newColumnDef(6).notSortable(),
                    // shipped date
                    DTColumnDefBuilder.newColumnDef(7).withOption('sWidth', '45px'),
                    DTColumnDefBuilder.newColumnDef(7).notSortable(),
                    // status
                    DTColumnDefBuilder.newColumnDef(8).withOption('sWidth', '35px'),
                    // invoice no
                    DTColumnDefBuilder.newColumnDef(9).withOption('sWidth', '15px'),
                    DTColumnDefBuilder.newColumnDef(9).notSortable()
                ]
            }

            $scope.setOrderSearch = function (searchType) {
                $scope.orderSearchType = searchType;
                log("setting scope.orderSearchType to: ", searchType);
                var OrderListService;
                switch (searchType) {
                    case 'new':
                        OrderListService = $resource("orders/new");
                        $rootScope.orderListTitle = "Current New Orders";
                        break;
                    case 'dist':
                        OrderListService = $resource("orders/dist");
                        $rootScope.orderListTitle = "Current Orders Sent to Distribution";
                        break;
                    case 'ship':
                        OrderListService = $resource("orders/ship");
                        $rootScope.orderListTitle = "Current Shipped Orders";
                        break;
                    case 'auto':
                        OrderListService = $resource("orders/auto");
                        $rootScope.orderListTitle = "Current Auto-On-Hold Orders";
                        break;
                    case 'manual':
                        OrderListService = $resource("orders/manual");
                        $rootScope.orderListTitle = "Current Manual-On-Hold Orders";
                        break;
                    case 'cancel':
                        OrderListService = $resource("orders/cancel");
                        $rootScope.orderListTitle = "Cancelled Orders";
                        break;
                }
                $rootScope.orderList = OrderListService.query();
                $rootScope.orderList.$promise.then(function() {
                    log("$rootScope.orderList.length")
                    log($rootScope.orderList.length)
                    if ($rootScope.orderList.length == 0) {
                        $rootScope.displayErrorMsg("No orders found matching selected search criteria");
                    }
                })
            }

        })

    /**
     * Controller for the Order Lookup by Invoice Page
     */
    .controller("OrderLookupController",
        function ($scope, $routeParams, breadcrumbs, $resource, $route, OrderService) {
            $scope.breadcrumbs = breadcrumbs;

            $scope.status = {
                isLookupOpen: false
            }
            $scope.lookupOrder = function () {
                console.log("invoice number :" + $scope.invoice.number);
                var orderDto = OrderService.lookup({invoiceNo: $scope.invoice.number}, null, function () {
                    $scope.orderNumber = orderDto.orderNo;
                    $scope.status.isLookupOpen = true;
                    $scope.isLookupOpen = true;
                    log("orderNumber ", $scope.orderNumber);
                })
            }

            $scope.resubmitOrder = function () {
                log("invoice number :" + $scope.invoice.number);
                log("order number :" + $scope.orderNumber);
                var statusDto = OrderService.resubmit({orderNo: $scope.orderNumber}, null, function () {
                    $scope.code = statusDto.responseCode;
                    $scope.message = statusDto.responseMessage;
                    log("code ", $scope.code);
                    log("message ", $scope.message);
                })
            }
        })

    /**
     * Controller for the Order Search by Card Page
     */
    .controller("OrderSearchController",
        function ($scope, $routeParams, breadcrumbs, $resource, $route, OrderService) {
            $scope.breadcrumbs = breadcrumbs;
        })

    /**
     * Controller for the Order Detail Page
     */
    .controller("OrderDetailController",
        function ($scope, $routeParams, breadcrumbs, $resource, $route, OrderService, StatesLookup) {
            $scope.breadcrumbs = breadcrumbs;
            $scope.errors = null;
            $scope.oneAtATime = true;
            $scope.states = StatesLookup;
            log("in OrderDetailController....")

            $scope.showOrderError = false;
            $scope.showOrderSuccess = false;
            $scope.orderReason = "";
            $scope.orderDetail = {};
            $scope.orderStatusList = {};
            $scope.detail = {};

            // get the order details
            $scope.orderDetail = OrderService.getOrder({id: $routeParams.id},
                function (detail) {
                    $scope.orderDetail = detail;
                    log("$scope.orderDetail");
                    log($scope.orderDetail);
                });

            // make a local copy of the orderDetail object
            $scope.orderDetail.$promise.then(function () {
                $scope.detail = angular.copy($scope.orderDetail);
            })

            var OrderStatusService = $resource("orders/status/" + $routeParams.id);
            $scope.orderStatusList = OrderStatusService.query();
            log("$scope.orderStatusList");
            log($scope.orderStatusList);

            // get the order payment
            var OrderPaymentService = $resource("orders/payment/" + $routeParams.id);
            $scope.orderPaymentList = OrderPaymentService.query();
            log("$scope.orderPaymentList");
            log($scope.orderPaymentList);

            $scope.status = {
                isBillingOpen: true,
                isPaymentOpen: false,
                isDetailsOpen: false,
                isItemDetailOpen: false,
                isStatusOpen: false
            }

            $scope.showTracking = function(number) {
                var str = 'http://www.fedex.com/Tracking?action=track&amp;language=english&amp;cntry_code=us&amp;mps=y&amp;ascend_header=1&amp;tracknumbers=';
                var uri = str + number;
                log (uri);
                //var uri = 'http://www.fedex.com/Tracking?action=track&amp;language=english&amp;cntry_code=us&amp;mps=y&amp;ascend_header=1&amp;tracknumbers=' + number;
                window.open(uri);
            }

            // function used to format the billing and shipping addresses
            $scope.formatAddress = function (type) {
                var item = "";
                if (type == 'billing') {
                    if ($scope.orderDetail.billingAddress2 != null && $scope.orderDetail.billingAddress2.length > 1) {
                        item = $scope.orderDetail.billingAddress + '  ' + $scope.orderDetail.billingAddress2;
                    }
                    else {
                        item = $scope.orderDetail.billingAddress;
                    }
                }
                else if (type == 'shipping') {
                    if ($scope.orderDetail.shippingAddress2 != null && $scope.orderDetail.shippingAddress2.length > 1) {
                        item = $scope.orderDetail.shippingAddress + '  ' + $scope.orderDetail.shippingAddress2;
                    }
                    else {
                        item = $scope.orderDetail.shippingAddress;
                    }
                }
                return item;
            }

            $scope.saveBillingAddress = function() {
                log("in saveBillingAddress");
                log("$scope.billAddressForm");
                log($scope.billAddressForm);
                log("billingZip:", $scope.orderDetail.billingZip);
                log("billingZip:", $scope.detail.billingZip)
            }
            $scope.saveBillingInfo = function() {
                log("in saveBillingInfo");
                log("$scope.orderDetail");
                log($scope.orderDetail);
                log("$scope.detail");
                log($scope.detail);
                log("billingZip:", $scope.orderDetail.billingZip);
                log("billingZip:", $scope.detail.billingZip)

            }
            /**
             $scope.resubmitOrder = function () {
                console.log("order number :" + $scope.order.number);
                var gcDto = GiftCardService.shred({gcNumber: $scope.gc.number}, null, function () {
                    // display success or error message 
                    console.log(gcDto);
                    console.log("status", gcDto.status);
                    console.log("reason", gcDto.reason);
                    if (gcDto.status == "FAIL") {
                        $scope.showOrderError = true;
                        $scope.showOrderSuccess = false;
                        switch (gcDto.reason) {
                            case "HAS_ACTIVITY":
                                $scope.orderReason = "The card has activity";
                                break;
                            case "NOT_IN_SYSTEM":
                                $scope.orderReason = "The card is not in the system";
                                break;
                            case "ALREADY_ACTIVATED":
                                $scope.orderReason = "The card has already been activated";
                                break;
                            case "NOT_ACCESSED":
                                $scope.orderReason = "The card has not been accessed";
                                break;
                        }
                    }
                    else {
                        $scope.showOrderError = false;
                        $scope.showOrderSuccess = true;
                    }
                })
            }
             **/

        })
