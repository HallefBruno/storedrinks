$(document).ready(function () {
    $('.nav li a').click(function(e) {
        $(this).removeClass('active');
        var $parent = $(this).parent();
        $parent.addClass('active');
    });
});