var refreshRate = 2000;


$(function() {
    allIntervals.push(setInterval(ajaxUsersList, refreshRate));
});

$(function () {
    $.ajax({
        url: "loadUsersInfo",
        dataType: 'json',
        error: function(errorObject) {
            $("#error-label").text(errorObject.responseText)
        },
        success: function(data)
        {
            refreshUsersTable(data);
            setInterval(ajaxUsersList, refreshRate);
        }
    });
    return false;
});


function appendToUsersTable(user)
{
    var newRowContent = "<tr>\n" +
        "      <td>" + user.id + "</td>\n" +
        "      <td>" + user.name + "</td>\n" +
        "    </tr>"
    $("#all-users-data").append(newRowContent);
}


function refreshUsersTable(users) {
    $("#all-users-data").empty();
    if (users.length > 0)
    {
        users.forEach((user) => {
            appendToUsersTable(user);
        })
    }
}

function ajaxUsersList() {
    $.ajax({
        url: "loadUsersInfo",
        dataType: "json",
        success: function (data) {
            refreshUsersTable(data);
        }
    });
}
