function returnToCart() {
    window.location.href = "/user/cart";
}

function doCheckout() {
    window.location.href = "/doCheckout";
}

function placeOrder() {
    alert('Order placed successfully');
    doCheckout();
}

async function applyCoupon() {
    var couponCode = document.getElementById("couponCode").value;

    try {
        const response = await fetch(`/validateCoupon?couponCode=${couponCode}`);
        if (response.ok) {
            const isValidCoupon = await response.json();

            if (isValidCoupon) {

                // alert("Coupon applied successfully!");
                // document.getElementById("couponMessage").innerHTML="Coupon applied successfully!";
                showToast('success')
                document.getElementById("totalAmount").value=document.getElementById("totalAmount").value*0.9
            } else {
                // alert("Invalid coupon code. Please try again.");
                showToast('failed')
                document.getElementById("couponMessage").innerHTML="Invalid coupon code. Please try again."
            }
        } else {
            console.error("Failed to validate coupon");
        }
    } catch (error) {
        console.error("Error during coupon validation", error);
    }
}

let toastId = 0;

function showToast(status) {
    const toastContainer = document.getElementById('toast-container');

    // Create a new toast element
    const toast = document.createElement('div');
    toast.className = 'toast';
    // toast.innerText = 'Coupon applied successfully!';
    // toast.style.color='black'
    // toast.style.backgroundColor='white'
    toast.id = 'toast-' + toastId++;

    if(status==='success'){
        toast.innerText='Coupon applied successfully!'
        toast.style.color='black'
        toast.style.backgroundColor='white'
    }

    // Add the toast to the container
    toastContainer.appendChild(toast);

    // Trigger reflow to apply the initial opacity transition
    toast.offsetWidth;

    // Show the toast
    toast.style.opacity = 1;

    // Automatically hide the toast after a few seconds
    setTimeout(() => {
        hideToast(toast.id);
    }, 3000);
}

function hideToast(toastId) {
    const toast = document.getElementById(toastId);

    // Fade out the toast
    toast.style.opacity = 0;

    // Remove the toast from the DOM after the fade-out transition
    setTimeout(() => {
        toast.parentNode.removeChild(toast);
    }, 500);
}