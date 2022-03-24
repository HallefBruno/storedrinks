
$(function () {
  
  var context = $("#context").val();
  
  var parametros = {
    columns: [
      {data: "remetente", orderable: false, width: "200px"},
      {data: "mensagem", orderable: false},
      {data: "dataHoraMensagemRecebida", orderable: false,
        render: function (data,type,row,meta) {
          return formatDataHora(data);
        }
      }
    ],
    
    columnDefs: [
      {
        targets: 0,
        className: "sd-fw-bold__cursor-mouse"
      },
      {
        targets: 1,
        className: "sd-fw-bold__cursor-mouse",
        render: $.fn.dataTable.render.ellipsis(60)
      },
      {
        targets: 2,
        className: "sd-fw-bold__cursor-mouse"
      }
    ],
    
    btnActions: false
  };

  setDefaultsDataTable(parametros);
  
  var tableMensagens = $("#mensagens").DataTable({
    ajax: {
      url: context + "mensagens/pesquisar/".concat($("#lidas").is(":checked"))
    }
  });

  $("#mensagens tbody").on("click", "tr", function (e) {
    e.stopImmediatePropagation();
    var mensagem = $("#mensagens").DataTable().row(this).data();
    $(this).find("td").each(function () {
      $(this).removeClass("sd-fw-bold__cursor-mouse");
    });
    
    window.console.log(mensagem);
    
    var modalMensagem = $('#modalMensagem');
    
    modalMensagem.modal('show');
    
    modalMensagem.on("shown.bs.modal", function (e) {
      modalMensagem.find(".remetente").text(mensagem.remetente);
      modalMensagem.find(".dataHoraEnviada").text(formatDataHora(mensagem.dataHoraMensagemRecebida));
      modalMensagem.find(".mensagem").val(mensagem.mensagem);
    });
    
    modalMensagem.on("hidden.bs.modal", function (e) {
      $.ajax({
        url: context.concat("mensagens/marcar-como-lida/").concat(mensagem.id),
        method: 'PUT',
        success: function(data){
          window.console.log(data);
          tableMensagens.ajax.reload();
        },
        error: function(data){
          
        }
      });
    });
  });
  
});
