/* global CONTEXT, formatter */

let somaValorTotal = 0;
let somaValorTotalSaida = 0;
let usuarioSelect2 = {};
let isCaixaFechado = false;
let movimentacoesCaixaFilters = {};
let dataAbertura;
let dataFechamento;

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
  
  const filtros = filtroUrl();
  let recursiveEncoded = $.param(filtros);
  
  $("#tbMovimentacao").DataTable({
    ajax: `${CONTEXT}movimentacao-caixa/movimentacoes?${recursiveEncoded}`
  });
  
  $("#btnPesquisar").click(function () {
    let filtros = filtroUrl();
    let recursiveEncoded = $.param(filtros);
    $("#tbMovimentacao").DataTable().ajax.url(`${CONTEXT}movimentacao-caixa/movimentacoes?${recursiveEncoded}`).load();
  });
  
  $("#btnLimpar").click(function () {
    $("form").trigger("reset");
    $("#usuarios").val("").trigger("change");
    $("#spanTipoCaixa").removeClass("bg-primary");
    $("#spanTipoCaixa").addClass("bg-danger");
    $("#spanTipoCaixa").text("NÃO");
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

function filtroUrl() {
  isCaixaFechado = changeFiltroSituacaoCaixa();
  usuarioSelect2 = filtroUsuario();
  movimentacoesCaixaFilters = {
    "somenteCaixaAberto": isCaixaFechado,
    "usuarioSelect2": usuarioSelect2
  };
  let montagemFiltro = {
    "movimentacoesCaixaFilters": JSON.stringify(movimentacoesCaixaFilters)
  };
  return montagemFiltro;
}

function changeFiltroSituacaoCaixa() {
  $("#isCaixaFechado").change(function () {
    isCaixaFechado = this.checked;
    if (isCaixaFechado) {
      $("#spanTipoCaixa").removeClass("bg-danger");
      $("#spanTipoCaixa").addClass("bg-primary");
      $("#spanTipoCaixa").text("SIM");
    } else {
      $("#spanTipoCaixa").removeClass("bg-primary");
      $("#spanTipoCaixa").addClass("bg-danger");
      $("#spanTipoCaixa").text("NÃO");
    }
  });
  if(isCaixaFechado === undefined) {
    return false;
  }
  return isCaixaFechado;
}

function filtroUsuario() {
  $("#usuarios").on("select2:select", function (e) {
    usuarioSelect2 = {
      usuarioId: e.params.data.id,
      nome: e.params.data.text
    };
  });
  return usuarioSelect2;
}

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

      {
        "data": "dataMovimentacao", render: function (data, type, row, meta) {
          return formatDataHora(data);
        }
      },

      {
        "data": "recolhimento", render: function (data, type, row, meta) {
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