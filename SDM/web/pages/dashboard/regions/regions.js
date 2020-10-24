var refreshRate = 2000;

$(function () {
    $.ajax({
        url: "loadRegionsInfo",
        dataType: 'json',
        error: function (errorObject) {
            $("#error-label").text(errorObject.responseText)
        },
        success: function (data) {
            refreshRegionsTable(data);
            setInterval(ajaxRegionsList, refreshRate);
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
        "      <td>" + region.orderCostAvg.toFixed(2) + "</td>\n" +
        "    </tr>"
    $("#all-regions-data").append(newRowContent);
}


function refreshRegionsTable(regions) {
    $("#all-regions-data").empty();
    if (regions.length > 0)
    {
        regions.forEach((region) => {
            appendToRegionsTable(region);
        })
    }
    else
    {
        $("#all-regions-data").append("<tr>No regions in the system</tr>"); // השורה לא עובדת - לתקן!
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

// $(function() {
//     setInterval(ajaxRegionsList, refreshRate);
// });


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
        $(this).addClass('highlight').siblings().removeClass('highlight');
        var selectedRegionName = $(this).closest("tr").find('td').eq(1).text();
        ajaxUpdateRegionInSession(selectedRegionName);
    });
});