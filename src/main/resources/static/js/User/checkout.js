function navigateTo(url){
    window.location.href=url
}

async function applyCoupon() {
    const couponCode = document.getElementById("couponCode").value;

    try {
        const response = await fetch(`/validateCoupon?couponCode=${couponCode}`);
        if (response.ok) {
            const isValidCoupon = await response.json();

            if (isValidCoupon) {
                showToast('success')
                document.getElementById("totalAmount").value = document.getElementById("totalAmount").value * 0.9
            } else {
                showToast('failed')
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

    const toast = document.createElement('div');
    toast.className = 'toast';
    toast.id = 'toast-' + toastId++;

    if (status === 'success') {
        toast.innerText = 'Coupon applied successfully!'
        toast.style.color = 'black'
        toast.style.backgroundColor = 'white'
    } else {
        toast.innerText = 'Invalid coupon code. Please try again'
        toast.style.color = 'red'
        toast.style.backgroundColor = 'white'
    }
    toastContainer.appendChild(toast);

    toast.offsetWidth;

    toast.style.opacity = '1'

    setTimeout(() => {
        hideToast(toast.id);
    }, 3000);
}

function hideToast(toastId) {
    const toast = document.getElementById(toastId);

    toast.style.opacity = '0';

    setTimeout(() => {
        toast.parentNode.removeChild(toast);
    }, 500);
}