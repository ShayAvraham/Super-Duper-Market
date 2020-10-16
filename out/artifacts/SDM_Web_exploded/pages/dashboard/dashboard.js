

$(document).ready(function() {
    $("#load-xml-btn").on("click", function () {
        $("#loader").load('loadXml/loadXml.html');
    });
    $("#users-btn").on("click", function () {
        $("#loader").load('users/users.html');
    });
    $("#stores-btn").on("click", function () {
        $("#loader").load('stores/stores.html');
    });
    $("#account-btn").on("click", function () {
        $("#loader").load('accounts/account.html');
    });
});
