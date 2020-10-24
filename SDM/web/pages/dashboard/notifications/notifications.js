var refreshRate = 2000;

function appendToNotificationsTable(notification) {
    var newRowContent = "<tr>\n" +
        "      <td >" + notification.description + "</td>\n" +
        "    </tr>"
    $("#all-notifications-data").append(newRowContent);
}


function refreshNotificationsTable(notifications) {
    $("#all-notifications-data").empty();
    if (notifications.length > 0)
    {
        console.log("test");
        notifications.forEach((notification) => {
            appendToNotificationsTable(notification);
        })
    }
    else
    {
        $("#all-notifications-data").append("<tr>\n" +
            "      <td >" + "Your notifications box is empty" + "</td>\n" +
            "    </tr>");
    }
}

function ajaxNotificationsList() {
    $.ajax({
        url: "loadNotifications",
        dataType: "json",
        success: function (data) {
            refreshNotificationsTable(data);
        }
    });
}

$(function() {
    $("#all-notifications-data").append("<tr>\n" +
        "      <td >" + "Your notifications box is empty" + "</td>\n" +
        "    </tr>");
    setInterval(ajaxNotificationsList, refreshRate);
});