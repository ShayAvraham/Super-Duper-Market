$(function() {
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
        return false;
    });
});

