/* global Swal, numeral, Intl, bootstrap */

var CONTEXT = $("#context").val();
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

let formatter = new Intl.NumberFormat('pt-BR', {style: 'currency',currency: 'BRL'});


StoreDrink.Mask = (function () {
  function Mask() {
    this.decimal = $('.js-decimal');
    this.plain = $('.js-plain');
    this.number = $('.js-number');
  }
  Mask.prototype.enable = function () {
    this.decimal.mask('#.##0,00', {reverse: true});
    this.plain.mask({integer: true, thousands: '.'});
    this.number.mask("0000", {reverse: true });
  };
  return Mask;
}());

StoreDrink.MascaraMoneteria = (function () {
  function MascaraMoneteria() {}
  MascaraMoneteria.prototype.enable = function () {
    $(".monetaria").maskMoney({prefix: 'R$ ', allowNegative: false, thousands: '.', decimal: ',', affixesStay: false});
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
      if(!naoInvocarGifLoading(settings.url,this.gifLoadingAutocomplete.bind(this))) {
        this.gifLoadingAutocomplete.css("display", "block");
        $(".setblockUI").block({message:null});
        return;
      }
      $(".setblockUI").block({message:null});
      this.divLoading.addClass("loading");
      this.gifLoadingAutocomplete.css("display", "block");
    }.bind(this));
    $(document).ajaxComplete(function (event, jqxhr, settings) {
      this.gifLoadingAutocomplete.css("display", "none");
      $(".setblockUI").unblock();
      setTimeout(() => {this.divLoading.removeClass("loading");}, 550);
    }.bind(this));
  };
  
  function naoInvocarGifLoading(url,element) {
    if(url.includes("mensagens/notificado")) {
      return false;
    } else if (url.includes("mensagens/nao-lidas")) {
      return false;
    } else if (url.includes("vendor/internationalisation/pt_br.json")) {
      return false;
    } else if (url.includes("mensagens/marcar-como-lida/")) {
      return false;
    } else if(url.includes("entradas/buscar/")) {
      return false;
    } else if(url.includes("vendas/produtos")) {
      return false;
    } else if(url.includes("/update-quantidade/")) {
      return false;
    } else if(url === `${CONTEXT}movimentacao-caixa/usuarios`) {
      return false;
    } else if (url === `${CONTEXT}caixa/usuarios`) {
      return false;
    } else if (url.includes("/movimentacao-caixa/formas-pagamento")) {
      return false;
    } else if (url.includes(`${CONTEXT}vendas/itens-vendas/`)) {
      return false;
    } else if (url.includes("mensagens/destinatario")) {
      element.css("display", "block");
      return false;
    } else if(url.includes("entradas/produtos")) {
      element.css("display", "block");
      return false;
    } else if(url.includes("movimentacao-caixa/formas-pagamento/")) {
      element.css("display", "block");
      return false;
    }
    return true;
  }
  return LoadGif;
}());


StoreDrink.AjaxError = (function () {
  function AjaxError() {}
  AjaxError.prototype.enable = function () {
    $(document).ajaxError(function (event, jqXHR, settings) {
      if (jqXHR.status === 0) {
      } else if (jqXHR.status === 400) {
        window.console.log(jqXHR.responseJSON);
        if(jqXHR.responseJSON && jqXHR.responseJSON.errors && jqXHR.responseJSON.errors.length > 0) {
          $.each(jqXHR.responseJSON.errors, function (i, item) {
            $.toast({
              heading: `${item.field}`,
              text: `${item.message}`,
              position: 'top-right',
              loader: false,
              icon: 'error'
            });
          });
        } else if (jqXHR.responseJSON.message) {
          $.toast({
            heading: `${"Atenção!"}`,
            text: `${jqXHR.responseJSON.message}`,
            position: 'top-right',
            loader: false,
            icon: 'error'
          });
        }
      }
    }.bind(this));
  };
  return AjaxError;
}());

StoreDrink.ShowToastContainsMessage = (function () {
  function ShowToastContainsMessage() {}
  
  ShowToastContainsMessage.prototype.showToast = function () {
    var html = 
      `<div id="liveRoot" class="position-fixed bottom-0 end-0 p-3" style="z-index: 11; opacity: 0.7">
        <div id="liveToast" class="toast hide" role="alert" aria-live="assertive" aria-atomic="true" data-bs-autohide="false">
          <div id="toastHeader" class="toast-header">
            <i id="faInfo" class="fa fa-info-circle" aria-hidden="true"></i>
            <strong id="strong" style="margin-left: 5px;" class="me-auto">Antenção!</strong>
            <button id="btnClose" type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
          </div>
          <div id="toastBody" class="toast-body">
            <i id="faEnvelope" class="fa fa-envelope" aria-hidden="true"></i> Você possui mensagem!
          </div>
        </div>
      </div>`;
    
    $.ajax({
      url: $("#context").val().concat("mensagens/nao-lidas"),
      type: "get",
      statusCode: {
        200: function (response) {
          $("html").find(".container-fluid").append(html);
          $("#liveToast").toast("show");
          addEventsMouse();
          $("#btnClose").click(function (event) {
            event.stopPropagation();
            $.ajax({
              url: $("#context").val().concat("mensagens/notificado"),
              method: "PUT",
              success: function (data) {},
              error: function (error) {window.console.debug(error);}
            });
          });
        }
      },
      error: function (xhr) {
        window.console.log(xhr);
      }
    });
  };
  
  ShowToastContainsMessage.prototype.hideToast = function () {
    $("#liveToast").hide();
  };

  function addEventsMouse() {
    $("#liveRoot").mouseenter(function () {
      $("#liveRoot").css('opacity', '');
    });

    $("#liveToast").mouseenter(function () {
      $("#liveRoot").css('opacity', '');
    });

    $("#toastBody").mouseenter(function () {
      $("#liveRoot").css('opacity', '');
    });

    $("#toastHeader").mouseenter(function () {
      $("#liveRoot").css('opacity', '');
    });

    $("#btnClose").mouseenter(function () {
      $("#liveRoot").css('opacity', '');
    });

    $("#strong").mouseenter(function () {
      $("#liveRoot").css('opacity', '');
    });

    $("#faInfo").mouseenter(function () {
      $("#liveRoot").css('opacity', '');
    });

    $("#faEnvelope").mouseenter(function () {
      $("#liveRoot").css('opacity', '');
    });

    $("#liveToast").mouseout(function () {
      $("#liveRoot").css('opacity', 0.8);
    });
  }
  return ShowToastContainsMessage;
}());

StoreDrink.Mensagem = (function () {
  function Mensagem() {}
  Mensagem.prototype.show = function (icon,mensagem) {
    const Toast = Swal.mixin({
      toast: true,
      position: 'top-end',
      showConfirmButton: false,
      timer: 5000,
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
      heading: `<p class='mb-1'>${heading}<p><hr/>`,
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
  
  let url = $(location).attr("href");
  if(!url.includes("localhost") && !url.includes("127.0.0.1") && $("#leftClick").val() === undefined) {
    $(document).bind("contextmenu", function (e) {
      return false;
    });
    $(document).keydown(function (event) {
      if (event.keyCode === 123) {
        return false;
      } else if (event.ctrlKey && event.shiftKey && event.keyCode === 73) {
        return false;
      }
    });
  }
    
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
  
  var mask = new StoreDrink.Mask();
  mask.enable();

  var ajaxError = new StoreDrink.AjaxError();
  ajaxError.enable();
  
  var showToastContainsMessage = new StoreDrink.ShowToastContainsMessage();
  showToastContainsMessage.showToast();
  
});
