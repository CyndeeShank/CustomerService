CustomerServiceApp.config(['$routeProvider', function($routeProvider) {
	$routeProvider
		.when('/home', {
			templateUrl: 'resources/welcome.html',
			//templateUrl: 'resources/login.html', // for cs portal with everything available
			controller : "UserLoginController",
			label: "Login"
		})
			
		// User Related Route Definitions
		.when('/users', {
			templateUrl: 'resources/users/users.html',
			controller : "UserController",
			label: "Users"
		})
		.when('/users/:id', {
			templateUrl: 'resources/users/user_details.html',
			controller : "UserDetailController",
			label: "User Detail"
		})
		.when('/users/new', {
			templateUrl: 'resources/users/user_details.html',
			controller : "UserDetailController",
			label: "New User"
		})
				
		// Gift Card Related Route Definitions
		.when('/giftcard', {
			templateUrl: 'resources/giftcard/giftcard.html',
			controller : "GiftCardController",
			label: "Gift Cards"
		})
		.when('/giftcard/history', {
			templateUrl: 'resources/giftcard/giftcard_history.html',
			controller : "GiftCardHistoryController",
			label: "Balance & History"
		})

		// Customer Review Related Route Definitions
		.when('/review', {
			templateUrl: 'resources/review/reviewcustomers.html',
			controller : "ReviewCustomerController",
			label: "Review Customers"
		})
		// Customer Review Related Route Definitions
		.when('/review/add', {
			templateUrl: 'resources/review/review_detail.html',
			controller : "ReviewCustomerDetailController",
			label: "Review Customer Detail"
		})

		// Loyalty Related Route Definitions
		.when('/loyalty', {
			templateUrl: 'resources/loyalty/loyalty_search.html',
			controller : 'LoyaltyController',
			label: 'Loyalty Search'
		})
		.when('/loyalty/list', {
			templateUrl: 'resources/loyalty/loyalty_list.html',
			controller : 'LoyaltyController',
			label: 'Loyalty List'
		})
		/**
		.when('/loyalty/detail', {
			templateUrl: 'resources/loyalty/loyalty_detail.html',
			controller : 'LoyaltyDetailController',
			label: 'Loyalty Detail'
		})
		 **/
		.when('/loyalty/detail/:number', {
			templateUrl: 'resources/loyalty/loyalty_detail.html',
			controller : 'LoyaltyDetailController',
			label: 'Loyalty Detail'
		})
		.when('/loyalty/new', {
			templateUrl: 'resources/loyalty/loyalty_detail.html',
			controller : 'LoyaltyNewController',
			label: 'New Loyalty Customer'
		})

		// Order Related Route Definitions
		.when('/orders', {
			templateUrl: 'resources/order/order_search.html',
			controller : "OrderController",
			label: "Order Search"
		})
		.when('/orders/card', {
			templateUrl: 'resources/order/order_search_card.html',
			controller : "OrderSearchController",
			label: "Order Search By Card"
		})
        .when('/orders/lookup', {
            templateUrl: 'resources/order/order_lookup.html',
            controller : "OrderLookupController",
            label: "Order Lookup / Resubmission"
        })
		.when('/orders/list', {
			templateUrl: 'resources/order/orders.html',
			controller : "OrderListController",
			label: "Order List"
		})
		.when('/orders/list/:id', {
			templateUrl: 'resources/order/order_details.html',
			controller : "OrderDetailController",
			label: "Order Details"
		})
		.when('/order/:id', {
			templateUrl: 'resources/order/order_details.html',
			controller : "OrderDetailController",
			label: "Order Details"
		})
				
		// Reports Related Route Definitions
		.when('/reports', {
			templateUrl: 'resources/reports/reports.html',
			controller : "ReportsController",
			label: "Reports"
		})
		.when('/reports/grossdemand', {
			templateUrl: 'resources/reports/grossdemand.html',
			controller : "ReportsGrossDemandController",
			label: "Gross Demand"
		})
		.when('/reports/allowances', {
			templateUrl: 'resources/reports/allowances.html',
			controller : "ReportsAllowancesController",
			label: "Allowances"
		})
		.when('/reports/cancels', {
			templateUrl: 'resources/reports/cancels.html',
			controller : "ReportsCancelController",
			label: "Cancellations"
		})
		.when('/reports/returns', {
			templateUrl: 'resources/reports/returns.html',
			controller : "ReportsReturnsController",
			label: "Returns"
		})
		.when('/reports/rma', {
			templateUrl: 'resources/reports/rma.html',
			controller : "ReportsRmaController",
			label: "RMAs"
		})
		.when('/reports/hold', {
			templateUrl: 'resources/reports/hold.html',
			controller : "ReportsHoldController",
			label: "Released from Hold"
		})
		.when('/reports/netsales', {
			templateUrl: 'resources/reports/netsales.html',
			controller : "ReportsNetSalesController",
			label: "Net Sales"
		})
		.when('/reports/orderbatch', {
			templateUrl: 'resources/reports/orderbatch.html',
			controller : "ReportsOrderBatchController",
			label: "Order Batch Information"
		})
		.when('/reports/transactions', {
			templateUrl: 'resources/reports/transactions.html',
			controller : "ReportsTransactionsController",
			label: "Open Transaction Information"
		})
        .when('/reports/shipping', {
            templateUrl: 'resources/reports/shipping.html',
            controller : "ReportsShippingController",
            label: "Shipping Information"
        })
        .when('/reports/shipping/update', {
            templateUrl: 'resources/reports/shipping.html',
            controller : "ReportsShippingController"
        })
        .when('/reports/inventory', {
            templateUrl: 'resources/reports/inventory.html',
            controller : "ReportsInventoryController",
            label: "Style/Inventory Lookup"
        })
		.when('/reports/styles', {
			templateUrl: 'resources/reports/styles.html',
			controller : "ReportsStylesController",
			label: "Style with no Inventory"
		})
		.otherwise({redirectTo:'/home'});
}]);

