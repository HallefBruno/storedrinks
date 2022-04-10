/* global StoreDrink */

let mensagem = new StoreDrink.Mensagem();
let listProdutos = [];
let quantidadeEstoqueAtual = 0;

$(function () {
  initDataTable();
  popularSelectProdutos();
  eventKeyEnter();
});

function initDataTable() {
  $("#itensVenda").DataTable({
    paginate: false,
    lengthChange: false,
    info: false,
    autoWidth: false,
    filter: false,
    responsive: true,
    language: {
      url: `${$("#context").val()}vendor/internationalisation/pt_br.json`
    },
    columnDefs: [
      {
        targets: [2, 4],
        className: 'text-center'
      }
    ]
  });
}

function popularSelectProdutos() {
  
  $("#produtos").on("select2:open", function (e) {
    $(".select2-search__field")[0].focus();
  });
  
  $("#produtos").on("select2:select", function (e) {
    const produto = e.params.data;
    window.console.log(produto);
    quantidadeEstoqueAtual = Number(produto.quantidade);
    $("#descricaoProduto").val(produto.descricaoProduto);
    $("#codigoBarra").val(produto.codBarra);
    $("#codProduto").val(produto.codProduto);
    $("#valorVenda").val("R$ " + produto.valorVenda);
    $("#qtdEstoque").val(produto.quantidade);
    $("#quantidade").focus();
  });
  
  $("#produtos").on("select2:unselecting", function (e) {
    clearFormFocusSelect();
  });
  
  $("#produtos").select2({
    theme: "bootstrap-5",
    allowClear: true,
    language: "pt-BR",
    multiple: false,
    closeOnSelect: true,
    minimumInputLength: 3,
    ajax: {
      url: `${$("#context").val()}vendas/produtos`,
      dataType: "json",
      delay: 1000,
      data: function (params) {
        return {
          q: params.term,
          page: params.page
        };
      },
      processResults: function (data, params) {
        params.page = params.page || 1;
        return {
          results: data.items,
          pagination: {
            more: (params.page * 10) < data.totalItens
          }
        };
      },
      cache: true
    },
    templateResult: templateResultProduto,

    escapeMarkup: function (markup) {
      return markup;
    },
    templateSelection: function (produto) {
      if (produto && produto.text !== "Procure aqui seu produto") {
        return $("<span class='badge bg-secondary fw-normal' style='font-size:14px;'>" + produto.text + "</span>");
      }
      return $("<span class=''>" + produto.text + "</span>");
    }
  });
}

function templateResultProduto(produto) {
  if (produto && !produto.text.includes("Buscando")) {
    return $("<span class='badge bg-secondary fw-normal' style='font-size:14px;'>" + produto.text + "</span>");
  }
  return $("<span>" + produto.text + "</span>");
}

function clearFormFocusSelect() {
  $("form").trigger("reset");
  $("#produtos").val(null).trigger("change");
  $("#codigoBarra").focus();
}

function eventKeyEnter() {
  
  let typingTimer;
  let intervaloDigitacao = 800;

  $("#quantidade").keyup(function () {
    clearTimeout(typingTimer);
    if ($("#quantidade").val()) {
      typingTimer = setTimeout(qtdAtualMaiorQtdVenda, intervaloDigitacao);
    }
  });

  $(document).on("keypress", function (e) {
    if (e.which === 13) {
      if ($("#produtos option:selected").text()) {
        if (qtdAtualMaiorQtdVenda()) {
          addProduto();
        }
      }
    }
  });
}

function qtdAtualMaiorQtdVenda() {
  const quantidade = Number($("#quantidade").val());
  if (quantidade > quantidadeEstoqueAtual) {
    $.toast({
      heading: "<p class='mb-1'>Atenção<p><hr/>",
      text: 'Você não possui essa quantidade em estoque!',
      bgColor: '#000000',
      textColor: '#ffffff',
      position: 'top-right',
      hideAfter: 7000,
      loader: false
    });
    $("#quantidade").val("");
    return false;
  }
  return true;
}

function addProduto() {
  var produto = {};
  if ($("#descricaoProduto").val() && qtdAtualMaiorQtdVenda()) {
    var valorUni = Number($("#valorVenda").val());
    var quantidade = Number($("#quantidade").val());
    if (quantidade === 0) {
      quantidade = quantidade + 1;
    }
    produto = {
      descricaoProduto: $("#descricaoProduto").val(),
      codigoBarra: $("#codigoBarra").val(),
      codProduto: $("#codProduto").val(),
      valorVenda: valorUni.toLocaleString('pt-br', {style: 'currency', currency: 'BRL'}),
      quantidade: quantidade,
      valorTotal: (quantidade * $("#valorVenda").val()).toLocaleString('pt-br', {style: 'currency', currency: 'BRL'})
    };
    
    listProdutos.push(produto);
    //popularTable(listProdutos);
    clearFormFocusSelect();

  } else if ($("#descricaoProduto").val().length === 0) {
    clearFormFocusSelect();
    mensagemToast("Por favor selecione um item!","#000000","#ffffff");
  }
}

function mensagemToast(text, bgColor, textColor) {
  $.toast({
    heading: `<p class='mb-1'>Atenção<p><hr/>`,
    text: `${text}`,
    bgColor: `${bgColor}`,
    textColor: `${textColor}`,
    position: 'top-right',
    hideAfter: 7000,
    loader: false
  });
}


//if (containsAttInList(produto, produto.codigoBarra, listProdutos)) {
//  mensagemToast("Este produto ja foi selecionado!", "#b89a06", "#000000");
//  return;
//}