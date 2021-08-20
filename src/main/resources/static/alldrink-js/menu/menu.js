$(document).ready(function () {
    $('a').click(function (e) {
        $("#divLoading").addClass("loading");        
    });
    
    $("button[type='submit']").click(function (e) {
        $("#divLoading").addClass("loading");
    });
});

//setTimeout(function () {
//  $("#divLoading").removeClass("loading");
//}, 1000);