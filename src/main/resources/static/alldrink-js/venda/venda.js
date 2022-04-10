/* global StoreDrink, formatter */

let mensagem = new StoreDrink.Mensagem();
let listProdutos = [];
let produto = {};
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
    produto = e.params.data;
    window.console.log(produto);
    quantidadeEstoqueAtual = Number(produto.quantidade);
    $("#descricaoProduto").val(produto.descricaoProduto);
    $("#codigoBarra").val(produto.codBarra);
    $("#codProduto").val(produto.codProduto);
    $("#valorVenda").val("R$ " + produto.valorVenda);
    $("#qtdEstoque").val(produto.quantidade);
    $("#quantidade").val(1);
    $("#quantidade").focus();
    $("#quantidade").select();
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
      hideAfter: 5000,
      loader: false
    });
    $("#quantidade").val("");
    return false;
  }
  return true;
}

function addProduto() {
  let prod = {};
  if ($("#descricaoProduto").val() && qtdAtualMaiorQtdVenda()) {
    let valorVenda = Number(produto.valorVenda);
    let quantidade = Number($("#quantidade").val());
    
    prod = {
      descricaoProduto: $("#descricaoProduto").val(),
      codigoBarra: $("#codigoBarra").val(),
      codProduto: $("#codProduto").val(),
      valorVenda: formatter.format(valorVenda),
      quantidade: quantidade,
      valorTotal: formatter.format((quantidade * valorVenda))
    };
    
    listProdutos.push(prod);
    popularTable(listProdutos);
    clearFormFocusSelect();

  } else if ($("#descricaoProduto").val().length === 0) {
    clearFormFocusSelect();
    mensagemToast("Por favor selecione um item!","#000000","#ffffff");
  }
}

function popularTable(listProdutos) {
  $("#itensVenda").DataTable().clear().draw();
  $.each(listProdutos, function (index, produto) {
    var htmlbtnGroup = `
      <div class='btn-group btn-group-sm' role='group' aria-label='grupo vendas'> 
          <button data-key='${produto.codigoBarra}' type="button" title="Diminuir quantidade" class="btnDiminuirQuantidade btn btn-outline-secondary"><i class="fa fa-minus" aria-hidden="true"></i></i></button>
          <button data-key='${produto.codigoBarra}' type="button" title='Aumentar quantidade' class="btnAumentarQuantidade btn btn-outline-secondary"><i class="fa fa-plus-circle" aria-hidden="true"></i></button>
          <button data-key='${produto.codigoBarra}' type='button' title='Remover item' class='btnRemoverItemVenda btn btn-outline-secondary'><i class="fa fa-trash-o" aria-hidden="true"></i></button>
      </div>`;
    $("#itensVenda").DataTable().row.add([
      produto.descricaoProduto,
      produto.valorVenda,
      produto.quantidade,
      produto.valorTotal,
      htmlbtnGroup
    ]).draw(false);
  });
}

function mensagemToast(text, bgColor, textColor) {
  $.toast({
    heading: `<p class='mb-1'>Atenção<p><hr/>`,
    text: `${text}`,
    bgColor: `${bgColor}`,
    textColor: `${textColor}`,
    position: 'top-right',
    hideAfter: 5000,
    loader: false
  });
}