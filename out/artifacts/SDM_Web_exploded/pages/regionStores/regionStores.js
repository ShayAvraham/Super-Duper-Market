var refreshRate = 2000;
var allIntervals = [];
var ownerNotifications = [];
var counter = 0;

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
    feather.replace();
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
        "                            <span data-feather=\"shopping-cart\"></span>\n" +
        "                            Place Order\n" +
        "                        </a>\n" +
        "                    </li>" +
        "                   <li class=\"nav-item\">\n" +
        "                        <a id=\"orders-history-btn\" class=\"nav-link\">\n" +
        "                            <span data-feather=\"archive\"></span>\n" +
        "                            Orders History\n" +
        "                        </a>\n" +
        "                    </li>\n" +
        "<script>\n" +
        "    feather.replace()\n" +
        "</script>"
    $(newLiContent).insertBefore($("#back-li"));
}

$(function () {
    ajaxNotificationsList();
    setInterval(ajaxNotificationsList, refreshRate);
});


function ajaxNotificationsList() {
    $.ajax({
        url: "loadNotifications",
        dataType: "json",
        success: function (data) {
            if (data.length > ownerNotifications.length) {
                counter = counter + (data.length - ownerNotifications.length);
                $("#notifications-counter").text(counter.toString());
                ownerNotifications.length = 0;
                data.forEach((notification) => {
                    ownerNotifications.push(notification);
                })
            }
        }
    });
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
        "                            <span data-feather=\"archive\"></span>\n" +
        "                            Show Store Orders History\n" +
        "                        </a>\n" +
        "                    </li>" +
        "                   <li class=\"nav-item\">\n" +
        "                        <a id=\"show-feedbacks-btn\" class=\"nav-link\">\n" +
        "                            <span data-feather=\"thumbs-up\"></span>\n" +
        "                            Show Feedbacks\n" +
        "                        </a>\n" +
        "                    </li>" +
        "                   <li class=\"nav-item\">\n" +
        "                        <a id=\"add-new-store-btn\" class=\"nav-link\">\n" +
        "                            <span data-feather=\"plus\"></span>\n" +
        "                            Add New Store\n" +
        "                        </a>\n" +
        "                    </li>\n" +
        "<li class=\"nav-item\">\n" +
        "    <a id=\"notifications-btn\" class=\"nav-link\">\n" +
        "        <span data-feather=\"bell\"></span>\n" +
        "        Notifications\n" +
        "        <span id=\"notifications-counter\" class=\"badge\"></span>\n" +
        "    </a>\n" +
        "</li>\n" +
        "<script>\n" +
        "    feather.replace()\n" +
        "</script>"

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
    $("#notifications-btn").on("click", function () {
        clearAllIntervals();
        counter = 0;
        $("#notifications-counter").text("");
        $("#loader").load('../common/notifications.html');
    });
}
