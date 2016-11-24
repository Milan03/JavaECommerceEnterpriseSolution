/*
 * File:         VendorCtrl.js
 * Author:       Milan Sobat
 * Last Updated: October 16, 2014
 * Purpose:      Logic for interacting with Vendors.
 */

(function(app) {
    var VendorCtrl = function($scope, $modal, RESTFactory, $filter) {
        var baseurl = 'webresources/vendor/vendors';
        var init = function() {
            // load data for page from WEB api
            $scope.status = 'Loading Vendors...';
            $scope.vendors = RESTFactory.restCall('get', baseurl, -1, '').then(function(vendors) {
                if (vendors.length > 0) {
                    $scope.vendors = vendors;
                    $scope.status = 'Vendors Retrieved';
                } else {
                    $scope.status = 'Vendors not retrieved code - ' + vendors;
                }
            }, function(reason) {
                $scope.status = 'Vendors not retrieved ' + reason;
            });
            $scope.vendor = $scope.vendors[0];
//            $scope.alert = { type: 'success', msg: $scope.status };
//            $scope.closeAlert = function(index) {
//                $scope.alert.splice(index, 1);
//            };
        }; //init
        $scope.selectRow = function(row, vendor) {
            if(row < 0) {
                $scope.todo = 'add';
                $scope.vendor = new Object();
                $scope.status = "";
            } else {
                $scope.vendor = vendor;
                $scope.selectedRow = row;
                $scope.todo = 'update';
                $scope.status = "";
            }
            
            var modalInstance = $modal.open({
                templateUrl: 'partials/vendorModal.html',
                controller: 'VendorModalCtrl',
                scope: $scope,
                backdrop: 'static'
            });
            
            modalInstance.result.then(function(results) {
               if (results.operation === 'add') {
                   if (results.numRows === 1) {
                       $scope.status = 'Vendor ' +results.vendorno + ' added!';
                       $scope.selectedRow = $scope.vendors.length - 1;
                   } else {
                       $scope.status = 'Vendor not added.';
                   }
               } else if (results.operation === 'update') {
                   if (results.numRows === 1) 
                       $scope.status = 'Vendor ' +results.vendorno + ' updated!';
                   else
                       $scope.status = 'Vendor not updated.';
                } else { //delete
                    for (var i = 0; i < $scope.vendors.length; i++) {
                        if ($scope.vendors[i].vendorno === results.vendorno) {
                            $scope.vendors.splice(i, 1);
                        }
                    }
                    if (results.numRows === 1) {
                        $scope.selectedRow = null;
                        $scope.status = 'Vendor ' +results.vendorno +' deleted!';
                    } else
                        $scope.status = 'Vendor ' +results.vendorno +' not deleted.';
                }
            }, function() {
                $scope.status = 'Vendor not updated.';
            }, function(reason) {
                $scope.status = reason;
            });
        }; //selectRow

        init();
    }; // VendorCtrl
    
    app.controller('VendorCtrl', ['$scope', '$modal', 'RESTFactory', '$filter', VendorCtrl]);
})(angular.module('case2'));  