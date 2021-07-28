/* global Swal */

var listProdutos = [];
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
        const produto = e.params.data;
        $("#descricaoProduto").val(produto.descricaoProduto);
        $("#codigoBarra").val(produto.codBarra);
        $("#codProduto").val(produto.codProduto);
        $("#valorVenda").val(produto.valorVenda);
        $("#quantidade").focus();
        
    });
    
    $("#produtos").on("select2:unselecting", function (e) {
        clearFormFocusSelect();
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
    
    actionTableItens();
    addItemVenda();
    
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

function addItemVenda() {
    var produto = {};
    $("#btnItemVenda").click(function (event) {
        var valorUni = Number($("#valorVenda").val());
        
        produto = {
            descricaoProduto:$("#descricaoProduto").val(),
            codigoBarra:$("#codigoBarra").val(),
            codProduto:$("#codProduto").val(),
            valorVenda:valorUni.toLocaleString('pt-br',{style: 'currency', currency: 'BRL'}),
            quantidade:$("#quantidade").val(),
            valorTotal:Number($("#quantidade").val() * $("#valorVenda").val()).toLocaleString('pt-br',{style: 'currency', currency: 'BRL'})
        };

        var htmlbtnGroup =`
            <div class='btn-group btn-group-sm' role='group' aria-label='grupo vendas'> 
                <button id='btnDiminuirQuantidade' data-key='${produto.codigoBarra}' type="button" title="Diminuir quantidade" class="btn btn-outline-secondary"><i class="fa fa-minus" aria-hidden="true"></i></i></button>
                <button id='btnAumentarQuantidade' data-key='${produto.codigoBarra}' type="button" title='Aumentar quantidade' class="btn btn-outline-secondary"><i class="fa fa-plus-circle" aria-hidden="true"></i></button>
                <button id='btnRemoverItemVenda' data-key='${produto.codigoBarra}' type='button' title='Remover item' class='btn btn-outline-secondary'><i class="fa fa-trash-o" aria-hidden="true"></i></button>
            </div>`;
        
        acoes(produto, htmlbtnGroup);
    });
}

function acoes(produto, htmlbtnGroup) {
    
    window.console.log("ações!");
    
    if(containsObject(produto,listProdutos)) {
        mensagem();
        return;
    }
    
    listProdutos.push(produto);
    window.console.log(listProdutos);
    
    $("#itensVenda").on("click", "#btnDiminuirQuantidade", function (e) {
        e.stopImmediatePropagation();
        var codBarra = $(this).data("key");
        alert(codBarra);
    });

    popularTable(produto, htmlbtnGroup);
    clearFormFocusSelect();
}

function popularTable(produto,htmlbtnGroup) {
    $("#itensVenda").DataTable().row.add([
        produto.descricaoProduto,
        produto.valorVenda,
        produto.quantidade,
        produto.valorTotal,
        htmlbtnGroup
    ]).draw(false);
}

function actionTableItens() {
    
    $("#itensVenda").DataTable({
        paginate: false,
        lengthChange: false,
        info: false,
        autoWidth: false,
        filter: false,
        responsive: true,
//        language: {
//            url: "https://cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Portuguese-Brasil.json"
//        },
        columnDefs: [
            {
                targets: [2,4],
                className: 'text-center'
            }
        ]
    });

}

function clearFormFocusSelect() {
    $("form").trigger("reset");
    $("#produtos").val(null).trigger("change");
    $("#produtos").focus();
}

function containsObject(obj, list) {
    return list.some(elem => elem.codigoBarra === obj.codigoBarra);
}

function mensagem() {
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
        text: `Esse produto já está na lista, é possível alterar a quantidade`
    });
}