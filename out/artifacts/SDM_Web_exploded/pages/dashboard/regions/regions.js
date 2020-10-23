$(function () {
    $.ajax({
        url: "loadRegionsInfo",
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


function refreshRegionsTable(regions) {
    if (regions.length > 0)
    {
        $("#all-regions-data").empty();
        regions.forEach((region) => {
            appendToRegionsTable(region);
        })
    }
}

function ajaxRegionsList() {
    $.ajax({
        url: "loadRegionsInfo",
        dataType: "json",
        success: function (data) {
            refreshRegionsTable(data);
        }
    });
}

$(function() {
    setInterval(ajaxRegionsList, refreshRate);
});


function ajaxUpdateRegionInSession(selectedRegionName) {
    console.log("ajax");
    $.ajax({
        url: "updateSelectedRegion",
        dataType: "json",
        data: "selectedRegionName=" + selectedRegionName,
        success: function () {
            window.location.replace("../regionStores/regionStores.html");
        }
    });
}


$(function() {
    $('#regions-table').on("click", 'tbody tr', function(e)
    {
        console.log("click");
        $(this).addClass('highlight').siblings().removeClass('highlight');
        var selectedRegionName = $(this).closest("tr").find('td').eq(1).text();
        ajaxUpdateRegionInSession(selectedRegionName);
    });
});