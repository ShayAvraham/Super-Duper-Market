$(function() {
    $("#load-xml-form").submit(function() {
        let file = this[0].files[0];
        let formData = new FormData();
        formData.append("file", file);
        $.ajax({
            method: "POST",
            data: formData,
            url: this.action,
            processData: false,
            contentType: false,
            error: function(data) {
                $("#upload-file-msg").attr("class", "alert-danger");
                $("#upload-file-msg").text("Failed to load file due to: " + data.responseText);
            },
            success: function()
            {
                $("#upload-file-msg").attr("class", "alert-success");
                $("#upload-file-msg").text("File was loaded successfully!");
            }
        });
        return false
    });
});

