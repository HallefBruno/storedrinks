/* global StoreDrink, CONTEXT */

$(function () {
  
  const toast = new StoreDrink.Toast();
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
            toast.show("success","Atenção","Recolhimento realizado com sucesso!","top-right");
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
    toast.show("warning","Atenção","Por favor insira o valor!","top-right");
    valorRetirada.focus();
  });
});
