/* global CONTEXT, formatter, Swal */
const ENDPOINT = "vendas";
let movimentacaoCaixaId = null;
let vendaId = null;

$(function () {
  initDataTable();
  eventSelectVenda();
  cancelarVenda();
});

function eventSelectVenda() {
  $("input[type=radio][name=flexRadioDefault]").change(function () {
    $.get(`${CONTEXT}${ENDPOINT}/itens-vendas/${this.id}`, function (response) {
      if(response && response.length > 0) {
        popularTable(response);
        $("#btnCancelarVenda").prop("disabled",false);
      } else {
        $("#btnCancelarVenda").prop("disabled",true);
      }
    });
    vendaId = this.id;
    movimentacaoCaixaId = this.value;
  });
}

function cancelarVenda() {
  $("#btnCancelarVenda").click(function () {
    
    Swal.fire({
      title: 'Tem certeza?',
      text: "Você não será capaz de reverter isso!",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sim, exclua!'
    }).then((result) => {
      if (result.isConfirmed) {
        $.ajax({
          url: `${CONTEXT}${ENDPOINT}/cancelar-venada/${movimentacaoCaixaId}/${vendaId}`,
          method: 'DELETE',
          statusCode: {
            204: function (data) {
              Swal.fire('Atenção', 'Venda cancelada com sucesso!', 'success');
              window.location.reload();
            },
            403: function (jqXHR) {
            },
            500: function (jqXHR) {
            }
          }
        });
      }
    });
  });
}

function popularTable(listProdutos) {
  $("#itensVenda").DataTable().clear().draw();
  $.each(listProdutos, function (index, produto) {
    let valorTotal = produto.quantidade * produto.valorVenda;
    $("#itensVenda").DataTable().row.add([
      produto.descricaoProduto,
      produto.quantidade,
      formatter.format(produto.valorVenda),
      formatter.format(valorTotal)
    ]).draw(false);
  });
}

function initDataTable() {
  $("#itensVenda").DataTable({
    paginate: false,
    lengthChange: false,
    info: false,
    autoWidth: false,
    filter: false,
    responsive: true,
    language: {
      url: `${CONTEXT}vendor/internationalisation/pt_br.json`
    },
    columnDefs: [
      {
        targets: [2],
        className: 'text-center'
      },
      {
        targets: [3],
        className: 'text-center'
      }
    ]
  });
}