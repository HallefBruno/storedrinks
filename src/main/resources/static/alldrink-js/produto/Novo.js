/* global Swal */

$(document).ready(function () {
    $("#valorCusto").mask("#,##0.00", {reverse: true});
    $("#valorVenda").mask("#,##0.00", {reverse: true});
    $("#codigoBarra").focus();
    
    $("form").submit(function (event) {
        if($("#valorCusto").val() && $("#valorVenda").val()) {
            if($("#valorCusto").val().length > $("#valorVenda").val().length) {
                mensagem("warning","O valor de custo precisa ser menor que o valor de venda!");
                return false;
            }
            $("#valorCusto").val(removeMask($("#valorCusto").val()));
            $("#valorVenda").val(removeMask($("#valorVenda").val()));
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