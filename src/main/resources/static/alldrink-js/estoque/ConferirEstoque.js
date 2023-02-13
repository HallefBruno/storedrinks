$(document).ready(function() {
  $('#tbConferirEstoque').DataTable({
    dom: 'Bfrtip',
    buttons: [
      {
        extend: "excel",
        className: "btn btn-sm btn-success",
        titleAttr: 'Exportar para Excel',
        text: '<i class="fa-regular fa-file-excel"></i>',
        init: function( api, node, config) {
          $(node).removeClass('dt-button');
        }
      },
      {
        extend: "pdf",
        className: "btn btn-sm btn-danger",
        titleAttr: 'Exportar para PDF',
        text: '<i class="fa-regular fa-file-pdf"></i>',
        init: function( api, node, config) {
          $(node).removeClass('dt-button');
        }
      }
    ],
    searching: true,
    paging: false,
    info: true,
    responsive: true,
    destory: true,
    language: {
      url: `${$("#context").val()}vendor/internationalisation/pt_br.json`
    },
    order: [[ 5, "desc" ]]
    
  });
  
});


//$('#tbConferirEstoque').DataTable({
//    ajax: {
//      url: `${CONTEXT}estoque/conferir-produtos`
//    },
//    drawCallback: function (settings) { 
//      var response = settings.json;
//      console.log(response);
//    },
//    dom: 'Bfrtip',
//    buttons: [
//      {
//        extend: "excel",
//        className: "btn btn-sm btn-success",
//        titleAttr: 'Exportar para Excel',
//        text: '<i class="fa-regular fa-file-excel"></i>',
//        init: function( api, node, config) {
//          $(node).removeClass('dt-button');
//        }
//      },
//      {
//        extend: "pdf",
//        className: "btn btn-sm btn-danger",
//        titleAttr: 'Exportar para PDF',
//        text: '<i class="fa-regular fa-file-pdf"></i>',
//        init: function( api, node, config) {
//          $(node).removeClass('dt-button');
//        }
//      }
//    ],
//    searching: true,
//    paging: false,
//    info: true,
//    responsive: true,
//    language: {
//      url: `${$("#context").val()}vendor/internationalisation/pt_br.json`
//    },
//    columns: [
//      {data: 'id'},
//      {
//        "data": "ativo", render: function (data, type, row, meta) {
//          if (data) {
//            return `<span class="text-primary">Ativo</span>`;
//          }
//          return `<span class="text-danger">Inativo</span>`;
//        }
//      },
//      {data: 'codProduto'},
//      {data: 'codBarra'},
//      {data: 'descricaoProduto'},
//      {data: 'quantidade'}
//    ]
//  });