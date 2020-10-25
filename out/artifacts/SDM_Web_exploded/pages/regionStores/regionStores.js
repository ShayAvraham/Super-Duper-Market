var allIntervals = [];


function clearAllIntervals()
{
    if(allIntervals.length != 0)
    {
        for (interval of allIntervals)
        {
            clearInterval(interval);
        }
    }
}


$(function() {
    $("#show-products-btn").on("click", function () {
        clearAllIntervals();
        $("#loader").empty();
        $("#loader").load('common/showProducts/showProducts.html');
    });
    $("#show-stores-btn").on("click", function () {
        clearAllIntervals()
        $("#loader").empty();
        $("#loader").load('common/showStores/showStores.html');
    });
    $("#back-btn").on("click", function () {
        clearAllIntervals()
        window.location.replace("../dashboard/dashboard.html");
    });
});

$(function() {
    $.ajax({
        url: "loggedUser",
        dataType: "json",
        timeout: 2000,
        error: function (errorObject) {
            $("#error-placeholder").text(errorObject.responseText);
        },
        success: function (data) {
            loadUserButtons(data.role)
        }
    });
});

function loadUserButtons(userRole)
{
        switch(userRole) {
        case "customer":
            createCustomerButtons()
            addCustomerButtonsEvents()

            break;
        case "owner":
            createOwnerButtons()
            addOwnerButtonsEvents()
            break;
        }
}


function createCustomerButtons()
{
    var newLiContent = "                   <li class=\"nav-item\">\n" +
        "                        <a id=\"place-order-btn\" class=\"nav-link\">\n" +
        "                            <span data-feather=\"users\"></span>\n" +
        "                            Place Order\n" +
        "                        </a>\n" +
        "                    </li>" +
        "                   <li class=\"nav-item\">\n" +
        "                        <a id=\"orders-history-btn\" class=\"nav-link\">\n" +
        "                            <span data-feather=\"users\"></span>\n" +
        "                            Orders History\n" +
        "                        </a>\n" +
        "                    </li>"
    $(newLiContent).insertBefore($("#back-li"));
}

function addCustomerButtonsEvents ()
{
    $("#place-order-btn").on("click", function () {
        clearAllIntervals();
        $("#loader").load('customer/placeOrder/placeOrder.html');
    });
    $("#orders-history-btn").on("click", function () {
        clearAllIntervals();
        $("#loader").load('customer/showOrdersHistory/showOrdersHistory.html');
    });
}

function createOwnerButtons()
{
    var newLiContent = "                   <li class=\"nav-item\">\n" +
        "                        <a id=\"store-orders-btn\" class=\"nav-link\">\n" +
        "                            <span data-feather=\"users\"></span>\n" +
        "                            Store Orders History\n" +
        "                        </a>\n" +
        "                    </li>" +
        "                   <li class=\"nav-item\">\n" +
        "                        <a id=\"show-feedbacks-btn\" class=\"nav-link\">\n" +
        "                            <span data-feather=\"users\"></span>\n" +
        "                            Show Feedbacks\n" +
        "                        </a>\n" +
        "                    </li>" +
        "                   <li class=\"nav-item\">\n" +
        "                        <a id=\"add-new-store-btn\" class=\"nav-link\">\n" +
        "                            <span data-feather=\"users\"></span>\n" +
        "                            Add New Store\n" +
        "                        </a>\n" +
        "                    </li>"

    $(newLiContent).insertBefore($("#back-li"));
}


function addOwnerButtonsEvents ()
{
    $("#store-orders-btn").on("click", function () {
        clearAllIntervals();
        $("#loader").load('owner/showStoreOrders/showStoreOrders.html');
    });
    $("#show-feedbacks-btn").on("click", function () {
        clearAllIntervals();
        $("#loader").load('owner/showFeedbacks/showFeedbacks.html');
    });
    $("#add-new-store-btn").on("click", function () {
        clearAllIntervals();
        $("#loader").load('owner/addNewStore/addNewStore.html');
    });
}
