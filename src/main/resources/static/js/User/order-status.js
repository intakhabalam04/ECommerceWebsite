const progressBar = document.querySelector('.progress-bar');
const orderStatus = document.getElementById('order-status');

async function updateProgress(orderId) {

    switch (orderStatus.textContent) {
        case 'PENDING':
            progressBar.style.width = '20%';
            break;
        case 'PROCESSING':
            progressBar.style.width = '40%';
            break;
        case 'SHIPPED':
            progressBar.style.width = '60%';
            break;
        case 'OUT_FOR_DELIVERY':
            progressBar.style.width = '80%';
            break;
        case 'DELIVERED':
            progressBar.style.width = '100%';
            break;
        case 'CANCELLED':
            progressBar.style.width='50%'
            progressBar.style.backgroundColor='red'
            break
        default:
            break;
    }
}
