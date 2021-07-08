$(function () {
    $("#produtos").select2({
        theme: "bootstrap-5"
    });
    $("#produtos").on("select2:select", function (e) {
        $.get($("#contextApp").val()+"/entradas/buscar/"+e.params.data.id, function (data) {
            console.log(data);
        });
    });
    
    

    $("#situacao").select2({
        theme: "bootstrap-5"
    });
});