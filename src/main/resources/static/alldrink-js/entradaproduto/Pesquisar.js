$(function () {
  $("#fornecedor").select2({
    theme: "bootstrap-5",
    allowClear: true,
    multiple: false,
    closeOnSelect: true,
    language: "pt-BR"
  });

  $("#numeroNota").focus();
  setTimeout(() => {
    $(".toast").toast('show');
  }, 0);

  $("#entradas").DataTable({
    searching: false,
    destroy: true,
    paging: false,
    info: false,
    responsive: true,
    lengthChange: false,
    language: {
      url: `${$("#context").val()}vendor/internationalisation/pt_br.json`
    }
  });
});