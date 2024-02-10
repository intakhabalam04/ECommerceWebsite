document.onreadystatechange = function () {
    var spinner = document.getElementById("loading-spinner");
    var customerSection = document.getElementById("customer-link");

    spinner.style.display = "block";

    setTimeout(function () {
        spinner.style.display = "none";
        customerSection.style.display = "block";
    }, 2000);
};