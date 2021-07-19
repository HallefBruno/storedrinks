/* global Swal, numeral */

var StoreDrink = StoreDrink || {};

StoreDrink.DialogoExcluir = (function () {

    function DialogoExcluir() {
        this.exclusaoBtn = $('.js-exclusao-btn');
    }

    DialogoExcluir.prototype.iniciar = function () {
        this.exclusaoBtn.on('click', onExcluirClicado.bind(this));

        if (window.location.search.indexOf('excluido') > -1) {
            Swal.fire('Pronto!', 'Excluído com sucesso!', 'success');
        }
    };

    function onExcluirClicado(evento) {
        event.preventDefault();
        var botaoClicado = $(evento.currentTarget);
        var url = botaoClicado.data('url');
        var objeto = botaoClicado.data('objeto');

        Swal.fire({
            title: 'Tem certeza?',
            text: 'Excluir "' + objeto + '"? Você não poderá recuperar depois.',
            showCancelButton: true,
            confirmButtonColor: '#DD6B55',
            confirmButtonText: 'Sim, exclua agora!'
        }).then((result) => {
            if (result.isConfirmed) {
                onExcluirConfirmado(url);
            }
        });
    }

    function onExcluirConfirmado(url) {
        $.ajax({
            url: url,
            method: 'DELETE',
            success: onExcluidoSucesso.bind(this),
            error: onErroExcluir.bind(this)
        });
    }

    function onExcluidoSucesso() {
        var urlAtual = window.location.href;
        var separador = urlAtual.indexOf('?') > -1 ? '&' : '?';
        var novaUrl = urlAtual.indexOf('excluido') > -1 ? urlAtual : urlAtual + separador + 'excluido';
        window.location = novaUrl;
    }

    function onErroExcluir(e) {
        console.log('ahahahah', e.responseText);
        Swal.fire('Oops!', e.responseText, 'error');
    }

    return DialogoExcluir;

}());


StoreDrink.Security = (function () {
    
    function Security() {
        this.token = $("meta[name='_csrf']").attr("content");
        this.header = $("meta[name='_csrf_header']").attr("content");
    }

    Security.prototype.enable = function () {
        $(document).ajaxSend(function (event, jqxhr, settings) {
            jqxhr.setRequestHeader(this.header, this.token);
        }.bind(this));
    };

    return Security;

}());


StoreDrink.formatarMoeda = function (valor) {
    return numeral(valor).format('0,0.00');
};

StoreDrink.recuperarValor = function (valorFormatado) {
    return numeral().unformat(valorFormatado);
};

StoreDrink.MascaraMoneteria = (function () {
    function MascaraMoneteria() {}
    MascaraMoneteria.prototype.enable = function () {
        $(".monetaria").maskMoney({prefix: 'R$ ', allowNegative: false, thousands: ',', decimal: '.', affixesStay: false});;
    };
    return MascaraMoneteria;
}());


StoreDrink.LoadGif = (function () {

    function LoadGif() {}
    LoadGif.prototype.enable = function () {
        $(document).ajaxSend(function (event, jqxhr, settings) {
            $("#divLoading").addClass("loading");
        }.bind(this));
        $(document).ajaxComplete(function (event, jqxhr, settings) {
            $("#divLoading").removeClass("loading");
        }.bind(this));
    };
    return LoadGif;
}());


$(function () {

    var dialogo = new StoreDrink.DialogoExcluir();
    dialogo.iniciar();

    
    var loadGif = new StoreDrink.LoadGif();
    loadGif.enable();
    
    var inputM = new StoreDrink.MascaraMoneteria();
    inputM.enable();
    
//    var security = new StoreDrink.Security();
//    security.enable();
});
