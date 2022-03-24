function setDefaultsDataTable(parametros) {
  
  if(parametros.btnActions)  {
    var buttons =
    {
      data: "id", title: "Ações", wrap: true,
      render: function (id) {
        var stringButtons = "<button class='btn-detalhe btn btn-outline-info btn-sm' data-detalhe='" + id + "' type='button' title='Detalhes' ><i class='fa fa-info-circle'></i></button>";
        stringButtons += "<button class='btn-excluir btn btn-outline-danger btn-sm mr-1 ml-1 mt-1 mb-1' data-excluir='" + id + "' type='button' title='Excluir'><i class='fa fa-trash-o'></i></button>";
        stringButtons += "<button class='btn-editar btn btn-outline-edit btn-sm' data-editar='" + id + "' type='button' title='Editar'><i class='fa fa-pencil'></i></button>";
        return stringButtons;
      }
    };
    parametros.columns.push(buttons);
  }

  $.extend(true, $.fn.dataTable.defaults, {
    searching: false,
    scrollY: "400px",
    scrollCollapse: true,
    serverSide: true,
    processing: false,
    destroy: true,
    pageLength: 10,
    responsive: true,
    info: true,
    lengthChange: false,
    language: {
      url: $("#context").val()+"vendor/internationalisation/pt_br.json"
    },
    columnDefs: parametros.columnDefs,
    columns: parametros.columns
  });
}

function mascaraStringTel(numero) {
  if (numero.length === 10) {
    return numero.replace(/(\d{2})(\d{4})(\d{4})/, '($1) $2-$3');
  } else {
    return numero.replace(/(\d{2})(\d{5})(\d{4})/, '($1) $2-$3');
  }
}