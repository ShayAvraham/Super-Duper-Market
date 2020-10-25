var refreshRate = 2000;


$(function() {
    allIntervals.push(setInterval(ajaxTransactionsList, refreshRate));
});


$(function () {
    $.ajax({
        url: "loggedUser",
        timeout: 2000,
        dataType: "json",
        error: function (errorObject) {
            $("#error-label").text(errorObject.responseText);
        },
        success: function (data) {
            refreshTransactionsTable(data);
            setInterval(ajaxTransactionsList, refreshRate);
        }
    });
});

$(function() {
    $("#user-balance-form").submit(function() {
        var amountToRecharge = $("#money-amount").val();
        var datePicked = $("#date").val();
        $.ajax({
            url: "chargeUserMoney",
            dataType: 'json',
            data: "amount=" + amountToRecharge + "&" + "date=" + datePicked,
            error: function(data) {
                $("#error-label").text("Failed to charge the amount of money to your account");
            },
            success: function(data)
            {
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
        "      <td>" + transaction.balanceBefore.toFixed(2) + "</td>\n" +
        "      <td>" + transaction.balanceAfter.toFixed(2) + "</td>\n" +
        "    </tr>"
    $("#user-transactions-data").append(newRowContent);
}


function refreshTransactionsTable(user) {
    updateUserCurrentBalance(user.balance);
    if (user.transactions.length > 0)
    {
        $("#user-transactions-data").empty();
        user.transactions.forEach((transaction) => {
            appendToTransactionTable(transaction);
        })
    }
}


function ajaxTransactionsList() {
    $.ajax({
        url: "loggedUser",
        dataType: "json",
        success: function (data) {
            refreshTransactionsTable(data);
        }
    });
}



