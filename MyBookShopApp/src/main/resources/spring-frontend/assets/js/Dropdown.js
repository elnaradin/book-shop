/* When the user clicks on the button, 
toggle between hiding and showing the dropdown content */
function authorsFunction() {
    document.getElementById("authorsDropdown").classList.toggle("show");
}
function tagsFunction() {
    document.getElementById("tagsDropdown").classList.toggle("show");
}
function genresFunction() {
    document.getElementById("genresDropdown").classList.toggle("show");
}
//Close the dropdown if the user clicks outside of it
window.onclick = function (event) {
    if (!event.target.matches('.dropbtn') && !event.target.matches('.search_input')) {
        var dropdowns = document.getElementsByClassName("dropdown-content");
        var i;
        for (i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    }
}
function filterAuthorsFunction() {
    var input, filter, ul, li, a, i;
    input = document.getElementById("authorInput");
    filter = input.value.toUpperCase();
    div = document.getElementById("authorsDropdown");
    a = div.getElementsByTagName("a");
    for (i = 0; i < a.length; i++) {
        txtValue = a[i].textContent || a[i].innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            a[i].style.display = "";
        } else {
            a[i].style.display = "none";
        }
    }
}
function filterGenresFunction() {
    var input, filter, ul, li, a, i;
    input = document.getElementById("genreInput");
    filter = input.value.toUpperCase();
    div = document.getElementById("genresDropdown");
    a = div.getElementsByTagName("a");
    for (i = 0; i < a.length; i++) {
        txtValue = a[i].textContent || a[i].innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            a[i].style.display = "";
        } else {
            a[i].style.display = "none";
        }
    }
}
function filterTagsFunction() {
    var input, filter, ul, li, a, i;
    input = document.getElementById("tagInput");
    filter = input.value.toUpperCase();
    div = document.getElementById("tagsDropdown");
    a = div.getElementsByTagName("a");
    for (i = 0; i < a.length; i++) {
        txtValue = a[i].textContent || a[i].innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            a[i].style.display = "";
        } else {
            a[i].style.display = "none";
        }
    }
}