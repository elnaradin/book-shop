$(document).ready(function () {

    $('#logout').click(function (e) {
        e.preventDefault();
        $.get("/logout", function(){
            window.location.replace("/")
        });
    });

});