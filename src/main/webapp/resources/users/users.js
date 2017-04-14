/**
 * Created by cshank on 3/3/16.
 */
CustomerServiceApp
    .factory('UsersDepartmentLookup', function ($resource) {
        var userDeptLookupService = $resource("user/departments");
        var userDeptLookup = userDeptLookupService.query(
            function (response) {
                userDeptLookup = response;
            });
        return userDeptLookup;
    })
    .factory('SizesLookup', function ($resource) {
        var sizeLookupService = $resource("lookup/sizes");
        var sizeLookup = sizeLookupService.query(
            function (response) {
                sizeLookup = response;
            });
        return sizeLookup;
    })
    .factory('QuantityLookup', function ($resource) {
        var qtyLookupService = $resource("lookup/quantity");
        var qtyLookup = qtyLookupService.query(
            function (response) {
                qtyLookup = response;
            });
        return qtyLookup;
    })
    .factory("UserService", function ($resource) {
        return $resource("rest/user", null, {
            // User
            getUser: {
                url: "user/:agentId"
            },
            saveUser: {
                url: "user/:agentId?save",
                method: "POST"
            },
            // save tshirt entry
            saveTShirt: {
                url: "user/saveTShirtOrder",
                method: "POST"
            },
        });
    })
    .controller("UserLoginController",
        function ($rootScope, $scope, breadcrumbs, $resource, $route) {
            $scope.breadcrumbs = breadcrumbs;
            $scope.errors = null;
            $scope.login = {};
            $scope.userLogin = function () {
                console.log("email :" + $scope.login.email);
                console.log("pw: " + $scope.login.pw);
                // if successful login, then set global variable for logged in
                $rootScope.loggedInUser = true;
            }
        })

    .controller("UserController",
        function ($scope, $routeParams, $resource, $route, UserService, breadcrumbs, DTOptionsBuilder, DTColumnDefBuilder) {
            $scope.breadcrumbs = breadcrumbs;
            $scope.errors = null;
            log("in UserController....")
            $scope.dt = {
                options: DTOptionsBuilder.newOptions()
                    .withOption("order", [[0, "asc"]])
                    .withOption("displayLength", 25),
                cols: [
                    DTColumnDefBuilder.newColumnDef(0).withOption('sWidth', '80px'), // name
                    DTColumnDefBuilder.newColumnDef(1).withOption('sWidth', '95px'), // department
                    DTColumnDefBuilder.newColumnDef(2).withOption('sWidth', '45px'), // login
                    DTColumnDefBuilder.newColumnDef(3).withOption('sWidth', '35px'), // refunds?
                    DTColumnDefBuilder.newColumnDef(3).notSortable(), // refunds?
                    DTColumnDefBuilder.newColumnDef(4).withOption('sWidth', '45px'), // status
                    DTColumnDefBuilder.newColumnDef(4).notSortable() // status?
                ]
            }


            var UserListService = $resource("user/all");
            $scope.userList = UserListService.query();

            //ng-change="updateUser(user, 'active')"
            $scope.updateUser = function (user, type) {
                log('in updateUser...user', type)
                log('in updateUser...type', user)
                if (type == 'active') {
                    // set user to inactive (by default only active are displayed)
                }
                else if (type == 'refund') {
                    // update the 'canDoRefunds' value for the user
                }
            }
        })

    .controller("UserDetailController",
        function ($scope, $routeParams, $resource, $route, UserService, breadcrumbs, UsersDepartmentLookup) {
            $scope.breadcrumbs = breadcrumbs;
            $scope.errors = null;
            $scope.deptList = UsersDepartmentLookup;

            log("in UserDetailController....")

            //$scope.hlc = HotListService.getHL({id: $routeParams.id}, function () { }
            $scope.user = UserService.getUser({agentId: $routeParams.id}, function () {
                log($scope.user);

            })

            var UserListService = $resource("user/all");
            $scope.userList = UserListService.query();
        })
    .controller("TShirtController",
        function ($scope, $routeParams, $rootScope, $resource, $route, UserService, breadcrumbs, SizesLookup, QuantityLookup) {
            $scope.breadcrumbs = breadcrumbs;
            $scope.errors = null;
            $scope.sizes = SizesLookup;
            $scope.qtyList = QuantityLookup;
            $scope.info = {};
            // set defaults
            //$scope.info.qty = "1";
            //$scope.info.size.abbreviation = "w-1";

            log("in TShirtController....")

            $scope.isFormDirty = function () {
                return $scope.tshirtOrderForm.$dirty;
            }

            $scope.clearFields = function() {
                $scope.info.name = '';
                $scope.info.empno = '';
                $scope.info.size = '';
                $scope.info.qty = '';
            }

            $scope.validateFields = function() {
                if ($scope.info.name == null) {
                    return;
                }
                if ($scope.info.empno == null) {
                    return;
                }
                if ($scope.info.size == null) {
                    return;
                }
                if ($scope.info.qty == null) {
                    return;
                }
                else {
                    $scope.saveForm();
                }
            }

            $scope.saveForm = function () {
                $scope.errors = null;
                // Save TShirt Info
                $scope.info.qty = $scope.info.qty.abbreviation;
                $scope.info.size = $scope.info.size.description;
                log($scope.info);

                UserService.saveTShirt({}, $scope.info,
                    function (data) {
                        $rootScope.displaySuccessMsg("TShirt order successfully saved");
                        // clear the fields
                        $scope.clearFields();
                    }, function (res) {
                        var err = res.data;
                        $scope.errors = err;
                    });
            };


        })
