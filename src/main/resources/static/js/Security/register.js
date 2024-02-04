async function checkAvailability(id) {

    let signupSubmitbutton = document.getElementById("signup-submit-button")

    let response;
    try {
        if (id === 'username') {
            const username = document.getElementById("username").value;
            response = await fetch(`/checkAvailability?userId=${username}&type=username`);

            if (response.ok) {
                const isUserFound = await response.json();
                const errorDiv = document.getElementById("username-error");


                if (isUserFound) {
                    signupSubmitbutton.disabled = true;
                    signupSubmitbutton.style.cursor = "not-allowed"
                    errorDiv.textContent = username + " Not available";
                    errorDiv.style.color = "#e74c3c";
                } else {
                    signupSubmitbutton.style.cursor = "pointer"
                    signupSubmitbutton.disabled = false;
                    errorDiv.textContent = "";
                }
            } else {
                console.error("Error!");
            }

        } else if (id === 'emailId') {
            const emailId = document.getElementById("email").value;

            response = await fetch(`/checkAvailability?userId=${emailId}&type=emailId`);

            if (response.ok) {

                const isUserFound = await response.json();

                const errorDiv = document.getElementById("email-error");


                if (isUserFound) {
                    signupSubmitbutton.style.cursor = "not-allowed"
                    signupSubmitbutton.disabled = true;
                    errorDiv.textContent = emailId + " Not available";
                    errorDiv.style.color = "#e74c3c";

                } else {
                    signupSubmitbutton.disabled = false;
                    errorDiv.textContent = "";
                    signupSubmitbutton.style.cursor = "pointer"

                }
            } else {
                console.error("Error!");
            }

        } else if (id === 'phoneNumber') {
            const phoneNumber = document.getElementById("phone").value
            response = await fetch(`/checkAvailability?userId=${phoneNumber}&type=phoneNumber`);

            if (response.ok) {
                const isUserFound = await response.json();
                const errorDiv = document.getElementById("phoneNumber-error");


                if (isUserFound) {
                    signupSubmitbutton.disabled = true;
                    signupSubmitbutton.style.cursor = "not-allowed"
                    errorDiv.textContent = phoneNumber + " Not available";
                    errorDiv.style.color = "#e74c3c";
                } else {
                    signupSubmitbutton.disabled = false;
                    errorDiv.textContent = "";
                    signupSubmitbutton.style.cursor = "pointer"
                }
            } else {
                console.error("Error!");
            }
        }
    } catch (e) {
        console.error("Error!" + e.toString());
    }
}