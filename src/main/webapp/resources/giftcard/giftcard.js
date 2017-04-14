/**
 * Created by cshank on 3/23/16.
 */
CustomerServiceApp
    .factory("GiftCardService", function ($resource) {
        return $resource("giftcard/shredGC", null, {
            // Trip
            shred: {
                url: "giftcard/shredGC",
                method: "POST"
            },
            history: {
                url: "giftcard/history",
                method: "POST"
            },
            activate: {
                url: "giftcard/activate",
                method: "POST"
            }
        });
    })
    .factory("dtConfig", function (DTOptionsBuilder) {
        return {
            options: DTOptionsBuilder.newOptions().withBootstrap()
        }
    })
    .controller("GiftCardController",
        function ($scope, $routeParams, $resource, $route, $location, $rootScope, breadcrumbs, GiftCardService) {
            $scope.breadcrumbs = breadcrumbs;
            $scope.errors = null;
            log("in GiftCardController....")

            $scope.gcReason = "";
            $scope.gc = {};

            // Get/set search type
            $scope.setType = function (type) {
                $location.search("type", type).replace();
                //log("$scope.type", $scope.type);
                log(type);
            };
            $scope.type = $routeParams.type ? $routeParams.type : $scope.setType("balance");

            $scope.getHistory = function () {
                GiftCardService.history({}, $scope.giftcard,
                    function (data) {
                        $rootScope.historyList = data.historyList;
                        $location.path("/giftcard/history");
                    }, function (res) {
                        var err = res.data;
                    });
            }

            $scope.activateCard = function () {
                GiftCardService.activate({}, $scope.giftcard,
                    function (data) {
                        log("data.status:", data.status);
                        log("data.success:", data.success);
                        if (data.success == true) {
                            $rootScope.displaySuccessMsg("Gift Card Successfully Activated for " + data.balance);
                        }
                        else {
                            switch (data.status) {
                                case "ALREADY_ACTIVATED":
                                    $rootScope.displayWarningMsg("Gift Card Already Activated, Current Balance: " + data.balance);
                                    break;
                                case "GC_NOT_FOUND":
                                    $rootScope.displayErrorMsg("Gift Card Number Not Found in EMS");
                                    break;
                                case "INVALID_PIN":
                                    $rootScope.displayErrorMsg("Invalid Pin for the Gift Card");
                                    break;
                                case "INACTIVE_ACCOUNT":
                                    $rootScope.displayErrorMsg("Inactive Account");
                                    break;
                            }
                        }
                    }, function (res) {
                        var err = res.data;
                    });
                log("$rootScope.errorMessage");
                log($rootScope.errorMessage);
            }

            $scope.submitGC = function () {
                log("$scope.type");
                log($scope.type);
                log("giftcard number: " + $scope.giftcard.number);
                log("giftcard pin: " + $scope.giftcard.pin);
                if ($scope.type == 'balance') {
                    $scope.getHistory();
                } else if ($scope.type == 'activate') {
                    $scope.activateCard();
                } else if ($scope.type == 'shred') {
                    $scope.shredGC();
                }
            }

            $scope.shredGC = function () {
                console.log("giftcard number: " + $scope.giftcard.number);
                var gcDto = GiftCardService.shred({gcNumber: $scope.giftcard.number}, null, function () {
                    // display success or error message
                    console.log(gcDto);
                    console.log("status", gcDto.status);
                    console.log("reason", gcDto.reason);
                    if (gcDto.status == "FAIL") {
                        switch (gcDto.reason) {
                            case "HAS_ACTIVITY":
                                $rootScope.displayWarningMsg("The card could not be shredded for the following reason: There is no activity on the card");
                                break;
                            case "NOT_IN_SYSTEM":
                                $rootScope.displayErrorMsg("The card could not be shredded for the following reason: The card is not in the system");
                                break;
                            case "ALREADY_ACTIVATED":
                                $rootScope.displayWarningMsg("The card could not be shredded for the following reason:  The card has already been activated");
                                break;
                            case "NOT_ACCESSED":
                                $rootScope.displayWarningMsg("The card could not be shredded for the following reason:  The card has not been accessed");
                                break;
                        }
                    }
                    else {
                        $rootScope.displaySuccessMsg("The Gift Card was successfully shredded.");
                    }
                })
            }

        })
    .controller("GiftCardHistoryController",
        function ($scope, $rootScope, breadcrumbs, GiftCardService) {
            $scope.breadcrumbs = breadcrumbs;
            log("$rootScope.historyList");
            log($rootScope.historyList);
        })
