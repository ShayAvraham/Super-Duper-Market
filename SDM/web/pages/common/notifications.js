var refreshNotificationsRate = 1000;
var notificationsLength = 0;


function prependToNotificationsTable(notification) {
    var newRowContent = "<tr>\n" +
        "      <td >" + notification.strMessage + "</td>\n" +
        "    </tr>"
    $("#all-notifications-data").prepend(newRowContent);
}


function refreshNotificationsTable() {
    if (ownerNotifications.length > 0)
    {
        $("#empty-table-msg").remove();
    }
    else
    {
        let isEmptyTableMsgHidden = $("#empty-table-msg").attr("hidden");
        if (isEmptyTableMsgHidden === "hidden") {
            $("#empty-table-msg").attr("hidden", false);
        }
    }
    if (ownerNotifications.length > notificationsLength)
    {
        notificationsLength = ownerNotifications.length;
        $("#all-notifications-data").empty();
        ownerNotifications.forEach((notification) => {
            prependToNotificationsTable(notification);
        })
    }
}

$(function() {
    refreshNotificationsTable();
    cleanNotificationCounterOnClickNotificationsTable();
    allIntervals.push(setInterval(refreshNotificationsTable, refreshNotificationsRate));
});


function cleanNotificationCounterOnClickNotificationsTable() {
    $('#notifications-table').on("click", 'tbody tr', function(e)
    {
        $(this).addClass('highlight').siblings().removeClass('highlight');
        counter = 0;
        $("#notifications-counter").text("");
    });
}