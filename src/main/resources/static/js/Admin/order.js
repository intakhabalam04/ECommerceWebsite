function updateOrderStatus(selectElement, orderId) {
    var newStatus = selectElement.value;

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/admin/updateOrderStatus?orderId=" + orderId + "&status=" + newStatus, true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
        }
    };
    xhr.send();
}