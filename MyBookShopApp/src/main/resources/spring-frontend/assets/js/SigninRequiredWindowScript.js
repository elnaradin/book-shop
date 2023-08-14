$(document).ready(function () {

    $('.popup-trigger').click(function (event) {
        var triggerElement = $(this);
        var popupOverlay;
        event.preventDefault();
        if(triggerElement.hasClass("rating-trigger")){
            popupOverlay = $("#rating-popup");
        }
        if(triggerElement.hasClass("download-trigger")){
            popupOverlay = $("#download-popup");
        }
        if(triggerElement.hasClass("like-trigger")){
            popupOverlay = $("#like-popup");
        }
        popupOverlay.find('.signinOverlay').fadeIn(297, function () {
            popupOverlay.find('.signinPopup').css('display', 'block').animate({opacity: 1}, 198);
        });
    });

    $('.signinPopup_close, .signinOverlay').click(function () {
        $('.signinPopup').animate({opacity: 1}, 198, function () {
            $(this).css('display', 'none');
            $('.signinOverlay').fadeOut(297);
        });
    });
});