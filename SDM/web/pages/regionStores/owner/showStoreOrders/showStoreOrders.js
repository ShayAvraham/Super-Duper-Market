var loadedUserStores;
var selectedStoreOptionStr;
var selectedStore;

$(function() {
    loadUserStores();
});

$(function() {
    allIntervals.push(setInterval(loadUserStores, 2000));
});

function loadUserStores()
{
    $.ajax({
        url: "loadUserStores",
        timeout: 2000,
        dataType: 'json',
        error: function(errorObject) {
            $("#error-placeholder").append(errorObject.responseText)
        },
        success: function(data)
        {
            loadedUserStores = data;
            if(data.length === 0)
            {
                appendAlertDiv("No orders to display because you dont own any store.");
            }
            else if(!isAnyStoreHaveOrders())
            {
                appendAlertDiv("No orders to display because customers yet make one in any of your stores.");
            }

            else
            {
                changeStoreOrdersFormVisibility();
                $("#select-store option").remove();
                $.each(data || [], appendToSelectStore);
                $("#select-store").val(selectedStoreOptionStr);

            }
        }
    });
    return false;
}

function appendAlertDiv(message)
{
    $("#no-orders-label").empty();
    $("#no-orders-label").append(message);
    $("#alert-div").removeClass("invisible");
}

function isAnyStoreHaveOrders()
{
    for(store of loadedUserStores)
    {
        if(store.orders.length !== 0)
        {
            return true;
        }
    }
    return false;

}

function changeStoreOrdersFormVisibility()
{
    $("#alert-div").remove();
    $("#store-orders-form").removeClass("invisible");
}

function appendToSelectStore(index,store)
{
    $("#select-store").append("<option>" + "id: "+ store.id + " | " + store.name +"</option>")
}

$("#select-store").on("change", function () {
    selectedStoreOptionStr = $( "#select-store" ).val();
});

$(function() {
    $("#store-orders-form").submit(function() {
        $("#h1-headline").removeClass("invisible");
        $("#orders-table").removeClass("invisible");
        selectedStore = getStore(selectedStoreOptionStr.split(" ")[1]);
        $("#orders-table tbody").empty();
        $("#order-details-div").addClass("invisible");

        selectedStore.orders.forEach(order =>appendToOrdersTable(order));
        return  false;
    });
});


// $("#show-orders-btn").on("click", function () {
//     $("#orders-table").removeClass("invisible");
//     selectedStore = getStore(selectedStoreOptionStr.split(" ")[1]);
//     $("#orders-table tbody").empty();
//     $("#order-details-div").addClass("invisible");
//     selectedStore.orders.forEach(order =>appendToOrdersTable(order));
// });

function getStore(storeID)
{
    for (store of loadedUserStores)
    {
        if(store.id == storeID)
        {
            return store;
        }
    }
    return false;
}

function appendToOrdersTable(order)
{
    var location = "(" + order.orderDestination.x.toString() +"," + order.orderDestination.y.toString() +")";
    var dateArray = order.date.split(" ");
    var date = dateArray[0] + " " + dateArray[1] + " " + dateArray[2] + " ";

    var newRowContent = "<tr>\n" +
        "      <th scope=\"row\">" + order.id + "</th>\n" +
        "      <td >" + date + "</td>\n" +
        "      <td >" + order.customerName + "</td>\n" +
        "      <td>" + location + "</td>\n" +
        "      <td>" + order.amountOfProductsTypes + "</td>\n" +
        "      <td>" + order.costOfAllProducts.toFixed(2) + "</td>\n" +
        "      <td>" + order.deliveryCost.toFixed(2) + "</td>\n" +
        "    </tr>"
    $("#orders-table tbody").append(newRowContent);
}

$(function() {
    $('#orders-table').on("click", 'tbody tr', function(e)
    {
        $(this).addClass('highlight').siblings().removeClass('highlight');
        var selectedOrderID = $(this).closest("tr").find('th').eq(0).text()
        $("#order-details-div").removeClass("invisible");
        loadOrderDetails(selectedOrderID);
    });
});

function loadOrderDetails(selectedOrderID)
{
    $("#orders-details-table tbody").empty()
    for(order of selectedStore.orders)
    {
        if(order.id == selectedOrderID)
        {
            $.map(order.products,function (products,storeID) {
                for(product of products) {
                    appendProductsToOrdersDetailsTable(product)
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

function appendProductsToOrdersDetailsTable(product)
{
    var price = getProductPrice(product.id,selectedStore);

    var newRowContent = "<tr>\n" +
        "      <th scope=\"row\">" + product.id + "</th>\n" +
        "      <td>" + product.name+ "</td>\n" +
        "      <td>" + product.purchaseForm +"</td>\n" +
        "      <td>" + product.amount + "</td>\n" +
        "      <td>" + price + "</td>\n" +
        "      <td>" + (product.amount * price).toFixed(2) + "</td>\n" +
        "      <td>" + "No" + "</td>\n" +
        "    </tr>";

    $("#orders-details-table tbody").append(newRowContent);
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

function appendDiscountsToOrdersDetailsTable(discount,offerProductID)
{
    var amount = discount.amountForOfferProduct[offerProductID];
    var price = discount.priceForOfferProduct[offerProductID];
    var product = getProduct(offerProductID,selectedStore.products);

    var newRowContent = "<tr>\n" +
        "      <th scope=\"row\">" + offerProductID+ "</th>\n" +
        "      <td>" + product.name+ "</td>\n" +
        "      <td>" + product.purchaseForm +"</td>\n" +
        "      <td>" + amount + "</td>\n" +
        "      <td>" + price + "</td>\n" +
        "      <td>" + (amount * price).toFixed(2) + "</td>\n" +
        "      <td>" + "Yes" + "</td>\n" +
        "    </tr>"

    $("#orders-details-table tbody").append(newRowContent);
}


