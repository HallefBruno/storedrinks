/* global CONTEXT */

let usuarios = [];

$(function () {
  
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
            return $("<span class='badge bg-primary fw-normal' style='font-size:12px;'>" + response.text + "</span>");
          }
          return $("<span class=''>" + response.text + "</span>");
        }
      });
    });
  }
  
  
  
});

function templateResultProduto(usuario) {
  if (usuario.loading) {
    return $(`<span class='badge bg-primary fw-normal' style='font-size:12px;'>${usuario.text}</span>`);
  }
  return $("<span class='badge bg-primary fw-normal' style='font-size:12px;'>" + usuario.text + "</span>");
}
