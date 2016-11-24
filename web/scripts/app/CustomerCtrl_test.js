describe('CustomerCtrl', function() {
	beforeEach(module('case2'));

	 var $controller;

	 beforeEach(inject(function(_$controller_){
	 	// injector unwraps underscores when matching
	 	$controller = _$controller_;
	 }));

	 describe('customer', function() {
	 	var $scope, controller;

	 	beforeEach(function() {
	 		$scope = {};
	 		controller = $controller('CustomerCtrl', { $scope: $scope });
	 	});

	 	it('should define a customer', function() {
		 	$scope.myCustomer = {
		 		name: 'Milan',
		 		address: '789 Grenfell Dr',
		 		combined: ''
		 	};
		 	expect($scope.myCustomer).toBeDefined();
		 	expect($scope.myCustomer.name).toEqual('Milan');
			expect($scope.myCustomer.address).toEqual('789 Grenfell Dr');
			expect($scope.myCustomer.combined).toEqual('');
		});
		it('should combine name and address with showName', function() {
			$scope.myCustomer = {
		 		name: 'Milan',
		 		address: '789 Grenfell Dr',
		 		combined: ''
		 	};
		 	$scope.showName();
		 	expect($scope.myCustomer.combined).toEqual('Milan, 789 Grenfell Dr');
		})
	 });
});