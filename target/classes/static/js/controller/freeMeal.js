app.controller('freemealreportcontroller', function ($scope, $http, $rootScope, Notification) {
    $scope.hiposServerURL = "/hipos/";
    $scope.customer = 1;
    $scope.isFilterApplied = false;
    $scope.today = function () {
        $scope.fromDate = new Date();
        $scope.toDate = new Date();
    };
    $scope.today();
    $scope.format = 'dd/MM/yyyy';

    $scope.open1 = function () {
        $scope.popup1.opened = true;
    };

    $scope.popup1 = {
        opened: false
    };

    $scope.open2 = function () {
        $scope.popup2.opened = true;
    };

    $scope.isFilterApplied = false;
    $scope.popup2 = {
        opened: false
    };


    angular.isUndefinedOrNull = function (val) {
        return angular.isUndefined(val) || val === null
    }
    /*
     *adding for date on 12/09/2017 @rahul
     */
    $scope.getFinStartDate = function () {
        var url = "company/getCompany";
        $http.get(url)
            .then(function mySuccess(response) {
                $scope.fromDate = new Date(response.data.startyear);
                $scope.fromDate.setHours(10);
                $scope.toDate = new Date();
                $scope.dateOptions = {
                    minDate: response.data.startyear,
                    maxDate: response.data.endyear
                };
                console.log($scope.toDate);
                console.log($scope.fromDate);
            });
    }
    // $scope.getFinStartDate();
    $http.get("/reports/invoice/onLoadPageData").then(function (data) {
        $scope.currencyList = data.data.currencyList;
        $scope.customerList = data.data.customerList;
        $scope.salesList = data.data.salesList;
        $scope.fromsalesinvoice = $scope.salesList[0];
        $scope.toSalesinvoice = $scope.salesList[$scope.salesList.length - 1];
        $scope.itemList = data.data.itemList;
        $scope.paymentList = data.data.paymentList;
        $scope.customerPage = 2;
    })
    $http.get("/hipos/getAllOrderTypes").then(function (data) {
        $scope.orderTypes = data.data;
    })
    $scope.updateCustomerId = function (newCustVal) {
        $scope.customer = newCustVal.customerId;
        $scope.removeAllItems();
    }
    $scope.toggleAll = function () {
        var toggleStatus = $scope.isAllSelected;
        angular.forEach($scope.employeeList, function (itm, key) {
            itm.selected = toggleStatus;
            if (toggleStatus == true) {
                $scope.employeeList[key].selectedEmployee = true;
            }
            else {
                $scope.employeeList[key].selectedEmployee = false;
            }
        });
    }
    $scope.getCustomerListSearch = function (val) {
        $(".loader").css("display", "block");
        if (angular.isUndefined(val)) {
            val = "";
        }
        $http.post($scope.hiposServerURL  + '/getCustomerList?searchCustomerText=' + val).then(function (response) {
                var data = response.data;
                $scope.customerList = angular.copy(data);
                $("#selectCustomer").modal('show');
                $scope.searchCustomerText = val;
            }, function (error) {
                Notification.error({
                    message: 'Something went wrong, please try again',
                    positionX: 'center',
                    delay: 2000
                });
            }
        )
    };
    $scope.appendCustomer = function (customerId) {
        $scope.customerSearchText = customerId.customerName;
        $scope.customerId = customerId.customerId;
        $scope.customer = $scope.customerId;
        $scope.showEmailBox = false;
        $("#selectCustomer").modal('hide');

    }

    $scope.invoiceTotal = 0;
    $scope.receivedTotal = 0;
    $scope.salesTaxAmt = 0;
    $scope.discountAmount = 0;
    $scope.totalPersons = 0;
    $scope.totalValues = function (val) {
        angular.forEach(val, function (value, key) {
            $scope.invoiceTotal = $scope.invoiceTotal + value.totalAmount;
            if(value.pax!=undefined){
                $scope.totalPersons = $scope.totalPersons + parseInt(value.pax);
            }
            $scope.receivedTotal = $scope.receivedTotal + value.totalReceivable;
            $scope.salesTaxAmt = $scope.salesTaxAmt + value.salesTotalTaxAmt;
            $scope.discountAmount = $scope.discountAmount + value.discountAmount;

        })
    }

    // $scope.exportFullData = function () {
    //     $http.get("/reports/sales/salesInvoiceExport").then(function (response) {
    //         var data = response.data;
    //         $scope.exportList = angular.copy(data);
    //         console.log(data);
    //         $rootScope.exportAction($scope.exportType);
    //     });
    // };
    $scope.appendEmployee = function (employeeId) {
        $scope.employeeSearchText = employeeId.employeeName;
        $scope.employeeId = employeeId.employeeId;
        $scope.employee = $scope.employeeId;
        $scope.showEmailBox = false;
        $("#selectEmployee").modal('hide');

    }
    $scope.isUndefinedOrNull = function (val) {
        return !angular.isDefined(val) || val === null;
    }
    $scope.getSalesInvoicewithFilter = function (page) {
        $scope.isFilterApplied = true;
        $scope.getPaginatedSalesInvoice(page);
    }
    $scope.getPaginatedSalesInvoice = function (page) {

        $scope.invoiceTotal = 0;
        $scope.totalPersons = 0;
        $scope.receivedTotal = 0;
        $scope.salesTaxAmt = 0;
        $scope.discountAmount = 0;

        switch (page) {
            case 'firstPage':
                $scope.firstPage = true;
                $scope.lastPage = false;
                $scope.pageNo = 0;
                break;
            case 'lastPage':
                $scope.lastPage = true;
                $scope.firstPage = false;
                $scope.pageNo = 0;
                break;
            case 'nextPage':
                $scope.isNext = true;
                $scope.isPrev = false;
                $scope.lastPage = false;
                $scope.firstPage = false;
                $scope.pageNo = $scope.pageNo + 1;
                break;
            case 'prevPage':
                $scope.isPrev = true;
                $scope.lastPage = false;
                $scope.firstPage = false;
                $scope.isNext = false;
                $scope.pageNo = $scope.pageNo - 1;
                break;
            default:
                $scope.isPrev = false;
                $scope.lastPage = false;
                $scope.firstPage = true;
                $scope.isNext = false;
                $scope.pageNo = 0;
                break;
        }

        if (!$scope.isFilterApplied) {
            if ($scope.fromDate > $scope.toDate) {
                Notification.info({
                    message: 'FromDate should be less than ToDate ',
                    positionX: 'center',
                    delay: 2000
                });
                return false;
            }
            $http.get("/reports/restaurantInvoicePaymentList?type="+"freemeal", {
                params: {
                    "fromDate": $scope.fromDate,
                    "toDate": $scope.toDate,
                    "firstPage": $scope.firstPage,
                    "lastPage": $scope.lastPage,
                    "pageNo": $scope.pageNo,
                    "prevPage": $scope.prevPage,
                    "invoiceType": $scope.invoiceType,
                    "nextPage": $scope.isNext
                }
            }).then(function (data) {
                $scope.exportList = data.data.data;
                $scope.totalValues($scope.exportList);
                $scope.first = data.data.first;
                $scope.last = data.data.last;
                $scope.prev = data.data.prev;
                $scope.next = data.data.next;
                $scope.pageNo = data.data.pageNo;
            })


        } else {
            if ($scope.fromDate > $scope.toDate) {
                Notification.info({
                    message: 'FromDate should be less than ToDate ',
                    positionX: 'center',
                    delay: 2000
                });
                return false;
            }
            custId = 0;
            toSalesinvoice = 0;
            fromsalesinvoice = 0;
            fromPaymentId=null;
            $scope.invoiceTotal = 0;
            $scope.receivedTotal = 0;
            $scope.totalPersons = 0;
            $scope.salesTaxAmt = 0;
            $scope.discountAmount = 0;
            if (!$scope.isUndefinedOrNull($scope.fromsalesinvoice) && !$scope.isUndefinedOrNull($scope.toSalesinvoice)) {
                toSalesinvoice = $scope.toSalesinvoice.sqid;
                fromsalesinvoice = $scope.fromsalesinvoice.sqid;
            }
            if (!$scope.isUndefinedOrNull($scope.paymentId)) {
                fromPaymentId = $scope.paymentId.sqid;
            }
            $http.get("/reports/restaurantInvoicePaymentList?type="+"freemeal", {
                params: {
                    "fromDate": $scope.fromDate,
                    "toDate": $scope.toDate,
                    "fromSID": fromsalesinvoice,
                    "toSID": toSalesinvoice,
                    "selectedFilter": $scope.selectedFilter,
                    "customerId": $scope.customerId,
                    "employeeId": $scope.employeeId,
                    "selectedList": $scope.selectedList,
                    "paymentId":fromPaymentId,
                    "itemId": $scope.item,
                    "invoiceType": $scope.invoiceType,
                    "status": $scope.statusText,
                    "firstPage": $scope.firstPage,
                    "lastPage": $scope.lastPage,
                    "prevPage": $scope.isPrev,
                    "nextPage": $scope.isNext,
                    "filterApplied": $scope.isFilterApplied
                }
            }).then(function (data) {
                $scope.exportList = data.data.data;
                $scope.totalValues($scope.exportList);
                $scope.first = data.data.first;
                $scope.last = data.data.last;
                $scope.prev = data.data.prev;
                $scope.next = data.data.next;
                $scope.pageNo = data.data.pageNo;
            })
        };

    }
    $scope.getPaginatedSalesInvoice("firstPage");

    $scope.selectedList = [];
    $scope.selectedEmployeeList = function (employeeName) {
        $scope.selectedList.push(employeeName);
    }

    $scope.getEmployeeList = function (val) {
        $scope.isAllSelected = false;
        $scope.selectedList = [];
        $(".loader").css("display", "block");
        if (angular.isUndefined(val)) {
        }
        $http.post($scope.hiposServerURL +'/getEmployeeList?employeeSearchText=' + val).then(function (response) {
            var data = response.data;
            console.log(data);
            $scope.employeeList = angular.copy(data);
            $("#selectEmployee").modal('show');
            $scope.employeeSearchText = val;
            $scope.searchText = val;
        }, function (error) {
            Notification.error({
                message: 'Something went wrong, please try again',
                positionX: 'center',
                delay: 2000
            });
        })
    };
    $scope.appendEmployee = function (employeeId) {
        $scope.employeeSearchText = employeeId.employeeName;
        $scope.employeeId = employeeId.employeeId;
        $scope.employee = $scope.employeeId;
        $scope.showEmailBox = false;
        $("#selectEmployee").modal('hide');

    }
    $scope.updateEmployeeId = function (newItemVal) {
        $scope.employee = newItemVal.employeeId;
    }
});




