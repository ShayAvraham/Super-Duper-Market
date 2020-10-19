
$(function() {
    $("#products-form").submit(function() {
        if(checkIfOneProductSelected())
        {
            saveFormInputs()
            switch (orderType)
            {
                case "Static":
                    selectedStore = $("#select-store").val().split(" ")[1]
//                    $("#main-place-order-container").load('customer/placeOrder/showStoresToBuyFrom/showStoresToBuyFrom.html');
                    loadAvailableDiscounts()
                    break;
                case "Dynamic":
                   loadStoresToBuyFrom()
                    break;
            }


        }
        return  false

    });
});

function checkIfOneProductSelected()
{
    if ($('input:checkbox:checked', $("#products-table")).length === 0)
    {
        $("#table-error-placeholder").append("please choose a least one products")
        return false
    }
    return true
}

function saveFormInputs()
{
    orderType = $("#order-type").val()
    xPosition = $("#x-location-text").val()
    yPosition = $("#y-location-text").val()
    deliveryDate = $("#date")
    productsAmounts = getProductsAmounts()
}

function getProductsAmounts()
{
    var productsAmounts = {}
    $('input:checkbox:checked', $("#products-table")).each(function() {
        var productID = $(this).closest('tr').find('td').eq(0).text();
        productsAmounts[productID] = $(this).closest('tr').find('td input').val();
    }).get();
    return productsAmounts
}

function loadAvailableDiscounts()
{
    storesToBuyFrom = {}
    storesToBuyFrom[selectedStore] = getSelectedProducts()
    $.ajax({
        url: "loadAvailableDiscounts",
        type:"POST",
        data: "selectedProducts=" + JSON.stringify(selectedProducts) + "&" + "productsAmounts=" + JSON.stringify(productsAmounts),
        timeout: 2000,
        dataType: 'json',
        error: function(errorObject) {
          $("#error-placeholder").append(errorObject.responseText)
        },
        success: function(data)
        {
            availableDiscounts = data
            $("#main-place-order-container").load('customer/placeOrder/chooseDiscounts/chooseDiscounts.html');
        }
    });
}

function getSelectedProducts()
{
    var selectedProducts = [];
    $('input:checkbox:checked', $("#products-table")).each(function() {
        selectedProducts.push($(this).closest('tr').find('td').eq(0).text());
    }).get();
    return selectedProducts
}

function loadStoresToBuyFrom()
{
    $.ajax({
        url: "loadDynamicAllocation",
        type: "POST",
        data: "selectedProducts=" + JSON.stringify(getSelectedProducts()),
      //  timeout: 12000,
        dataType: 'json',
        error: function(errorObject) {
            $("#error-placeholder").append(errorObject.responseText)
        },
        success: function(data)
        {
            storesToBuyFrom = data
            $("#main-place-order-container").load('customer/placeOrder/showStoresToBuyFrom/showStoresToBuyFrom.html');
        }
    });
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
            $("#products-table tbody").empty();
            $.each(data || [], appendToProductsTable);
        }
    });
    return false;
}

function appendToProductsTable(index,product)
{
    var newRowContent = "<tr>\n" +
        "      <th scope=\"row\">\n" +
        "<input type=\"checkbox\" value=\"\" id=\"select-product\">\n"+
        "</th>\n" +
        "      <td>" + product.id+ "</td>\n" +
        "      <td>" + product.name +"</td>\n" +
        "      <td>" + product.purchaseForm + "</td>\n" +
        "      <td>\n" +
        "      <input type=\"number\" class = \"form-control\" id=\"product-amount\" min = 0.1 step='0.1' value='1'>\n" +
        "</td>" +
        "    </tr>"
    $("#products-table tbody").append(newRowContent);
}




$(function() {
    $("#order-type").on("change", function () {
        $("#static-order-row").empty()
        if($("#order-type").val()==="Static")
        {
            createStaticOrderRow()
            loadPossibleStore()
            $("#products-table tbody").empty();
        }
        else if($("#order-type").val()==="Dynamic")
        {
            loadAllProducts()
        }
    });
});


function createStaticOrderRow() {

    $("#static-order-row").append("                <div class = \"col\">\n" +
     "                    <label for=\"select-store\">Store To Buy From</label>\n" +
     "                    <select class=\"form-control select\" id=\"select-store\">\n" +
                        "<option id='place'>" + "Choose..." +"</option>\n" +
     "                    </select>\n" +
     "                </div>")


    $("#select-store").on("change", function () {
        $("#select-store option[id='place']").remove();
        loadStoreProducts()
    });
}

function loadStoreProducts() {
    $.ajax({
        url: "loadStoreProducts",
        timeout: 2000,
        dataType: 'json',
        data: "storeID=" + $( "#select-store" ).val().split(" ")[1],
        error: function (errorObject) {
            $("#error-placeholder").append(errorObject.responseText)
        },
        success: function (data) {
            $("#products-table tbody").empty();
            $.each(data || [], appendToProductsTable);
        }
    });
};

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
            $.each(data || [], appendToSelectStore);
        }
    });
}

function appendToSelectStore(index,store)
{
    $("#select-store").append("<option>" + "id: "+ store.id + " | " + store.name +"</option>")

}

