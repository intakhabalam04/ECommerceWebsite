function addToCart(productId) {
    if (isProductAlreadyAdded(productId)) {
        alert('This product is already in your cart.');
    } else {
        window.location.href = '/addToCart/' + productId;
    }
}

function isProductAlreadyAdded(productId) {
    const cartProductIds = document.getElementById('cartProductIds').value.split(',');
    return cartProductIds.includes(productId);
}

function logout() {
    alert('Logged out');
}

function filterProducts(category) {
    const products = document.querySelectorAll('.product-card');

    products.forEach(product => {
        const productCategory = product.getAttribute('data-category');
        if (category === 'all' || category === productCategory) {
            product.style.display = 'block';
        } else {
            product.style.display = 'none';
        }
    });
}