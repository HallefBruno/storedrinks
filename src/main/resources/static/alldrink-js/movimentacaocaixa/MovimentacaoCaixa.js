/* global CONTEXT */

let usuarios = [];

$(document).ready(function () {
  
  let isCaixaFechado = false;
  
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
            return $("<span class='badge bg-light text-dark fw-normal' style='font-size:12px;'>" + response.text + "</span>");
          }
          return $("<span class=''>" + response.text + "</span>");
        }
      });
    });
  }
  
  parametrosConfigDataTable();
  
  
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
        movimentacoesCaixaFilters:JSON.stringify(movimentacoesCaixaFilters)
      }
    }
  });
  
  $("#btnPesquisar").click(function () {
    tbMovimentacao.ajax.load();
  });
  
  $("#btnLimpar").click(function () {
    $("form").trigger("reset");
    $("#usuarios").val("").trigger("change");
  });
  
});


function parametrosConfigDataTable() {
  var parametros = {
    btnActions: false
  };
  setDefaultsDataTableUsingChange(parametros);
}

function templateResultProduto(usuario) {
  if (usuario.loading) {
    return $(`<span class='badge bg-light text-dark fw-normal' style='font-size:12px;'>${usuario.text}</span>`);
  }
  return $("<span class='badge bg-light text-dark fw-normal' style='font-size:12px;'>" + usuario.text + "</span>");
}
