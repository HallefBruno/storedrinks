
/* global StoreDrink */

$(function () {
  
  var context = $("#context").val();
  var isLida = $("#lidas").is(":checked");
  
  parametrosConfigDataTable(isLida);
  
  var dataTableMensagens = $("#mensagens").DataTable({
    ajax: {
      url: context + "mensagens/pesquisar/".concat(isLida)
    }
  });
  
  $("#lidas").change(function () {
    isLida = $("#lidas").is(":checked");
    if (isLida) {
      $(".check-input-lidas").text("Lidas");
      dataTableMensagens.ajax.url(context.concat("mensagens/pesquisar/").concat(isLida)).load();
      return;
    }
    $(".check-input-lidas").text("Não lidas");
    dataTableMensagens.ajax.url(context.concat("mensagens/pesquisar/").concat(isLida)).load();
  });

  $("#mensagens tbody").on("click", "tr", function (e) {
    var mensagem = null;
    mensagem = $("#mensagens").DataTable().row(this).data();
    if(!isEmpaty(mensagem)) {
      var modalMensagem = $('#modalMensagem');
      modalMensagem.modal('show');

      modalMensagem.off("shown.bs.modal").on("shown.bs.modal", function (e) {
        modalMensagem.find(".remetente").text(mensagem.remetenteDestinatarioMensagem.remetente);
        modalMensagem.find(".dataHoraEnviada").text(formatDataHora(mensagem.dataHoraMensagemRecebida));
        modalMensagem.find(".mensagem").val(mensagem.remetenteDestinatarioMensagem.mensagem);
      });

      modalMensagem.off("hidden.bs.modal").on("hidden.bs.modal", function (e) {
        if (!$("#lidas").is(":checked")) {
          $.ajax({
            url: context.concat("mensagens/marcar-como-lida/").concat(mensagem.id),
            method: "PUT",
            success: function(data){
              $(this).find("td").each(function () {
                $(this).removeClass("sd-fw-bold__cursor-mouse");
              });
              var showToastContainsMessage = new StoreDrink.ShowToastContainsMessage();
              showToastContainsMessage.hideToast();
              dataTableMensagens.clear().draw();
            },
            error: function(error){
              window.console.log(error);
            }
          });
        }
        $("body").removeClass("backgroundblur");
      });
    }
  });
});


function parametrosConfigDataTable(isLida) {
  var parametros = {
    columns: [
      {data: "remetenteDestinatarioMensagem.remetente", orderable: false, width: "200px"},
      {data: "remetenteDestinatarioMensagem.mensagem", orderable: false},
      {data: "dataHoraMensagemRecebida", orderable: false,
        render: function (data,type,row,meta) {
          return formatDataHora(data);
        }
      }
    ],
    columnDefs: [
      {
        targets: 0,
        className: "cursor-mouse"
      },
      {
        targets: 1,
        className: "cursor-mouse",
        render: $.fn.dataTable.render.ellipsis(60)
      },
      {
        targets: 2,
        className: "cursor-mouse"
      }
    ],
    btnActions: false
  };
  setDefaultsDataTable(parametros);
}
