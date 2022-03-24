/* global moment */

$(function () {
  
  var context = $("#context").val();
  
  var parametros = {
    columns: [
      {data: "remetente", orderable: false,
        render: function(data) {
          return "<span class='sd-fw-bold__curso-mouse'>"+data+"</span>";
        }
      },
      {data: "mensagem", orderable: false, width: "30px",
        render: function(data) {
          return "<span class='sd-fw-bold__curso-mouse sb-limit-text'>"+data+"</span>";
        }
      },
      {data: "dataHoraMensagemRecebida", orderable: false,
        render: function (data,type,row,meta) {
          return "<span class='sd-fw-bold__curso-mouse'>"+moment(data).format('DD/MM/YYYY, hh:mm:ss')+"</span>";
        }
      }
    ],
    
    columnDefs: [
      {className: "sb-limit-text", "targets": [0]}
    ],
    
    btnActions: false
  };

  setDefaultsDataTable(parametros);
  
  $('#mensagens').DataTable({
    ajax: {
      url: context + "mensagens/pesquisar/".concat($('#lidas').is(':checked'))
    }
  });

  $('#mensagens tbody').on('click', 'tr', function () {
    var data = $("#mensagens").DataTable().row(this).data();
    window.console.log(data);
  });
  
});