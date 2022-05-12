/* global StoreDrink, CONTEXT */

$(function () {
  
  var mensagemToast = new StoreDrink.Mensagem();
  const ENDPOINT = "recolhimento";
  let valorRetirada = $("#valorRetirada");
  valorRetirada.focus();
  
  $("#btnSalvarRecolhimento").click(function () {
    let valorSemFormato = valorRetirada.val();
    valorSemFormato = valorSemFormato.replace("R$","");
    valorSemFormato = valorSemFormato.replace(".","");
    valorSemFormato = valorSemFormato.replace(",",".");
    console.log(valorSemFormato);
    if(valorSemFormato) {
      $.ajax({
        url: `${CONTEXT}${ENDPOINT}`,
        method: 'POST',
        data: {
          "valorRetirada": valorSemFormato
        },
        statusCode: {
          201: function (data) {
            mensagemToast.show("success","Recolhimento realizado com sucesso!");
            valorRetirada.val("");
            valorRetirada.focus();
          },
          403: function (jqXHR) {
            console.log(jqXHR);
          },
          500: function (jqXHR) {
            console.log(jqXHR);
          }
        }
      });
      return;
    }
    mensagemToast.show("warning","Por favor insira o valor!");
    valorRetirada.focus();
  });
});
