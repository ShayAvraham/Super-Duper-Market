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
        $.ajax({
            url: "loadProducts",
            timeout: 2000,
            dataType: 'json',
            error: function(errorObject) {
                $("#error-placeholder").append(errorObject.responseText)
            },
            success: function(data) {
                $.each(data || [], appendToProductsTable);

            }
        });
        return false;
});

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