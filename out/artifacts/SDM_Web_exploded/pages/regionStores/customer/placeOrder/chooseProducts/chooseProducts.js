var loadedProducts;
var selectedStore;
var selectedStoreOptionStr;

$(function() {
    $("#products-form").submit(function() {
        if(checkIfOneProductSelected())
        {
            $.ajax({
                url: "validatePosition",
                data: "xPosition=" + $("#x-location-text").val()
                    + "&" + "yPosition=" + $("#y-location-text").val(),
                timeout: 2000,
                dataType: 'json',
                error: function(errorObject) {
                    $("#error-placeholder").append(errorObject.responseText);
                },
                success: function(data)
                {
                    if(data === false)
                    {
                        $("#table-error-placeholder").empty();
                        $("#table-error-placeholder").append("the location you enter is invalid," +
                            " there already a store on that location.");

                    }
                    else
                    {
                        changePage();
                    }
                }
            });
        }
        return  false;
    });
});

function checkIfOneProductSelected()
{
    if ($('input:checkbox:checked', $("#products-table")).length === 0)
    {
        $("#table-error-placeholder").empty();
        $("#table-error-placeholder").append("please choose a least one products");
        return false;
    }
    return true;
}

function changePage()
{
    saveFormInputs()
    switch (orderType)
    {
        case "Static":
            clearAllIntervals();
            moveToChooseDiscounts()
            break;
        case "Dynamic":
            $("#main-place-order-container").load('customer/placeOrder/showStoresToBuyFrom/showStoresToBuyFrom.html');
            break;
    }
    return false;
}

function saveFormInputs()
{
    orderType = $("#order-type").val();
    xPosition = $("#x-location-text").val();
    yPosition = $("#y-location-text").val();
    deliveryDate = $("#date").val();

    $('input:checkbox:checked', $("#products-table")).each(function() {
        var productID = $(this).closest('tr').find('td').eq(0).text();
        productsAmounts[productID] = $(this).closest('tr').find('td input').val();
    }).get();

    $('input:checkbox:checked', $("#products-table")).each(function() {
        selectedProducts.push(findProduct($(this).closest('tr').find('td').eq(0).text()));
    }).get();
}

function findProduct(productID)
{
    for(loadedProduct of loadedProducts)
    {
        if(loadedProduct.id == productID)
        {
            return loadedProduct;
        }
    }
    return false;
}

function moveToChooseDiscounts()
{
    var storeToBuyFrom = {store:selectedStore, products:selectedProducts};
    storesToBuyFrom.push(storeToBuyFrom);
    $("#main-place-order-container").load('customer/placeOrder/chooseDiscounts/chooseDiscounts.html');
}




$(function() {
    loadAllProducts()
});

function loadAllProducts()
{
    $.ajax({
        url: "loadProducts",
        timeout: 2000,
        dataType: 'json',
        error: function(errorObject) {
            $("#error-placeholder").append(errorObject.responseText)
        },
        success: function(data)
        {
            loadedProducts = data;
            $("#products-table tbody").empty();
            $.each(data || [], appendToProductsTable);
        }
    });
    return false;
}

function appendToProductsTable(index,product)
{
    var amountInput = product.purchaseForm === "WEIGHT" ?
        "<input type=\"number\" class = \"form-control\" id=\"product-amount\" min = '0.1' step= '0.1' value='1'>\n"
        :
        "<input type=\"number\" class = \"form-control\" id=\"product-amount\" min = '1' step= '1' value='1'>\n";
    var newRowContent = "<tr>\n" +
        "      <th scope=\"row\">\n" +
        "<input type=\"checkbox\" value=\"\" id=\"select-product\">\n"+
        "</th>\n" +
        "      <td>" + product.id+ "</td>\n" +
        "      <td>" + product.name +"</td>\n" +
        "      <td>" + product.purchaseForm + "</td>\n" +
        "      <td>\n" + amountInput + "</td>" +
        "    </tr>"
    $("#products-table tbody").append(newRowContent);
}




$(function() {
    $("#order-type").on("change", function () {
        $("#static-order-row").empty()
        if($("#order-type").val()==="Static")
        {
            createStaticOrderRow();
            loadPossibleStore();
            allIntervals.push(setInterval(loadPossibleStore, 2000));
            $("#products-table tbody").empty();
        }
        else if($("#order-type").val()==="Dynamic")
        {
            clearAllIntervals();
            loadAllProducts();
        }
    });
});


function createStaticOrderRow() {
    $("#static-order-row").append("<div class = \"col\">\n" +
     "                    <label for=\"select-store\">Store To Buy From</label>\n" +
     "                    <select class=\"form-control select\" id=\"select-store\">\n" +
                            "<option id='place'>" + "Choose..." +"</option>\n" +
     "                    </select>\n" +
    "                    <label id=\"delivery-cost-label\">Store To Buy From</label>\n" +
        "                </div>" +
    "                    <div class = \"col\">\n" +
        "                    <label for=\"delivery-cost-label\">Delivery Cost</label>\n" +
    "                        <input type=\"text\" class = \"form-control\" id=\"delivery-cost-text\" readonly>\n" +
"                        </div>")


    $("#select-store").on("change", function () {
        selectedStoreOptionStr =  $( "#select-store" ).val();
        $("#select-store option[id='place']").remove();
        loadStoreProducts()
    });
}

function loadStoreProducts() {
    $.ajax({
        url: "loadStore",
        timeout: 2000,
        dataType: 'json',
        data: "storeID=" + $( "#select-store" ).val().split(" ")[1],
        error: function (errorObject) {
            $("#error-placeholder").append(errorObject.responseText)
        },
        success: function (data) {
            selectedStore = data;
            loadedProducts = data.products;
            appendToDeliveryCostText();
            $("#products-table tbody").empty();
            $.each(data.products || [], appendToProductsTable);
        }
    });
};

function appendToDeliveryCostText()
{
    if($("#delivery-cost-text").length>0 && $("#x-location-text").val()>0
        && $("#y-location-text").val()>0  && typeof selectedStore !== 'undefined')
    {
        var distance = Math.sqrt(Math.pow((selectedStore.position.x - $("#x-location-text").val()), 2) +
            Math.pow((selectedStore.position.y - $("#y-location-text").val()), 2));
        $("#delivery-cost-text").val((selectedStore.ppk * distance).toFixed(2));
    }
    return false;
}

$("#x-location-text").on("change", function () {
    appendToDeliveryCostText()
});

$("#y-location-text").on("change", function () {
    appendToDeliveryCostText()
});



function loadPossibleStore()
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
            $("#select-store option[id!='place']").remove();
            $.each(data || [], appendToSelectStore);
            $("#select-store").val(selectedStoreOptionStr);
        }
    });
}

function appendToSelectStore(index,store)
{
    $("#select-store").append("<option>" + "id: "+ store.id + " | " + store.name +"</option>")
}

