/* global CONTEXT, Swal */

let ENDPOINT = "caixa";

$(function () {
  
  popularSelectUsuarios();
  eventSelectUsuario();
  fecharCaixa();
  
});

function popularSelectUsuarios() {
  if ($("#usuarios").length) {
    $.get(`${CONTEXT}${ENDPOINT}/usuarios`, function (response) {
      $("#usuarios").select2({
        theme: "bootstrap-5",
        allowClear: true,
        language: "pt-BR",
        multiple: false,
        closeOnSelect: true,
        data: response,
        templateResult: templateResultProduto,
        templateSelection: function (response) {
          if (response && response.text !== "Usuários") {
            return $("<span class='badge bg-light text-dark fw-bold' style='font-size:12px;'>" + response.text + "</span>");
          }
          return $("<span class=''>" + response.text + "</span>");
        }
      });
    });
  }
}

function fecharCaixa() {
  $("#btnFecharCaixa").click(function () {
    Swal.fire({
      title: 'Tem certeza?',
      text: "Você não será capaz de reverter isso!",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sim, feche o caixa agora!'
    }).then((result) => {
      if (result.isConfirmed) {
        location.href=`${CONTEXT}${ENDPOINT}/fechar/${$("#id").val()}`;
      }
    });
  });
}

function eventSelectUsuario() {
  $("#usuarios").on("select2:select", function (e) {
    $("#divLoading").addClass("loading");
    location.href = `${CONTEXT}${ENDPOINT}/por-usuario/${e.params.data.id}`;
  });
}

function templateResultProduto(usuario) {
  if (usuario.loading) {
    return $(`<span class='badge bg-light text-dark fw-bold' style='font-size:12px;'>${usuario.text}</span>`);
  }
  return $("<span class='badge bg-light text-dark fw-bold' style='font-size:12px;'>" + usuario.text + "</span>");
}
