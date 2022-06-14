/* globals Chart:false, feather:false */
/* global Utils, CONTEXT, StoreDrink, Morris */


$(function () {
  
  let toast = new StoreDrink.Toast();
  $("#myfirstchart").empty();
  
  let filters = {
    dataInicial: $("#dataInicial").val(),
    dataFinal: $("#dataFinal").val()
  };
  
  montarGraficoProdutosMaisVendidos(filters);
  
  $("#btnPesquisar").click(function () {
    
    filters = {
      dataInicial: $("#dataInicial").val(),
      dataFinal: $("#dataFinal").val()
    };
    
    if(filters.dataInicial && !filters.dataFinal) {
      toast.show("warning","Atenção","A data final é obrigatória!","top-right");
      return;
    }
    montarGraficoProdutosMaisVendidos(filters);
  });

});

function montarGraficoProdutosMaisVendidos(filters) {
  
//  let ctx = $("#myChart").get(0).getContext('2d');
//  new Chart(ctx).destroy();
  
  $.ajax({
    url: `${CONTEXT}dashBoard/pesquisar`,
    type: "GET",
    dataType: "json",
    contentType: "application/json",
    data: {
      "filters":JSON.stringify(filters)
    },
    success: function (response, textStatus, jqXHR) {
      console.log(response);
      $("#myfirstchart").empty();
      if(response.length > 0) {
        new Morris.Bar({
          element: 'myfirstchart',
          data: response,
          xkey: 'descricaoProduto',
          ykeys: ['quantidade'],
          labels: ['Quantidade']
        });
      }
      
      let tBadyProdutosMaisVendidos = $("#tbProdutosMaisVendidos").find("tbody");
      tBadyProdutosMaisVendidos.empty();
      
      for(let i = 0; i < response.length; i++) {
        tBadyProdutosMaisVendidos.append(`<tr><td class="">${response[i].quantidade}</td> <td class="text-success">${response[i].descricaoProduto}</td></tr>`);
      }
      
      if(response.length === 0) {
        tBadyProdutosMaisVendidos.append(`<tr><td colspan="2" class="text-center">Nenhum produto encontrado</td></tr>`);
      }
    },
    error: function (jqXHR, textStatus, errorThrown) {
      alert("error");
    }
  });
}
