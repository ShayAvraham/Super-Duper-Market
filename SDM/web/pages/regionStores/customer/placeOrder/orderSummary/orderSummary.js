var selectedStoreID;
var deliveryCost = 0;
var productsCost = 0;
var totalOrderCost = 0;

$(function() {
    appendToOrderCostSummary();
    $.each(storesToBuyFrom || [], appendToStoreSummaryTable);
});

function appendToOrderCostSummary()
{
    calculateDeliveryAndAllProductsCost();
    totalOrderCost = deliveryCost + productsCost;
    appendToOrderCostSummaryRow();
}

function calculateDeliveryAndAllProductsCost()
{
    for(storeToBuyFrom of storesToBuyFrom)
    {
        var distance = Math.sqrt(Math.pow((storeToBuyFrom.store.position.x - xPosition), 2) +
            Math.pow((storeToBuyFrom.store.position.y - yPosition), 2));
        deliveryCost += storeToBuyFrom.store.ppk * distance;
        calculateProductsCost(storeToBuyFrom);
    }
    calculateDiscountsCost();
}

function calculateProductsCost(storeToBuyFrom)
{
    for(product of storeToBuyFrom.products)
    {
        productsCost += getProductPrice(product.pricePerStore,storeToBuyFrom.store.id)*getProductAmount(product);
    }
}

function calculateDiscountsCost()
{
    for(selectedDiscount of selectedDiscounts)
    {
        if(selectedDiscount.discount.discountType == "ONE_OF")
        {
            calculateOfferCost(selectedDiscount.discount.selectedOfferID,selectedDiscount)
        }
        else
        {
            $.map(selectedDiscount.discount.priceForOfferProduct,function (value,key) {
                calculateOfferCost(key,selectedDiscount)
            })
        }
    }
}

function calculateOfferCost(offerID,selectedDiscount)
{
    var offerProduct = getOfferProduct(offerID, selectedDiscount.storeID);
    productsCost += parseFloat(getOfferProductAmount(offerProduct, selectedDiscount.discount.amountForOfferProduct))
        * parseFloat(getOfferProductPrice(offerProduct, selectedDiscount.discount.priceForOfferProduct));
}

function appendToOrderCostSummaryRow()
{
    $("#products-cost-text").val(productsCost.toFixed(2));
    $("#delivery-cost-text").val(deliveryCost.toFixed(2));
    $("#order-total-cost-text").val(totalOrderCost.toFixed(2));
}

function appendToStoreSummaryTable(index, storeToBuyFrom)
{
    var store = storeToBuyFrom.store;
    var distance = Math.sqrt(Math.pow((store.position.x-xPosition),2)+
        Math.pow((store.position.y-yPosition),2));
    var deliveryCost = store.ppk*distance
    var newRowContent = "<tr>\n" +
        "      <th scope=\"row\">" + store.id +"</th>\n" +
        "      <td>" + store.name+ "</td>\n" +
        "      <td>" + store.ppk +"</td>\n" +
        "      <td>" + distance.toFixed(2) + "</td>\n" +
        "      <td>\n"+ deliveryCost.toFixed(2) + "</td>" +
        "    </tr>"
    $("#stores-summary-table tbody").append(newRowContent);
}

$(function() {
    $('#stores-summary-table').on("click", 'tbody tr', function(e)
    {
        $(this).addClass('highlight').siblings().removeClass('highlight');
        selectedStoreID = $(this).closest("tr").find('th').eq(0).text()
        if(!$("#store-products-summary-table").length) {
            createStoreProductsSummaryTable();
        }
        $("#store-products-summary-table tbody").empty()
        $.each(storesToBuyFrom || [], appendProductsToProductsSummaryTable);
        $.each(selectedDiscounts || [], appendDiscountsToProductsSummaryTable);
    });
});

function createStoreProductsSummaryTable()
{
    $("#store-products-summary-table").empty()
    $("<h2>Store Products </h2>").appendTo($("#products-summary-table-row"))
    $("    <table class=\"table\" id =\"store-products-summary-table\">\n" +
        "        <thead>\n" +
        "        <tr>\n" +
        "            <th scope=\"col\">I.D</th>\n" +
        "            <th scope=\"col\">Name</th>\n" +
        "            <th scope=\"col\">Purchase Form</th>\n" +
        "            <th scope=\"col\">Amount</th>\n" +
        "            <th scope=\"col\">Price</th>\n" +
        "            <th scope=\"col\">Total Price</th>\n" +
        "            <th scope=\"col\">Is Discount</th>\n" +
        "        </tr>\n" +
        "        </thead>\n" +
        "        <tbody/> "+
        "    </table>").appendTo($("#products-summary-table-row"))
}

function appendProductsToProductsSummaryTable(index, storeToBuyFrom)
{
    if(storeToBuyFrom.store.id == selectedStoreID)
    {
        storeToBuyFrom.products.forEach(product => appendProductToProductsSummaryTable(product));
    }
}

function appendProductToProductsSummaryTable(product)
{
    var price = getProductPrice(product.pricePerStore,selectedStoreID);
    var amount = getProductAmount(product);

    var newRowContent = "<tr>\n" +
        "      <th scope=\"row\">" + product.id + "</th>\n" +
        "      <td>" + product.name+ "</td>\n" +
        "      <td>" + product.purchaseForm +"</td>\n" +
        "      <td>" + amount + "</td>\n" +
        "      <td>" + price + "</td>\n" +
        "      <td>" + (amount * price).toFixed(2) + "</td>\n" +
        "      <td>" + "No" + "</td>\n" +
        "    </tr>"

    $("#store-products-summary-table tbody").append(newRowContent);
}



function appendDiscountsToProductsSummaryTable(index,selectedDiscount)
{
    if (selectedDiscount.storeID == parseInt(selectedStoreID))
    {
        if(selectedDiscount.discount.discountType == "ONE_OF")
        {
            appendDiscountToProductsSummaryTable(selectedDiscount.discount,selectedDiscount.discount.selectedOfferID);
        }
        else
        {
            $.map(selectedDiscount.discount.priceForOfferProduct,function (value,key) {
                appendDiscountToProductsSummaryTable(selectedDiscount.discount,key)
            })
        }
    }
}

function getProductAmount(product)
{
    var amount = $.map(productsAmounts,function (value,key) {
        if(key == product.id)
        {
            return value;
        }
    });
    return amount;
}

function getProductPrice(pricePerStore,storeID)
{
    var price = $.map(pricePerStore,function (value,key) {
        if(key == storeID)
        {
            return value;
        }
    });
    return price;
}


function appendDiscountToProductsSummaryTable(discount,offerID)
{
    var product = getOfferProduct(offerID,selectedStoreID);
    var price = getOfferProductPrice(product,discount.priceForOfferProduct);
    var amount = getOfferProductAmount(product,discount.amountForOfferProduct)

    var newRowContent = "<tr>\n" +
        "      <th scope=\"row\">" + product.id + "</th>\n" +
        "      <td>" + product.name+ "</td>\n" +
        "      <td>" + product.purchaseForm +"</td>\n" +
        "      <td>" + amount + "</td>\n" +
        "      <td>" + price + "</td>\n" +
        "      <td>" + (amount * price).toFixed(2) + "</td>\n" +
        "      <td>" + "Yes" + "</td>\n" +
        "    </tr>"

    $("#store-products-summary-table tbody").append(newRowContent);
}

function getOfferProduct(offerID,storeID)
{
    for(storeToBuyFrom of storesToBuyFrom)
    {
        if (storeID == storeToBuyFrom.store.id)
        {
            for(product of storeToBuyFrom.store.products)
            {
                if (offerID == product.id)
                {
                    return product;
                }
            }
        }
    }
}

function getOfferProductPrice(offerProduct,priceForOfferProduct)
{
    var price = $.map(priceForOfferProduct,function (value,key) {
        if(key == offerProduct.id)
        {
            return value;
        }
    });
    return price;
}

function getOfferProductAmount(offerProduct,amountForOfferProduct)
{
    var amount = $.map(amountForOfferProduct,function (value,key) {
        if(key == offerProduct.id)
        {
            return value;
        }
    });
    return amount;
}


$(function() {
    $("#order-summary-form").submit(function()
    {
        $.ajax({
            url: "addNewOrder",
            type: "POST",
            timeout: 2000,
            dataType: 'json',
            data: "storesToBuyFrom=" + JSON.stringify(convertToIntegerToProductCollectionMap(storesToBuyFrom))
                + "&" + "productsAmounts=" + JSON.stringify(convertToIntegerToFloatMap(productsAmounts))
                + "&" + "selectedDiscounts=" + JSON.stringify(convertToIntegerToDiscountsCollectionMap(selectedDiscounts))
                + "&" + "deliveryDate=" + deliveryDate
                + "&" + "xDestinationPosition=" + xPosition
                + "&" + "yDestinationPosition=" + yPosition
                + "&" + "orderType=" + orderType
                + "&" + "productsCost=" + productsCost
                + "&" + "deliveryCost=" + deliveryCost
                + "&" + "totalOrderCost=" + totalOrderCost,
            error: function (errorObject) {
                $("#error-placeholder").append(errorObject.responseText);
            },
            success: function (data) {
                $("#main-place-order-container").empty();
                $("#main-place-order-container").load('customer/placeOrder/sendFeedback/sendFeedback.html');
            }
        });
        return false;
    });
});

function convertToIntegerToProductCollectionMap(storesToBuyFrom)
{
    var jsonMap = {};
    for(key of storesToBuyFrom)
    {
        var jsonKey = key.store.id;
        var jsonValues = key.products;
        jsonMap[jsonKey]=jsonValues;
    }

    return jsonMap;
}

function convertToIntegerToFloatMap(productsAmounts)
{
    var jsonMap = {};
    for(key of selectedProducts)
    {
        var jsonKey = parseInt(key.id);
        var jsonValues = parseFloat(productsAmounts[key.id]);
        jsonMap[jsonKey]=jsonValues;
    }
    return jsonMap;
}

function convertToIntegerToDiscountsCollectionMap(selectedDiscounts)
{
    var jsonMap = {};
    for(elem1 of selectedDiscounts)
    {
        var discounts = [];
        for(elem2 of selectedDiscounts)
        {
            if(elem1.storeID == elem2.storeID)
            {
                discounts.push(elem2.discount);
            }
        }
        jsonMap[elem1.storeID] = discounts;
    }
    return jsonMap;
}

$(function() {
    $("#cancel-btn").on("click", function () {
        window.location.replace("");
        return false;
    });
});
