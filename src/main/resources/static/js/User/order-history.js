function cancelConfirmation(orderId) {
    console.log(orderId)
    if (window.confirm('Do you want to cancel ?')) {
        window.location.href = "/cancelOrder/" + orderId;
        alert('Order ' + orderId + ' cancelled');
    }
}

function returnConfirmation(orderId) {
    if (window.confirm('Do you want to return ?')) {
        window.location.href="/returnOrder/"+orderId;
        window.alert('order ' + orderId + ' returned')
    }
}