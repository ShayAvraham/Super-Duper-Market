var refreshRate = 2000; //milli seconds




$(function()
{
    ajaxGetLoggedUser();


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
        $("#loader").load('account/account.html');
    });
});


function ajaxGetLoggedUser()
{
    $.ajax({
        url: "loggedUser",
        dataType: 'json',
        success: function(user) {
            loadPage(user);
        }
    });
}

function loadPage(user)
{

}

//activate the timer calls after the page is loaded
$(function() {
    //The users list is refreshed automatically every second
    setInterval(ajaxUsersList, refreshRate);
});

