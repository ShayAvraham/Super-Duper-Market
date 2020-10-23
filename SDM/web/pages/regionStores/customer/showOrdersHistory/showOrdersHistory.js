var loadedOrders;
var loadedStores;
var loadedProducts

$(function() {
    console.log("orders history page");
    $.ajax({
        url: "loadUserOrders",
        timeout: 2000,
        dataType: 'json',
        error: function(errorObject) {
            $("#error-placeholder").append(errorObject.responseText)
        },
        success: function(data)
        {
            loadedOrders = data
            $.each(data || [], appendToOrdersTable);
        }
    });
    return false;
});

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
            loadedStores = data;
        }
    });
    return false;
});

$(function() {
    $.ajax({
        url: "loadProducts",
        timeout: 2000,
        dataType: 'json',
        error: function(errorObject) {
            $("#error-placeholder").append(errorObject.responseText)
        },
        success: function(data) {
           loadedProducts = data
        }
    });
    return false;
});

function appendToOrdersTable(index,order)
{
    var location = "(" + order.orderDestination.x.toString() +"," + order.orderDestination.y.toString() +")"
    var newRowContent = "<tr>\n" +
        "      <th scope=\"row\">" + order.id + "</th>\n" +
        "      <td >" + order.date+ "</td>\n" +
        "      <td>" + location + "</td>\n" +
        "      <td>" + order.products.length +"</td>\n" +
        "      <td>" + order.amountOfProductsTypes + "</td>\n" +
        "      <td>" + order.costOfAllProducts + "</td>\n" +
        "      <td>" + order.deliveryCost + "</td>\n" +
        "      <td>" + order.amountOfProductsTypes + "</td>\n" +
        "    </tr>"
    $("#orders-table tbody").append(newRowContent);
}


$(function() {
    $('#orders-table').on("click", 'tbody tr', function(e)
    {
        $(this).addClass('highlight').siblings().removeClass('highlight');
        var selectedOrderID = $(this).closest("tr").find('th').eq(0).text()
        if(!$("#orders-details-table").length)
        {
            createOrderDetailsTable();
        }
        loadOrderDetails(selectedOrderID)
    });
});


function createOrderDetailsTable()
{
    // $("#orders-details-table").empty();
    $("<h2>Order Details </h2>").appendTo($("#tablesContainer"))
    $("    <table class=\"table\" id =\"orders-details-table\">\n" +
        "        <thead>\n" +
        "        <tr>\n" +
        "            <th scope=\"col\">I.D</th>\n" +
        "            <th scope=\"col\">Name</th>\n" +
        "            <th scope=\"col\">Purchase Form</th>\n" +
        "            <th scope=\"col\">Buy From Store</th>\n" +
        "            <th scope=\"col\">Amount</th>\n" +
        "            <th scope=\"col\">Price</th>\n" +
        "            <th scope=\"col\">Total Price</th>\n" +
        "            <th scope=\"col\">Is Discount</th>\n" +
        "        </tr>\n" +
        "        </thead>\n" +
        "        <tbody/> "+
        "    </table>").appendTo($("#tablesContainer"))
}



function loadOrderDetails(selectedOrderID)
{
    $("#orders-details-table").empty()
    for(order of loadedOrders)
    {
        if(order.id == selectedOrderID)
        {
            $.map(order.products,function (product,storeID) {
                appendProductsToOrdersDetailsTable(product,storeID)
            });
            $.map(order.discounts,function (discount,storeID) {
                $.map(discount.priceForOfferProduct ,function (offerProductPrice,offerProductID)
                {
                    appendDiscountsToOrdersDetailsTable(discount,offerProductID,storeID);
                });
            });
            return false;
        }
    }
}

function appendProductsToOrdersDetailsTable(product,storeID)
{
    var price = product.pricePerStore[storeID];
    var store = "id: " + storeID + " | " + getStore(storeID).name;

    var newRowContent = "<tr>\n" +
        "      <th scope=\"row\">" + product.id + "</th>\n" +
        "      <td>" + product.name+ "</td>\n" +
        "      <td>" + product.purchaseForm +"</td>\n" +
        "      <td>" + store +"</td>\n" +
        "      <td>" + product.amount + "</td>\n" +
        "      <td>" + price + "</td>\n" +
        "      <td>" + (product.amount * price).toFixed(2) + "</td>\n" +
        "      <td>" + "No" + "</td>\n" +
        "    </tr>"

    $("#orders-details-table tbody").append(newRowContent);
}

function appendDiscountsToOrdersDetailsTable(discount,offerProductID,storeID)
{
    var amount = discount.amountForOfferProduct[offerProductID];
    var price = discount.priceForOfferProduct[offerProductID];
    var store = "id: " + storeID + " | " + getStore(storeID).name;
    var product = getProduct(offerProductID);

    var newRowContent = "<tr>\n" +
        "      <th scope=\"row\">" + offerProductID+ "</th>\n" +
        "      <td>" + product.name+ "</td>\n" +
        "      <td>" + product.purchaseForm +"</td>\n" +
        "      <td>" + store +"</td>\n" +
        "      <td>" + amount + "</td>\n" +
        "      <td>" + price + "</td>\n" +
        "      <td>" + (amount * price).toFixed(2) + "</td>\n" +
        "      <td>" + "Yes" + "</td>\n" +
        "    </tr>"

    $("#orders-details-table tbody").append(newRowContent);
}

function getStore(storeID)
{
    for (store of loadedStores)
    {
        if(store.id == storeID)
        {
            return store;
        }
    }
    return false;
}

function getProduct(productID)
{
    for (product of loadedStores)
    {
        if(product.id == productID)
        {
            return product;
        }
    }
    return false;
}
