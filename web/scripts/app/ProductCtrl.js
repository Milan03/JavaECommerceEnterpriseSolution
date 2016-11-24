/*
 * File:         ProductCtrl.js
 * Author:       Milan Sobat
 * Last Updated: October 16, 2014
 * Purpose:      Logic for interacting with Products view.
 */

(function(app) {
    var ProductCtrl = function($scope, $modal, RESTFactory, $filter) {
        var baseurl = "webresources/product/products";
        var init = function() {
            // load data for page from WEB api
            $scope.status = 'Loading Products...';
            $scope.products = RESTFactory.restCall('get', baseurl, -1, '').then(function(products) {
                if (products.length > 0) {
                    $scope.products = products;
                    $scope.status = 'Products Retrieved';
                } else {
                    $scope.status = 'Products not retrieved code - ' + products;
                }
            }, function(reason) {
                $scope.status = 'Products not retrieved ' + reason;
            });
            $scope.product = $scope.products[0];
//            $scope.alert = { type: 'success', msg: $scope.status };
//            $scope.closeAlert = function(index) {
//                $scope.alert.splice(index, 1);
//            };
        }; //init
        $scope.selectRow = function(row, product) {
            if(row < 0) {
                $scope.todo = 'add';
                $scope.product = new Object();
                $scope.status = "";
                
            } else {
                $scope.product = product;
                $scope.selectedRow = row;
                $scope.todo = 'update';
                $scope.status = "";
            }
            
            var modalInstance = $modal.open({
                templateUrl: 'partials/productModal.html',
                controller: 'ProductModalCtrl',
                scope: $scope,
                backdrop: 'static'
            });
            
            modalInstance.result.then(function(results) {
                if (results.operation === 'add') {
                    if (results.numRows === 1) {
                        $scope.status = 'Product ' +results.productcode + ' added!';
                        $scope.selectedRow = $scope.products.length - 1;
                     } else {
                         $scope.status = 'Product not added.';
                     }
                } else if (results.operation === 'update') {
                    if (results.numRows === 1) {
                        $scope.status = 'Product ' + results.productcode + ' Updated!';
                    }
                    else {
                        $scope.status = 'Product Not Updated!';
                    }
                } else { //delete
                    for (var i = 0; i < $scope.products.length; i++) {
                        if ($scope.products[i].productcode === results.productcode) {
                            $scope.products.splice(i, 1);
                        }
                    }
                    if (results.numRows === 1) {
                        $scope.selectedRow = null;
                        $scope.status = 'Product ' +results.productcode +' deleted!';
                    } else
                        $scope.status = 'Product ' +results.productcode +' not deleted.';
                }
            }, function(reason) {
                $scope.status = reason;
            });
        
        }; // selectRow
        init();
    }; // ProductCtrl
    
    app.controller('ProductCtrl', ['$scope', '$modal', 'RESTFactory', '$filter', ProductCtrl]);
})(angular.module('case2'));