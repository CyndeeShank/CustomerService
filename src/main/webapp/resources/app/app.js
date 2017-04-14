var CustomerServiceApp = angular.module('CustomerServiceApp',
    [
        'ngRoute', 'ngResource', 'ngAnimate', 'ui.bootstrap', 'datatables', 'ui.select', 'cgBusy',
        'datatables.bootstrap', 'ng-breadcrumbs', 'bc.TelephoneFilter', 'xeditable'
    ])
    .service('UsersService', function ($log, $resource) {
        return {
            getAll: function () {
                var userResource = $resource('users', {}, {
                    query: {method: 'GET', params: {}, isArray: true}
                });
                return userResource.query();
            }
        }
    })
    .run(function ($rootScope, $location, $route, $timeout, editableOptions, $uibModal) {
        $rootScope.setActive = function (tab) {
            $rootScope.currentTab = tab;
            log("currentTab:", $rootScope.currentTab);
            switch (tab) {
                case 'home':
                    $location.path("/home");
                    break;
                case 'users':
                    $location.path("/users");
                    break;
                case 'gift':
                    $location.path("/giftcard");
                    break;
                case 'review':
                    $location.path("/review");
                    break;
                case 'loyalty':
                    $location.path("/loyalty");
                    break;
                case 'order':
                    $location.path("/orders");
                    break;
                case 'reports':
                    $location.path("/reports");
                    break;
            }
        }
        // TODO:  default this to true for testing
        $rootScope.loggedInUser = true;

        // default the success and error messages
        $rootScope.showSuccess = false;
        $rootScope.showError = false;

        // Dynamically set module css class
        $rootScope.$on("$viewContentLoaded", function() {
            // Get current module name
            var moduleName = $route.current.originalPath.trim().split('/')[1];
            log("Current module:", moduleName);

            // Set module class for current module
            var container = angular.element("#generic-container");
            if (moduleName && moduleName != "") {
                container.addClass(moduleName);
            }
            if ($rootScope.$currentModule && $rootScope.$currentModule != "" && $rootScope.$currentModule != moduleName) {
                container.removeClass($rootScope.$currentModule);
            }
            $rootScope.$currentModule = moduleName;
        });

        $rootScope.displayErrorMsg = function(message) {
            $rootScope.errorMessage = message;
            $timeout(function() {
                $rootScope.errorMessage = null;
            },  10000);
        };
        $rootScope.displayWarningMsg = function(message) {
            $rootScope.warningMessage = message;
            $timeout(function() {
                $rootScope.warningMessage = null;
            },  10000);
        };
        $rootScope.displaySuccessMsg = function(message) {
            $rootScope.successMessage = message;
            $timeout(function() {
                $rootScope.successMessage = null;
            },  50000);
        };

        // set the theme for xeditable options
        editableOptions.theme = 'bs3';


        // Report Date Picker Settings
        $rootScope.useDateDefault = true;
        $rootScope.dateObj = {};
        $rootScope.displayDatePicker = function (type, useDefault) {
            log("type", type);
            log("useDefault", useDefault);
            $rootScope.reportType = type;
            $rootScope.useDateDefault = useDefault;
            switch (type) {
                case 'demand':
                    $rootScope.datePickerTitle = "Gross Demand";
                    $rootScope.dateDefault = 30;
                    break;
                case 'cancel':
                    $rootScope.datePickerTitle = "Cancellations";
                    $rootScope.dateDefault = 30;
                    break;
                case 'return':
                    $rootScope.datePickerTitle = "Returns";
                    $rootScope.dateDefault = 30;
                    break;
                case 'rma':
                    $rootScope.datePickerTitle = "RMAs";
                    $rootScope.dateDefault = 30;
                    break;
                case 'hold':
                    $rootScope.datePickerTitle = "Released from Hold";
                    $rootScope.dateDefault = 30;
                    break;
                case 'netsales':
                    $rootScope.datePickerTitle = "Net Sales";
                    $rootScope.dateDefault = 30;
                    break;
                case 'shipping':
                    $rootScope.datePickerTitle = "Shipping";
                    $rootScope.dateDefault = 30;
                    break;
            }
            $rootScope.modalInstance = $uibModal.open({
                templateUrl: 'resources/app/date_selector.html',
                controller: 'ReportsController',
                scope: $rootScope,
                //size: 'sm'
                backdrop: "static"
            })
        }
        // set start date based on report type
        /**
        $rootScope.startDate = new Date();
        var start = new Date();
        $rootScope.startDate.setDate(start.getDate() - $rootScope.dateDefault);

        // set end date to today
        $rootScope.endDate = new Date();

        // date picker functions
        $rootScope.open1 = function () {
            $rootScope.popup1.opened = true;
        };
        $rootScope.open2 = function () {
            $rootScope.popup2.opened = true;
        };
        $rootScope.setDate = function (year, month, day) {
            $rootScope.dt = new Date(year, month, day);
        };
        $rootScope.format = 'dd-MMMM-yyyy';

        $rootScope.popup1 = {
            opened: false
        };
        $rootScope.popup2 = {
            opened: false
        };
         **/

    })
    /**
    .config(function ($tooltipProvider) {
        $tooltipProvider.options({
            placement: "right",
            animation: false,
            //popupDelay: 800,
            appendToBody: true
        })
    })
     **/
    .directive('bootstrapSwitch', [
        function() {
            return {
                restrict: 'A',
                require: '?ngModel',
                link: function(scope, element, attrs, ngModel) {
                    element.bootstrapSwitch();

                    element.on('switchChange.bootstrapSwitch', function(event, state) {
                        if (ngModel) {
                            scope.$apply(function() {
                                ngModel.$setViewValue(state);
                            });
                        }
                    });
                    scope.$watch(attrs.ngModel, function(newValue, oldValue) {
                        if (newValue) {
                            element.bootstrapSwitch('state', true, true);
                        }
                        else {
                            element.bootstrapSwitch('state', false, true);
                        }
                    });
                }
            };
        }
    ])
    .directive('onlyDigits', function () {
        return {
            restrict: 'A',
            require: '?ngModel',
            link: function (scope, element, attrs, modelCtrl) {
                modelCtrl.$parsers.push(function (inputValue) {
                    if (inputValue == undefined) {
                        return '';
                    }
                    //var transformedInput = inputValue.replace(/(^[0-9]*$)/g, '');
                    var transformedInput = inputValue.replace(/[^0-9]/g, '');
                    //var transformedInput = inputValue.replace(/([^0-9]|[^\\s])/g, '');
                    if (transformedInput !== inputValue) {
                        modelCtrl.$setViewValue(transformedInput);
                        modelCtrl.$render();
                    }
                    return transformedInput;
                });
            }
        };
    })
    // Configure CgBusy
    .value("cgBusyDefaults", {
        templateUrl: "resources/app/bower/angular-busy/angular-busy.html",
        minDuration: 500
    })

// Log to console
function log(m1, m2) {
    if (m2 == null) {
        console.log(m1);
    }
    else {
        console.log(m1, m2);
    }
}
