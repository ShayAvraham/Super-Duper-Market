$(function() {
    $.ajax({
        url: "loadDynamicAllocation",
        type: "POST",
        data: "selectedProducts=" + JSON.stringify(selectedProducts),
        timeout: 2000,
        dataType: 'json',
        error: function(errorObject) {
            $("#error-placeholder").append(errorObject.responseText)
        },
        success: function(data)
        {
            storesToBuyFrom = data
            $.each(data || [], appendToStoresToBuyFromTable);
        }
    });
});



function appendToStoresToBuyFromTable(index,storeToBuyFrom)
{

    var location = "(" + storeToBuyFrom.store.position.x.toString()
        +"," + storeToBuyFrom.store.position.y.toString() +")"
    var distance = Math.sqrt(Math.pow((storeToBuyFrom.store.position.x-xPosition),2)+
        Math.pow((storeToBuyFrom.store.position.y-yPosition),2));
    var deliveryCost = storeToBuyFrom.store.ppk*distance

    var productsCost = getProductsCost(storeToBuyFrom.products,storeToBuyFrom.store)

    var newRowContent = "<tr>\n" +
        "      <th scope=\"row\">\n" + storeToBuyFrom.store.id  +"</th>\n" +
        "      <td>" + storeToBuyFrom.store.name + "</td>\n" +
        "      <td>" + location + "</td>\n" +
        "      <td>" + distance.toFixed(2) + "</td>\n" +
        "      <td>" + storeToBuyFrom.store.ppk + "</td>\n" +
        "      <td>" + deliveryCost.toFixed(2) + "</td>\n" +
        "      <td>" + storeToBuyFrom.products.length + "</td>\n" +
        "      <td>" + productsCost + "</td>\n" +
        "    </tr>"
    $("#stores-to-buy-from-table").append(newRowContent);
}

function getProductsCost(products,store)
{
    var price = 0
    for (product of products)
    {
        price += getProductCost(product,store)

    }
    return price
}

function getProductCost(product,store)
{
    var price = $.map(product.pricePerStore,function (value,key) {
        if(key == store.id)
        {
            return value
        }
    })
    // return parseInt(price)*productsAmounts[product.id]
    return parseInt(price)*productsAmounts[product]
}


$(function() {
    $("#stores-to-buy-from-form").submit(function() {
        $("#main-place-order-container").empty()
        $("#main-place-order-container").load('customer/placeOrder/chooseDiscounts/chooseDiscounts.html');
    });
});
