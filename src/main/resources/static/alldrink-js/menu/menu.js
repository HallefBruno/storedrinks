$(document).ready(function () {
  $('a').click(function (e) {
    if (!$(this).is(".link-calculadora") && ($(this).attr("target") === undefined) && $(this).is(".dropdown-item")) {
      $("#divLoading").addClass("loading");
    }
    if($(this).hasClass("invock-gif")) {
      $("#divLoading").addClass("loading");
    }
  });

  $("button[type='submit']").click(function (e) {
    if (!$("form").get(0).checkValidity()) {
      $("#divLoading").removeClass("loading");
    } else {
      $("#divLoading").addClass("loading");
    }
  });
  
});
