/* global Swal */

var Deletar = Deletar || {};

Deletar.DialogoExcluir = (function () {

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
            window.console.log(url);
            if (result.isConfirmed) {
                onExcluirConfirmado(url);
            }
        });
    }

    function onExcluirConfirmado(url) {
        window.console.info("excluir");
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

$(function () {
    var dialogo = new Deletar.DialogoExcluir();
    dialogo.iniciar();
});