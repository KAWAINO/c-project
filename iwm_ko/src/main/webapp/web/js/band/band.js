
$(document).ready(function(){
	//추가 modal 닫을시 초기화
  	$('#addModal').on('hidden.bs.modal', function (e) {
  		$('#addBandForm')[0].reset();
  	});

  	// 추가
 	$("#btnAdd").bind("click", function() {
		addBand();
 	});
});

function addBand() {
    if ('' == $("#add_band").val()) {
        alert(noBandValue);
        $("#add_band").focus();
        return;
    }

    if (!/^\d+$/.test($("#add_band").val())) {
        alert(bandValueNumber);
        $("#add_band").focus();
        return;
    }

    // 유효성 검사를 통과했을 때만 실행될 코드
    var datas = $("#addBandForm").serialize();

    $.ajax({
        type: "POST",
        url: "/band/bandAdd.ajax",
        data: "ajax=true&" + datas,
        dataType: "json",
        error: function (e) {
            alert(error);
        },
        success: function (data) {
            if (data.result == "1") {
                alert(success);
                document.location.reload();
            } else if (data.result == "-2") {
                alert(bandValue);
            } else {
                alert(error);
            }
        }
    });
}


