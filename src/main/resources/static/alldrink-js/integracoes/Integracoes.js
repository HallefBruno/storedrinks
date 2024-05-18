/* global Swal */
$(function () {
  $("#btnMercadoLivre").click(function () {
    $.get(`${CONTEXT}mercado-livre/code`, function (response) {
      $(location).attr("href", response);
    }).fail(function(response) {
      Swal.fire({
        title: "Atenção",
        text: "Não é possível fazer essa integração a partir desse navegador!",
        icon: "warning"
      });
    });
  });
});