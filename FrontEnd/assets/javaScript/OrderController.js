generateOrderId();
loadAllCustomerIds();
loadAllItemIds();
getOrderCount();
loadAllOrderTable();

tempDB=new Array();

//Set current date
var now = new Date();
var day = ("0" + now.getDate()).slice(-2);
var month = ("0" + (now.getMonth() + 1)).slice(-2);
var today = now.getFullYear() + "-" + (month) + "-" + (day);
$('#txtOrderDate').val(today);

//START ORDER BTN FUNCTION
$("#btnAddItem").click(function () {
    let itemID=$("#cmbItemId").val();
    let name=$("#txtOrderItemName").val();
    var price=$("#txtOrderItemPrice").val();
    var qtyOnHand=$("#txtOrderQtyOnHand").val();
    var qty=$("#txtOrderQty").val();
    var itemTotal=price*qty;

    let previousItemTotal = $("#txtTotal").val();
    var total=(+itemTotal)+(+previousItemTotal);


    let item = checkItemExist(itemID);
    if (item) {
        if ((+item.qty)+(+qty)<=qtyOnHand){
            item.qty=(+item.qty)+(+qty);
            item.total=(+item.total)+(+itemTotal);

            $("#txtTotal").val(total.toFixed(2));
        }else {
            alert("Numbers of order quantity are exceed the limit");
        }

    }else {
        if (+qty <=qtyOnHand) {
            var tempObj = {
                itemId: itemID,
                name: name,
                price: price,
                qty: qty,
                total: itemTotal
            }
            tempDB.push(tempObj);
            $("#txtTotal").val(total.toFixed(2));
        } else {
            alert("Numbers of order quantity are exceed the limit");
        }
    }
    loadCart();
    $("#txtDiscount").prop('disabled',false);


});

$("#btnPlaceOrder").click(function () {
    placeOrder();
    clearAllDetails();


});

$("#btnDeleteOrder").click(function (){
    let res = confirm("Do you really need to delete this order ?");
    if (res) {
        deleteOrder();
    }
});

$("#btnOrderSearch").click(function (){
    searchOrder();
});
//END ORDER BTN FUNCTION

//START ORDER CRUD OPERATIONS
function loadAllCustomerIds() {
    $("#cmbCustomerId").empty();
    $.ajax({
        url: "http://localhost:8080/artifact07/customer?option=GET_ALL_ID",
        method: "GET",
        success: function (resp) {
            for (var customer of resp.data) {
                let id = `<option>${customer.id}</option>`
                $("#cmbCustomerId").append(id);
            }
        }
    });

}

function loadAllItemIds() {
    $("#cmbItemId").empty();

    $.ajax({
        url: "http://localhost:8080/artifact07/item?option=GET_ALL_ID",
        method: "GET",
        success: function (resp) {
            for (var item of resp.data) {
                let code = `<option>${item.id}</option>`
                $("#cmbItemId").append(code);
            }
        }
    });


}

function generateOrderId() {
    $.ajax({
        url: "http://localhost:8080/artifact07/order?option=GET_ID",
        method: "GET",
        success: function (resp) {
            for (const order of resp.data) {
                $("#txtOrderId").val(order.id);
            }
        }
    })

}

function loadCart() {
    $("#addItemTable").empty();

    for (var i of tempDB){
        let row = `<tr><td>${i.itemId}</td><td>${i.name}</td><td>${i.price}</td><td>${i.qty}</td><td>${i.total}</td></tr>`;
        $("#addItemTable").append(row);
    }
}

function checkItemExist(id) {
    for (var i = 0; i < tempDB.length; i++) {
        if (tempDB[i].itemId == id) {
            return tempDB[i];
        }
    }
}

function placeOrder() {
    let orderId = $("#txtOrderId").val();
    let cusId = $("#cmbCustomerId").val();
    let date = $("#txtOrderDate").val();
    let total = $("#txtSubTotal").val();
    let discount = $("#txtDiscount").val();

    let orderObj={
        orderDetail:tempDB,
        orderId:orderId,
        custId:cusId,
        date:date,
        cost:total,
        discount:discount
    }
    $.ajax({
        url: "http://localhost:8080/artifact07/order" ,
        method: "POST",
        contentType:"application/json",
        data:JSON.stringify(orderObj),
        success: function (res) {
            if (res.status == 200) {
                alert(res.message);
                generateOrderId();
                getOrderCount();
                loadAllOrderTable();
            } else if (res.status == 400) {
                alert(res.message);
            } else {
                alert(res.data);
            }
        },
        error: function (ob, errorStus) {
            console.log(ob);
        }
    });


}

function clearAllDetails() {
    $("#txtOrderCustomerName,#txtOrderSalary,#txtOrderAddress,#txtOrderItemName,#txtOrderItemPrice,#txtOrderQtyOnHand,#txtOrderQty,#txtTotal,#txtSubTotal,#txtCash,#txtDiscount,#txtBalance").val("");
    $("#btnAddItem,#btnPlaceOrder").prop('disabled',true);
    $("#txtDiscount,#txtCash").prop('disabled',true);
    tempDB.splice(0,tempDB.length);
    $("#addItemTable").empty()
}

function loadAllOrderTable() {
    $("#allOrderTable").empty();

    $.ajax({
        url: "http://localhost:8080/artifact07/order?option=GET_ALL_DETAILS",
        method: "GET",
        success: function (resp) {
            for (const order of resp.data) {
                let row = `<tr><td>${order.id}</td><td>${order.date}</td><td>${order.name}</td><td>${order.discount}</td><td>${order.cost.toFixed(2)}</td></tr>`;
                $("#allOrderTable").append(row);
            }
        }
    });
}

function searchOrder() {
    var searchID = $("#txtOrderSearch").val();
    $.ajax({
        url: "http://localhost:8080/artifact07/order?option=SEARCH&orderId=" + searchID,
        method: "GET",
        success: function (res) {
            if (res.status == 200) {
                for (const order of res.data) {
                    $("#homeOrderId").val(order.id);
                    $("#homeOrderDate").val(order.date);
                    $("#homeDiscount").val(order.discount);
                    $("#homeCost").val(order.cost.toFixed(2));
                    $("#homeCustomerName").val(order.name);
                }
                $("#btnDeleteOrder").prop('disabled', false);

            } else {
                alert("Wrong ID inserted");
                clearAllOrderDetails();
            }
        },
        error: function (ob, textStatus, error) {
            console.log(ob);
            console.log(textStatus);
            console.log(error);
        }
    });
}

function clearAllOrderDetails() {
    $('#txtOrderSearch,#homeOrderId,#homeOrderDate,#homeCustomerName,#homeDiscount,#homeCost').val("");
    $("#btnDeleteOrder").prop('disabled', true);

}

function deleteOrder() {

    var orderId = $("#homeOrderId").val();
    console.log(orderId);
    $.ajax({
        url: "http://localhost:8080/artifact07/order?orderId=" + orderId,
        method: "DELETE",
        success: function (res) {
            alert(res);
            loadAllOrderTable();
            clearAllOrderDetails();
            getOrderCount();
        }
    });
}

function getOrderCount() {
    $.ajax({
        url: "http://localhost:8080/artifact07/order?option=COUNT",
        method: "GET",
        success: function (res) {
            if (res.status == 200) {
                for (const order of res.data) {
                    $("#txtOrderCount").text(order.count);
                }
            }
        },
        error: function (ob, textStatus, error) {
            console.log(ob);
            console.log(textStatus);
            console.log(error);
        }
    });
}
//END ORDER CRUD OPERATIONS


//START ORDER KEY FUNCTION
function activeAddItemBtn() {
    if ($("#txtOrderItemName").val().length!==0 && $("#txtOrderQty").val().length!==0) {
        $("#btnAddItem").prop('disabled', false);
    }
}

function activePurchaseBtn() {
    if ($("#txtOrderCustomerName").val().length!==0 && $("#txtBalance").val().length!==0 && tempDB.length!==0){
        $("#btnPlaceOrder").prop('disabled', false);
    }else {
        $("#btnPlaceOrder").prop('disabled', true);
    }
}

$("#txtOrderQty").keyup(function (){
    if ($("#txtOrderQty").val() >0){
        activeAddItemBtn();
    }else{
        $("#btnAddItem").prop('disabled', true);
    }
});

$("#txtOrderDate").keyup(function (){
    activePurchaseBtn();
});

$("#txtCash").keyup(function (event){
    if (event.key=="Enter"){
        if ($("#txtCash").val().length!==0){
            var cash=$("#txtCash").val();
            var subTotal=$("#txtSubTotal").val();

            var balance = (+cash)-(+subTotal);
            $("#txtBalance").val(balance.toFixed(2));

            activePurchaseBtn();
        }
    }
});

$("#txtDiscount").keyup(function (event){
    if (event.key=="Enter"){
        if ($("#txtDiscount").val().length!==0){
            var total=$("#txtTotal").val();
            var discount=$("#txtDiscount").val();

            var subTotal=total-(total*(discount/100));
            $("#txtSubTotal").val(subTotal.toFixed(2));
            $("#txtCash").prop('disabled',false);

        }
    }
});

$("#txtBalance").keyup(function (){
    activePurchaseBtn();
});

$("#cmbCustomerId").click(function () {
    var customerId = $("#cmbCustomerId").val();

    $.ajax({
        url: "http://localhost:8080/artifact07/customer?option=SEARCH&cusId="+customerId,
        method: "GET",
        success: function (res) {
            if (res.status == 200) {
                for (const customer of res.data) {
                    $("#txtOrderCustomerName").val(customer.name);
                    $("#txtOrderSalary").val(customer.salary.toFixed(2));
                    $("#txtOrderAddress").val(customer.address);
                }
            }
            activePurchaseBtn();
        },
        error: function (ob, textStatus, error) {
            console.log(ob);
            console.log(textStatus);
            console.log(error);
        }
    });
});

$("#cmbItemId").click(function () {
    var itemId = $("#cmbItemId").val();

    $.ajax({
        url: "http://localhost:8080/artifact07/item?option=SEARCH&itemId="+itemId,
        method: "GET",
        success: function (res) {
            if (res.status == 200) {
                for (const customer of res.data) {
                    $("#txtOrderItemName").val(customer.name);
                    $("#txtOrderItemPrice").val(customer.price.toFixed(2));
                    $("#txtOrderQtyOnHand").val(customer.qty);
                }
            }
            activeAddItemBtn();
        },
        error: function (ob, textStatus, error) {
            console.log(ob);
            console.log(textStatus);
            console.log(error);
        }
    });
});




//END ORDER KEY FUNCTION








