/* global Swal */

$(function () {
  
  const URI = $("#context").val();
  const URL_USUARIOS = URI.concat("mensagens/usuarios");
  var comercio = {};
  
  $("#destinatario").on("select2:open", function (e) {
    $(".select2-search__field")[0].focus();
  });

  $("#destinatario").select2({
    theme: "bootstrap-5",
    allowClear: true,
    language: "pt-BR",
    multiple: false,
    closeOnSelect: true,
    selectionCssClass: 'select2--small',
    dropdownCssClass: 'select2--small',
    ajax: {
      url: URI.concat("mensagens/destinatario"),
      dataType: "json",
      delay: 1700,
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
        `<span class='badge bg-secondary'> ${usuario.text} </span>
         <span class='badge bg-secondary' text-dark'> ${usuario.nome} </span>`;
        return html;
      }
      return $("<span class=''>" + usuario.text + "</span>");
    }
  });
  
  $("#destinatario").on("select2:select", function (e) {
    comercio = e.params.data;
  });
  
  $("#btnEnviar").click(function () {
    $.ajax({
      url: $("#contextApp").val() + "validar/cliente",
      type: "post",
      dataType: 'json',
      contentType: "application/json",
      data: JSON.stringify(comercio),
      success: function (response) {
        
      },
      error: function (xhr) {
        if (xhr.responseJSON) {
          Swal.fire(
            'Atenção!',
            `${xhr.responseJSON.message}`,
            'warning'
          );
        } else if (xhr.responseText) {
          Swal.fire(
            'Atenção!',
            `${xhr.responseText}`,
            'warning'
          );
        }
        window.console.log(xhr);
      },
      beforeSend: function () {
        $("#divLoading").addClass("loading");
      },
      complete: function () {
        $("#divLoading").removeClass("loading");
      }
    });
    console.log(comercio);
  });
  
});

function templateResultUsuario(usuario) {
  if (usuario && usuario.text !== "Searching…") {
    var html = 
    `<span class='badge bg-secondary'> ${usuario.text} </span>
     <span class='badge bg-secondary'> ${usuario.nome} </span>`;
    return html;
  }
  return $("<span>" + usuario.text + "</span>");
}