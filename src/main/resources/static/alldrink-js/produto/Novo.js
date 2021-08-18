/* global Swal, StoreDrink */

$(document).ready(function () {
    
    var mensagem = new StoreDrink.Mensagem();//.show("warning","Por favor conferir as datas!");
    var rm = new StoreDrink.RemoveMask();
    
    $("#valorCusto").mask("#,##0.00", {reverse: true});
    $("#valorVenda").mask("#,##0.00", {reverse: true});
    $("#codigoBarra").focus();
    
    $("form").submit(function (event) {
        if($("#valorCusto").val() && $("#valorVenda").val()) {
            if($("#valorCusto").val().length > $("#valorVenda").val().length) {
                //mensagem.
                //mensagem("warning","O valor de custo precisa ser menor que o valor de venda!");
                mensagem.show("warning","O valor de custo precisa ser menor que o valor de venda!");
                return false;
            }
            $("#valorCusto").val(rm.remover($("#valorCusto").val()));
            $("#valorVenda").val(rm.remover($("#valorVenda").val()));
            return true;
        }
    });
    
});

function removeMask(value) {
    if(value.length >= 8) {
        value = value.replace(/[^0-9.-]+/g,"");
        if(!value.includes(".")) {
            var val = value.substring(0,value.length-2)+"."+value.substring(value.length-2,value.length);
            return Number(val);
        }
    }
    return value;
}

function mensagem(icon,mensagem) {
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
}