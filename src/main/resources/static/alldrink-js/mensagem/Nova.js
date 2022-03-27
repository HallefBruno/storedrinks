/* global Swal, StoreDrink */

$(function () {
  
  const URI = $("#context").val();
  var dataSelect2 = null;
  var textMensagem = $("#mensagem");
  $("#destinatario").focus();
  
  $("#destinatario").on("select2:open", function (e) {
    $(".select2-search__field")[0].focus();
    $("#destinatario").val(null).trigger("change");
    $("#destinatario").focus();
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
      delay: 1200,
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
        `<span class='badge bg-primary fw-normal' style='font-size: 12px;'> ${usuario.text} </span>
         <span class='badge bg-primary fw-normal' style='font-size: 12px;'> ${usuario.destinatario} </span>`;
        return html;
      }
      return $("<span class=''>" + usuario.text + "</span>");
    }
  });
  
  $("#destinatario").on("select2:select", function (e) {
    dataSelect2 = e.params.data;
  });
  
  $("#destinatario").on("select2:open", function (e) {
    dataSelect2 = null;
  });
  
  $("#btnEnviar").click(function () {
    
    if (validacaoCampos(dataSelect2, textMensagem)) {
      
      var mensagemdto = {
        tenant: dataSelect2.tenant,
        destinatario: dataSelect2.destinatario,
        mensagem: textMensagem.val()
      };
      
      $.ajax({
        url: URI.concat("mensagens"),
        type: "POST",
        dataType: 'json',
        contentType: "application/json",
        data: JSON.stringify(mensagemdto),
        statusCode: {
          201: function (response) {
            var toast = new StoreDrink.Toast();
            toast.show('success','Atenção','Mensagem enviada com sucesso!','top-right');
            clearFormFocusSelect();
            if(dataSelect2.id === Number($("meta[name=_flag]").attr("content"))) {
              var showToastContainsMessage = new StoreDrink.ShowToastContainsMessage();
              showToastContainsMessage.showToast();
            }
          }
        },
        error: function (xhr) {
          if(status >= 400) {
            window.console.log(xhr);
          }
        }
      });
    }
  });
});

function templateResultUsuario(usuario) {
  if (usuario && usuario.text !== "Searching…") {
    var html = 
    `<span class='badge bg-primary fw-normal' style='font-size: 12px;'> ${usuario.text} </span>
     <span class='badge bg-primary fw-normal' style='font-size: 12px;'> ${usuario.destinatario} </span>`;
    return html;
  }
  return $("<span>" + usuario.text + "</span>");
}

function validacaoCampos(dataSelect2, textMensagem) {
  var isValida = true;
  var toast = new StoreDrink.Toast();
  if (dataSelect2 === undefined || dataSelect2 === null) {
    $("#destinatario").addClass("is-invalid");
    toast.show('error','Atenção','Destinatário é obrigatório!','top-right');
    isValida = false;
  } else {
    $("#destinatario").removeClass("is-invalid");
  }
  
  if(textMensagem.val() === null || textMensagem.val() === undefined || textMensagem.val() === "") {
    $("#mensagem").addClass("is-invalid");
    toast.show('error','Atenção','Mensagem é obrigatório!','top-right');
    isValida = false;
  } else {
    $("#mensagem").removeClass("is-invalid");
  }
  
  return isValida;
}

function clearFormFocusSelect() {
  $("form").trigger("reset");
  $("#mensagem").val("");
  $("#destinatario").val(null).trigger("change");
  $("#destinatario").focus();
}