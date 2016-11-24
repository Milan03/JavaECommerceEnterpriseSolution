(function(app) {
    var CustomerCtrl = function($scope) {
        $scope.myCustomer = {
            name: 'Milan',
            street: '1234 Anywhere St.',
            combined: ''
        };
        
        $scope.showName = function() {
            $scope.myCustomer.combined = $scope.myCustomer.name + ', ' +
                    $scope.myCustomer.street;
            alert($scope.myCustomer.combined);
        };
    };
    app.controller('CustomerCtrl', ['$scope', CustomerCtrl]);
})(angular.module('case2'));