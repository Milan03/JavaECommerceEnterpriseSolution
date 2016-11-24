/*
 * File:         GeneratorCtrl.js
 * Author:       Milan Sobat
 * Last Updated: October 16, 2014
 * Purpose:      Logic for generating Purchase Orders.
 */

(function(app) {
    var GeneratorCtrl = function($scope, $window, RESTFactory) {
        
        $scope.items = new Array();
        $scope.subTotal = 0;
        $scope.tax = 0;
        $scope.total = 0;
        $scope.vendorNo = 0;
        $scope.generated = false;
        
        var resetProduct = function() {
            $scope.product.productname = "";
            $scope.product.productcode = "";
        };
  
        
        var init = function() {
            $scope.vendors = RESTFactory.restCall('get', 'webresources/vendor/vendors', -1, '').then(function(vendors) {
                if (vendors.length > 0) {
                    $scope.status = "-- Choose Vendor to start --"
                    $scope.prodTable = false;
                    $scope.pickedVendor = false;
                    $scope.pickedProd = false;
                    $scope.pickedQty = false;
                    $scope.vendors = vendors;
                } else {
                    $scope.status = 'Vendors not retrieved. Error: ' +vendors;
                }
            }, function(reason) {
                $scope.status = 'Vendors not retrieved ' +reason;
            });
        }; //init
        init();
        $scope.vendorChosen = function() {
            $scope.pickedVendor = true;
            $scope.getVendorProducts($scope.vendor.vendorno);
        }; //vendorChosen
        $scope.getVendorProducts = function(vendorno) {
            $scope.products = RESTFactory.restCall('get', 'webresources/product', vendorno, '').then(function(products) {
                    if (products.length > 0) {
                        $scope.products = products;
                        $scope.pickedProd = false;
                    } else {
                        $scope.status = 'Products not retrieved. Error: ' +products;
                    }
                }, function(reason) {
                    $scope.status = 'Products not retrieved ' +reason;
                });
        }; //getVendorProducts
        $scope.productChosen = function() {
            $scope.pickedProd = true;
            $scope.quantity = 'choose';
        }; //productChosen
        $scope.quantityChosen = function() {
            $scope.pickedQty = true;
        };
        $scope.addToOrder = function() {
            $scope.prodTable = true;
            var addItem = new Object();
            var duplicateFound = false;
            addItem = { name: '', productcode: '', quantity: 0, price: 0 };
            for (var i = 0; i < $scope.products.length; ++i) {
                if ($scope.generator.product_code === $scope.products[i].productcode) {
                    for (var n = 0; n < $scope.items.length; ++n) {
                        try {
                            if ($scope.generator.product_code === $scope.items[n].productcode && $scope.quantity != 0) {
                                $scope.status = "Error: Duplicate Product";
                                duplicateFound = true;
                                break;
                            }
                        } catch (e) {
                            $scope.status = "Error in addItem";
                        }
                            
                    }
                    if (duplicateFound === true) {
                        break;
                    } else {
                        $scope.product = $scope.products[i];
                        if ($scope.quantity === 'EOQ')
                            $scope.status = "Product [" +$scope.product.productname +"] Quantity [" +$scope.product.eoq +"] added.";
                        else
                            $scope.status = "Product [" +$scope.product.productname +"] Quantity [" +$scope.quantity +"] added.";
                    }
                }
            }
            addItem.productcode = $scope.product.productcode;
            addItem.name = $scope.product.productname;
            if ($scope.quantity === '0') {
                if ($scope.items.length === 1) {
                    // reinitialize product from last add
                    resetProduct();
                    
                    for (var i = 0; i < $scope.items.length; ++i) 
                        if ($scope.generator.product_code === $scope.items[i].productcode)
                            $scope.items.splice(i, 1);
                    
                    $scope.prodTable = false;
                    $scope.status = "No items";
                    $scope.subTotal = 0;
                    $scope.tax = 0;
                    $scope.total = 0;
                } else {
                    resetProduct();
                    for (var i = 0; i < $scope.items.length; ++i) {
                        if ($scope.generator.product_code === $scope.items[i].productcode) {
                            $scope.subTotal -= $scope.items[i].price;
                            $scope.total -= $scope.items[i].price;
                            $scope.items.splice(i ,1);
                            $scope.tax = $scope.tax.toFixed(2);
                            $scope.total = $scope.total.toFixed(2);
                            $scope.status = "Item " +$scope.product.productname +" removed";
                        }
                    }
                }
                addItem.quantity = 0;
            } //quantity === 0
            else if ($scope.quantity === 'EOQ')
                addItem.quantity = $scope.product.eoq;
            else
                addItem.quantity = parseInt($scope.quantity);
            
            addItem.price = $scope.product.costprice * addItem.quantity;
            
            if ($scope.quantity !== '0' && !duplicateFound)
                $scope.items.push(addItem);
            
            if ($scope.quantity != 0 && !duplicateFound) {
                $scope.subTotal += addItem.price;
                $scope.tax = $scope.subTotal * 0.13;
                $scope.total = $scope.tax + $scope.subTotal;
                $scope.tax = $scope.tax.toFixed(2);
                $scope.total = $scope.total.toFixed(2);
            }
            $scope.productChosen();
            $scope.vendorNo = $scope.vendor.vendorno;
            $scope.vendor.vendorno = 'choose';
            $scope.generator.product_code = 'choose';
        }; //addToOrder
        $scope.createPO = function() {
            $scope.status = "Processing...";
            var PODTO = new Object();
            PODTO.total = parseFloat($scope.total);
            PODTO.vendorno = $scope.vendorNo;
            PODTO.items = $scope.items;
            for (var i = 0; i < PODTO.items.length; ++i) 
                PODTO.items[i].productcode = $scope.items[i].productcode;
            $scope.PO = RESTFactory.restCall('post', 'webresources/po',
                                                -1,
                                                PODTO).then(function(results) {
                if (results.length > 0) {
                    $scope.status = "Purchase Order: " +results +" added!";
                    $scope.pono = results;
                    $scope.generated = true;
                } else {
                    $scope.status = 'PO not created. Error: ' +results;
                    $scope.generated = false;
                }}, function(reason) {
                    $scope.status = 'PO not created. Error: ' +reason;
                    $scope.generated = false;
                });
        }; //createPO
        $scope.viewPDF = function() {
            $window.location.href = 'POPDF?po=' +$scope.pono;
        }; //viewPDF
    }; //GeneratorCtrl
    app.controller('GeneratorCtrl', ['$scope', '$window', 'RESTFactory', GeneratorCtrl]);
})(angular.module('case2'));