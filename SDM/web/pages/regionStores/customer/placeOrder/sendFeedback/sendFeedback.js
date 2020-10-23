$(function() {

    console.log("feedback page");
    $.each(storesToBuyFrom || [], appendToStoreSelect);
});

function appendToStoreSelect(index,storeToBuyFrom)
{
    $("#store-select").append("<option>" + "id: "+ storeToBuyFrom.store.id + " | " + storeToBuyFrom.store.name +"</option>");
}

$(function() {
    $("#feedback-form").submit(function()
    {
        var storeID = $("#store-select").val().split(" ")[1];

        $.ajax({
            url: "addNewFeedback",
            data: "storeID=" + storeID + "&" +
                "date=" + deliveryDate + "&" +
                "rank=" + $( "#rank-select" ).val() + "&" +
                "description=" + $("#description-text").val(),
            timeout: 2000,
            dataType: 'json',
            error: function(errorObject) {
                $("#error-placeholder").append(errorObject.responseText);
            },
            success: function(data)
            {
                $("#feedback-success-label").empty();
                $("#feedback-success-label").append("Your feedback send successfully, thank you for sharing");
                updateFeedbackForm();
            }
        });
        return false;
    });
});

function updateFeedbackForm()
{
    $("#store-select option:selected").remove();
    $("#rank-select").val(1);
    $("#description-text").val("");
    if(!$('#store-select').val())
    {
        $("#rank-select").val("");
        $("*", "#feedback-form").prop('disabled', true);
    }


}

$(function() {
    $("#continue-form").submit(function()
    {
        window.location.replace("");
        return false;
    });
});
