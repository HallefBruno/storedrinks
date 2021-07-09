$(document).ready(function () {
    $("#valorCusto").maskMoney({prefix:'R$ ', allowNegative: false, thousands:',', decimal:'.', affixesStay: false});
    $("#valorVenda").maskMoney({prefix:'R$ ', allowNegative: false, thousands:',', decimal:'.', affixesStay: false});
});