(function(app) {
    app.directive('confirmClick', function() {
        return {
            restrict: 'A',
            priority: 1,
            terminal: true,
            link: function(scope, element, attr) {
                var msg = attr.confirmationNeeded || "Confirm delete?";
                var clickAction = attr.ngClick;
                element.bind('click', function() {
                    if (window.confirm(msg)) 
                        scope.$apply(clickAction);
                });
            }
        };
    });
    app.directive('myHelloWorld', function() {
        return {
            restrict: 'E',
            replace: 'true',
            template: '<h2>Hello World!!</h2>'
        };
    });
    app.directive('myOtherHelloWorld', function() {
        return {
            restrict: 'E',
            replace: 'true',
            scope: {sz: '@'},
            template: '<div style="font-size:{{sz}}px;">Hello Other World!!' +
                      ' at {{sz}} pixels</div>'
        };
    });
    app.directive('myThirdDirective', function() {
        return {
            restrict: 'E',
            replace: 'true',
            scope: {customer: '='}, // 2 way binding with controller
            template: '<div>Output from the 3rd Example directive<br />' +
                    '<input ng-model="customer.name" required type="text" />' +
                    '</div>'
        };
    });
    app.directive('myFourthDirective', function() {
        function link($scope, element, attrs) {
            element.bind('input', function() {
                $scope.myCustomer.combined = $scope.myCustomer.name + ' '
                        +attrs.lastname;
                if ($scope.myCustomer.name === 'Milan') {
                    alert($scope.myCustomer.combined + ' lives on ' +
                            $scope.myCustomer.street);
                    element.css('visibility', 'hidden');
                }
            })
        }
        return {
            restrict: 'E',
            replace: 'true',
            template: '<div>Enter "John" to complete 4th directive<br />' +
                    '<input ng-model="myCustomer.name" required type="text" />' +
                    '</div>',
            link: link
        };
    });
    app.directive('myReqfield', function() {
        var getTemplate = function(attrs) {
            var template = '';
            
            switch(attrs.type) {
                case 'txt':
                    template = '<div class="form-group">' +
                                   '<label>{{label}}</label>' +
                                   '<input type="text" ng-model="ngModel" required maxlength="{{max}}" size="{{size}}">' +
                                   '<div class="custom-error" ng-show="!ngModel">{{label}} is required.</div>' +
                                '/div>';
                    break;
            }
        };
        return {
            restrict: 'E',
            scope: {ngModel: '=', size: '@', label: '@', max: '@'},
            template: function(element, attrs) {
                return getTemplate(attrs);
            },
            link: function($scope, element, attrs) {
                var origVal = $scope.ngModel;
                element.bind('input', function() {
                    if (origVal !== $scope.ngModel) { //modified
                        element.addClass('changed');
                    } else {
                        element.removeClass('changed');
                    }
                });
            }
        };
    });
}(angular.module('case2')));