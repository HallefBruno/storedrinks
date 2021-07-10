/* global bootstrap */

$(function () {
    
    $("#valorCusto").mask("#,##0.00", {reverse: true});
//    $("#valorTotal").mask("#,##0.00", {reverse: true});
    $("#valorCusto").maskMoney({prefix:'R$ ', allowNegative: false, thousands:',', decimal:'.', affixesStay: false});
    
    $("#produtos").select2({
        theme: "bootstrap-5"
    });
    
    $("#produtos").on("select2:select", function (e) {
        $.get($("#contextApp").val()+"entradas/buscar/"+e.params.data.id, function (data) {
            $("#valorCusto").val(data.valorCusto);
            $("#valorCusto").focus();
        });
    });

    $("#situacao").select2({
        theme: "bootstrap-5"
    });
});












//function popoverDetalheProduto(produto) {
//    var popover = new bootstrap.Popover(document.querySelector('.popover-dismiss'), {
//        trigger: 'focus',
//        content: function () {
//            return $(this).next('.popper-content').html();
//        }
//    });
//}


//    $(".popper").popover({
//        placement: "bottom",
//        container: "body",
//        html: true,
//        content: function () {
//            return $(this).next('.popper-content').html();
//        }
//    });
//    data-bs-content="And here's some amazing content. It's very engaging. Right?"