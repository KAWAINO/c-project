$(document).ready(function(){

	// 추가
     $("#btnAdd").bind("click", function() {
        addDport();
     });

	// 삭제
	$("#btnDel").bind("click", function() {
  		delDport();
  	});

});

function addDport() {
	
    if ('' == $("#add_from").val()) {
        alert(startPort);
        $("#add_from").focus();
        return;
    }
    
    if ('' == $("#add_to").val()) {
        alert(endPort);
        $("#add_to").focus();
        return;
    }

    if (!/^\d+$/.test($("#add_from").val()) || !/^\d+$/.test($("#add_to").val())) {
        alert(allowedPort);
        $("#add_from").focus();
        return;
    }
    if ($("#add_from").val() > $("#add_to").val()) {
        alert(againPort);
        $("#add_from").focus();
        return;
    }

    if(65535 < $("#add_to").val() || 65535 < $("#add_from").val() || $("#add_to").val() < 0 || $("#add_from").val() < 0){
      alert(portRange);
        $("#add_from").focus();
        return;
    }
    var datas = $("#addDportForm").serialize();
    $('.contents-area').append('<div class="ajax-loading"></div>');

    $.ajax({
        type: "POST",
        url: "/dport/dportAdd.ajax",
        data: "ajax=true&" + datas,
        dataType: "json",
        error: function (e) {
            alert(error);
        },
        success: function (data) {
         $('.ajax-loading').remove();
            if (data.result == "1") {
                alert(success);
                document.location.reload();
            } else if (data.result == "-2") {
                alert(portValue);
            } else {
                alert(error);
            }
        }
    });
}



