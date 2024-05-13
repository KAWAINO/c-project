
$(document).ready(function(){

	//추가 modal 닫을시 초기화
    $('#addModal').on('hidden.bs.modal', function (e) {
    	$('#addConfForm')[0].reset();
    });
    
	// 검색
    $("#btnSearch").on("click", function() {
        $("#pageno").val(1);
        $('.contents-area').append('<div class="ajax-loading"></div>');
        $("#searchConfForm").attr("action", "/conf/conf.do");
        $("#searchConfForm").submit();
    });

    // 추가
    $("#btnAdd").bind("click", function() {
    	addConf();
 	});

 	// 수정
    $("#btnUpd").bind("click", function() {
    	updateConf();
    });

  	// 삭제
  	$("#btnDel").bind("click", function() {
  		delConf();
  	});

  	// excel
    $("#btnExcel").bind("click", function() {
        goExcelDown();
    });

});

//등록
function addConf() {
	if ('' == $("#add_conf").val()) {
	    alert(configurationName);
	    $("#add_conf").focus();
	    return;
	}
    if ('' == $("#add_val").val()) {
        alert(value);
        $("#add_val").focus();
        return;
    }
  	var datas = $("#addConfForm").serialize();
    $('.contents-area').append('<div class="ajax-loading"></div>');

     $.ajax({
         type: "POST",
         url: "/conf/confAdd.ajax",
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
                 alert(configuration);
             } else {
                 alert(error);
             }
         }
     });
     return;
 }

//삭제
function delConf(){

    var upd_conf = $('#u_upd_conf').val();
    if(!confirm(upd_conf + deleteConfiguration)){
        return;
    }

    $.ajax({
        type : "POST",
        url : "/conf/confDelete.ajax",
        data : "ajax=true&u_upd_conf=" + upd_conf,
        dataType : "json",
        error	: function (e) { alert(error); },
        success : function(data) {
            if(data.result == "1") {
                alert(del);
                document.location.reload();
            }else {
                alert(error);
            }
        }
    });
}

//paging
function goPage(pageno) {
    $("#pageno").val(pageno);
    $("#searchConfForm").attr("action", "/conf/conf.do");
    $("#searchConfForm").submit();
}

//수정 페이지 이동
 function goUpdate(conf_name){
    $.ajax({
        type : "POST",
        url : "/conf/confSetUpdateData.ajax",
        data : "ajax=true&conf_name=" + conf_name ,
        dataType : "json",
        error	: function (e) { alert(error); },
        success : function(data) {

            $('#upd_conf').text(data.confVo.conf_name);
            $('#u_upd_conf').val(data.confVo.conf_name);
            $('#upd_val').val(data.confVo.val);
            $('#upd_descr').val(data.confVo.descr);

            $("#updateModal").modal("show");
        }
    });
 }

    //수정
    function updateConf() {
        var upd_val = $('#upd_val').val();
        var upd_val = $('#u_upd_conf').val();


        if ('' == $("#upd_val").val()) {
            alert(value);
            $("#upd_val").focus();
            return;
        }


      var datas = $("#updConfForm").serialize();
        $('.contents-area').append('<div class="ajax-loading"></div>');
        $.ajax({
            type: "POST",
            url: "/conf/confUpdate.ajax",
            data: "ajax=true&" + datas,
            dataType: "json",
            error: function (e) {
                alert(error);
            },
            success: function(data) {
                 $('.ajax-loading').remove();
                if(data.result == "1") {
                    alert(success);
                    document.location.reload();
                } else {
                    alert(error);
                }
            }
        });
        return;
    }

   // 엑셀 다운로드
      function goExcelDown() {
	
		  var excel_msg = prompt(downloadExcelMessage);

          if (!excel_msg) {
              alert(downloadCanceledMessage);
              return;
          }

          var form = $("#searchConfForm");

          var input = $('<input>')
              .attr('type', 'hidden')
              .attr('name', 'excel_msg')
              .val(excel_msg);
          form.append(input);

          form.attr("action", "/conf/confExcelDownload.do");
          form.submit();
      }
