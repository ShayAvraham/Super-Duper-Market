$(function () {
    $.ajax({
        url: "loadRegionsInfo",
        timeout: 2000,
        dataType: 'json',
        error: function (errorObject) {
            $("#error-label").text(errorObject.responseText)
        },
        success: function (data) {
            $("#all-regions-data").empty();
            if (data.length > 0) {
                data.forEach((region) => {
                    appendToRegionsTable(region);
                })
            }
        }
    });
    return false;
});


function appendToRegionsTable(region) {
    var newRowContent = "<tr>\n" +
        "      <td >" + region.ownerName + "</td>\n" +
        "      <td>" + region.name + "</td>\n" +
        "      <td>" + Object.keys(region.productsData).length + "</td>\n" +
        "      <td>" + region.numOfStores + "</td>\n" +
        "      <td>" + region.numOfOrders + "</td>\n" +
        "      <td>" + region.orderCostAvg + "</td>\n" +
        "    </tr>"
    $("#all-regions-data").append(newRowContent);
}