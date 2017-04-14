/**
 * Created by cshank on 3/23/16.
 */
CustomerServiceApp
    .factory('DatatypesLookup', function ($resource) {
        datatypeLookupService = $resource("reviewcustomer/datatypes");
        var datatypeLookup = datatypeLookupService.query(
            function (response) {
                datatypeLookup = response;
            });
        return datatypeLookup;
    })
    .factory("ReviewCustomerService", function ($resource) {
        return $resource("review/", null, {
            // save review customer entry
            saveRC: {
                url: "reviewcustomer/save",
                method: "POST"
            },
            getGrossDemandFile: {
                url: "reports/grossDemand/export",
                method: "GET"
            }
        });
    })
    .factory("dtConfig", function (DTOptionsBuilder) {
        return {
            options: DTOptionsBuilder.newOptions().withBootstrap()
        }
    })
    .controller("ReviewCustomerController",
        function ($scope, $routeParams, $resource, $location, $route, breadcrumbs, DTOptionsBuilder, DTColumnDefBuilder) {
            $scope.breadcrumbs = breadcrumbs;
            $scope.errors = null;
            log("in ReviewCustomerController....")

            $scope.dt = {
                options: DTOptionsBuilder.newOptions()
                    .withOption("order", [[2, "desc"]])
                    .withOption("displayLength", 50)
                    .withBootstrap(),
                cols: [
                    // Match Value
                    DTColumnDefBuilder.newColumnDef(0).withOption('sWidth', '20px'),
                    // Fraud Type
                    DTColumnDefBuilder.newColumnDef(1).withOption('sWidth', '20px'),
                    // Date Created
                    DTColumnDefBuilder.newColumnDef(2).withOption('sWidth', '20px'),
                    // Reason
                    DTColumnDefBuilder.newColumnDef(3).withOption('sWidth', '20px'),
                ]
            };


            var FraudService = $resource("reviewcustomer/list");
            $scope.reviewFraudList = FraudService.query();

            $scope.addReviewCustomer = function(){
                $location.path("/review/add");
            }
        })

    .controller("ReviewCustomerDetailController",
        function ($scope, $routeParams, $resource, breadcrumbs, $location, $route, $rootScope, $timeout, ReviewCustomerService) {
            $scope.breadcrumbs = breadcrumbs;
            $scope.errors = null;
            $scope.detail = {};
            log("in ReviewCustomerDetailController....")
            $scope.showEmail = false;

            $scope.cancelNew = function() {
                $location.path("/review");
            }

            $scope.isFormDirty = function () {
                return $scope.reviewDetailForm.$dirty;
            }
            $scope.clearFields = function() {
                log("in clearFields")
                $scope.detail.address= '';
                $scope.detail.zip= '';
                $scope.detail.email= '';
                $scope.detail.reason= '';
            }

            $scope.saveForm = function () {
                $scope.errors = null;
                // Save Review Customer
                log($scope.detail);
                ReviewCustomerService.saveRC({}, $scope.detail,
                    function (data) {
                        if (data.status == 1) {
                            $rootScope.displaySuccessMsg("Review Customer Successfully Added");
                            $location.path("/review");
                        }
                        else {
                            $rootScope.displayErrorMsg("Review Customer Already Exists");
                        }
                    }, function (res) {
                        var err = res.data;
                        $scope.errors = err;
                    });
            };

        })
