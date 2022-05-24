/* global CONTEXT, formatter */

let usuarios = [];
let somaValorTotal = 0;
let somaValorTotalSaida = 0;
let isCaixaFechado = false;

$(document).ready(function () {

  parametrosConfigDataTable();
  
  if($("#usuarios").length) {
    $.get(`${CONTEXT}movimentacao-caixa/usuarios`,function (response) {
      $("#usuarios").select2({
        theme: "bootstrap-5",
        allowClear: true,
        language: "pt-BR",
        multiple: false,
        closeOnSelect: true,
        data: response,
        templateResult: templateResultProduto,
        templateSelection: function (response) {
          if (response && response.text !== "Usuários") {
            return $("<span class='badge bg-light text-dark fw-bold' style='font-size:12px;'>" + response.text + "</span>");
          }
          return $("<span class=''>" + response.text + "</span>");
        }
      });
    });
  }
  
  
  $("#isCaixaFechado").change(function () {
    isCaixaFechado = this.checked;
    $("#spanTipoCaixa").text("");
    if (this.checked) {
      $("#spanTipoCaixa").removeClass("bg-danger");
      $("#spanTipoCaixa").addClass("bg-primary");
      $("#spanTipoCaixa").text("SIM");
    } else {
      $("#spanTipoCaixa").removeClass("bg-primary");
      $("#spanTipoCaixa").addClass("bg-danger");
      $("#spanTipoCaixa").text("NÃO");
    }
  });
  
  var movimentacoesCaixaFilters = {
    usuarioSelect2: {
      usuarioId:1,
      nome:"Nome"
    },
    somenteCaixaAberto: false
  };
  
  let tbMovimentacao = $("#tbMovimentacao").DataTable({
    ajax: {
      url: `${CONTEXT}movimentacao-caixa/movimentacoes`,
      data: {
        isCaixaFechado: function () {
          return isCaixaFechado;
        },
        movimentacoesCaixaFilters: JSON.stringify(movimentacoesCaixaFilters)
      }
    },
    drawCallback: function (settings) {
      var response = settings.json;
      //console.log(response);
    }
  });
  
  $("#btnPesquisar").click(function () {
    tbMovimentacao.ajax.load();
  });
  
  $("#btnLimpar").click(function () {
    $("form").trigger("reset");
    $("#usuarios").val("").trigger("change");
  });
  
  $(document).on("click", "#formaPagamento", function () {
    let modalDetalhes = $("#detalhesFormaPagamento");
    let tBadyFormasPagamento = modalDetalhes.find("tbody");
    tBadyFormasPagamento.empty();
    $.get(`${CONTEXT}movimentacao-caixa/formas-pagamento/${$(this).data("value")}`, function (response) {
      if(response.length <= 2) {
        tBadyFormasPagamento.prop("style","height:80px;");
      }
      $.each(response, function (i,item) {
        tBadyFormasPagamento.append(`<tr><td class="">${item.tipoPagamento}</td> <td class="text-success">${formatter.format(item.valor)}</td></tr>`);
      });
    });
    modalDetalhes.modal('show');
  });
  
});


function parametrosConfigDataTable() {
  var parametros = {
    "columns": [
      {
        "data": "valorRecebido", render: function (data, type, row, meta) {
          return `<span class='text-primary'>${formatter.format(data)}</span>`;
        }
      },

      {
        "defaultContent": "", render: function (data, type, row, meta) {
          if (row.recolhimento) {
            return "-";
          }
          return `<a id="formaPagamento" data-value=${row.movimentacaoId} style="text-decoration: none;" href="javascript:void(0)">Ver</a>`;
        }
      },

      {
        "data": "valorTroco", render: function (data, type, row, meta) {
          return `<span class='text-danger'>-${formatter.format(data)}</span>`;
        }
      },

      {
        "defaultContent": "", render: function (data, type, row, meta) {
          if (row.recolhimento) {
            return "-";
          }
          return `<span class='text-success'>${formatter.format(row.valorRecebido - row.valorTroco)}</span>`;
        }
      },

      {
        "defaultContent": "", render: function (data, type, row, meta) {
          somaValorTotalSaida = somaValorTotalSaida + (row.valorTroco);
          return `<span class='text-danger'>${formatter.format(somaValorTotalSaida)}</span>`;
        }
      },

      {
        "defaultContent": "", render: function (data, type, row, meta) {
          somaValorTotal = somaValorTotal + (row.valorRecebido - row.valorTroco);
          return `<span class='text-success'>${formatter.format(somaValorTotal)}</span>`;
        }
      },

      {"data": "dataMovimentacao", render: function (data, type, row, meta) {
          return formatDataHora(data);
        }
      },

      {"data": "recolhimento", render: function (data, type, row, meta) {
          if (data) {
            return `<span class="badge bg-warning text-black">Recolhimento</span>`;
          }
          return `<span class="badge bg-primary">Normal</span>`;
        }
      }
    ],

    columnDefs: [
      {
        targets: 1,
        className: ""
      }
    ],

    btnActions: false
  };
  setDefaultsDataTableUsingChange(parametros);
}

function templateResultProduto(usuario) {
  if (usuario.loading) {
    return $(`<span class='badge bg-light text-dark fw-bold' style='font-size:12px;'>${usuario.text}</span>`);
  }
  return $("<span class='badge bg-light text-dark fw-bold' style='font-size:12px;'>" + usuario.text + "</span>");
}
