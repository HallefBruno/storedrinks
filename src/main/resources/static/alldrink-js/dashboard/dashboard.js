/* globals Chart:false, feather:false */
/* global Utils, CONTEXT, StoreDrink, Morris */

let usuarioSelect2 = {};
$(function () {
  
  let toast = new StoreDrink.Toast();
  $("#donutChart").empty();
  
  let filters = {
    dataInicial: $("#dataInicial").val(),
    dataFinal: $("#dataFinal").val()
  };
  
  responseData(filters);
  
  $("#btnPesquisar").click(function () {
    
    console.log(usuarioSelect2);
    
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
  
  $("#usuarios").on("select2:clear", function () {
    usuarioSelect2 = {};
    $("#usuarios").val("").trigger("change");
  });
  
  $("#usuarios").on("select2:select", function (e) {
    usuarioSelect2 = {
      usuarioId: e.params.data.id,
      nome: e.params.data.text
    };
  });
  
});

function responseData(filters) {

  $.ajax({
    url: `${CONTEXT}dashBoard/pesquisar`,
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
        $("#chartProdutosMaisVendidos").addClass("text-center").append("<span class='fs-6 badge bg-primary'>Nenhum gráfico</span>");
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

  new Morris.Donut(
    {
      element: 'donutChart',
      data: response,
      resize: true,
      redraw: true,
      formatter: function (y) { return " " + y; }
    }
  );
}






//$(function () {
//  
//  let toast = new StoreDrink.Toast();
//  $("#chartProdutosMaisVendidos").empty();
//  
//  let filters = {
//    dataInicial: $("#dataInicial").val(),
//    dataFinal: $("#dataFinal").val()
//  };
//  
//  barChart(filters);
//  
//  $("#btnPesquisar").click(function () {
//    
//    filters = {
//      dataInicial: $("#dataInicial").val(),
//      dataFinal: $("#dataFinal").val()
//    };
//    
//    if(filters.dataInicial && !filters.dataFinal) {
//      toast.show("warning","Atenção","A data final é obrigatória!","top-right");
//      return;
//    }
//    
//    barChart(filters);
//    
//  });
//
//});
//
//function barChart(filters) {
//
//  $.ajax({
//    url: `${CONTEXT}dashBoard/pesquisar`,
//    type: "GET",
//    dataType: "json",
//    contentType: "application/json",
//    data: {
//      "filters":JSON.stringify(filters)
//    },
//    success: function (response, textStatus, jqXHR) {
//      $("#chartProdutosMaisVendidos").empty();
//      if(response.length > 0) {
//        donutChart(response);
//        new Morris.Bar({
//          element: 'chartProdutosMaisVendidos',
//          data: response,
//          xkey: 'label',
//          ykeys: ['value'],
//          labels: ['Quantidade'],
//          //barRatio: 0.5,
//          //xLabelAngle: 5,
//          hideHover: 'auto',
//          grid: true,
//          resize: true
//        });
//      }
//      
//      let tBadyProdutosMaisVendidos = $("#tbProdutosMaisVendidos").find("tbody");
//      tBadyProdutosMaisVendidos.empty();
//      
//      for(let i = 0; i < response.length; i++) {
//        tBadyProdutosMaisVendidos.append(`<tr><td class="">${response[i].value}</td> <td>${response[i].label}</td></tr>`);
//      }
//      
//      if(response.length === 0) {
//        tBadyProdutosMaisVendidos.append(`<tr><td colspan="2" class="text-center"><span>Nenhum produto encontrado</span></td></tr>`);
//        $("#chartProdutosMaisVendidos").addClass("text-center").append("<span class='fs-6 badge bg-primary'>Nenhum gráfico</span>");
//      }
//    },
//    error: function (jqXHR, textStatus, errorThrown) {
//      alert("error");
//    }
//  });
//}
//
//
//function donutChart(response) {
//  
//  for (let i = 0; i < response.length; i++) {
//    response[i].label = response[i].descricaoProduto;
//    response[i].value = response[i].quantidade;
//    delete response[i].descricaoProduto;
//    delete response[i].quantidade;
//  }
//  
//  console.log(response);
//  
//  new Morris.Donut(
//    {
//      element: 'donut-chart',
//      data: response,
//      resize: true,
//      redraw: true,
//      formatter: function (y) { return " " + y; }
//    }
//  );
//}
//
