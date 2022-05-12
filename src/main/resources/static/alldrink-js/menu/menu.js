$(document).ready(function () {
  $('a').click(function (e) {
    if (!$(this).is(".link-calculadora") && ($(this).attr("target") === undefined)) {
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
  
  $("li.nav-item a.nav-link").mouseover(function () {
    $(this).prop("style","color: #56008f; background: #f5f5f5; font-weight: 500");
  });
  
  $("li.nav-item a.nav-link").mouseout(function () {
    $(this).attr("style","");
  });
  
});