var loadedOrders;
var loadedStores;
var loadedProducts

$(function() {
    loadUserOrders();
    loadStores();
    loadProducts();
});

function loadUserOrders()
{
    $.ajax({
        url: "loadUserOrders",
        timeout: 2000,
        dataType: 'json',
        error: function(errorObject) {
            $("#error-placeholder").append(errorObject.responseText)
        },
        success: function(data)
        {
           if(data.length === 0)
           {
                appendNoOrdersExist();
            }
           else
           {
               loadedOrders = data;
               $("#orders-table tbody").empty();
               $.each(data || [], appendToOrdersTable);
           }
        }
    });
    return false;
}

function appendNoOrdersExist()
{
    $("#tablesContainer").empty();
    $("#tablesContainer").append("<div class=\"alert alert-info\" role=\"alert\">\n" +
        "No orders no display because you yet make one" +
        "</div>")
}

function loadStores()
{
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
}

function loadProducts()
{
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
}

function appendToOrdersTable(index,order)
{

    var location = "(" + order.orderDestination.x.toString() +"," + order.orderDestination.y.toString() +")";
    var dateArray = order.date.split(" ");
    var date = dateArray[0] + " " + dateArray[1] + " " + dateArray[2] + " "; 
    var numberOfStoresOrderedFrom = 0;
    $.map(order.products,function (value,key) {numberOfStoresOrderedFrom++});

    var newRowContent = "<tr>\n" +
        "      <th scope=\"row\">" + order.id + "</th>\n" +
        "      <td >" + date + "</td>\n" +
        "      <td>" + location + "</td>\n" +
        "      <td>" + numberOfStoresOrderedFrom +"</td>\n" +
        "      <td>" + order.amountOfProductsTypes + "</td>\n" +
        "      <td>" + order.costOfAllProducts.toFixed(2) + "</td>\n" +
        "      <td>" + order.deliveryCost.toFixed(2) + "</td>\n" +
        "      <td>" + order.totalCost.toFixed(2) + "</td>\n" +
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
       loadOrderDetails(selectedOrderID);
    });
});


function createOrderDetailsTable()
{
    $("<h2>Order Details </h2>").appendTo($("#tablesContainer"));
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
        "    </table>").appendTo($("#tablesContainer"));
}



function loadOrderDetails(selectedOrderID)
{
    $("#orders-details-table tbody").empty()
    for(order of loadedOrders)
    {
        if(order.id == selectedOrderID)
        {
            $.map(order.products,function (products,storeID) {
                for(product of products) {
                    appendProductsToOrdersDetailsTable(product, storeID)
                }
            });
            $.map(order.discounts,function (discounts,storeID) {
                for(discount of discounts) {
                    $.map(discount.priceForOfferProduct, function (offerProductPrice, offerProductID) {
                        appendDiscountsToOrdersDetailsTable(discount, offerProductID, storeID);
                    });
                }
            });
            return false;
        }
    }
}

function appendProductsToOrdersDetailsTable(product,storeID)
{
    var store = getStore(storeID);
    var price = getProductPrice(product.id,store);
    var store = "id: " + storeID + " | " + store.name;

    var newRowContent = "<tr>\n" +
        "      <th scope=\"row\">" + product.id + "</th>\n" +
        "      <td>" + product.name+ "</td>\n" +
        "      <td>" + product.purchaseForm +"</td>\n" +
        "      <td>" + store +"</td>\n" +
        "      <td>" + product.amount + "</td>\n" +
        "      <td>" + price + "</td>\n" +
        "      <td>" + (product.amount * price).toFixed(2) + "</td>\n" +
        "      <td>" + "No" + "</td>\n" +
        "    </tr>";

    $("#orders-details-table tbody").append(newRowContent);
    return false;
}

function appendDiscountsToOrdersDetailsTable(discount,offerProductID,storeID)
{
    var amount = discount.amountForOfferProduct[offerProductID];
    var price = discount.priceForOfferProduct[offerProductID];
    var store = "id: " + storeID + " | " + getStore(storeID).name;
    var product = getProduct(offerProductID,loadedProducts);

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

function getProduct(productID,products)
{
    for (product of products)
    {
        if(product.id == productID)
        {
            return product;
        }
    }
    return false;
}

function getProductPrice(productID,store)
{
    var pricePerStore = getProduct(productID,store.products).pricePerStore;
    var price = $.map(pricePerStore,function (value,key)
    {
        if(key == store.id)
        {
            return value;
        }
    });
    return price[0];
}

