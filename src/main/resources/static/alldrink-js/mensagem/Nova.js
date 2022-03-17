$(function () {
  
  const URL = $("#context").val() + "mensagens/usuarios";
  var usuarios = [];
  
  $.getJSON(URL, function (response) {
    console.log(response);
    usuarios = response;
    $("#destinatario").select2({
      theme: "bootstrap-5",
      allowClear: true,
      language: "pt-BR",
      multiple: false,
      closeOnSelect: true,
      templateResult: templateResultUsuario,
      data: usuarios
    });
  });
  
});

function templateResultUsuario(usuario) {
  if (usuario && usuario.text !== "Searching…") {
    return $("<span class='badge bg-primary' style='font-size:13px;'>" + usuario.text + "</span>");
  }
  return $("<span>" + usuario.text + "</span>");
}