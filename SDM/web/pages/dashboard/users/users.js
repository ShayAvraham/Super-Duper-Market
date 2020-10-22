$(function () {
    $.ajax({
        url: "loadUsersInfo",
        timeout: 2000,
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

