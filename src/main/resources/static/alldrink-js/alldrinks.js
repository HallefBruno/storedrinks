/* global Swal, numeral, Intl, bootstrap */

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
      $("#divLoading").removeClass("loading");
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
    $("#divLoading").removeClass("loading");
  }

  function onErroExcluir(e) {
    console.log(e.responseText);
    Swal.fire('Oops!', e.responseText, 'error');
    $("#divLoading").removeClass("loading");
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

StoreDrink.FormatarValor = (function () {
  function MascaraMonetariaPrincipal() {}
  MascaraMonetariaPrincipal.prototype.enable = function (teste) {
    const formatter = new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    });
    window.console.log(formatter.format(teste));
    //console.log(formatter.format(1234567.89)); // R$ 1.234.567,89
  };
  return MascaraMonetariaPrincipal;
}());

StoreDrink.MaskMoneyTest = (function () {
  function MaskMoneyTest() {
    this.decimal = $('.js-decimal');
    this.plain = $('.js-plain');
  }
  MaskMoneyTest.prototype.enable = function () {
    this.decimal.maskNumber({decimal: ',', thousands: '.'});
    this.plain.maskNumber({integer: true, thousands: '.'});
  };
  return MaskMoneyTest;
}());

StoreDrink.MascaraMoneteria = (function () {
  function MascaraMoneteria() {}
  MascaraMoneteria.prototype.enable = function () {
    $(".monetaria").maskMoney({prefix: 'R$ ', allowNegative: false, thousands: ',', decimal: '.', affixesStay: false});;
  };
  return MascaraMoneteria;
}());

StoreDrink.Cep = (function () {
  function Cep() {
    this.cep = $(".cep");
  }
  Cep.prototype.enable = function () {
    this.cep.mask("00000-000");
  };
  return Cep;
}());

StoreDrink.MascaraCpfCnpj = (function () {
    
  function MascaraCpfCnpj() {
    this.mascaraCpfCnpj = $(".mascara-cpf-cnpj");
  }
    
  MascaraCpfCnpj.prototype.enable = function () {

    var CpfCnpjMaskBehavior = function (val) {
      return val.replace(/\D/g, '').length <= 11 ? '000.000.000-009' : '00.000.000/0000-00';
    },
    cpfCnpjpOptions = {
      onKeyPress: function (val, e, field, options) {
        field.mask(CpfCnpjMaskBehavior.apply({}, arguments), options);
      }
    };
    this.mascaraCpfCnpj.mask(CpfCnpjMaskBehavior,cpfCnpjpOptions);
  };
    
  return MascaraCpfCnpj;
}());

StoreDrink.MaskPhoneNumber = (function() {
	
  function MaskPhoneNumber() {
    this.inputPhoneNumber = $('.js-phone-number');
  }
  MaskPhoneNumber.prototype.enable = function () {
    var maskBehavior = function (val) {
      return val.replace(/\D/g, '').length === 11 ? '(00) 00000-0000' : '(00) 0000-00009';
    };
    var options = {
      onKeyPress: function (val, e, field, options) {
        field.mask(maskBehavior.apply({}, arguments), options);
      }
    };
    this.inputPhoneNumber.mask(maskBehavior, options);
  };
  return MaskPhoneNumber;
	
}());

StoreDrink.LoadGif = (function () {
  
  function LoadGif() {
    this.gifLoadingAutocomplete = $(".js-img-loading");
    this.divLoading = $("#divLoading");
  }
  LoadGif.prototype.enable = function () {
    $(document).ajaxSend(function (event, jqxhr, settings) {
      if(settings.url.includes("mensagens/destinatario")) {
        this.gifLoadingAutocomplete.css("display", "block");
        return;
      }
      this.divLoading.addClass("loading");
    }.bind(this));
    $(document).ajaxComplete(function (event, jqxhr, settings) {
      if(settings.url !== null && settings.url !== "" && settings.url === "/alldrinks/mensagens") {
        if(jqxhr.status === 201 && settings.type === "POST") {
          alert("OK");
        } 
      }
      this.divLoading.removeClass("loading");
      this.gifLoadingAutocomplete.css("display", "none");
    }.bind(this));
  };
  return LoadGif;
}());


StoreDrink.AjaxError = (function () {
  function AjaxError() {}
  AjaxError.prototype.enable = function () {
    $(document).ajaxError(function (event, jqXHR, settings) {
      if (jqXHR.status === 0) {
      } else if (jqXHR.status === 400) {
        $.each(jqXHR.responseJSON.errors, function (i, item) {
          alert(item.field);
          $.toast({
            heading: `${item.field}`,
            text: `${item.message}`,
            position: 'top-right',
            loader: false,
            icon: 'error'
          });
        });
      }
    }.bind(this));
  };
  return AjaxError;
}());

StoreDrink.Mensagem = (function () {
  function Mensagem() {}
  Mensagem.prototype.show = function (icon,mensagem) {
    const Toast = Swal.mixin({
      toast: true,
      position: 'top-end',
      showConfirmButton: false,
      timer: 8000,
      timerProgressBar: true,
      didOpen: (toast) => {
        toast.addEventListener('mouseenter', Swal.stopTimer);
        toast.addEventListener('mouseleave', Swal.resumeTimer);
      }
    });

    Toast.fire({
      icon: `${icon}`,
      text: `${mensagem}`
    });
  };
  return Mensagem;
}());

StoreDrink.Toast = (function () {
  /*
   bottom-left
   bottom-right
   bottom-center
   top-right
   top-left
   top-center
   mid-center
   */
  function Toast() {}
  /*
   * @param {type} icon
   * @param {type} heading 
   * @param {type} text
   * @param {type} position
   */
  Toast.prototype.show = function (icon,heading,text,position) {
    $.toast({
      heading: `${heading}`,
      text: `${text}`,
      position: `${position}`,
      loader: false,
      icon: `${icon}`,
      hideAfter: 5000
    });
  };
  return Toast;
}());

StoreDrink.RemoveMask = (function () {
    
  function RemoveMask() {}

  RemoveMask.prototype.remover = function (value) {
    if (value.length >= 8) {
      value = value.replace(/[^0-9.-]+/g, "");
      if (!value.includes(".")) {
        var val = value.substring(0, value.length - 2) + "." + value.substring(value.length - 2, value.length);
        return Number(val);
      }
    }
    return value;
  };
  return RemoveMask;
}());

$(function () {

  $('[data-bs-toggle="popover"]').popover();
  $('[data-bs-toggle="tooltip"]').tooltip();

  var dialogo = new StoreDrink.DialogoExcluir();
  dialogo.iniciar();

  var loadGif = new StoreDrink.LoadGif();
  loadGif.enable();

  var inputM = new StoreDrink.MascaraMoneteria();
  inputM.enable();

  var security = new StoreDrink.Security();
  security.enable();

  var mascaraCpfCnpj = new StoreDrink.MascaraCpfCnpj();
  mascaraCpfCnpj.enable();

  var mascaraCep = new StoreDrink.Cep();
  mascaraCep.enable();

  var maskPhone = new StoreDrink.MaskPhoneNumber();
  maskPhone.enable();
  
  var maskPrincipal = new StoreDrink.FormatarValor();
  maskPrincipal.enable("121212.00");
  
  var ajaxError = new StoreDrink.AjaxError();
  ajaxError.enable();

});
