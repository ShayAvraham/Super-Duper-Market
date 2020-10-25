$(function() {
    console.log("show feedbacks page");
    loadUserFeedbacks();
});

$(function() {
    allIntervals.push(setInterval(loadUserFeedbacks, 2000));
});

function loadUserFeedbacks()
{
    $.ajax({
        url: "loadUserRegionFeedbacks",
        timeout: 2000,
        dataType: 'json',
        error: function(errorObject) {
            $("#error-placeholder").append(errorObject.responseText)
        },
        success: function(data)
        {
            if(data.length === 0)
            {
                $("#alert-div").removeClass("invisible");
            }
            else
            {
                $("#alert-div").remove();
                $("#feedbacks-table").removeClass("invisible");
                $("#feedbacks-table tbody").empty();
                $.each(data || [], appendToFeedbackTable);
            }
        }
    });
    return false;
}

function appendToFeedbackTable(index,feedback)
{
    var dateArray = feedback.date.split(" ");
    var date = dateArray[0] + " " + dateArray[1] + " " + dateArray[2] + " ";

    var newRowContent = "<tr>\n" +
        "      <th scope=\"row\">" + feedback.customerName + "</th>\n" +
        "      <td>" + date+ "</td>\n" +
        "      <td>" + feedback.rank +"</td>\n" +
        "      <td>" + feedback.description + "</td>\n" +
        "    </tr>";

    $("#feedbacks-table tbody").append(newRowContent);
}