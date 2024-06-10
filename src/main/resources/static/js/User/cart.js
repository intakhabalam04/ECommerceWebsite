function logout() {
    alert('Logged out')
}

function editCart() {
    alert('Edit Cart')
}

function proceedToCheckout() {
    window.location.href = "/checkout"
}

function confirmDelete(productId) {
    console.log(productId)
    const confirmation = confirm("Are you sure want to remove from cart?")
    if (confirmation) {
        window.location.href = "/removeFromCart/" + productId;
    }
}


async function incrementQuantity(button, id) {
    const subtotalElement = button.parentElement.nextElementSibling;

    const quantityElement = button.parentElement.querySelector('.quantity');
    const quantity = parseInt(quantityElement.textContent);
    if(quantity<9) {
        quantityElement.textContent = quantity + 1
        try {
            const response = await fetch(`/user/updateQuantity?cartItemId=+${id}&type=inc`)
            if (response.ok) {
                const updatedAmount = await response.text()
                subtotalElement.textContent = '₹'.concat(updatedAmount)
                await updateTotal()
            }
        } catch (e) {
            console.error(e)
        }
    }
}

async function decrementQuantity(button, id) {
    const subtotalElement = button.parentElement.nextElementSibling;


    const quantityElement = button.parentElement.querySelector('.quantity');
    const quantity = parseInt(quantityElement.textContent);

    /*if (quantity <= 2) {
        button.style.backgroundColor = 'red'
        button.style.cursor='not-allowed'
    }else{
        button.style.backgroundColor='007bff'
        button.style.cursor='allowed'
    }*/

    if (quantity > 1) {
        quantityElement.textContent = quantity - 1;
        try {
            const response = await fetch(`/user/updateQuantity?cartItemId=${id}&type=dec`);
            if (response.ok) {
                const updatedAmount = await response.text()
                subtotalElement.textContent = '₹'.concat(updatedAmount)
                await updateTotal()
            } else {
                throw new Error('Failed to update quantity');
            }
        } catch (e) {
            console.error(e);
        }
    }
}


async function updateTotal() {

    try {
        const response = await fetch(`/user/getCartPrice`)
        if (response.ok) {
            document.querySelector('.cart-total span').textContent = await response.text()
        }
    } catch (e) {
        console.error(e)
    }
}

