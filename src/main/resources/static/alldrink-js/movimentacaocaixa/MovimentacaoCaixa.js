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
    },
    drawCallback: function (settings) {
        // Here the response
        var response = settings.json;
        console.log(response);
    },
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
   "columns": [
	  { "data": "valorRecebido", 
	  	render: function(data,type,row,meta) {
		  return `<span class='text-success'>${formatter.format(data)}</span>`;
		}
	  },
	  
	  { "data": "valorTroco",
	  	render: function(data,type,row,meta) {
		  return `<span class='text-danger'>-${formatter.format(data)}</span>`;
		}
	  },
	  
	  { "data": "dataMovimentacao",
	   	render: function (data,type,row,meta) {
          return formatDataHora(data);
        }
      },
      
	  { "data": "recolhimento",
	  	render: function (data,type,row,meta) {
		  if(data) {
			return `<span class="badge bg-warning text-black">Recolhimento</span>`;
		  }
          return `<span class="badge bg-primary">Normal</span>`;
        }
	  }
    ],
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
