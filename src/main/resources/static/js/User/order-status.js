const progressBar = document.querySelector('.progress-bar');
const orderStatus = document.getElementById('order-status');
const deliveredTick = document.querySelector('.tick.tick-5')

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
            deliveredTick.setAttribute('data-text','Cancelled')
            progressBar.style.width='100%'
            progressBar.style.backgroundColor='red'
            break
        case 'RETURNED':
            deliveredTick.setAttribute('data-text','Returned')
            progressBar.style.width='100%'
            progressBar.style.backgroundColor='grey'
        default:
            break;
    }
}
