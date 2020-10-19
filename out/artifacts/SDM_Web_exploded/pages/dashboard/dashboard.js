

$(function () {
    $("#users-btn").on("click", function () {
        $("#loader").empty();
        $("#loader").append(UsersContainer);
    });
    $("#stores-btn").on("click", function () {
        $("#loader").empty();
        $("#loader").append(StoreAreasContainer);
    });
    $("#account-btn").on("click", function () {
        $("#loader").empty();
        $("#loader").append(AccountContainer);
    });
});

$(function () {
    $.ajax({
        url: "loggedUser",
        timeout: 2000,
        dataType: "json",
        error: function (errorObject) {
            $("#error-placeholder").text(errorObject.responseText);
        },
        success: function (data) {
            loadUserButtons(data.role)
        }
    });
});

function loadUserButtons(userRole) {
    if (userRole === "owner") {
        createOwnerButtons()
        addOwnerButtonsEvents()
    }
}

function createOwnerButtons() {
    var newLiContent = "                   <li class=\"nav-item\">\n" +
        "                        <a id=\"load-xml-btn\" class=\"nav-link active\">\n" +
        "                            <span data-feather=\"file\"></span>\n" +
        "                            Load XML\n" +
        "                        </a>\n" +
        "                    </li>"

    $("#nav-bar-buttons").prepend(newLiContent);
}

function addOwnerButtonsEvents() {
    $("#load-xml-btn").on("click", function () {
        $("#loader").empty();
        $("#loader").append(selectFileContainer);
    });
}




$(function() {
    $.ajax({
        url: "loadStores",
        timeout: 2000,
        dataType: 'json',
        error: function(errorObject) {
            $("#error-placeholder").append(errorObject.responseText)
        },
        success: function(data)
        {
            $.each(data || [], appendToStoreTable);
        }
    });
    return false;
});

function appendToStoreTable(index,store)
{
    var location = "(" + store.position.x.toString() +"," + store.position.y.toString() +")"
    var newRowContent = "<tr>\n" +
        "      <th scope=\"row\">" + store.id + "</th>\n" +
        "      <td >" + store.name+ "</td>\n" +
        "      <td>" + store.ownerName + "</td>\n" +
        "      <td>" + location +"</td>\n" +
        "      <td>" + store.ppk + "</td>\n" +
        "      <td>" + store.orders.length + "</td>\n" +
        "      <td>" + store.totalIncomeFromProducts + "</td>\n" +
        "      <td>" + store.totalIncomeFromDeliveries + "</td>\n" +
        "    </tr>"
    $("#storesTable tbody").append(newRowContent);
}

$(function() {
    $('#storesTable').on("click", 'tbody tr', function(e)
    {
        $(this).addClass('highlight').siblings().removeClass('highlight');
        var selectedStoreID = $(this).closest("tr").find('th').eq(0).text()
        if(!$("#storeProductsTable").length) {
            createStoreProductsTable();
        }
        loadStoreProducts(selectedStoreID)
    });
});

function createStoreProductsTable()
{
    $("#storeProductsTable").empty()
    $("<h2>Store Products </h2>").appendTo($("#tablesContainer"))
    $("    <table class=\"table\" id =\"storeProductsTable\">\n" +
        "        <thead>\n" +
        "        <tr>\n" +
        "            <th scope=\"col\">I.D</th>\n" +
        "            <th scope=\"col\">Name</th>\n" +
        "            <th scope=\"col\">Purchase Form</th>\n" +
        "            <th scope=\"col\">Price</th>\n" +
        "            <th scope=\"col\">Sold Amount</th>\n" +
        "        </tr>\n" +
        "        </thead>\n" +
        "        <tbody/> "+
        "    </table>").appendTo($("#tablesContainer"))
}

/*
 data will arrive in the next form:
 {
    "products": [
        {
            "id":1,
            "name":"moshe",
            "purchaseForm":WEIGHT
            "pricePerStore":{1:20,...},
            "soldAmountPerStore":{1:5...},
        },...
    ]
 }
 */


function loadStoreProducts(storeID) {
    $.ajax({
        url: "loadStoreProducts",
        timeout: 2000,
        dataType: 'json',
        data: "storeID=" + storeID,
        error: function(errorObject) {
            $("#error-placeholder").append(errorObject.responseText)
        },
        success: function(data)
        {
            $("#storeProductsTable tbody").empty()
            data.forEach(element => appendToStoreProductsTable(element,storeID));
        }
    });
    return false;
}

function appendToStoreProductsTable(product,storeID)
{
    var price = $.map(product.pricePerStore,function (value,key) {
        if(key === storeID)
        {
            return value
        }
    })

    var amount = $.map(product.soldAmountPerStore,function (value,key) {
        if(key === storeID)
        {
            return value
        }
    })

    var newRowContent = "<tr>\n" +
        "      <th scope=\"row\">" + product.id + "</th>\n" +
        "      <td>" + product.name+ "</td>\n" +
        "      <td>" + product.purchaseForm +"</td>\n" +
        "      <td>" + price + "</td>\n" +
        "      <td>" + amount + "</td>\n" +
        "    </tr>"

    $("#storeProductsTable tbody").append(newRowContent);
}

