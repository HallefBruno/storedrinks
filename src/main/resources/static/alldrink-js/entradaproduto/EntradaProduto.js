/* global bootstrap */

$(function () {
    
    $("#valorCusto").mask("#,##0.00", {reverse: true});
    $("#valorCusto").maskMoney({prefix: 'R$ ', allowNegative: false, thousands: ',', decimal: '.', affixesStay: false});

    popularSelectProdutos();
    
    $("#produtos").on("select2:open", function (e) {
        $(".select2-search__field")[0].focus();
    });
    
    $("#produtos").on("select2:select", function (e) {
        $.get($("#contextApp").val() + "entradas/buscar/" + e.params.data.id, function (data) {
            $("#valorCusto").val(data.valorCusto);
            $("#valorCusto").focus();
        });
    });

    $("#situacao").select2({
        theme: "bootstrap-5"
    });
    
    $("#codigoBarra").focus();

    $("#codigoBarra").on("focusout", function (event) {
        $.get($("#contextApp").val() + "entradas/buscar/produtoPorCodBarra/" +$("#codigoBarra").val(), function (data) {
            //$("#produtos").select2().select2('val',data.codigoBarra);
            $("#produtos").val(null).trigger('change');
        });
    });
    
    $(document).on('focus', '.select2-selection.select2-selection--single', function (e) {
        $(this).closest(".select2-container").siblings('select:enabled').select2('open');
    });
    
});

function popularSelectProdutos() {
    $("#produtos").select2({
        theme: "bootstrap-5",
        allowClear: true,
        language: "pt-BR",
        multiple: false,
        closeOnSelect: true,
        minimumInputLength: 3,
        ajax: {
            url: $("#contextApp").val() + "entradas/produtos",
            dataType: "json",
            data: function (params) {
                return {
                    q: params.term,
                    page: params.page
                };
            },
            processResults: function (data, params) {
                params.page = params.page || 0;
                return {
                    results: data.items,
                    pagination: {
                        more: (params.page * 10) < data.totalItens
                    }
                };
            },
            cache: true
        }
    });
}











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