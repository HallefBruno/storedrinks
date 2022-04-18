/* global StoreDrink */

$(function () {
  $("#cpfCnpj").focus();

  $("form").submit(function () {
    var cpfCnpj = $("#cpfCnpj").val();
    var telefone = $("#telefone").val();

    if (cpfCnpj) {
      if (cpfCnpj.length === 14 || cpfCnpj.length === 18) {
      } else {
        new StoreDrink.Mensagem().show("warning", "CPF/CNPJ inválido!");
        $("#divLoading").removeClass("loading");
        return false;
      }
    }

    if (telefone) {
      if (telefone.length < 14) {
        new StoreDrink.Mensagem().show("warning", "Telefone inválido!");
        $("#divLoading").removeClass("loading");
        return false;
      }
    }
    return true;
  });
});