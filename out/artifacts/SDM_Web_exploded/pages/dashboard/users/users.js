$(function () {
    $.ajax({
        url: "loadUsersInfo",
        dataType: 'json',
        error: function(errorObject) {
            $("#error-label").text(errorObject.responseText)
        },
        success: function(data)
        {
            $("#all-users-data").empty();
            $.each(data || [], appendToUsersTable);
        }
    });
    return false;
});


function appendToUsersTable(index, user)
{
    var newRowContent = "<tr>\n" +
        "      <th scope=\"row\">" + user.id + "</th>\n" +
        "      <td >" + user.name + "</td>\n" +
        "    </tr>"
    $("#all-users-data").append(newRowContent);
}


function refreshUsersTable(users) {
    if (users.length > 0)
    {
        $("#all-users-data").empty();
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

$(function() {
    setInterval(ajaxUsersList, refreshRate);
});