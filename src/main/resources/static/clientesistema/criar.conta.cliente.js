/* global bootstrap */

$(function () {
    
    $("#cpfCnpj").val(atob(localStorage.getItem("cpfCnpj")));
    $("#password").mask("00000S0S");
    $('[data-bs-toggle="popover"]').popover("show");
//$("#password").mask("#####S#S");
//    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
//    popoverTriggerList.map(function (popoverTriggerEl) {
//        return new bootstrap.Popover(popoverTriggerEl);
//    });

});