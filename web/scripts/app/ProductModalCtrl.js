(function(app) { 
    var ProductModalCtrl = function($scope, $modalInstance, RESTFactory) {
        var baseurl = 'webresources/product';
        var retVal = { operation: "", productcode: "", numRows: -1};
        
        init = function() {
            $scope.vendors = RESTFactory.restCall('get', 'webresources/vendor/vendors', -1, '').then(function(vendors) {
                if (vendors.length > 0) {
                    $scope.vendors = vendors;
                } else {
                    $scope.status = 'Vendors not retrieved. Error: ' +vendors;
                }
            }, function(reason) {
                $scope.status = 'Vendors not retrieved ' +reason;
            });
            if ($scope.todo === 'add') {
                $scope.product.vendorno = 1;
            }
        }; //init
        init();
        $scope.addProduct = function() {
            $scope.status = "Wait...";
            RESTFactory.restCall('post', baseurl, -1, $scope.product).then(function(results) {
                if (results.substring) {
                        $scope.products.push($scope.product);
                        retVal.numRows = 1;
                        retVal.operation = 'add';
                        retVal.productcode = results;
                        $modalInstance.close(retVal);
                    
                } else {
                    retVal = 'Product not added! Error: ' +results;
                    $modalInstance.close(retVal);
                }
            }, function(reason) {
                retVal = 'Product not added! Error: ' +results;
                $modalInstance.close(retVal);
            });
        }; // addProduct
        $scope.updateProduct = function() {
            RESTFactory.restCall('put', baseurl, -1, $scope.product).then(function(results) {
                retVal.operation = 'update';
                retVal.productcode = $scope.product.productcode;
                
                if (results.substring) 
                    retVal.numRows = parseInt(results);
                else 
                    retVal.numRows = -1;
                
                $modalInstance.close(retVal);
            }, function(reason) {
               retVal = 'Product was not updated! Error: ' +reason;
               $modalInstance.close(retVal);
            });
        }; //updateVendor
        $scope.deleteProduct = function() {
            RESTFactory.restCall('delete', baseurl, $scope.product.productcode, '').then(function(results) {
                retVal.operation = 'delete';
                retVal.productcode = $scope.product.productcode;
                
                if (results.substring) {
                    retVal.numRows = parseInt(results);
                } else {
                    retVal.numRows = -1;
                }
                
                $modalInstance.close(retVal);
            }, function() {
                retVal.numRows = -1;
                $modalInstance.close(retVal);
            });
        };
        $scope.closeModal = function() {
            $modalInstance.close();
        }; //closeModal
    } //ProductModalCtrl
    app.controller('ProductModalCtrl', ['$scope', '$modalInstance', 'RESTFactory', ProductModalCtrl]);
})(angular.module('case2'));  