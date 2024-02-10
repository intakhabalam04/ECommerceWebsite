function logout() {
    alert('Logged out');
}

async function changePassword() {
    const currentPassword = document.getElementById("currentPassword").value;
    const newPassword = document.getElementById("newPassword").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    if (newPassword !== confirmPassword) {
        alert("New Password and Confirm Password do not match");
        return;
    }

    try {
        const response = await fetch(`/change-password?cp=${currentPassword}&np=${newPassword}`)

        if (response.ok) {
            const isPasswordChange = await response.json()

            if (isPasswordChange) {
                alert("Password change successfully")
                location.reload()
            } else {
                alert("Error! while changing password")
            }
        } else {
            console.error("Error!")
        }
    } catch (e) {
        console.error(e);
    }
}

async function uploadProfileImage(input) {
    const file = input.files[0];
    if (file) {
        const formData = new FormData();
        formData.append('file', file);

        try {
            const uploadResponse = await fetch('/upload-profile-image', {
                method: 'POST',
                body: formData,
            });

            const uploadResponseText = await uploadResponse.text();

            if (uploadResponse.ok) {
                const profileImage = document.getElementById('profileImage');

                const getImageResponse = await fetch('/user/image');
                const imageData = await getImageResponse.arrayBuffer();
                const blob = new Blob([imageData], {
                    type: 'image/jpeg'
                });
                profileImage.src = URL.createObjectURL(blob);
                alert('Profile image uploaded successfully!');
            } else {
                console.error('Error uploading profile image:', uploadResponseText);
            }

        } catch (e) {
            console.error(e);
        }
    }
    closeProfileImageModal()
}

function openProfileImageModal() {
    document.getElementById("profileImageModal").style.display = "block";
}

function closeProfileImageModal() {
    document.getElementById("profileImageModal").style.display = "none";
}

async function removeProfileImage() {

    const  response = await fetch(`/removeProfileImage`)
    await response.json()

    const profileImage = document.getElementById('profileImage');
    const getImageResponse = await fetch('/user/image');
    const imageData = await getImageResponse.arrayBuffer();
    const blob = new Blob([imageData], {
        type: 'image/jpeg'
    });
    profileImage.src = URL.createObjectURL(blob);

    alert("Profile image removed!");
    closeProfileImageModal();
}

// Function to open the file input when clicking on "Upload DP" button
function openUploadProfileImageInput() {
    document.getElementById("uploadProfileImageInput").click();
}
