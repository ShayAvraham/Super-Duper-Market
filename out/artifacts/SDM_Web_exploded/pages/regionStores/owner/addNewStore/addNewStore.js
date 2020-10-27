var loadedProducts;
var selectedProducts;

$(function() {
    console.log("add new store page");
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
    var priceInput = "<input type=\"number\" class = \"form-control\" id=\"product-price\" min = '1' step= '1' value='1'>\n";

    var newRowContent = "<tr>\n" +
        "      <th scope=\"row\">\n" +
        "<input type=\"checkbox\" value=\"\" id=\"select-product\">\n"+
        "</th>\n" +
        "      <td>" + product.id+ "</td>\n" +
        "      <td>" + product.name +"</td>\n" +
        "      <td>" + product.purchaseForm + "</td>\n" +
        "      <td>" + priceInput + "</td>\n" +
        "    </tr>"
    $("#products-table tbody").append(newRowContent);
}

$(function() {
    $("#products-form").submit(function() {
        $("#table-error-placeholder").empty();
        $("#success-alert-div").addClass("invisible");
        if(checkIfOneProductSelected())
        {
            $.ajax({
                url: "validatePosition",
                data: "xPosition=" + $("#x-location-input").val()
                    + "&" + "yPosition=" + $("#y-location-input").val(),
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
                        validateStoreID();
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

function validateStoreID()
{
    $.ajax({
        url: "validateStoreID",
        data: "storeID=" + $("#id-input").val(),
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
                $("#table-error-placeholder").append("the I.D you enter is invalid," +
                    " there is already a store with that I.D");

            }
            else
            {
                addNewStore();
            }
        }
    });
    return false;
}

function addNewStore()
{
    createSelectedProducts();
    $.ajax({
        url: "addNewStore",
        data: "id=" + $("#id-input").val()
            + "&" + "name=" + $("#name-input").val()
            + "&" + "xPosition=" + $("#x-location-input").val()
            + "&" + "yPosition=" + $("#y-location-input").val()
            + "&" + "ppk=" + $("#ppk-input").val()
            + "&" + "selectedProducts=" + JSON.stringify(selectedProducts),
        type: "POST",
        timeout: 2000,
        dataType: 'json',
        error: function(errorObject) {
            $("#error-placeholder").append(errorObject.responseText);
        },
        success: function(data)
        {
            $("#success-alert-div").removeClass("invisible");
        }
    });
    return false;
}

function createSelectedProducts()
{
    selectedProducts = [];
    $('input:checkbox:checked', $("#products-table")).each(function() {
        var product = findProduct($(this).closest('tr').find('td').eq(0).text());
        product.newPrice = $(this).closest('tr').find('td input').val();
        selectedProducts.push(product);
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

