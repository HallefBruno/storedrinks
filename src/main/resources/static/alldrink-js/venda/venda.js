$(function () {
    $("#valorCusto").mask("#,##0.00", {reverse: true});
    
    $("form").submit(function (e) {
        return false;
    });
    
    popularSelectProdutos();
    
    $("#produtos").focus();
    
    $("#produtos").on("select2:open", function (e) {
        $(".select2-search__field")[0].focus();
    });
    
    
    $("#produtos").on("select2:select", function (e) {
        window.console.log(e.params.data);
        const produto = e.params.data;
        $("#descricaoProduto").val(produto.descricaoProduto);
        $("#codigoBarra").val(produto.codBarra);
        $("#codProduto").val(produto.codProduto);
        $("#valorVenda").val(produto.valorVenda);
        
    });
    
    $("#produtos").on("select2:unselecting", function (e) {
        $("form").trigger("reset");
        $("#produtos").val(null).trigger("change");
    });

    var typingTimer;
    var intervaloDigitacao = 700;

    $("#quantidade").keyup(function () {
        clearTimeout(typingTimer);
        if ($("#quantidade").val()) {
            typingTimer = setTimeout(doneTyping, intervaloDigitacao);
        }
    });
    
    $(window).keyup(function (event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode === 13) {
            event.preventDefault();
        }
    });
    
});
function doneTyping() {
    console.log('parei de digitar');
}

function popularSelectProdutos() {
    
    $("#produtos").select2({
        theme: "bootstrap-5",
        allowClear: true,
        language: "pt-BR",
        multiple: false,
        closeOnSelect: true,
        minimumInputLength: 3,
        ajax: {
            url: $("#contextApp").val() + "pdv/produtos",
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
                return {
                    results: data.items,
                    pagination: {
                        more: (params.page * 10) < data.totalItens
                    }
                };
            },
            cache: true
        },
        templateResult:styleSelectAutomoveis,
        
        escapeMarkup: function (markup) {
            return markup;
        },
        templateSelection: function (produto) {
            if(produto && produto.text !== "Procure aqui seu produto") {
                return $("<span class='badge bg-primary' style='font-size:13px;'>" + produto.text + "</span>");
            }
            return $("<span class=''>" + produto.text + "</span>");
        }
    });
}

function styleSelectAutomoveis(produto) {
    if(produto && produto.text !== "Searching…") {
        return $("<span class='badge bg-primary' style='font-size:13px;'>" + produto.text + "</span>");
    }
    return $("<span>" + produto.text + "</span>");
}