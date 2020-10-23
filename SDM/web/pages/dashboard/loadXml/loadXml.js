$(function() {
    console.log("111");
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
                $("#upload-file-msg").text("Failed to load file due to " + data)
            },
            success: function(data)
            {
                $("#upload-file-msg").text("File was loaded successfully!")
            }
        });
        return false
    });
});

