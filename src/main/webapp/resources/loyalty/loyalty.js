/**
 * Created by Cyndee Shank on 3/23/16.
 */
CustomerServiceApp
    .factory("LoyaltyService", function ($resource) {
        return $resource("loyalty/byNumber", null, {
            // lookup loyalty
            search: {
                url: "loyalty/search",
                method: "POST",
                isArray: true
            },
            // retrieve details
            getDetail: {
                url: "loyalty/detail",
                method: "GET"
            },
            // retrieve details with number
            getDetailWithNumber: {
                url: "loyalty/detail/:number",
                method: "GET"
            },
            // save loyalty customer entry
            saveDetail: {
                url: "loyalty/save",
                method: "POST"
            },
            // deactivate loyalty card
            deactivate: {
                url: "loyalty/deactivate",
                method: "POST"
            },
            // get order(s) for loyalty number
            getOrders: {
                url: "orders/byCard/:type/:number",
                method: "POST",
                isArray: true
            }
        });
    })
    .factory("dtConfig", function (DTOptionsBuilder) {
        return {
            options: DTOptionsBuilder.newOptions().withBootstrap()
        }
    })
    .controller("LoyaltyController",
        function ($scope, $routeParams, $location, $rootScope, $resource, $route, breadcrumbs, LoyaltyService) {
            $scope.breadcrumbs = breadcrumbs;
            $scope.errors = null;
            log("in LoyaltyController....")
            $scope.loyalty = {};
            //$scope.loyaltyList = {};
            $scope.searchType = 'number';
            $scope.loyaltyForm = {};
            $rootScope.loyaltyMatch = false;

            $rootScope.detail = {};
            $scope.addLoyaltyCard = function () {
                $location.path("/loyalty/new");
            }

            $scope.setCurrentTab = function (type) {
                $scope.searchType = type;
            }


            // lookup loyalty list
            $scope.lookupLoyalty = function () {
                console.log("in lookupLoyalty");
                $scope.loyalty.searchType = $scope.searchType;
                $rootScope.loyaltyList = LoyaltyService.search({}, $scope.loyalty,
                    function (response) {
                        // if none are returned, display 'not found' message
                        if ($rootScope.loyaltyList.length == 0) {
                            $rootScope.displayWarningMsg("Unable to find a match for entered search criteria");
                        }
                        // if only 1 is returned, then display detail page, else display list page
                        else if ($scope.loyaltyList.length == 1) {
                            angular.copy($rootScope.loyaltyList[0], $rootScope.detail);
                            $rootScope.loyaltyMatch = true;
                            $location.path("/loyalty/detail/" + $scope.loyaltyList[0].loyaltyNumber);
                        }
                        else {

                            $location.path("/loyalty/list");
                        }
                    })
            };
        })
    .controller("LoyaltyDetailController",
        function ($scope, $routeParams, $location, $rootScope, $resource, $route, breadcrumbs, LoyaltyService) {
            log("in LoyaltyDetailController....")
            $scope.breadcrumbs = breadcrumbs;
            log("$scope.breadcrumbs");
            log($scope.breadcrumbs);
            $scope.currentDetail = {};
            $scope.title = "Loyalty Customer Details";

            // set the view mode to true
            $scope.viewMode = true;
            // set the new mode to false
            $scope.newLoyalty = false;

            // populate the loyalty detail object
            if ($rootScope.loyaltyMatch) {
                $scope.detail = LoyaltyService.getDetail();
            }
            else {
                $scope.detail = LoyaltyService.getDetailWithNumber({number: $routeParams.number}, function () {
                    if ($scope.detail == null) {

                    }
                });
                //$scope.detail = $resource("loyalty/detail/" + $routeParams.number);
                //$scope.hlc = HotListService.getHL({id: $routeParams.id}, function () {
            }

            // Date Picker Options for Expiration Date
            var today = new Date();
            var expDate = new Date();
            expDate.setFullYear(today.getFullYear() + 1);
            $scope.expDateOptions = {
                minDate: new Date(),
                initDate: expDate,
                maxDate: expDate
            }
            $scope.detail.expTimestamp = expDate;
            $scope.open = function () {
                $scope.popup.opened = true;
            };
            $scope.popup = {
                opened: false
            };
            $scope.format = 'MM-dd-yyyy';

            // Date Picker Options for Birth Date
            $scope.bdFormat = 'MM-dd';
            $scope.bdDateOptions = {
                minDate: new Date()
            }
            $scope.detail.bdTimestamp = new Date();
            $scope.open2 = function () {
                $scope.popup2.opened = true;
            };
            $scope.popup2 = {
                opened: false
            };

            $scope.setEditMode = function () {
                $scope.viewMode = false;
                $scope.currentDetail = angular.copy($scope.detail);
                //angular.copy($scope.detail, $scope.currentDetail);
            }
            $scope.cancelEdit = function () {
                $scope.viewMode = true;
                // copy original to current to remove any changes that might have been made
                $scope.errors = null;
                $scope.detail = angular.copy($scope.currentDetail);
            }
            $scope.cancelUpdate = function () {
                $location.path("/loyalty/list");
            }
            $scope.isFormDirty = function () {
                return $scope.loyaltyDetailForm.$dirty;
            }

            $scope.deactivate = function () {
                var response = LoyaltyService.deactivate({number: $scope.detail.loyaltyNumber}, null, function () {
                    log(response);
                    if (response.status == "success") {
                        $rootScope.displaySuccessMsg("Loyalty Card Successfully Deactivated");
                        $location.path("/loyalty")
                    }
                })
            }

            // lookup if there is a corresponding EMS Order
            $scope.findOrder = function () {
                $rootScope.orderList = LoyaltyService.getOrders({type: 'loyalty', number: $scope.detail.loyaltyNumber}, null,
                    function (response) {
                        // if none are returned, display 'not found' message
                        if ($rootScope.orderList.length == 0) {
                            $rootScope.displayWarningMsg("Unable to find an order for the associated loyalty number");
                        }
                        // if only 1 is returned, then display detail page, else display list page
                        else if ($rootScope.orderList.length == 1) {
                            angular.copy($rootScope.orderList[0], $rootScope.orderDetail);
                            $location.path("/order/" + $rootScope.orderList[0].orderNo);
                        }
                        else {
                            $rootScope.orderListTitle = "Orders Matching Loyalty Number: " + $scope.detail.loyaltyNumber;
                            $location.path("/orders/list");
                        }
                    })
            }

            $scope.saveForm = function () {
                $scope.errors = null;
                // Save Loyalty Detail
                log($scope.detail);
                LoyaltyService.saveDetail({}, $scope.detail,
                    function (data) {
                        $rootScope.displaySuccessMsg("Loyalty Customer Information Successfully Updated");
                        // where should this go after successful update???
                        $location.path("/loyalty");
                    }, function (res) {
                        var err = res.data;
                        $scope.errors = err;
                    });
            };
        })
    .controller("LoyaltyNewController",
        function ($scope, $routeParams, $location, breadcrumbs, $rootScope, $resource, $route, LoyaltyService) {
            $scope.breadcrumbs = breadcrumbs;
            $scope.errors = null;
            log("in LoyaltyNewController....")
            $scope.detail = {};
            $scope.title = "New Loyalty Customer";

            // set the view mode to true
            $scope.viewMode = false;
            // set the new mode to false
            $scope.newLoyalty = true;
            // default the store number to 980
            $scope.detail.signUpStore = 980;

            // Date Picker Options for Expiration Date
            $scope.open = function () {
                $scope.popup.opened = true;
            };
            $scope.format = 'MM-dd-yyyy';
            var today = new Date();
            var expDate = new Date();
            expDate.setFullYear(today.getFullYear() + 1);
            $scope.expDateOptions = {
                minDate: new Date(),
                initDate: expDate,
                maxDate: expDate
            }
            $scope.detail.expTimestamp = expDate;

            $scope.popup = {
                opened: false
            };

            // Date Picker Options for Birth Date
            $scope.bdFormat = 'MM-dd';
            $scope.bdDateOptions = {
                minDate: new Date(),
            }
            $scope.detail.bdTimestamp = new Date();
            $scope.open2 = function () {
                $scope.popup2.opened = true;
            };
            $scope.popup2 = {
                opened: false
            };

            $scope.cancelNew = function () {
                $location.path("/loyalty");
            }
            $scope.isFormDirty = function () {
                return $scope.loyaltyDetailForm.$dirty;
            }

            $scope.saveForm = function () {
                $scope.errors = null;
                // Save Loyalty Detail
                log($scope.detail);
                LoyaltyService.saveDetail({}, $scope.detail,
                    function (data) {
                        $rootScope.displaySuccessMsg("Loyalty Card Successfully Added");
                        log($rootScope.successMessage);
                        // where should this go after successful update???
                        $location.path("/loyalty");
                    }, function (res) {
                        var err = res.data;
                        $scope.errors = err;
                    });
            };
        })
