/*
 data will arrive in the next form:
 {
    "products": [
        {
            "id":1,
            "name":"moshe",
            "purchaseForm":WEIGHT
            "numberOfStoresSellProduct":1,
            "averagePrice":25.7,
            "numOfProductWasOrdered":0
        },...
    ]
 }
 */

$(function() {
    $("#btnProducts").click(function () {
        $.ajax({
            url: "loadProducts",
            timeout: 2000,
            dataType: 'json',
            error: function(errorObject) {
                $("#error-placeholder").append(errorObject.responseText)
            },
            success: function(data) {
                createProductsTable();
                $.each(data || [], appendToProductsTable);

            }
        });
        return false;
    })


});

function createProductsTable()
{
    $("#tablesContainer").empty();
    $("<h2>Products</h2>").appendTo($("#tablesContainer"))
    $("    <table class=\"table\" id =\"productsTable\">\n" +
        "        <thead>\n" +
        "        <tr>\n" +
        "            <th scope=\"col\">I.D</th>\n" +
        "            <th scope=\"col\">Name</th>\n" +
        "            <th scope=\"col\">Purchase Form</th>\n" +
        "            <th scope=\"col\">Number Of Stores That Sold</th>\n" +
        "            <th scope=\"col\">Average Price</th>\n" +
        "            <th scope=\"col\">Sold Amount</th>\n" +
        "        </tr>\n" +
        "        </thead>\n" +
        "        <tbody/> "+
        "    </table>").appendTo($("#tablesContainer"))
}

function appendToProductsTable(index,product)
{
    var newRowContent = "<tr>\n" +
        "      <th scope=\"row\">" + product.id + "</th>\n" +
        "      <td>" + product.name+ "</td>\n" +
        "      <td>" + product.purchaseForm +"</td>\n" +
        "      <td>" + product.numberOfStoresSellProduct + "</td>\n" +
        "      <td>" + product.averagePrice + "</td>\n" +
        "      <td>" + product.numOfProductWasOrdered + "</td>\n" +
        "    </tr>"

    $("#productsTable tbody").append(newRowContent);
}

/*
 data will arrive in the next form:
 {
    "stores": [
        {
            "id":1,
            "name":"super1",
            "ownerName":"moshe"
            "position":(1,3),
            "ppk":25,
            "totalIncomeFromDeliveries":0
            "totalIncomeFromProducts":0
            "products":[{,...,}],
            "orders":[{,...,}],
            "discounts":[{,...,}]
        },...
    ]
 }
 */


$(function() {
    $("#btnStores").click(function () {
        $.ajax({
            url: "loadStores",
            timeout: 2000,
            dataType: 'json',
            error: function(errorObject) {
                $("#error-placeholder").append(errorObject.responseText)
            },
            success: function(data)
            {
               // createTabs()
                createStoresTable();
                $.each(data || [], appendToStoreTable);
                createStoreProductsTable();
                $.each(data || [], appendToStoreProductsTable);
                // $('table tr td a').click(function(){
                //     var selectedRow = $(this).closest("td").text();
                // });
                $('#tablesContainer').on("click", "#storesTable", function()
                {
                    var id = $(this).closest("tr").find('#name1').text()
                    var selectedRow = $(this).closest("tr").find('td:eq(2)').text()
               //     $("#productsTab").not('.active').removeClass("disabled")

                });

            }
        });
        return false;
    })
});

// function createTabs()
// {
//     $("#tablesContainer").empty();
//     $("<ul class=\"nav nav-tabs\">\n" +
//         "  <li class=\"nav-item active\" >\n" +
//         "    <a class=\"nav-link\" href=\"#\">Active</a>\n" +
//         "  </li>\n" +
//         "  <li class=\"nav-item disabled\" id=\"productsTab\" >\n" +
//         "    <a class=\"nav-link\" href=\"#\">Products</a>\n" +
//         "  </li>" +
//         "</ul>" ).appendTo($("#tablesContainer"))
// }

function createStoresTable()
{
    $("#tablesContainer").empty();
    $("    <table class=\"table table-hover\" id =\"storesTable\">\n" +
        "        <thead class=\"thead-dark\">\n" +
        "        <tr>\n" +
        "            <th scope=\"col\" id=\"name1 \">I.D</th>\n" +
        "            <th scope=\"col\">Name</th>\n" +
        "            <th scope=\"col\">Owner</th>\n" +
        "            <th scope=\"col\">Location</th>\n" +
        "            <th scope=\"col\">PPK</th>\n" +
        "            <th scope=\"col\">Number Of Orders</th>\n" +
        "            <th scope=\"col\">Total Ordered Products Cost</th>\n" +
        "            <th scope=\"col\">Total Orders Income</th>\n" +
        "        </tr>\n" +
        "        </thead>\n" +
        "        <tbody/> "+
        "    </table>").appendTo($("#tablesContainer"))
}

function appendToStoreTable(index,store)
{
    var location = "(" + store.position.x.toString() +"," + store.position.y.toString() +")"
    var newRowContent = "<tr>\n" +
        "      <th scope=\"row\">" + store.id + "</th>\n" +
        "      <td>" + store.name+ "</td>\n" +
        "      <td>" + store.ownerName + "</td>\n" +
        "      <td>" + location +"</td>\n" +
        "      <td>" + store.ppk + "</td>\n" +
        "      <td>" + store.orders.length + "</td>\n" +
        "      <td>" + store.totalIncomeFromProducts + "</td>\n" +
        "      <td>" + store.totalIncomeFromDeliveries + "</td>\n" +
        "    </tr>"

    $("#storesTable tbody").append(newRowContent);
}

function createStoreProductsTable()
{
    $("<h2>Store Products </h2>").appendTo($("#tablesContainer"))
    $("    <table class=\"table table-hover\" id =\"storeProductsTable\">\n" +
        "        <thead class=\"thead-dark\">\n" +
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

function appendToStoreProductsTable(index,product)
{
    // var newRowContent = "<tr>\n" +
    //     "      <th scope=\"row\">" + product.id + "</th>\n" +
    //     "      <td>" + product.name+ "</td>\n" +
    //     "      <td>" + product.purchaseForm +"</td>\n" +
    //     "      <td>" + product.numberOfStoresSellProduct + "</td>\n" +
    //     "      <td>" + product.averagePrice + "</td>\n" +
    //     "      <td>" + product.numOfProductWasOrdered + "</td>\n" +
    //     "    </tr>"
    //
    // $("#productsTable tbody").append(newRowContent);
}


