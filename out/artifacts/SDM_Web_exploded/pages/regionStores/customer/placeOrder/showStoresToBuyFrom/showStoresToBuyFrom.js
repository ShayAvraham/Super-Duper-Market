$(function() {
    // $.ajax({
    //     url: "loadDynamicAllocation",
    //     type: "POST",
    //     data: "selectedProducts=" + JSON.stringify(getSelectedProducts()),
    //     //  timeout: 12000,
    //     dataType: 'json',
    //     error: function(errorObject) {
    //         $("#error-placeholder").append(errorObject.responseText)
    //     },
    //     success: function(data)
    //     {
    //         storesToBuyFrom = data
    //         $.each(data || [], appendToProductsTable);
    //     }
    // });
    // return false;
    // storesToBuyFrom.forEach(element => appendToStoresToBuyFromTable(0,element));

    $.each(storesToBuyFrom || [], appendToStoresToBuyFromTable);
});


function temp1(index,productsCollection)
{
    $.each(productsCollection || [], appendToStoresToBuyFromTable);
}
function appendToStoresToBuyFromTable(index,storesToBuyFrom)
{
    var location = "(" + storesToBuyFrom.store.position.x.toString()
        +"," + storesToBuyFrom.store.position.y.toString() +")"

    var distance = Math.sqrt(Math.pow((storesToBuyFrom.store.position.x-xPosition),2)+
        Math.pow((storesToBuyFrom.store.position.y-yPosition),2));
    var newRowContent = "<tr>\n" +
        "      <th scope=\"row\">\n" + storesToBuyFrom.store.id  +"</th>\n" +
        "      <td>" + storesToBuyFrom.store.name + "</td>\n" +
        "      <td>" +  location + "</td>\n" +
        "      <td>" + distance + "</td>\n" +
        "      <td>" + storesToBuyFrom.store.ppk + "</td>\n" +
        "      <td>" + "deliveryCost" + "</td>\n" +
        "      <td>" + storesToBuyFrom.products.length + "</td>\n" +
        "      <td>" + "productsCost" + "</td>\n" +
        "    </tr>"
    $("#stores-to-buy-from-table").append(newRowContent);
}