/* global CONTEXT */

let ENDPOINT = "caixa";

$(function () {
  
  popularSelectUsuarios();
  eventSelectUsuario();
  
});

function popularSelectUsuarios() {
  if ($("#usuarios").length) {
    $.get(`${CONTEXT}${ENDPOINT}/usuarios`, function (response) {
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
}

function eventSelectUsuario() {
  $("#usuarios").on("select2:select", function (e) {
    window.location.href = `${CONTEXT}${ENDPOINT}/fechar-por-usuario/${e.params.data.id}`;
  });
}

function templateResultProduto(usuario) {
  if (usuario.loading) {
    return $(`<span class='badge bg-light text-dark fw-bold' style='font-size:12px;'>${usuario.text}</span>`);
  }
  return $("<span class='badge bg-light text-dark fw-bold' style='font-size:12px;'>" + usuario.text + "</span>");
}
