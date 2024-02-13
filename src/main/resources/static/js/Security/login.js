function openForgotPasswordModal() {
    document.getElementById("forgotPasswordModal").style.display = "block";
    const responseMessage = document.getElementById("responseMessage");

    responseMessage.innerText = "*Please enter your email address or userHandle.";
    responseMessage.className = "forgot-label"; // Add the forgot-label class
}

function closeForgotPasswordModal() {
    document.getElementById("forgotPasswordModal").style.display = "none";
    document.getElementById("forgotUsername").value = "";
    document.getElementById("responseMessage").innerText = "";
    document.getElementById("responseMessage").className = "";
}

async function submitForgotPage() {
    const userId = document.getElementById("forgotUsername").value
    const sendResetLinkButton = document.getElementById("sendResetLinkButton");
    const spinner = document.getElementById("loadingSpinner");
    const responseMessage = document.getElementById("responseMessage");

    responseMessage.innerText = "*Please enter your email address or userHandle."
    responseMessage.className = "forgot-label";

    try {
        sendResetLinkButton.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Fetching...';
        sendResetLinkButton.disabled = true;

        const response = await fetch(`/forgot?userId=${userId}`)

        if (response.ok) {
            const isResetMailSent = await response.json();
            if (isResetMailSent) {
                responseMessage.innerText = "A reset link has been sent to your registered email. Please use it to reset your password.";
                responseMessage.className = "success";
            } else {
                await new Promise(resolve => setTimeout(resolve, 1000));
                responseMessage.innerText = "Incorrect login credentials i.e. userHandle/email!" + '/login';
                responseMessage.className = "error";
            }
        } else {
            console.error("Error");
        }
    } catch (e) {
        console.error("Error !");
    } finally {
        spinner.style.display = "none";
        sendResetLinkButton.innerHTML = 'Send Reset Link';
        sendResetLinkButton.disabled = false;
    }
}

function LoginFill(user) {
    const username = document.getElementById("username");
    const password = document.getElementById("password");
    if (user === 'admin') {
        username.value = 'admin'
        password.value = 'admin'
    } else if (user === 'user') {
        username.value = 'user'
        password.value = '000'
    } else if (user === 'intakhab') {
        username.value = 'intakhab'
        password.value = '000'
    }
}
