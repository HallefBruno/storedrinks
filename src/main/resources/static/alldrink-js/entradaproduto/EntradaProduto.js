/* global StoreDrink */
var toast = new StoreDrink.Toast();

$(function () {
  
  let URI = $("#context").val();
  $("#codigoBarra").focus();
  
  popularSelectProdutos(URI);
  eventOpenAndClearSelectProdutos();
  produtoSelectionado(URI);
  iniciarSelectFornecedorSituacaoCompraFormaPagamento();
  getProdutoEventCodBarra(URI);
  
  
  $("#btnSalvar").click(function() {
    validarCampos();
    let entradaProduto = {};
    $.each($("form").serializeArray(), function (i, field) {
      entradaProduto[field.name] = field.value;
    });
    window.console.log(entradaProduto);
  });
  
});

function validarCampos() {
  
  let descricaoProdutoAtual = $("#descricaoProdutoAtual").val();
  if(descricaoProdutoAtual === undefined || descricaoProdutoAtual === "") {
    toast.show('error','Atenção','É necessário selecionar um produto!','top-right');
    return;
  }
  
  
//  idProduto = $("#produto").select2("data")[0].id;
//  if(idProduto === undefined || idProduto === "") {
//    $("#produto").addClass("is-invalid");
//    toast.show('error','Atenção','Produto é obrigatório!','top-right');
//  } else {
//    $("#produto").removeClass("is-invalid");
//  }
}

function getProdutoEventCodBarra(URI) {
  $("#codigoBarra").on("focusout", function (event) {
    let codBarra = $("#codigoBarra").val();
    if (codBarra !== undefined && codBarra !== null && codBarra !== "") {
      $.get(URI.concat(`entradas/buscar/produtoPorCodBarra/${codBarra}`), function (data) {
        if (data === undefined || data === null && codBarra !== "") {
          clearFormShowMessage();
          $("#codigoBarra").focus();
          toast.show('info','Atenção','Nenhum produto encontrado!','top-right');
        }
        if (data !== undefined && data !== null) {
          $("#produtos").val(null).trigger("change");
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
        }
      });

    } else if ($("#codigoBarra").val() === "") {
      $("form").trigger("reset");
      $("#produtos").val(null).trigger("change");
      $("#situacaoCompra").val(null).trigger("change");
    }
  });
}


function eventOpenAndClearSelectProdutos() {
  $("#produto").on("select2:open", function (e) {
    $(".select2-search__field")[0].focus();
  });
  
  $("#produto").on("select2:clearing", function () {
    $("#descricaoProdutoAtual").val("");
    $("#codigoBarra").val("");
    $("#codigoProdutoAtual").val("");
    $("#valorCusto").val("");
    $("#valorVenda").val("");
    $("#quantidade").val("");
  });
}

function produtoSelectionado(URI) {
  $("#produto").on("select2:select", function (e) {
    $.get(URI.concat("entradas/buscar/").concat(e.params.data.id), function (data) {
      if (data !== undefined && data !== null) {
        $("#descricaoProdutoAtual").val(data.descricaoProduto);
        $("#codigoBarra").val(data.codigoBarra);
        $("#codigoProdutoAtual").val(data.codProduto);
        $("#quantidade").val(data.quantidade);
        $("#valorCusto").val(data.valorCusto);
        $("#valorVenda").val(data.valorVenda);
        $("#valorCusto").focus();
        $("#valorVenda").focus();
        $("#quantidade").focus();
      } else {
        //clearFormShowMessage(toast);
      }
    });
  });
}

function popularSelectProdutos(URI) {
  $("#produto").select2({
    theme: "bootstrap-5",
    allowClear: true,
    language: "pt-BR",
    multiple: false,
    closeOnSelect: true,
    minimumInputLength: 3,
    ajax: {
      url: URI.concat("entradas/produtos"),
      dataType: "json",
      data: function (params) {
        return {
          q: params.term,
          page: params.page
        };
      },
      processResults: function (data, params) {
        params.page = params.page || 1;
        if (data.items === null) {
          $("form").trigger("reset");
          $("#produto").val(null).trigger("change");
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
    },
    templateResult: styleTemplateResult,

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

function styleTemplateResult(produto) {
  if (produto && produto.text !== "Searching…") {
    return $("<span class='badge bg-secondary fw-normal' style='font-size:14px;'>" + produto.text + "</span>");
  }
  return $("<span>" + produto.text + "</span>");
}

function clearFormShowMessage() {
  $("form").trigger("reset");
  $("#produto").val(null).trigger("change");
  $("#situacaoCompra").val(null).trigger("change");
  $("#formaPagamento").val(null).trigger("change");
}

function iniciarSelectFornecedorSituacaoCompraFormaPagamento() {
  $("#fornecedor").select2({
    theme: "bootstrap-5",
    allowClear: true,
    multiple: false,
    closeOnSelect: true
  });

  $("#situacaoCompra").select2({
    theme: "bootstrap-5",
    allowClear: true,
    multiple: false,
    closeOnSelect: true
  });

  $("#formaPagamento").select2({
    theme: "bootstrap-5",
    allowClear: true,
    multiple: false,
    closeOnSelect: true
  });
}


//function validarCampos() {
//  $.each($("form")[0], function(){
//    getValue = $(this)[0];
//    component = $(this);
//    console.log(getValue);
//    if(getValue.value === undefined || getValue.value === "") {
//      component.addClass("is-invalid");
//    }
//  });
//}