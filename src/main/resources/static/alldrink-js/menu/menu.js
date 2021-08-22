$(document).ready(function () {
    $('a').click(function (e) {
        $("#divLoading").addClass("loading");        
    });
    
    $("button[type='submit']").click(function (e) {
        if(!$("form").get(0).checkValidity()) {
            $("#divLoading").removeClass("loading");
        } else {
            $("#divLoading").addClass("loading");
        }
    });
});

//setTimeout(function () {
//  $("#divLoading").removeClass("loading");
//}, 1000);