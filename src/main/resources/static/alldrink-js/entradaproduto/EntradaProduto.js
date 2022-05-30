/* global StoreDrink, EnumEntradaProduto, moment */
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
    if(validarCampos()) {

      let entradaProduto = {};
      
      $.each($("form").serializeArray(), function (i, field) {
        entradaProduto[field.name] = field.value === "" ? null : field.value;
      });

      let produto = {id:Number(entradaProduto.produto)};
      let fornecedor = {id:Number(entradaProduto.fornecedor)};

      entradaProduto["produto"] = produto;
      entradaProduto["fornecedor"] = fornecedor;
      entradaProduto["dataEmissao"] = moment($("#dataEmissao").val()).format('YYYY-MM-DD');
      
      if(!isEmpaty(entradaProduto.novoValorCusto)) {
        let novoValorCusto = entradaProduto.novoValorCusto.replace(",","");
        entradaProduto["novoValorCusto"] = novoValorCusto;
      }
      
      if(!isEmpaty(entradaProduto.novoValorVenda)) {
        let novoValorVenda = entradaProduto.novoValorVenda.replace(",","");
        entradaProduto["novoValorVenda"] = novoValorVenda;
      }
      
      $.ajax({
        url: URI.concat("entradas"),
        type: "POST",
        dataType: 'json',
        contentType: "application/json",
        data: JSON.stringify(entradaProduto),
        statusCode: {
          201: function (response) {
            toast.show('success', 'Atenção', 'Entrada salva com sucesso!', 'top-right');
            clearForm();
          }
        },
        error: function (xhr) {
          if (status >= 400) {
            window.console.log(xhr);
          }
        }
      });
    }
  });
});

function validarCampos() {
  
  let isCamposValidos = true;
  let descricaoProdutoAtual = $("#descricaoProdutoAtual").val();
  
  if(isEmpaty(descricaoProdutoAtual)) {
    toast.show('warning','Atenção','É necessário selecionar um produto!','top-right');
    isCamposValidos = false;
    return;
  }
  
  let numeroNota = $("#numeroNota");
  let fornecedor = $("#fornecedor");
  let dataEmissao = $("#dataEmissao");
  let formaPagamento = $("#formaPagamento");
  let qtdParcelas = $("#qtdParcelas");
  let situacaoCompra = $("#situacaoCompra");
  
  if(isEmpaty(numeroNota.val())) {
    numeroNota.addClass("is-invalid");
    toast.show('warning','Atenção','Número da nota é obritória!','top-right');
    isCamposValidos = false;
  } else {
    numeroNota.removeClass("is-invalid");
  }
  
  if(isEmpaty(fornecedor.val())) {
    fornecedor.addClass("is-invalid");
    toast.show('warning','Atenção','Fornecedor é obrigatório!','top-right');
    isCamposValidos = false;
  } else {
    fornecedor.removeClass("is-invalid");
  }
  
  if(isEmpaty(dataEmissao.val())) {
    dataEmissao.addClass("is-invalid");
    toast.show('warning','Atenção','Data de emissão é obrigatória!','top-right');
    isCamposValidos = false;
  } else {
    dataEmissao.removeClass("is-invalid");
  }
  
  if(isEmpaty(formaPagamento.val())) {
    formaPagamento.addClass("is-invalid");
    toast.show('warning','Atenção','Forma de pagamento é obrigatória!','top-right');
    isCamposValidos = false;
  } else {
    formaPagamento.removeClass("is-invalid");
  }
  
  if(isEmpaty(situacaoCompra.val())) {
    situacaoCompra.addClass("is-invalid");
    toast.show('warning','Atenção','Situação da compra é obrigatória!','top-right');
    isCamposValidos = false;
  } else {
    situacaoCompra.removeClass("is-invalid");
  }
  
  if(!isEmpaty(formaPagamento.val()) && formaPagamento.val() === EnumEntradaProduto.CARTAO_CREDITO) {
    if(isEmpaty(qtdParcelas.val())) {
      qtdParcelas.addClass("is-invalid");
      toast.show('warning','Atenção','Quantidade de parcelas é obrigatória!','top-right');
      isCamposValidos = false;
    } else if (Number(qtdParcelas.val()) <= 0) {
      qtdParcelas.addClass("is-invalid");
      toast.show('warning','Atenção','Quantidade de parcelas é inválida!','top-right');
      isCamposValidos = false;
    } else {
      qtdParcelas.removeClass("is-invalid");
    }
  }
  return isCamposValidos;
}

function getProdutoEventCodBarra(URI) {
  $("#codigoBarra").on("focusout", function (event) {
    let codBarra = $("#codigoBarra").val();
    if (!isEmpaty(codBarra)) {
      $.get(URI.concat(`entradas/buscar/produtoPorCodBarra/${codBarra}`), function (data) {
        if (data === undefined || data === null && codBarra !== "") {
          clearForm();
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
          $("#numeroNota").focus();
        }
      });
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
        $("#produto").focus();
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

function clearForm() {
  $("form").trigger("reset");
  $("#produto").val(null).trigger("change");
  $("#situacaoCompra").val(null).trigger("change");
  $("#formaPagamento").val(null).trigger("change");
  $("#fornecedor").val(null).trigger("change");
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
  
  $("#formaPagamento").on("select2:select", function (e) {
    if(EnumEntradaProduto.CARTAO_CREDITO === e.params.data.id) {
      $("#qtdParcelas").prop("readonly",false);
      $("#qtdParcelas").focus();
    } else {
      $("#qtdParcelas").val("");
      $("#qtdParcelas").prop("readonly",true);
    }
  }); 
}