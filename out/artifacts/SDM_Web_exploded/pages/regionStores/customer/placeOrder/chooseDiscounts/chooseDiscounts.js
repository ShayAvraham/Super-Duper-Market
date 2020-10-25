var loadedDiscounts;

$(function() {
    $.ajax({
        url: "loadAvailableDiscounts",
        type:"POST",
        data: "storesToBuyFrom=" + JSON.stringify(convertToIntegerToProductCollectionMap(storesToBuyFrom))
            + "&" + "productsAmounts=" + JSON.stringify(convertToIntegerToFloatMap(productsAmounts)),
        timeout: 2000,
        dataType: 'json',
        error: function(errorObject) {
            $("#error-placeholder").append(errorObject.responseText)
        },
        success: function(data)
        {
            loadedDiscounts = data;
            if(data.length === 0)
            {
                $("#main-place-order-container").empty();
                $("#main-place-order-container").load('customer/placeOrder/orderSummary/orderSummary.html');
            }
            $.each(data || [], appendToDiscountsTable);
        }
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

function appendToDiscountsTable(index,availableDiscounts)
{
    availableDiscounts.discounts.forEach(discount => {
        var newRowContent = "<tr>\n" +
            "      <th scope=\"row\">\n" +
            "<input type=\"checkbox\" value=\"\" id=\"select-discount\">\n"+
                "</th>\n" +
            "      <td>" + availableDiscounts.store.name + "</td>\n" +
            "      <td>" + discount.discountName + "</td>\n" +
            "      <td>" + discount.ifYouBuyDescription + "</td>\n" +
            "      <td>" + discount.thenYouGetDescription + "</td>\n" +
            "      <td>" + createSelectOfferTD(availableDiscounts.store,discount) + " </td>\n" +

            "    </tr>"
        $("#discounts-table").append(newRowContent);
    });
}

function createSelectOfferTD(store,discount)
{
        var selectOfferTD = "";
        if(discount.discountType == "ONE_OF")
        {
            var offers = createOfferOptions(store, discount.priceForOfferProduct);
            selectOfferTD = "<select class=\"form-control select\" id=\"select-offer\" required>\n" + offers;
        }
        return selectOfferTD;
}

function createOfferOptions(store,priceForOfferProduct)
{
    var offers = $.map(priceForOfferProduct,function (value,key) {
        return key;
    })

    var offersProducts = createOfferProducts(store,offers);
    var options = "";

    for(offer of offersProducts)
    {
        options += "<option>" + "id: "+ offer.id + " | " + offer.name +"</option>";
    }
    return options;
}

function createOfferProducts(store,offers)
{
    var offersProducts = [];
    store.products.forEach(product =>
    {
        if(offers.includes(product.id.toString()))
        {
            offersProducts.push(product);
        }
    });
    return offersProducts
}



$(function() {
    $("#discounts-form").submit(function()
    {
        selectedDiscounts = [];
        $('input:checkbox:checked', $("#discounts-table")).each(function () {
            var storeName = $(this).closest('tr').find('td').eq(0).text();
            var selectedOfferID;
            if($(this).closest('tr').find('td select').length)
            {
                selectedOfferID = $(this).closest('tr').find('td select').val().split(" ")[1];
            }
            var discountName = $(this).closest('tr').find('td').eq(1).text();
            addToSelectedDiscounts(storeName,discountName,selectedOfferID)
        }).get();
        if(selectedDiscountsValidated())
        {
            $("#main-place-order-container").empty();
            $("#main-place-order-container").load('customer/placeOrder/orderSummary/orderSummary.html');
        }
        else
        {
            $("#discounts-error-placeholder").empty();
            $("#discounts-error-placeholder").append("You chosen to many discounts, please reselect discounts");
            return false;
        }
    });
});

function addToSelectedDiscounts(storeName ,discountName, selectedOfferID )
{
    for(loadedDiscount of loadedDiscounts)
    {
        if(loadedDiscount.store.name == storeName)
        {
            var store = loadedDiscount.store;
            for(discount of loadedDiscount.discounts)
            {
                if (discount.discountName == discountName)
                {
                    if(discount.discountType == "ONE_OF")
                    {
                        discount.selectedOfferID = selectedOfferID;
                    }
                    var selectedDiscount = {storeID:store.id, discount:discount};
                    selectedDiscounts.push(selectedDiscount);
                    return false;
                }
            }
        }
    }
}

function selectedDiscountsValidated()
{
    for (product of selectedProducts)
    {
        var sum = 0;
        for(selectedDiscount of selectedDiscounts )
        {
            if(selectedDiscount.discount.discountProduct.id == product.id)
            {
                sum += selectedDiscount.discount.amountForDiscount;
            }
            if(sum>productsAmounts[product.id])
            {
                return false;
            }
        }
    }
    return true;


}