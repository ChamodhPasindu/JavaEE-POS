generateOrderId();
loadAllCustomerIds();
loadAllItemIds();

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
    // loadAllOrderTable();

});

$("#btnDeleteOrder").click(function (){
    var orderId = $("#homeOrderId").val();
    var response = searchOrder(orderId);

    let index = orderId.indexOf(response);
    let res = confirm("Do you really need to delete this order ?");
    if (res) {
        deleteOrder(index);
    }
});

$("#btnOrderSearch").click(function (){
    var searchID = $("#txtOrderSearch").val();

    var response = searchOrder(searchID);
    if (response) {
        $("#homeOrderId").val(response.getOrderId());
        $("#homeOrderDate").val(response.getOrderDate());
        $("#homeDiscount").val(response.getOrderDiscount());
        $("#homeCost").val(response.getOrderTotal());
        $("#homeCustomerName").val(findCustomerName(response.getOrderCusId()));

        $("#btnDeleteOrder").prop('disabled', false);
    } else {
        clearAllOrderDetails();
        alert("No such a Order")
    }
});
//END ORDER BTN FUNCTION

//START ORDER CRUD OPERATIONS
function loadAllCustomerIds() {
    $("#cmbCustomerId").empty();
    $.ajax({
        url: "customer?option=GET_ALL_ID",
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
        url: "item?option=GET_ALL_ID",
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
        url: "order?option=GET_ID",
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
        url: "order" ,
        method: "POST",
        contentType:"application/json",
        data:JSON.stringify(orderObj),
        success: function (res) {
            if (res.status == 200) {
                alert(res.message);
                generateOrderId();
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

    // $("#txtOrderCount").text(orderDB.length);

}

function updateItemQty(id,qty) {
    for (var i=0;i< itemDB.length;i++){
        if (itemDB[i].getItemCode()==id){
            let itemQty = itemDB[i].getItemQty();
            itemDB[i].setItemQty((+itemQty)-(+qty))
        }
    }
}

function saveOrderDetails() {
    let orderId=$("#txtOrderId").val();
    for (var i of tempDB){
        var orderDetail=OrderDetailDTO(orderId,i.itemId,i.qty,i.total);
        orderDetailsDB.push(orderDetail);
        updateItemQty(i.itemId,i.qty);
    }
}

function clearAllDetails() {
    $("#txtOrderCustomerName,#txtOrderSalary,#txtOrderAddress,#txtOrderItemName,#txtOrderItemPrice,#txtOrderQtyOnHand,#txtOrderQty,#txtTotal,#txtSubTotal,#txtCash,#txtDiscount,#txtBalance").val("");
    $("#btnAddItem,#btnPlaceOrder").prop('disabled',true);
    $("#txtDiscount,#txtCash").prop('disabled',true);
    tempDB.splice(0,tempDB.length);
    $("#addItemTable").empty()
}

function findCustomerName(id) {
    for (var i=0;i<customerDB.length;i++){
        if (customerDB[i].getCustomerId()==id){
            return customerDB[i].getCustomerName();
        }
    }
}

function loadAllOrderTable() {
    $("#allOrderTable").empty();
    for (var i of orderDB){
        var orderId=i.getOrderId();
        var date =i.getOrderDate();
        var discount=i.getOrderDiscount();
        var cost=i.getOrderTotal();
        var cusId=i.getOrderCusId();

        let customerName = findCustomerName(cusId);
        let row = `<tr><td>${orderId}</td><td>${date}</td><td>${customerName}</td><td>${discount}</td><td>${cost}</td></tr>`;
        $("#allOrderTable").append(row);

    }
}

function searchOrder(searchID) {
    for (var i = 0; i < orderDB.length; i++) {
        if (orderDB[i].getOrderId() == searchID) {
            return orderDB[i];
        }
    }
}

function clearAllOrderDetails() {
    $('#txtOrderSearch,#homeOrderId,#homeOrderDate,#homeCustomerName,#homeDiscount,#homeCost').val("");
    $("#btnDeleteOrder").prop('disabled', true);

}

function deleteOrder(index) {
    orderDB.pop(index);
    clearAllOrderDetails();
    loadAllOrderTable();

    $("#txtOrderCount").text(orderDB.length);

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
        url: "customer?option=SEARCH&cusId="+customerId,
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
        url: "item?option=SEARCH&itemId="+itemId,
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








