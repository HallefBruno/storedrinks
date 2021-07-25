$(function () {

    var typingTimer;
    var intervaloDigitacao = 700;

    $("#quantidade").keyup(function () {
        clearTimeout(typingTimer);
        if ($("#quantidade").val()) {
            typingTimer = setTimeout(doneTyping, intervaloDigitacao);
        }
    });
});
function doneTyping() {
    console.log('parei de digitar');
}