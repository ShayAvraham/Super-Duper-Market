$(function() { // onload...do
    //add a function to the submit event
    $("#loginForm").submit(function() {
        console.log($(this).serialize());
        $.ajax({
            data: $(this).serialize(),
            url: this.action,
            timeout: 2000,
            error: function(errorObject) {
                $("#error-placeholder").append(errorObject.responseText)
            },
            success: function(nextPageUrl) {
                window.location.replace(nextPageUrl);
            }
        });

        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });
});

