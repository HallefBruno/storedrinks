/* global dcodeIO, Swal */
let bcrypt = dcodeIO.bcrypt;

$(function () {
  
  const cpfCnpj = localStorage.getItem("cpfCnpj");
  
  $.get($("#contextApp").val() + "validar/cpfcnpj/" + cpfCnpj, function (data, status) {
    if (data) {
      $("#cpfCnpj").val(atob(cpfCnpj));
    } else {
      location.href = $("#contextApp").val() + "validar";
    }
  });
  
  $('[data-bs-toggle="popover"]').popover();
  $('[data-bs-toggle="tooltip"]').tooltip();

  let isClickVerSenha = true;
  
  $('.btnVerSenha').click(function (e) {
    if (isClickVerSenha) {
      $('#password').prop("type", "text");
      $('#confirmPassword').prop("type","text");
      $('#iconBtnVerSenha').removeClass('fa fa-eye-slash');
      $('#iconBtnVerSenha').addClass('fa fa-eye');
      isClickVerSenha = false;
    } else {
      $('#password').prop("type", "password");
      $('#confirmPassword').prop("type","password");
      $('#iconBtnVerSenha').removeClass('fa fa-eye');
      $('#iconBtnVerSenha').addClass('fa fa-eye-slash');
      isClickVerSenha = true;
    }
    
  });
  
  $('.btnCriarConta').click(function (e) {
    if(validarCampos()) {
      $(".setblockUI").block({message: 'Validando email...'});
      response = $.get("https://isitarealemail.com/api/email/validate?email=" + $("#email").val(), function() {});
      response.always(function (data) {
        $(".setblockUI").unblock();
        if (data.status === 'valid') {
          $.ajax({
            url: $("#contextApp").val() + "validar/finalizar",
            type: "GET",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            headers: {
              "cpfcnpj": btoa($("#cpfCnpj").val()),
              "password": btoa(bcrypt.hashSync($("#password").val(), 12)),
              "email": $("#email").val(),
              "nome": $("#nome").val(),
              "dataNascimento": $("#dataNascimento").val()
            },
            statusCode: {
              200: function(xhr) {
                window.localStorage.removeItem('cpfCnpj');
                Swal.fire({
                  title: 'Parabéns, sua conta foi criada com sucesso!',
                  confirmButtonText: 'Ok'
                }).then((result) => {
                  if (result.isConfirmed) {
                    window.location.href = $("#contextApp").val() + "login";
                  }
                });
              },
              400: function(xhr) {
                Swal.fire('Atenção!',`Um erro 400 ocorreu, favor entrar em contato com Administrador do sistema`,'warning');
                window.console.log(xhr);
              },
              500: function(xhr) {
                Swal.fire('Atenção!',`Um erro 500 ocorreu, favor entrar em contato com Administrador do sistema`,'warning');
                window.console.log(xhr);
              }
            },
            beforeSend: function () {
              $("#divLoading").addClass("loading");
            },
            complete: function () {
              $("#divLoading").removeClass("loading");
            }
          });
        } else {
          alertWarning("Email inválido!");
        }
      });
    }
  });
  
  $("#password").on('keyup', function () {
    let number = /([0-9])/;
    let alphabets = /([a-zA-Z])/;
    let special_characters = /([~,!,@,#,$,%,^,&,*,-,_,+,=,?,>,<])/;
    if ($('#password').val().length < 9) {
      $('#password').addClass('is-invalid');
      $('#invalid-feedback-senha').html("Fraco (deve ter pelo menos 11 caracteres.)");
    } else {
      if ($('#password').val().match(number) && $('#password').val().match(alphabets) && $('#password').val().match(special_characters)) {
        $('#password').removeClass('is-invalid');
        $('#password').addClass('is-valid');
        $('#invalid-feedback-senha').removeClass();
        $('#invalid-feedback-senha').addClass('valid-feedback');
        $('#invalid-feedback-senha').html("Forte");
      } else {
        $('#invalid-feedback-senha').html("Médio (deve incluir alfabetos, números e caracteres especiais).");
      }
    }
  });
  
});

function validarCampos() {
  const mDataNascimento = moment($("#dataNascimento").val(), 'YYYY-MM-DD');
  const now = moment();
  let isCamposValido = true;
  if (!$("#nome").val()) {
    alertWarning("Nome obrigatório!");
    isCamposValido = false;
  } else if ($("#nome").val().length < 3) {
    alertWarning("Nome inválido!");
    isCamposValido = false;
  } else if (!mDataNascimento.isValid()) {
    alertWarning("Data nascimento obrigatória!");
    isCamposValido = false;
  } else if ((now.year() - mDataNascimento.year()) < 18) {
    alertWarning("Você precisa ser maior de 18 anos!");
    isCamposValido = false;
  } else if (!$("#email").val()) {
    alertWarning("Email é obrigatório!");
    isCamposValido = false;
  } else if (!$("#password").val()) {
    alertWarning("Senha é obrigatória!");
    isCamposValido = false;
  } else if ($("#password").val().length < 11) {
    alertWarning("Sua senha precisa ser de 11 caractere!");
    isCamposValido = false;
  } else if (!$("#confirmPassword").val()) {
    alertWarning("Confirmar senha é obrigatória");
    isCamposValido = false;
  } else if ($("#confirmPassword").val().length < 11) {
    alertWarning("Sua Confirmar senha precisa ser de 11 caractere!");
    isCamposValido = false;
  } else if ($("#password").val() !== $("#confirmPassword").val()) {
    alertWarning("Senha e Confirmar senha precisam ser iguais!");
    isCamposValido = false;
  }
  return isCamposValido;
}

function alertWarning(message) {
  Swal.fire('Atenção!',`${message}`,'warning');
}