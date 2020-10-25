var refreshNotificationsRate = 1000;
var notificationsLength = 0;


function appendToNotificationsTable(notification) {
    var newRowContent = "<tr>\n" +
        "      <td >" + notification.description + "</td>\n" +
        "    </tr>"
    $("#all-notifications-data").append(newRowContent);
}


function refreshNotificationsTable() {
    if (ownerNotifications.length > notificationsLength)
    {
        notificationsLength = ownerNotifications.length;
        $("#all-notifications-data").empty();
        ownerNotifications.forEach((notification) => {
            appendToNotificationsTable(notification);
        })
    }
    else if (ownerNotifications.length === 0)
    {
        $("#all-notifications-data").empty();
        $("#all-notifications-data").append("<tr>\n" +
            "      <td >" + "Your notifications box is empty" + "</td>\n" +
            "    </tr>");
    }
}

$(function() {
    cleanNotificationCounterOnClickNotificationsTable();
    setInterval(refreshNotificationsTable, refreshNotificationsRate);
});


function cleanNotificationCounterOnClickNotificationsTable() {
    $('#notifications-table').on("click", 'tbody tr', function(e)
    {
        $(this).addClass('highlight').siblings().removeClass('highlight');
        // $("#notifications-counter").text("");
    });
}