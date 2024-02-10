function openAddCategoryModal() {
    document.getElementById("categoryForm").reset();
    document.getElementById("modalTitle").innerHTML = "Add Category";
    document.getElementById("submitBtn").innerHTML = "Add Category";
    document.getElementById("categoryModal").style.display = "block";
    document.getElementById("").style.color="red"
}

function openUpdateCategoryModal(categoryId, categoryName, categoryDescription) {
    document.getElementById("categoryForm").reset();
    document.getElementById("modalTitle").innerHTML = "Update Category";
    document.getElementById("submitBtn").innerHTML = "Update Category";


    document.getElementById("categoryId").value = categoryId;
    document.getElementById("categoryName").value = categoryName;
    document.getElementById("categoryDescription").value = categoryDescription;

    document.getElementById("categoryForm").setAttribute("th:object", "${category}");
    document.getElementById("categoryModal").style.display = "block";
}

function closeModal() {
    document.getElementById("categoryModal").style.display = "none";
}

async function confirmDelete(categoryId) {
    const confirmation = confirm("Are you sure you want to delete this category?");
    if (confirmation) {
        try{
            const response = await fetch(`/admin/category/delete?categoryId=${categoryId}`,{
                method:"DELETE"
            })
            if (response.ok){
                const isDelete = await response.json()
                if (isDelete){
                    alert("Deleted successfully")
                    location.reload()
                }else{
                    alert("Cannot delete")
                }
            }else {
                console.error("Something wents wrong on the category.html")
            }
        }catch (e){
            console.error("Something wents wrong on the category.html")
        }
    }
}