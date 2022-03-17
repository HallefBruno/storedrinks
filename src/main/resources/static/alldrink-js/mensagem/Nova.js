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
      data: usuarios,
      templateResult: templateResultUsuario,
      escapeMarkup: function (markup) {
        return markup;
      },
      templateSelection: function (usuario) {
        if (usuario && usuario.text !== "Destinatário") {
          var html = 
          `<span class='badge bg-light text-dark' style='font-size:13px;'> ${usuario.comercio} </span>
           <span class='badge bg-light text-dark' style='font-size:13px; text-align-last: right;'> ${usuario.email} </span>
          `;
          return html;
        }
        return $("<span class=''>" + usuario.text + "</span>");
      }
    });
  });
  
});

function templateResultUsuario(usuario) {
  if (usuario && usuario.text !== "Searching…") {
    var html = 
    `<span class='badge bg-light text-dark' style='font-size:13px;'> ${usuario.comercio} </span>
     <span class='badge bg-light text-dark' style='font-size:13px; text-align-last: right;'> ${usuario.email} </span>
    `;
    return html;
  }
  return $("<span>" + usuario.text + "</span>");
}