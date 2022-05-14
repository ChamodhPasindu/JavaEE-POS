loadAllItem();
getItemCount();
//START ITEM VALIDATION

var regExItemID = /^(I00-)[0-9]{3,4}$/;
var regExItemName = /^[A-z\s+]{3,50}$/;
var regExItemQty = /^[1-9][0-9]*([.][0-9]{2})?$/;
var regExItemPrice = /^[1-9][0-9]*([.][0-9]{2})?$/;

//START NEW ITEM VALIDATION
function checkNewItemValidation() {
    let inputItemId = $("#txtItemId").val();
    let inputItemName = $("#txtItemName").val();
    let inputItemQty = $("#txtItemQty").val();
    let inputItemPrice = $("#txtItemPrice").val();

    if (regExItemID.test(inputItemId)) {
        if (regExItemName.test(inputItemName)) {
            if (regExItemQty.test(inputItemQty)) {
                if (regExItemPrice.test(inputItemPrice)) {
                    $("#btnItemSave").prop('disabled', false);
                }
            }
        }
    }
}

$("#txtItemId").keyup(function () {
    let inputItemId = $("#txtItemId").val();
    if (regExItemID.test(inputItemId)) {
        $("#txtItemId").css('border', '2px solid blue');
        $("#lblItemId").text("");

        checkNewItemValidation();
        $("#txtItemId").keydown(function (event) {
            if (event.key == "Enter") {
                $("#txtItemName").focus();
            }
        });
    } else {
        $("#txtItemId").css('border', '2px solid red');
        $("#lblItemId").text("Item ID is a required field : Pattern I00-000");
        $("#btnItemSave").prop('disabled', true);

    }
});

$("#txtItemName").keyup(function () {
    let inputItemName = $("#txtItemName").val();
    if (regExItemName.test(inputItemName)) {
        $("#txtItemName").css('border', '2px solid blue');
        $("#lblItemName").text("");

        checkNewItemValidation();
        $("#txtItemName").keydown(function (event) {
            if (event.key == "Enter") {
                $("#txtItemPrice").focus();
            }
        });

    } else {
        $("#txtItemName").css('border', '2px solid red');
        $("#lblItemName").text("Item Name is a required field : Mimimum 5, Max 20, Spaces Allowed");

        $("#btnItemSave").prop('disabled', true);

    }
});

$("#txtItemPrice").keyup(function () {
    let inputItemPrice = $("#txtItemPrice").val();
    if (regExItemPrice.test(inputItemPrice)) {
        $("#txtItemPrice").css('border', '2px solid blue');
        $("#lblItemPrice").text("");

        checkNewItemValidation();
        $("#txtItemPrice").keydown(function (event) {
            if (event.key == "Enter") {
                $("#txtItemQty").focus();
            }
        });

    } else {
        $("#txtItemPrice").css('border', '2px solid red');
        $("#lblItemPrice").text("Item Price is a required field : Pattern 100.00 or 100");

        $("#btnItemSave").prop('disabled', true);

    }
});

$("#txtItemQty").keyup(function () {
    let inputItemQty = $("#txtItemQty").val();
    if (regExItemQty.test(inputItemQty)) {
        $("#txtItemQty").css('border', '2px solid blue');
        $("#lblItemQty").text("");

        checkNewItemValidation()
        $("#txtItemQty").keydown(function (event) {
            if (event.key == "Enter") {
                $("#btnItemSave").focus();
            }
        });
    } else {
        $("#txtItemQty").css('border', '2px solid red');
        $("#lblItemQty").text("Item Qty is a required field : Mimum 1");

        $("#btnItemSave").prop('disabled', true);
    }
});
//END NEW ITEM VALIDATION

//START UPDATE ITEM VALIDATION
function checkUpdateItemValidation() {
    let inputItemName = $("#itemName").val();
    let inputItemPrice = $("#itemPrice").val();
    let inputItemQty = $("#itemQtyOnHand").val();

    if (regExItemName.test(inputItemName)) {
        if (regExItemQty.test(inputItemQty)) {
            if (regExItemPrice.test(inputItemPrice)) {
                $("#btnItemUpdate").prop('disabled', false);
            }
        }
    }
}

$("#itemName").keyup(function () {
    let inputItemName = $("#itemName").val();
    if (regExItemName.test(inputItemName)) {
        $("#itemName").css('border', '2px solid blue');
        checkUpdateItemValidation()
        $("#itemName").keydown(function (event) {
            if (event.key == "Enter") {
                $("#itemQtyOnHand").focus();
            }
        });

    } else {
        $("#itemName").css('border', '2px solid red');
        $("#btnItemUpdate").prop('disabled', true);

    }
});

$("#itemQtyOnHand").keyup(function () {
    let inputItemQty = $("#itemQtyOnHand").val();
    if (regExItemQty.test(inputItemQty)) {
        $("#itemQtyOnHand").css('border', '2px solid blue');
        checkUpdateItemValidation()
        $("#itemQtyOnHand").keydown(function (event) {
            if (event.key == "Enter") {
                $("#itemPrice").focus();
            }
        });

    } else {
        $("#itemQtyOnHand").css('border', '2px solid red');
        $("#btnItemUpdate").prop('disabled', true);

    }
});

$("#itemPrice").keyup(function () {
    let inputItemPrice = $("#itemPrice").val();
    if (regExItemPrice.test(inputItemPrice)) {
        $("#itemPrice").css('border', '2px solid blue');
        checkUpdateItemValidation()
        $("#itemPrice").keydown(function (event) {
            if (event.key == "Enter") {
                $("#btnItemUpdate").focus();
            }
        });
    } else {
        $("#itemPrice").css('border', '2px solid red');
        $("#btnCustomerUpdate").prop('disabled', true);
    }
});
//END UPDATE ITEM VALIDATION

//END ITEM VALIDATION


//START ITEM BTN FUNCTIONS
$("#btnItemSave").click(function () {
    saveItem();
    clearAllItemDetails();


});

$("#btnItemSearch").click(function () {
        searchItem();
});

$("#btnItemUpdate").click(function () {
    updateItem();
});

$("#btnItemDelete").click(function () {
    let res = confirm("Do you really need to delete this Item ?");
    if (res) {
        deleteItem();
    }
});
//END ITEM BTN FUNCTIONS

//START ITEM CRUD OPERATIONS
function saveItem() {
    var serialize = $("#itemForm").serialize();

    $.ajax({
        url: "http://localhost:8080/artifact07/item",
        method: "POST",
        data: serialize,
        success: function (res) {
            if (res.status == 200) {
                alert(res.message);
                loadAllItem();
                loadAllItemIds();
                getItemCount();
            } else {
                alert(res.data);
            }
        },
        error: function (ob, textStatus, error) {
            console.log(ob);
            console.log(textStatus);
            console.log(error);
        }
    })

}

function clearAllItemDetails() {
    $('#txtItemId,#txtItemName,#txtItemQty,#txtItemPrice,#txtItemSearch').val("");
    $('#itemId,#itemName,#itemQtyOnHand,#itemPrice').val("");

    $('#txtItemId,#txtItemName,#txtItemQty,#txtItemPrice').css('border', '2px solid #ced4da');
    $('#itemId,#itemName,#itemQtyOnHand,#itemPrice').css('border', '2px solid #ced4da');

    $('#txtItemId').focus();

    $('#btnItemSave,#btnItemUpdate,#btnItemDelete').prop('disabled', true);
    $('#itemName,#itemPrice,#itemQtyOnHand').prop('disabled', true);

}

function loadAllItem() {
    $("#itemTable").empty();

    $.ajax({
        url: "http://localhost:8080/artifact07/item?option=GET_ALL_DETAILS",
        method: "GET",
        success: function (resp) {
            for (const item of resp.data) {
                let row = `<tr><td>${item.id}</td><td>${item.name}</td><td>${item.qty}</td><td>${item.price.toFixed(2)}</td></tr>`;
                $("#itemTable").append(row);
            }
        }
    });
}

function searchItem() {
    let itemId = $("#txtItemSearch").val();
    $.ajax({
        url: "http://localhost:8080/artifact07/item?option=SEARCH&itemId="+itemId,
        method: "GET",
        success: function (res) {
            if (res.status == 200) {
                for (const item of res.data) {
                    $("#itemId").val(item.id);
                    $("#itemName").val(item.name);
                    $("#itemPrice").val(item.price.toFixed(2));
                    $("#itemQtyOnHand").val(item.qty);
                }

                $('#itemName,#itemPrice,#itemQtyOnHand').prop('disabled', false);
                $("#btnItemDelete").prop('disabled', false);
            } else {
                alert("Wrong ID inserted");
            }
        },
        error: function (ob, textStatus, error) {
            console.log(ob);
            console.log(textStatus);
            console.log(error);
        }
    });
}

function updateItem() {
    let formData = {
        id:$("#itemId").val() ,
        name:$("#itemName").val() ,
        price:$("#itemPrice").val() ,
        qty:$("#itemQtyOnHand").val()
    }
    $.ajax({
        url: "http://localhost:8080/artifact07/item" ,
        method: "PUT",
        contentType:"application/json",
        data:JSON.stringify(formData),
        success: function (res) {
            if (res.status == 200) {
                alert(res.message);
                loadAllItem();
                clearAllItemDetails();
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

function deleteItem() {
    let itemId = $("#itemId").val();

    $.ajax({
        url: "http://localhost:8080/artifact07/item?itemId=" + itemId,
        method: "DELETE",
        success: function (res) {
            alert(res);
            loadAllItem();
            getItemCount()
        }
    });
    clearAllItemDetails();

}

function getItemCount() {
    $.ajax({
        url: "http://localhost:8080/artifact07/item?option=COUNT",
        method: "GET",
        success: function (res) {
            if (res.status == 200) {
                for (const item of res.data) {
                    $("#txtItemCount").text(item.count);
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

//END ITEM CRUD OPERATIONS