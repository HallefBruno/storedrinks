/* global StoreDrink, formatter */
let mensagem = new StoreDrink.Mensagem();
let listProdutos = [];
let produto = {};
let valorTotalVenda = 0;
let quantidadeEstoqueAtual = null;
const CLASS_BTN_DIMINUIR = "btnDiminuirQuantidade";
const CLASS_BTN_AUMENTAR = "btnAumentarQuantidade";
const CLASS_BTN_REMOVER = "btnRemoverItem";
const ENDPOINT = "vendas";
const CONTEXT = $("#context").val();

$(function () {
  initDataTable();
  popularSelectProdutos();
  eventKeyEnter();
  eventButtonAddVenda();
  eventClickRowDataTable();
  finalizarVenda();
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
      url: `${CONTEXT}vendor/internationalisation/pt_br.json`
    },
    columnDefs: [
      {
        targets: [2,4],
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
      url: `${CONTEXT}${ENDPOINT}/produtos`,
      dataType: "json",
      delay: 1000,
      data: function (params) {
        return {
          q: params.term,
          page: params.page
        };
      },
      processResults: function (data, params) {
        if(data.items === null) {
          data.items = new Array({id:'',text:''});
        }
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
    templateSelection: function (produto) {
      if (produto && produto.text !== "Procure aqui seu produto") {
        return $("<span class='badge bg-secondary fw-normal' style='font-size:14px;'>" + produto.text + "</span>");
      }
      return $("<span class=''>" + produto.text + "</span>");
    }
  });
}

function templateResultProduto(produto) {
  if (produto.loading) {
    return $(`<span class='badge bg-secondary fw-normal' style='font-size:14px;'>${produto.text}</span>`);
  }
  return $("<span class='badge bg-secondary fw-normal' style='font-size:14px;'>" + produto.text + "</span>");
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
  let quantidade = Number($("#quantidade").val());
  if (quantidade > Number(quantidadeEstoqueAtual)) {
    mensagemToast("Você não possui essa quantidade em estoque!","#000000","#ffffff");
    $("#quantidade").val("");
    return false;
  }
  return true;
}

function addProduto() {
  let quantidade = Number($("#quantidade").val());
  if (quantidade > 0) {
    let valorVenda = Number(produto.valorVenda);
    let prod = {
      descricaoProduto: $("#descricaoProduto").val(),
      codigoBarra: $("#codigoBarra").val(),
      codProduto: $("#codProduto").val(),
      valorVenda: formatter.format(valorVenda),
      quantidade: quantidade,
      quantidadeEstoqueAtual: quantidadeEstoqueAtual,
      valorTotal: formatter.format((quantidade * valorVenda))
    };
    let qtdProdutosPorItem = funcQtdProdutosPorItem(prod);
    
    if (qtdProdutosPorItem <= quantidadeEstoqueAtual && (qtdProdutosPorItem + quantidade) <= quantidadeEstoqueAtual) {
      listProdutos.push(prod);
      quantidadeEstoqueAtual = null;
      popularTable(listProdutos);
      somaValorTotalVenda(listProdutos);
      clearFormFocusSelect();
    } else {
      mensagemToast("Quantidade em estoque excedida!", "#000000", "#ffffff");
    }
    
  } else if ($("#descricaoProduto").val().length === 0) {
    clearFormFocusSelect();
    mensagemToast("Por favor, selecione um produto!", "#000000", "#ffffff");
  } else {
    mensagemToast("A quantidade de produto precisa ser maior que zero!", "#000000", "#ffffff");
  }
}

function eventButtonAddVenda() {
  $("#btnItemVenda").click(function () {
    addProduto();
  });
}

function popularTable(listProdutos) {
  $("#itensVenda").DataTable().clear().draw();
  $.each(listProdutos, function (index, produto) {
    var htmlbtnGroup = `
      <div class='btn-group btn-group-sm' role='group' aria-label='grupo vendas'> 
        <button data-key='${produto.codigoBarra}' data-index='${index}' type='button' data-bs-toggle='tooltip' data-bs-placement='top' title='Diminuir quantidade' class='${CLASS_BTN_DIMINUIR} btn btn-outline-secondary'><i class="fa fa-minus-circle" aria-hidden="true"></i></button>
        <button data-key='${produto.codigoBarra}' data-index='${index}' type='button' data-bs-toggle='tooltip' data-bs-placement='top' title='Aumentar quantidade' class='${CLASS_BTN_AUMENTAR} btn btn-outline-secondary'><i class="fa fa-plus-circle" aria-hidden="true"></i></button>
        <button data-key='${produto.codigoBarra}' data-index='${index}' type='button' data-bs-toggle='tooltip' data-bs-placement='top' title='Remover item' class='${CLASS_BTN_REMOVER} btn btn-outline-secondary'><i class="fa fa-trash" aria-hidden="true"></i></button>
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

function eventClickRowDataTable() {
  $("#itensVenda tbody").on("click", "button", function () {
    const btnInvocado = $(this).attr("class").split(" ")[0];
    const codBarra = $(this).data("key");
    const index = $(this).data("index");
    if (btnInvocado.includes(CLASS_BTN_DIMINUIR)) {
      if (listProdutos[index].quantidade === 1) {
        listProdutos.splice(index, 1);
        popularTable(listProdutos);
        somaValorTotalVenda(listProdutos);
      } else {
        listProdutos[index].quantidade -= 1;
        listProdutos[index].valorTotal = formatter.format(calcularValorTotalNaTabela(listProdutos[index]));
        popularTable(listProdutos);
        somaValorTotalVenda(listProdutos);
      }
    } else if (btnInvocado.includes(CLASS_BTN_AUMENTAR)) {
      let qtdProdutosPorItem = funcQtdProdutosPorItem(listProdutos[index]);
      if (listProdutos[index].quantidade < listProdutos[index].quantidadeEstoqueAtual && qtdProdutosPorItem < listProdutos[index].quantidadeEstoqueAtual) {
        listProdutos[index].quantidade += 1;
        listProdutos[index].valorTotal = formatter.format(calcularValorTotalNaTabela(listProdutos[index]));
        popularTable(listProdutos);
        somaValorTotalVenda(listProdutos);
        return;
      }
      mensagemToast("Quantidade em estoque excedida!", "#000000", "#ffffff");
    } else {
      listProdutos.splice(index, 1);
      popularTable(listProdutos);
      somaValorTotalVenda(listProdutos);
    }
  });
}

function somaValorTotalVenda(listProdutos) {
  if(listProdutos.length > 0) {
    let valorUnitario = "";
    let total = 0;
    $.each(listProdutos, function(index, produto) {
      valorUnitario = removeMaskMonetariaCalcularValorTotalNaTabela(produto);
      total = total + Number(valorUnitario*produto.quantidade);
      $(".valorTotal").text(formatter.format(total));
    });
    valorTotalVenda = total;
    return;
  }
  $(".valorTotal").text("0,00");
  valorTotalVenda = 0.00;
}

function calcularValorTotalNaTabela(produto) {
  let valorUnitario = "";
  let total = 0;
  valorUnitario = removeMaskMonetariaCalcularValorTotalNaTabela(produto);
  total = total + Number(valorUnitario*produto.quantidade);
  return total;
}

function funcQtdProdutosPorItem(prod) {
  let listaMesmoProdutos = listProdutos.filter(function (produto) {
    return produto.codigoBarra === prod.codigoBarra;
  });
  let qtdProdutosPorItem = 0;
  $.each(listaMesmoProdutos, function (i, produto) {
    qtdProdutosPorItem += produto.quantidade;
  });
  return qtdProdutosPorItem;
}

function finalizarVenda() {
  $("#finalizarVenda").click(function () {
    if(listProdutos.length <= 0) {
      mensagemToast("Selecione os itens para venda!", "#000000", "#ffffff");
      $("#codigoBarra").focus();
    } else {
      modalFormaPagamento();
    }
  });
  $(document).keydown(function (event) {
    if(event.ctrlKey && event.altKey && event.keyCode === 70 && listProdutos.length > 0) {
      modalFormaPagamento();
    }
  });
}

function modalFormaPagamento() {
  let modalFormaPagamento = $("#modalFormaPagamento");
  modalFormaPagamento.modal("show");

  modalFormaPagamento.one("shown.bs.modal", function (e) {
    clearInputModal(modalFormaPagamento);
  });

  let inputDinheiro = modalFormaPagamento.find("#dinheiro");
  let inputDebito = modalFormaPagamento.find("#debito");
  let inputCredito = modalFormaPagamento.find("#credito");
  let inputPix = modalFormaPagamento.find("#pix");
  let spanTroco = modalFormaPagamento.find("#spanTroco");

  let getValueInputDinehro = 0;
  let getValueInputDebito = 0;
  let getValueInputCredito = 0;
  let getValueInputPix = 0;
  let soma = 0;
  
  $(document).on("keyup", `#${inputDinheiro.prop("id")}`, function (event) {
    if (inputDinheiro.val()) {
      getValueInputDinehro = removeMaskMonetaria(inputDinheiro.val());
      if (getValueInputDinehro >= 0) {
        somaValoresFormaPagamento(soma, getValueInputDinehro, getValueInputDebito, getValueInputCredito, getValueInputPix, spanTroco);
      }
    }
  });

  $(document).on("keyup", `#${inputDebito.prop("id")}`, function (event) {
    if (inputDebito.val()) {
      getValueInputDebito = removeMaskMonetaria(inputDebito.val());
      if (getValueInputDebito >= 0) {
        somaValoresFormaPagamento(soma, getValueInputDinehro, getValueInputDebito, getValueInputCredito, getValueInputPix, spanTroco);
      }
    }
  });

  $(document).on("keyup", `#${inputCredito.prop("id")}`, function (event) {
    if (inputCredito.val()) {
      getValueInputCredito = removeMaskMonetaria(inputCredito.val());
      if (getValueInputCredito >= 0) {
        somaValoresFormaPagamento(soma, getValueInputDinehro, getValueInputDebito, getValueInputCredito, getValueInputPix, spanTroco);
      }
    }
  });

  $(document).on("keyup", `#${inputPix.prop("id")}`, function (event) {
    if (inputPix.val()) {
      getValueInputPix = removeMaskMonetaria(inputPix.val());
      if (getValueInputPix >= 0) {
        somaValoresFormaPagamento(soma, getValueInputDinehro, getValueInputDebito, getValueInputCredito, getValueInputPix, spanTroco);
      }
    }
  });
}

function somaValoresFormaPagamento(soma,getValueInputDinehro, getValueInputDebito, getValueInputCredito, getValueInputPix, spanTroco) {
  if (getValueInputDinehro) {
    soma = soma + getValueInputDinehro;
  }
  if (getValueInputDebito) {
    soma = soma + getValueInputDebito;
  }
  if (getValueInputCredito) {
    soma = soma + getValueInputCredito;
  }
  if (getValueInputPix) {
    soma = soma + getValueInputPix;
  }
  
  if (Number(soma - valorTotalVenda) > 0) {
    $(spanTroco).text(formatter.format(soma - valorTotalVenda));
  } else {
    $(spanTroco).text("0,00");
  }
  
  return soma;
}

function clearInputModal(modalFormaPagamento) {
  modalFormaPagamento.find("#dinheiro").val("");
  modalFormaPagamento.find("#debito").val("");
  modalFormaPagamento.find("#credito").val("");
  modalFormaPagamento.find("#pix").val("");
  modalFormaPagamento.find("#dinheiro").focus();
  modalFormaPagamento.find("#spanTroco").text("0,00");
}

function removeMaskMonetaria(value) {
  let valor = value.toString().replace("R$ ","");
  valor = valor.replace(".","");
  valor = valor.replace(",",".");
  return Number(valor);
}

function removeMaskMonetariaCalcularValorTotalNaTabela(produto) {
  let valorUnitario = produto.valorVenda.replace("R$ ","");
  valorUnitario = valorUnitario.replace(".","");
  valorUnitario = valorUnitario.replace(",",".");
  return valorUnitario;
}

function mensagemToast(text, bgColor, textColor) {
  $.toast({
    heading: `<p class='mb-1'>Atenção!<p><hr/>`,
    text: `${text}`,
    bgColor: `${bgColor}`,
    textColor: `${textColor}`,
    position: 'top-right',
    hideAfter: 5000,
    loader: false
  });
}