$(function () {
    $("#users-btn").on("click", function () {
        $("#loader").empty();
        $("#loader").load('users/users.html');
    });
    $("#stores-btn").on("click", function () {
        $("#loader").empty();
        $("#loader").load('regions/regions.html');
    });
    $("#account-btn").on("click", function () {
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
            loadUserButtons(data.role)
        }
    });
});

function loadUserButtons(userRole) {
    if (userRole === "owner") {
        createOwnerButtons()
        addOwnerButtonsEvents()
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
        "    </a>\n" +
        "</li>"

    $("#nav-bar-buttons").prepend(newLiContent);
}

function addOwnerButtonsEvents() {
    $("#load-xml-btn").on("click", function () {
        $("#loader").empty();
        $("#loader").load('loadXml/loadXml.html');
    });
    $("#notifications-btn").on("click", function () {
        $("#loader").empty();
        $("#loader").load('notifications/notifications.html');
    });
}



