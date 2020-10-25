var refreshRate = 2000;
var allIntervals = [];
var userRole = "customer";
var ownerNotifications = [];
var counter = 0;


async function ajaxNotificationsList() {
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

$(function () {
    setInterval(ajaxNotificationsList, refreshRate);
});


function clearAllIntervals() {
    if (allIntervals.length != 0) {
        for (interval of allIntervals) {
            clearInterval(interval);
        }
    }
}


$(function () {
    $("#users-btn").on("click", function () {
        clearAllIntervals();
        $("#loader").empty();
        $("#loader").load('users/users.html');
    });
    $("#stores-btn").on("click", function () {
        clearAllIntervals();
        $("#loader").empty();
        $("#loader").load('regions/regions.html');
    });
    $("#account-btn").on("click", function () {
        clearAllIntervals();
        $("#loader").empty();
        $("#loader").load('account/account.html');
    });
});

$(function () {
    $.ajax({
        url: "loggedUser",
        timeout: 2000,
        dataType: "json",
        error: function (errorObject) {
            $("#error-placeholder").text(errorObject.responseText);
        },
        success: function (data) {
            userRole = data.role;
            loadUserButtons(data.role)
        }
    });
});

function loadUserButtons(userRole) {
    if (userRole === "owner") {
        createOwnerButtons();
        addOwnerButtonsEvents();
    }
}


function createOwnerButtons() {
    var newLiContent = "                   <li class=\"nav-item\">\n" +
        "                        <a id=\"load-xml-btn\" class=\"nav-link active\">\n" +
        "                            <span data-feather=\"file\"></span>\n" +
        "                            Load XML\n" +
        "                        </a>\n" +
        "                    </li>" +
        "<li class=\"nav-item\">\n" +
        "    <a id=\"notifications-btn\" class=\"nav-link\">\n" +
        "        <span data-feather=\"users\"></span>\n" +
        "        Notifications\n" +
        "        <span id=\"notifications-counter\" class=\"badge\"></span>\n" +
        "    </a>\n" +
        "</li>"

    $("#nav-bar-buttons").prepend(newLiContent);
}

function addOwnerButtonsEvents() {
    $("#load-xml-btn").on("click", function () {
        clearAllIntervals();
        $("#loader").empty();
        $("#loader").load('loadXml/loadXml.html');
    });
    $("#notifications-btn").on("click", function () {
        clearAllIntervals();
        $("#loader").empty();
        counter = 0;
        $("#notifications-counter").text("");
        $("#loader").load('notifications/notifications.html');
    });
}



