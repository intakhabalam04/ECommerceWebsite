function logout() {
    alert('Logged out');
}

function editCart() {
    alert('Edit Cart');
}

function proceedToCheckout() {
    window.location.href = "/checkout";
}

function confirmDelete(productId) {
    console.log(productId)
    const confirmation = confirm("Are you sure want to remove from cart?")
    if (confirmation) {
        window.location.href = "/removeFromCart/" + productId;
    }
}