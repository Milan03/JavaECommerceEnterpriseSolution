angular.module('case2', ['ngRoute']).
config(['$routeProvider', function($routeProvider) {
  $routeProvider.otherwise({redirectTo: ''});
}]);