$(function () {
  
  const URI = $("#context").val();
  const URL_USUARIOS = URI.concat("mensagens/usuarios");
  
  $("#destinatario").on("select2:open", function (e) {
    $(".select2-search__field")[0].focus();
  });

  $("#destinatario").select2({
    theme: "bootstrap-5",
    allowClear: true,
    language: "pt-BR",
    multiple: false,
    closeOnSelect: true,
    minimumInputLength: 3,
    selectionCssClass: 'select2--small',
    dropdownCssClass: 'select2--small',
    ajax: {
      url: URI.concat("mensagens/destinatario"),
      dataType: "json",
      delay: 1000,
      data: function (params) {
        return {
          q: params.term,
          page: params.page
        };
      },
      processResults: function (data, params) {
        params.page = params.page || 1;
        return {
          results: data.items,
          pagination: {
            more: (params.page * 10) < data.totalItens
          }
        };
      },
      cache: true
    },
    templateResult: templateResultUsuario,
    escapeMarkup: function (markup) {
      return markup;
    },
    templateSelection: function (usuario) {
      if (usuario && usuario.text !== "Destinatário") {
        var html =
        `<span class='badge bg-light text-dark' style='font-size:13px;'> ${usuario.comercio} </span>`;
        return html;
      }
      return $("<span class=''>" + usuario.text + "</span>");
    }
  });
  
});



function templateResultUsuario(usuario) {
  if (usuario && usuario.text !== "Searching…") {
    var html = 
    `<span class='badge bg-light text-dark' style='font-size:13px;'> ${usuario.comercio} </span>`;
    return html;
  }
  return $("<span>" + usuario.text + "</span>");
}