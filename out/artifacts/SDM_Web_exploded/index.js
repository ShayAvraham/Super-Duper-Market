$(function() { // onload...do
    //add a function to the submit event
    $("#loginForm").submit(function() {
        const userName = $("#username").val();
        if (userName) {
            if (userName.match("^[a-zA-Z]+$")) {
                $.ajax({
                    data: $(this).serialize(),
                    url: this.action,
                    timeout: 2000,
                    error: function (errorObject) {
                        $("#error-placeholder").text(errorObject.responseText);
                    },
                    success: function (nextPageUrl) {
                        window.location.replace(nextPageUrl);
                    }
                });
            }
            else {
                $("#error-placeholder").text("user name can contains only letters");
            }
        }
        else {
            $("#error-placeholder").text("user name can not be empty");
        }
        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });
});

