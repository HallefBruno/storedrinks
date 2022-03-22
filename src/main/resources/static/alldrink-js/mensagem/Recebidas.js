$(function () {
  
  var context = $("#context").val();
  
  $('#mensagens').DataTable({
    processing: true,
    serverSide: true,
    language: {
      url: context+"vendor/internationalisation/pt_br.json"
    },
    ajax: {
      url: context + "mensagens/pesquisar/".concat($('#lidas').is(':checked'))
    },
    columns: [
      {data: "id"},
      {data: "destinatario"},
      {data: "mensagem"},
      {data: "lida"},
      {data: "dataHoraMensagemRecebida"}
    ]
  });
  
});