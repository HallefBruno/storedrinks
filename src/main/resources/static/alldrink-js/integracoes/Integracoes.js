/* global Swal */
$(function () {
  $("#btnMercadoLivre").click(function () {
    $.get(`${CONTEXT}mercado-livre/code`, function (response) {
      $(location).attr("href", response);
    });
  });
  
//  $.get(`${CONTEXT}mercado-livre/refresh-token`, function (response) {
//    console.log(response);
//  });
  
});