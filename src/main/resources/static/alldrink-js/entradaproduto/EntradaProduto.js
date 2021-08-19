/* global bootstrap, Swal */

$(function () {
    
   $("#valorCusto").mask("#,##0.00", {reverse: true});
   $("#valorVenda").mask("#,##0.00", {reverse: true});
   $("#novoValorCusto").mask("#,##0.00", {reverse: true});
   $("#novoValorVenda").mask("#,##0.00", {reverse: true});
   $("#valorTotal").mask("#,##0.00", {reverse: true});
       
    var toast = "<div style='height: 44px; width: 100%;' class='toast align-items-center text-white bg-warning border-0 mt-1 mb-1' data-delay='5000' data-animation='true' data-autohide='true' role='alert' aria-live='assertive' aria-atomic='true'>" +
            "<div class='d-flex'>" +
            "<div class='toast-body'>" +
            "<p class=''>Nenhum produto encontrado!</p>" +
            "</div>" +
            "<button type='button' class='btn-close btn-close-white me-2 m-auto' data-bs-dismiss='toast' aria-label='Close'></button>" +
            "</div>" +
            "</div>";
    
    $("form").submit(function(event) {
        if($("#valorTotal").val()) {
            $("#valorTotal").val($("#valorTotal").val().replace("R$ ",""));
        }
    });
    
    $("#novaQuantidade").mask("0000");
    
    popularSelectProdutos(toast);

    $("#produtos").on("select2:open", function (e) {
        $(".select2-search__field")[0].focus();
    });
    
    $("#produtos").on("select2:unselecting", function (e) {
        $("form").trigger("reset");
        $("#produtos").val(null).trigger("change");
        $("#situacaoCompra").val(null).trigger("change");
    });

    $("#produtos").on("select2:select", function (e) {
        $.get($("#contextApp").val() + "entradas/buscar/" + e.params.data.id, function (data) {
            if (data !== undefined && data !== null) {
                $("#descricaoProdutoAtual").val(data.descricaoProduto);
                $("#codigoBarra").val(data.codigoBarra);
                $("#codigoProdutoAtual").val(data.codProduto);
                if (data.valorCusto === 0) {
                    $("#valorCusto").maskMoney("destroy");
                    $("#novoValorCusto").prop("required", true);
                }
                if (data.valorVenda === 0) {
                    $("#valorVenda").maskMoney("destroy");
                    $("#novoValorVenda").prop("required", true);
                }
                if (data.quantidade === 0) {
                    $("#novaQuantidade").prop("required", true);
                }
                $("#quantidade").val(data.quantidade);
                $("#valorCusto").val(data.valorCusto);
                $("#valorVenda").val(data.valorVenda);
                $("#valorCusto").focus();
                $("#valorVenda").focus();
                $("#quantidade").focus();
            } else {
                clearFormShowMessage(toast);
            }
        });
    });

    $("#situacaoCompra").select2({
        theme: "bootstrap-5"
    });

    $("#codigoBarra").focus();
    
    $("#codigoBarra").on("focusout", function (event) {
        if ($("#codigoBarra").val() !== undefined && $("#codigoBarra").val() !== null && $("#codigoBarra").val() !== "") {
            $.get($("#contextApp").val() + "entradas/buscar/produtoPorCodBarra/" + $("#codigoBarra").val(), function (data) {
                if (data === undefined || data === null && $("#codigoBarra").val() !== "") {
                    clearFormShowMessage(toast);
                }
                if (data !== undefined && data !== null) {
                    $("#produtos").val(null).trigger("change");
                    $("#descricaoProdutoAtual").val(data.descricaoProduto);
                    $("#codigoBarra").val(data.codigoBarra);
                    $("#codigoProdutoAtual").val(data.codProduto);
                    
                    if(data.valorCusto === 0) {
                        $("#valorCusto").maskMoney("destroy");
                        $("#novoValorCusto").prop("required",true);
                    }
                    if(data.valorVenda === 0) {
                        $("#valorVenda").maskMoney("destroy");
                        $("#novoValorVenda").prop("required",true);
                    }
                    if (data.quantidade === 0) {
                        $("#novaQuantidade").prop("required",true);
                    }
                    
                    $("#quantidade").val(data.quantidade);
                    $("#valorCusto").val(data.valorCusto);
                    $("#valorVenda").val(data.valorVenda);
                    $("#valorCusto").focus();
                    $("#valorVenda").focus();
                    $("#quantidade").focus();
                }
            });
            
        } else if ($("#codigoBarra").val() === "") {
            $("form").trigger("reset");
            $("#produtos").val(null).trigger("change");
            $("#situacaoCompra").val(null).trigger("change");
        }
    });
    
    $("#btnCalcular").click(function () {
        var novoValorCusto = $("#novoValorCusto");
        var novaQuantidade = $("#novaQuantidade");
        var valorTotal = $("#valorTotal");
        
        if((novaQuantidade !== undefined && novaQuantidade !== null && novaQuantidade.val() !== "" && Number(novaQuantidade.val()) > 0) && (novoValorCusto !== undefined && novoValorCusto !== null && novoValorCusto.val() !== "" && Number(novoValorCusto.val()) > 0)) {
            var soma = novaQuantidade.val() * novoValorCusto.val();
            soma = soma.toLocaleString({minimumFractionDigits: 2, maximumFractionDigits: 2}).replace(",",".");
            valorTotal.val("R$ "+soma); //.toFixed(2)
            valorTotal.focus();
        } else {
            const Toast = Swal.mixin({
                toast: true,
                position: 'top-end',
                showConfirmButton: false,
                timer: 8000,
                timerProgressBar: true,
                didOpen: (toast) => {
                    toast.addEventListener('mouseenter', Swal.stopTimer);
                    toast.addEventListener('mouseleave', Swal.resumeTimer);
                }
            });

            Toast.fire({
                icon: 'warning',
                text: `Para realizar o calculo, preencha a quantidade e valor de custo`
            });
        }
    });

});

function popularSelectProdutos(toast) {
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
            delay: 1000,
            data: function (params) {
                return {
                    q: params.term,
                    page: params.page
                };
            },
            processResults: function (data, params) {
                params.page = params.page || 0;
                if(data.items === null) {
                    $("form").trigger("reset");
                    $("#produtos").val(null).trigger("change");
                    $("#situacaoCompra").val(null).trigger("change");
                }
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

function clearFormShowMessage(toast) {
    $("#nenhumProduto").css("display", "block");
    $("#nenhumProduto").html(toast);
    $(".toast").toast('show');
    $("form").trigger("reset");
    $("#produtos").val(null).trigger("change");
    $("#situacaoCompra").val(null).trigger("change");
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