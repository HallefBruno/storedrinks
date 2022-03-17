/* global Swal */

var listProdutos = [];
var quantidadeEstoqueAtual = 0;

$(function () {
  $("#valorCusto").mask("#,##0.00", {reverse: true});

  $("form").submit(function (e) {
    return false;
  });

  popularSelectProdutos();

  $("#codigoBarra").focus();

  $("#produtos").on("select2:open", function (e) {
    $(".select2-search__field")[0].focus();
  });

  $("#produtos").on("select2:select", function (e) {
    const produto = e.params.data;
    quantidadeEstoqueAtual = Number(produto.quantidade);
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
  var intervaloDigitacao = 1500;

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
          addItemVenda();
        }
      }
    }
  });

  $("#btnItemVenda").click(function () {
    popularProduto();
  });

  actionTableItens();
  acoes();

});
function qtdAtualMaiorQtdVenda() {
  const quantidade = Number($("#quantidade").val());
  if (quantidade > quantidadeEstoqueAtual) {
    mensagem("warning", "Quantidade em estoque inválida!");
    return false;
  }
  return true;
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
    templateResult: templateResultProduto,

    escapeMarkup: function (markup) {
      return markup;
    },
    templateSelection: function (produto) {
      if (produto && produto.text !== "Procure aqui seu produto") {
        return $("<span class='badge bg-primary' style='font-size:13px;'>" + produto.text + "</span>");
      }
      return $("<span class=''>" + produto.text + "</span>");
    }
  });
}

function templateResultProduto(produto) {
  if (produto && produto.text !== "Searching…") {
    return $("<span class='badge bg-primary' style='font-size:13px;'>" + produto.text + "</span>");
  }
  return $("<span>" + produto.text + "</span>");
}

function addItemVenda() {
  popularProduto();
  $("#btnItemVenda").click(function (event) {
    popularProduto();
  });
}

function popularProduto() {
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

    if (containsObject(produto, listProdutos)) {
      mensagem("warning", "Esse item já está na lista, é possível alterar a quantidade");
      return;
    }

    listProdutos.push(produto);
    popularTable(listProdutos);
    clearFormFocusSelect();

  } else if ($("#descricaoProduto").val().length === 0) {
    clearFormFocusSelect();
    mensagem("warning", "Por favor selecione um item!");
  }
}


function acoes() {

  $("#itensVenda").on("click", ".btnDiminuirQuantidade", function (e) {
    e.stopImmediatePropagation();
    var codBarra = $(this).data("key");
    var dataTableRow = $("#itensVenda").DataTable().row($(this).parents('tr')).data();
    index = listProdutos.findIndex((prod => prod.codigoBarra === codBarra.toString()));
    if (index >= 0) {
      if (listProdutos[index].quantidade > 1) {
        listProdutos[index].quantidade -= 1;
        var qtd = listProdutos[index].quantidade;
        var vv = removeMask(listProdutos[index].valorVenda);
        listProdutos[index].valorTotal = Number(qtd * vv).toLocaleString('pt-br', {style: 'currency', currency: 'BRL'});
        mensagem("success", "A quantidade foi diminuida!");
      } else {
        listProdutos.splice(index, 1);
        mensagem("success", "Item removido da lista!");
        clearFormFocusSelect();
      }
    }
    popularTable(listProdutos);
  });

  $("#itensVenda").on("click", ".btnAumentarQuantidade", function (e) {
    e.stopImmediatePropagation();
    var codBarra = $(this).data("key");
    var dataTableRow = $("#itensVenda").DataTable().row($(this).parents('tr')).data();
    index = listProdutos.findIndex((prod => prod.codigoBarra === codBarra.toString()));
    if (index >= 0) {
      if (listProdutos[index].quantidade < quantidadeEstoqueAtual) {
        listProdutos[index].quantidade++;
        var qtd = listProdutos[index].quantidade;
        var vv = removeMask(listProdutos[index].valorVenda);
        listProdutos[index].valorTotal = Number(qtd * vv).toLocaleString('pt-br', {style: 'currency', currency: 'BRL'});
        mensagem("success", "A quantidade foi aumentada!");
      } else {
        mensagem("warning", "Quantidade máxima em estoque atingida!");
      }
    }
    popularTable(listProdutos);
  });

  $("#itensVenda").on("click", ".btnRemoverItemVenda", function (e) {
    e.stopImmediatePropagation();
    var codBarra = $(this).data("key");
    var dataTableRow = $("#itensVenda").DataTable().row($(this).parents('tr')).data();
    index = listProdutos.findIndex((prod => prod.codigoBarra === codBarra.toString()));
    if (index >= 0) {
      listProdutos.splice(index, 1);
      mensagem("success", "Item removido da lista!");
    }
    popularTable(listProdutos);
  });

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

function actionTableItens() {

  $("#itensVenda").DataTable({
    paginate: false,
    lengthChange: false,
    info: false,
    autoWidth: false,
    filter: false,
    responsive: true,
    // language: {
    //  url: "https://cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Portuguese-Brasil.json"
    // },
    columnDefs: [
      {
        targets: [2, 4],
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

function mensagem(icon, mensagem) {
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
    icon: `${icon}`,
    text: `${mensagem}`
  });
}
function removeMask(value) {
  if (value.length >= 7) {
    value = value.replace(/[^0-9.-]+/g, "");
    if (!value.includes(".")) {
      var val = value.substring(0, value.length - 2) + "." + value.substring(value.length - 2, value.length);
      return Number(val);
    }
  }
}

//function removeMask(value) {
//    value = value.replace(/[^0-9.-]+/g,"");
//    var val = value.substring(0,value.length-2)+"."+value.substring(value.length-2,value.length);
//    return Number(val);
//}

//var table = $('#itensVenda').DataTable();
//var colIndex = table.cell(this).index().column;
//var rowIndex = table.cell(this).index().row;
//table.cell(rowIndex, colIndex).data("new");
//$.each(listProdutos, function (index, prod) {
//    if(prod.codigoBarra === codBarra.toString()) {
//        produto = prod;
//        return false;
//    }
//});