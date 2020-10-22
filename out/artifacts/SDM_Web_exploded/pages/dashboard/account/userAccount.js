$(function () {
    $.ajax({
        url: "loggedUser",
        timeout: 2000,
        dataType: "json",
        error: function (errorObject) {
            $("#error-label").text(errorObject.responseText);
        },
        success: function (data) {
            updateUserCurrentBalance(data.balance);
            $("#user-transactions-data").empty();
            if (data.transactions.length > 0)
            {
                data.transactions.forEach((transaction) => {
                    appendToTransactionTable(transaction);
                })
            }
        }
    });
});

$(function() {
    console.log("bla");
    $("#user-balance-form").submit(function() {
        var amountToRecharge = $("#money-amount").val();
        var datePicked = $("#date").val();
        console.log(amountToRecharge);
        console.log(datePicked);
        $.ajax({
            url: "chargeUserMoney",
            timeout: 2000,
            dataType: 'json',
            // type: "POST",
            data: "amount=" + amountToRecharge + "&" + "date=" + datePicked,
            error: function(data) {
                $("#error-label").text("Failed to charge the amount of money to your account");
            },
            success: function(data)
            {
                updateUserCurrentBalance(data.balanceAfter);
                addNewTransactionToTransactionTable(datePicked, amountToRecharge, data.balanceBefore, data.balanceAfter);
                $("#money-amount").val("");
                $("#date").val("");
            }
        });
        return false
    });
});


function updateUserCurrentBalance(balance) {
    $("#user-balance-value").text(balance.toString());
}


function appendToTransactionTable(transaction) {
    var newRowContent = "<tr>\n" +
        "      <td >" + transaction.transactionCategory + "</td>\n" +
        "      <td>" + transaction.date + "</td>\n" +
        "      <td>" + transaction.cost + "</td>\n" +
        "      <td>" + transaction.balanceBefore + "</td>\n" +
        "      <td>" + transaction.balanceAfter + "</td>\n" +
        "    </tr>"
    $("#user-transactions-data").append(newRowContent);
}


function addNewTransactionToTransactionTable(datePicked, amountToRecharge, balanceBefore, balanceAfter) {
    var newRowContent = "<tr>\n" +
        "      <td >" + CHARGING + "</td>\n" +
        "      <td>" + datePicked + "</td>\n" +
        "      <td>" + amountToRecharge + "</td>\n" +
        "      <td>" + balanceBefore + "</td>\n" +
        "      <td>" + balanceAfter + "</td>\n" +
        "    </tr>"
    $("#user-transactions-data").append(newRowContent);
}


