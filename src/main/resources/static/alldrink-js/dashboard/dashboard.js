/* globals Chart:false, feather:false */
/* global Utils, CONTEXT, StoreDrink, Morris */

let usuarioSelect2 = {};
let dataTableAcompanharVendas;
let nIntervId;
let sessentaSegundos = 60;
let tempoSelecionado = null;
let arrayTempo = [];
let chart = null;
let intervalRelogio = null;

$(function () {
  
  let toast = new StoreDrink.Toast();
  $("#donutChart").empty();
  
  let arrayStorageSelecionado = addStorageArray();
  
  if(arrayStorageSelecionado.length > 0) {
    $("#ulTempos li > a").removeClass("disabled");
    tempoSelecionado = arrayStorageSelecionado[0].value;
    $(`#${arrayStorageSelecionado[0].id}`).addClass("disabled");
  } else {
    tempoSelecionado = sessentaSegundos;
  }
  
  removerStorage();
  
  let filters = {
    dataInicial: $("#dataInicial").val(),
    dataFinal: $("#dataFinal").val()
  };
  
  $("#nav-produtos-mais-vendidos-tab").click(function () {
    $("#donutChart").empty();
    responseData(filters);
  });
  
  popularTable();
  nIntervId = setInterval(popularTable, 1000 * tempoSelecionado);

  $("#btnPesquisar").click(function () {
    filters = {
      dataInicial: $("#dataInicial").val(),
      dataFinal: $("#dataFinal").val(),
      usuarioId: usuarioSelect2.usuarioId
    };
    
    if(filters.dataInicial && !filters.dataFinal) {
      toast.show("warning","Atenção","A data final é obrigatória!","top-right");
      return;
    } else if (moment(filters.dataFinal) < moment(filters.dataInicial)) {
      toast.show("warning","Atenção","A data final não pode ser mesno que a data inicial!","top-right");
      return;
    }
    responseData(filters);
    
  });
  
  if ($("#usuarios").length) {
    $.get(`${CONTEXT}movimentacao-caixa/usuarios`, function (response) {
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
  
  usuarioSelecionado();
  
  limparSelectUsuarioSelecionado();
  
  eventoTempoSelecionado();
  
});

function responseData(filters) {
  $.ajax({
    url: `${CONTEXT}dashboard/pesquisar`,
    type: "GET",
    dataType: "json",
    contentType: "application/json",
    data: {
      "filters":JSON.stringify(filters)
    },
    success: function (response, textStatus, jqXHR) {
      $("#donutChart").empty();
      if(response.length > 0) {
        donutChart(response);
      }
      let tBadyProdutosMaisVendidos = $("#tbProdutosMaisVendidos").find("tbody");
      tBadyProdutosMaisVendidos.empty();
      for(let i = 0; i < response.length; i++) {
        tBadyProdutosMaisVendidos.append(`<tr><td class="">${response[i].value}</td> <td>${response[i].label}</td></tr>`);
      }
      if(response.length === 0) {
        tBadyProdutosMaisVendidos.append(`<tr><td colspan="2" class="text-center"><span>Nenhum produto encontrado</span></td></tr>`);
      }
    },
    error: function (jqXHR, textStatus, errorThrown) {
      alert("error");
    }
  });
}

function templateResultProduto(usuario) {
  if (usuario.loading) {
    return $(`<span class='badge bg-light text-dark fw-bold' style='font-size:12px;'>${usuario.text}</span>`);
  }
  return $("<span class='badge bg-light text-dark fw-bold' style='font-size:12px;'>" + usuario.text + "</span>");
}

function donutChart(response) {
  for (let i = 0; i < response.length; i++) {
    response[i].label = response[i].descricaoProduto;
    response[i].value = response[i].quantidade;
    delete response[i].descricaoProduto;
    delete response[i].quantidade;
  }
  if (chart === null) {
    new Morris.Donut(
      {
        element: 'donutChart',
        data: response,
        resize: true,
        redraw: true
        //formatter: function (y) { return " " + y; }
      }
    );
  } else {
    chart.setData(response);
  }
}

function popularTable() {
  $("#tbAcompanharVendas").DataTable({
    ajax: {
      url: `${CONTEXT}dashboard/vendas-tempo-real?${usuarioSelect2.usuarioId}`
    },
    serverSide: true,
    destroy: true,
    info: true,
    responsive: true,
    pageLength: 10,
    lengthChange: false,
    searching: false,
    language: {
      url: `${CONTEXT}vendor/internationalisation/pt_br.json`
    },
    columns: [
      {
        "data": "nomeProduto", render: function (data, type, row, meta) {
          return `<span class='text-primary'>${data}</span>`;
        }
      },
      {
        "data": "quantidade", render: function (data, type, row, meta) {
          return `<span class='text-muted'>${data}</span>`;
        }
      },
      {
        "data": "valorTotal", render: function (data, type, row, meta) {
          return `<span class='text-muted'>${formatter.format(data)}</span>`;
        }
      },
      {
        "data": "nomeVendedor", render: function (data, type, row, meta) {
          return `<span class='text-muted'>${data}</span>`;
        }
      },
      {
        "data": "dataHoraVenda", render: function (data, type, row, meta) {
          return `<span class='text-muted'>${formatDataHora(data)}</span>`;
        }
      }
    ],
    ordering: false
  });
  
  clearInterval(intervalRelogio);
  intervalRelogio = null;
  let time = (1000 * tempoSelecionado) / 1000;
  intervalRelogio = setInterval(() => {
    $("#spanTimer").html("");
    $("#spanTimer").html(--time);
  }, 1000);
  
}

function stop() {
  clearInterval(nIntervId);
  nIntervId = null;
}

function removerStorage() {
  localStorage.removeItem("sessentaSegundos");
  localStorage.removeItem("cincoMinutos");
  localStorage.removeItem("dezMinutos");
  localStorage.removeItem("trintaMinutos");
}

function addStorageArray() {
  for (var i = 0; i < localStorage.length; i++) {
    const key = localStorage.key(i);
    const value = localStorage.getItem(key);
    if (key && key.includes("sessentaSegundos")) {
      arrayTempo.push({"id":"linkUmMinuto", "value":Number(value)});
    } else if (key && key.includes("cincoMinutos")) {
      arrayTempo.push({"id":"linkCincoMinutos", "value":Number(value)});
    } else if (key && key.includes("dezMinutos")) {
      arrayTempo.push({"id":"linkDezMinutos", "value":Number(value)});
    } else if (key && key.includes("trintaMinutos")) {
      arrayTempo.push({"id":"linkTrintaMinutos", "value":Number(value)});
    }
  }
  return arrayTempo;
}

function eventoTempoSelecionado() {
  $("#linkUmMinuto").click(function () {
    localStorage.setItem("sessentaSegundos", 60);
    window.location.reload();
  });
  
  $("#linkCincoMinutos").click(function () {
    localStorage.setItem("cincoMinutos", 300);
    window.location.reload();
  });
  
  $("#linkDezMinutos").click(function () {
    localStorage.setItem("dezMinutos", 600);
    window.location.reload();
  });
  
  $("#linkTrintaMinutos").click(function () {
    localStorage.setItem("trintaMinutos", 1800);
    window.location.reload();
  });
}

function usuarioSelecionado() {
  $("#usuarios").on("select2:select", function (e) {
    usuarioSelect2 = {
      usuarioId: e.params.data.id,
      nome: e.params.data.text
    };
  });
}

function limparSelectUsuarioSelecionado() {
  $("#usuarios").on("select2:clear", function () {
    usuarioSelect2 = {};
    $("#usuarios").val("").trigger("change");
  });
}