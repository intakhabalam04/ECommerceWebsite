function editProduct(productId, productName, productDescription, price, discount, categoryId, stockQuantity) {
    document.getElementById("productForm").reset();
    document.getElementById("modalTitle").innerHTML = "Edit Product";
    document.getElementById("submitBtn").innerHTML = "Update Product";

    document.getElementById("productId").value = productId;
    document.getElementById("productName").value = productName;
    document.getElementById("productDescription").value = productDescription;
    document.getElementById("price").value = price;
    document.getElementById("discount").value = discount;
    document.getElementById("stockQuantity").value = stockQuantity;

    var dropdown = document.getElementById("category");

    for (var i = 0; i < dropdown.options.length; i++) {
        var option = dropdown.options[i];
        var optionValue = option.value;

        if (optionValue === categoryId) {
            option.selected = true;
            break;
        }
    }

    // Set th:object attribute dynamically
    document.getElementById("productForm").setAttribute("th:object", "${product}");
    document.getElementById("productModal").style.display = "block";
}

function openAddProductModal() {
    document.getElementById("productForm").reset();
    document.getElementById("modalTitle").innerHTML = "Add Product";
    document.getElementById("submitBtn").innerHTML = "Add Product";
    document.getElementById("productModal").style.display = "block";
}

function closeModal() {
    document.getElementById("productModal").style.display = "none";
}

async function confirmDelete(productId) {
    const confirmation = confirm("Are you sure want to delete this Product");
    if (confirmation) {
        try {
            fetch(`/admin/product/delete?productId=${productId}`, {
                method: 'DELETE'
            })
                .then(response => {
                    if (response.ok) {
                        alert("Successfully Deleted");
                        // window.location.reload()
                        window.location.href = "/admin/product";
                    } else {
                        console.error("Something went wrong on the product.html");
                    }
                })
                .catch(error => {
                    console.error("Something went wrong on the product.html", error);
                });
        } catch (e) {
            console.error("Something went wrong on the product.html", e);
        }
    }
}


function openUploadImageModal(productId) {
    document.getElementById("productIdForImage").value = productId;
    document.getElementById("uploadImageModal").style.display = "block";
}

function closeUploadImageModal() {
    document.getElementById("uploadImageModal").style.display = "none";
}

document.getElementById("uploadImageForm").addEventListener("submit", function (event) {
    event.preventDefault(); // Prevent default form submission

    const productId = document.getElementById("productIdForImage").value;
    const formData = new FormData(document.getElementById("uploadImageForm"));

    fetch(`/product/upload-image/${productId}`, {
        method: 'POST',
        body: formData,
    })
        .then(response => {
            if (response.ok) {
                alert('Image uploaded successfully!');
                closeUploadImageModal()
                window.location.reload()
            } else {
                alert('Error uploading image!');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error uploading image!');
        });
});