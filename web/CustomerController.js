loadAllCustomer();

//START CUSTOMER VALIDATION
var regExCusID = /^(C00-)[0-9]{3,4}$/;
var regExCusName = /^[A-z\s+]{5,50}$/;
var regExCusAddress = /^[A-z\s+]{5,50}$/;
var regExCusSalary = /^[0-9]*([.][0-9]{2})$/;

//START NEW CUSTOMER VALIDATION
function checkNewCustomerValidation() {
    let inputCusId = $("#txtCusId").val();
    let inputCusName = $("#txtCusName").val();
    let inputCusAddress = $("#txtCusAddress").val();
    let inputCusSalary = $("#txtCusSalary").val();

    if (regExCusID.test(inputCusId)) {
        if (regExCusName.test(inputCusName)) {
            if (regExCusAddress.test(inputCusAddress)) {
                if (regExCusSalary.test(inputCusSalary)) {
                    $("#btnCustomerSave").prop('disabled', false);
                }
            }
        }
    }
}

$("#txtCusId").keyup(function () {
    let inputCusId = $("#txtCusId").val();
    if (regExCusID.test(inputCusId)) {
        $("#txtCusId").css('border', '2px solid blue');
        $("#lblCusId").text("");

        checkNewCustomerValidation()
        $("#txtCusId").keydown(function (event) {
            if (event.key == "Enter") {
                $("#txtCusName").focus();
            }
        });
    } else {
        $("#txtCusId").css('border', '2px solid red');
        $("#lblCusId").text("Cus ID is a required field : Pattern C00-000");
        $("#btnCustomerSave").prop('disabled', true);

    }
});

$("#txtCusName").keyup(function () {
    let inputCusName = $("#txtCusName").val();
    if (regExCusName.test(inputCusName)) {
        $("#txtCusName").css('border', '2px solid blue');
        $("#lblCusName").text("");

        checkNewCustomerValidation()
        $("#txtCusName").keydown(function (event) {
            if (event.key == "Enter") {
                $("#txtCusAddress").focus();
            }
        });

    } else {
        $("#txtCusName").css('border', '2px solid red');
        $("#lblCusName").text("Cus Name is a required field : Mimimum 5, Max 20, Spaces Allowed");

        $("#btnCustomerSave").prop('disabled', true);

    }
});

$("#txtCusAddress").keyup(function () {
    let inputCusAddress = $("#txtCusAddress").val();
    if (regExCusAddress.test(inputCusAddress)) {
        $("#txtCusAddress").css('border', '2px solid blue');
        $("#lblCusAddress").text("");

        checkNewCustomerValidation()
        $("#txtCusAddress").keydown(function (event) {
            if (event.key == "Enter") {
                $("#txtCusSalary").focus();
            }
        });

    } else {
        $("#txtCusAddress").css('border', '2px solid red');
        $("#lblCusAddress").text("Cus Name is a required field : Mimum 5");

        $("#btnCustomerSave").prop('disabled', true);

    }
});

$("#txtCusSalary").keyup(function () {
    let inputCusSalary = $("#txtCusSalary").val();
    if (regExCusSalary.test(inputCusSalary)) {
        $("#txtCusSalary").css('border', '2px solid blue');
        $("#lblCusSalary").text("");

        checkNewCustomerValidation()
        $("#txtCusSalary").keydown(function (event) {
            if (event.key == "Enter") {
                $("#btnCustomerSave").focus();
            }
        });
    } else {
        $("#txtCusSalary").css('border', '2px solid red');
        $("#lblCusSalary").text("Cus Salary is a required field : Pattern 100.00");

        $("#btnCustomerSave").prop('disabled', true);
    }
});
//END NEW CUSTOMER VALIDATION

//START UPDATE CUSTOMER VALIDATION
function checkUpdateCustomerValidation() {
    let inputCusName = $("#customerName").val();
    let inputCusAddress = $("#customerAddress").val();
    let inputCusSalary = $("#customerSalary").val();

    if (regExCusName.test(inputCusName)) {
        if (regExCusAddress.test(inputCusAddress)) {
            if (regExCusSalary.test(inputCusSalary)) {
                $("#btnCustomerUpdate").prop('disabled', false);
            }
        }
    }
}

$("#customerName").keyup(function () {
    let inputCusName = $("#customerName").val();
    if (regExCusName.test(inputCusName)) {
        $("#customerName").css('border', '2px solid blue');
        checkUpdateCustomerValidation()
        $("#customerName").keydown(function (event) {
            if (event.key == "Enter") {
                $("#customerAddress").focus();
            }
        });

    } else {
        $("#customerName").css('border', '2px solid red');
        $("#btnCustomerUpdate").prop('disabled', true);

    }
});

$("#customerAddress").keyup(function () {
    let inputCusAddress = $("#customerAddress").val();
    if (regExCusAddress.test(inputCusAddress)) {
        $("#customerAddress").css('border', '2px solid blue');
        checkUpdateCustomerValidation()
        $("#customerAddress").keydown(function (event) {
            if (event.key == "Enter") {
                $("#customerSalary").focus();
            }
        });

    } else {
        $("#customerAddress").css('border', '2px solid red');
        $("#btnCustomerUpdate").prop('disabled', true);

    }
});

$("#customerSalary").keyup(function () {
    let inputCusSalary = $("#customerSalary").val();
    if (regExCusSalary.test(inputCusSalary)) {
        $("#customerSalary").css('border', '2px solid blue');
        checkUpdateCustomerValidation()
        $("#customerSalary").keydown(function (event) {
            if (event.key == "Enter") {
                $("#btnCustomerUpdate").focus();
            }
        });
    } else {
        $("#customerSalary").css('border', '2px solid red');
        $("#btnCustomerUpdate").prop('disabled', true);
    }
});
//END UPDATE CUSTOMER VALIDATION

//END CUSTOMER VALIDATION


//START CUSTOMER BTN FUNCTIONS
$("#btnCustomerSave").click(function () {

    saveCustomer();
    clearAll();
    loadAllCustomerIds();

});

$("#btnCustomerSearch").click(function () {
    searchCustomer();
});

$("#btnCustomerDelete").click(function () {
    let res = confirm("Do you really need to delete this customer ?");
    if (res) {
        deleteCustomer();
    }
});

$("#btnCustomerUpdate").click(function () {
    updateCustomer();
});
//END CUSTOMER BTN FUNCTIONS


//START CUSTOMER CRUD OPERATIONS

function saveCustomer() {

    var serialize = $("#customerForm").serialize();
    console.log(serialize);

    $.ajax({
        url: "customer",
        method: "POST",
        data: serialize,
        success: function (res) {
            if (res.status == 200) {
                alert(res.message);
                loadAllCustomer();
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

function deleteCustomer() {
    let customerId = $("#customerId").val();

    $.ajax({
        url: "customer?CusId=" + customerId,
        method: "DELETE",
        success: function (res) {
            alert(res);
            loadAllCustomer();
        }
    });
    clearAll();
}

function updateCustomer() {
    let formData = {
        id:$("#customerId").val() ,
        name:$("#customerName").val() ,
        salary:$("#customerSalary").val() ,
        address:$("#customerAddress").val()
    }
    $.ajax({
        url: "customer" ,
        method: "PUT",
        contentType:"application/json",
        data:JSON.stringify(formData),
        success: function (res) {
            if (res.status == 200) { // process is  ok
                alert(res.message);
                loadAllCustomer();
            } else if (res.status == 400) { // there is a problem with the client side
                alert(res.message);
            } else {
                alert(res.data); // else maybe there is an exception
            }
        },
        error: function (ob, errorStus) {
            console.log(ob); // other errors
        }
    });
    clearAll();

}

function searchCustomer() {
    let customerId = $("#txtCustomerSearch").val();
    $.ajax({
        url: "customer?option=SEARCH&cusId="+customerId,
        method: "GET",
        success: function (res) {
            if (res.status == 200) {
                for (const customer of res.data) {
                    $("#customerId").val(customer.id);
                    $("#customerName").val(customer.name);
                    $("#customerSalary").val(customer.salary.toFixed(2));
                    $("#customerAddress").val(customer.address);
                }

                $('#customerName,#customerSalary,#customerAddress').prop('disabled', false);
                $("#btnCustomerDelete").prop('disabled', false);
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

function clearAll() {
    $('#txtCusId,#txtCusName,#txtCusAddress,#txtCusSalary,#txtCustomerSearch').val("");
    $('#customerId,#customerName,#customerAddress,#customerSalary').val("");

    $('#txtCusId,#txtCusName,#txtCusAddress,#txtCusSalary').css('border', '2px solid #ced4da');
    $('#customerId,#customerName,#customerAddress,#customerSalary').css('border', '2px solid #ced4da');

    $('#txtCusId').focus();

    $('#btnCustomerSave,#btnCustomerUpdate,#btnCustomerDelete').prop('disabled', true);
    $('#customerName,#customerSalary,#customerAddress').prop('disabled', true);

}

function loadAllCustomer() {
    $("#customerTable").empty();

    $.ajax({
        url: "customer?option=GET_ALL_DETAILS",
        method: "GET",
        success: function (resp) {
            for (const customer of resp.data) {
                let row = `<tr><td>${customer.id}</td><td>${customer.name}</td><td>${customer.salary.toFixed(2)}</td><td>${customer.address}</td></tr>`;
                $("#customerTable").append(row);
            }
        }
    });
}


//END CUSTOMER CRUD OPERATIONS
