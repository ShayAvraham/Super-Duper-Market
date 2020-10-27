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
            allIntervals.push(setInterval(ajaxRegionsList, refreshRate));
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
    if (regions.length > 0)
    {
        let isEmptyTableMsgHidden = $("#empty-table-msg").attr("hidden");
        let isRegionsHeaderHidden =  $("#regions-table-header").attr("hidden");
        if (isEmptyTableMsgHidden !== "hidden") {
            $("#empty-table-msg").attr("hidden", true);
        }
        if (isRegionsHeaderHidden === "hidden") {
            $("#regions-table-header").attr("hidden", false);
        }
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