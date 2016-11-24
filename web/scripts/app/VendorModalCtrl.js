(function(app) { 

    var VendorModalCtrl = function($scope, $modalInstance, RESTFactory) {
        var baseurl = 'webresources/vendor';
        var retVal = { operation: "", vendorno: -1, numRows: -1};
        
        var init = function() {
            if ($scope.todo === 'add') {
                retVal.operation = 'add';
                $scope.vendor.vendortype = 'Trusted';
                $scope.vendor.province = 'Ontario';
            }
        }; //init
        init();
        $scope.addVendor = function() {
            $scope.status = "Wait...";
            RESTFactory.restCall('post', baseurl, -1, $scope.vendor).then(function(results) {
                if (results.substring) {
                    if (parseInt(results) > 0) {
                        $scope.vendor.vendorno = parseInt(results);
                        $scope.vendors.push($scope.vendor);
                        retVal.numRows = 1;
                        retVal.operation = 'add';
                        retVal.vendorno = parseInt(results);
                        $modalInstance.close(retVal);
                    }
                } else {
                    retVal = 'Vendor not added! Error: ' +results;
                    $modalInstance.close(retVal);
                }
            }, function(reason) {
                retVal = 'Vendor not added! Error: ' +results;
                $modalInstance.close(retVal);
            });
        }; // addVendor
        $scope.updateVendor = function() {
            RESTFactory.restCall('put', baseurl, -1, $scope.vendor).then(function(results) {
                retVal.operation = 'update';
                retVal.vendorno = $scope.vendor.vendorno;
                
                if (results.substring) 
                    retVal.numRows = parseInt(results);
                else 
                    retVal.numRows = -1;
                
                $modalInstance.close(retVal);
            }, function(reason) {
               retVal = 'Vendor was not updated! Error: ' +reason;
               $modalInstance.close(retVal);
            });
        }; //updateVendor
        $scope.deleteVendor = function() {
            RESTFactory.restCall('delete', baseurl, $scope.vendor.vendorno, '').then(function(results) {
                retVal.operation = 'delete';
                retVal.vendorno = $scope.vendor.vendorno;
                
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
        }; //deleteVendor
        $scope.closeModal = function() {
            $modalInstance.close();
        }; //closeModal
    };
    
    app.controller('VendorModalCtrl', ['$scope', '$modalInstance', 'RESTFactory', VendorModalCtrl]);
})(angular.module('case2'));  