(function(app) {
    var ViewerCtrl = function($scope, $window, RESTFactory, $filter) {
        var addItem = { productcode: '', productname: '', quantity: 0, price: 0, extended: 0 };
        var init = function() {
            $scope.vendors = RESTFactory.restCall('get', 'webresources/vendor/vendors', -1, '').then(function(vendors) {
                if (vendors.length > 0) {
                    $scope.status = "Please chooose vendor.";
                    $scope.vendors = vendors;
                    $scope.pickedVendor = false;
                    $scope.pickedPO = false;
                } else {
                    $scope.status = 'Vendors not retrieved. Error: ' +vendors;
                }
            }, function(reason) {
                $scope.status = 'Vendors not retrieved ' +reason;
            });
        }; //init
        init();
        $scope.vendorChosen = function() {
            $scope.item = new Array();
            $scope.subTotal = 0;
            $scope.tax = 0;
            $scope.total = 0;
            $scope.pickedVendor = true;
            $scope.getVendorPOs($scope.vendor.vendorno);
            $scope.products = RESTFactory.restCall('get', 'webresources/product/products', -1, '').then(function(products) {
                    if (products.length > 0) {
                        $scope.status = "Please choose PO.";
                        $scope.products = products;
                        $scope.pickedProd = false;
                    } else {
                        $scope.status = 'Products not retrieved. Error: ' +products;
                    }
                }, function(reason) {
                    $scope.status = 'Products not retrieved ' +reason;
                });
        }; //vendorChosen    
        $scope.getVendorPOs = function(vendorno) {
            $scope.purchaseorders = RESTFactory.restCall('get', 'webresources/po', vendorno, '').then(function(purchaseorders) {
                if (purchaseorders.length > 0) {
                    $scope.status = "Purchase Orders Retrieved."
                    $scope.purchaseorders = purchaseorders;
                    $scope.pickedPO = false;
                } else {
                    $scope.status = 'POs not retrieved. Error: ' +purchaseorders;
                }
            }, function(reason) {
                $scope.status = 'POs not retrieved. Error: ' +purchaseorders;
            });
        }; //getVendorPOs
        $scope.poChosen = function() {
            $scope.items = new Array();
            $scope.subTotal = 0;
            $scope.tax = 0;
            $scope.total = 0;
            for (var i = 0; i < $scope.purchaseorders.length; i++) {
                if ($scope.purchaseorders[i].PONumber === $scope.ponum) {
                    for (var n = 0; n < $scope.purchaseorders[i].items.length; n++) {
                        addItem = { productcode: '', productname: '', quantity: 0, price: 0, extended: 0 };
                        for (var p = 0; p < $scope.products.length; ++p) {
                            if ($scope.products[p].productcode === $scope.purchaseorders[i].items[n].productcode) {
                                addItem.productname = $scope.products[p].productname;
                            }
                        }
                        addItem.productcode = $scope.purchaseorders[i].items[n].productcode;
                        addItem.quantity = $scope.purchaseorders[i].items[n].quantity;
                        //$scope.purchaseorders[i].items[n].price = $scope.purchaseorders[i].items[n].price / 1.13;
                        addItem.price = $scope.purchaseorders[i].items[n].price / addItem.quantity;
                        addItem.extended = $scope.purchaseorders[i].items[n].price;
                        $scope.subTotal += addItem.extended;
                        addItem.price = addItem.price.toFixed(2);
                        addItem.extended = addItem.extended.toFixed(2);
                        $scope.items.push(addItem);
                    }
                }
            }
            $scope.tax = $scope.subTotal * 0.13;
            $scope.total = $scope.tax + $scope.subTotal;
            $scope.total = $scope.total.toFixed(2);
            $scope.subTotal = $scope.subTotal.toFixed(2);
            $scope.tax = $scope.tax.toFixed(2);
            $scope.pickedPO = true;
        }; //poChosen
        $scope.viewPDF = function() {
            $window.location.href = 'POPDF?po=' +$scope.ponum;
        }; //viewPDF
    }; //ViewerCtrl
    
    app.controller('ViewerCtrl', ['$scope', '$window', 'RESTFactory', '$filter', ViewerCtrl]);
})(angular.module('case2'));