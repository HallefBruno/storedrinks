const usuarios = [];

$(function () {
  
  $.get("url",function (response) {
    usuarios = response;
  });
  
  $("#usuarios").select2({
    theme: "bootstrap-5",
    allowClear: true,
    language: "pt-BR",
    multiple: false,
    closeOnSelect: true,
    data: usuarios,
    templateResult: templateResultProduto,
    templateSelection: function (usuario) {
      if (usuario && usuario.text !== "Usuários") {
        return $("<span class='badge bg-secondary fw-normal' style='font-size:14px;'>" + usuario.text + "</span>");
      }
      return $("<span class=''>" + usuario.text + "</span>");
    }
  });
  
});

function templateResultProduto(usuario) {
  if (usuario.loading) {
    return $(`<span class='badge bg-secondary fw-normal' style='font-size:14px;'>${usuario.text}</span>`);
  }
  return $("<span class='badge bg-secondary fw-normal' style='font-size:14px;'>" + usuario.text + "</span>");
}
