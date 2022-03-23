/* global moment */

$(function () {
  
  var context = $("#context").val();
  
  var parametros = {
    columns: [
      {data: "id", orderable: false, className: 'text-center'},
      {data: "destinatario", orderable: false},
      {data: "mensagem", orderable: false},
      {data: "lida", orderable: false, className: 'text-center'},
      {data: "dataHoraMensagemRecebida", orderable: false, className: 'text-center',
        render: function (data,type,row,meta) {
          return moment(data).format('dd/MM/yyyy, h:mm:ss a');
        }
      }
    ],
    btnActions: false
  };

  setDefaultsDataTable(parametros);
  
  $('#mensagens').DataTable({
    ajax: {
      url: context + "mensagens/pesquisar/".concat($('#lidas').is(':checked'))
    }
  });
  
});