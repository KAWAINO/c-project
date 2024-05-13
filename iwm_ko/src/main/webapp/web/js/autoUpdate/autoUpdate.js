
      $(document).ready(function(){

              //추가 modal 닫을시 초기화
                          $('#addModal').on('hidden.bs.modal', function (e) {
                              $('#addAutoUpdateForm')[0].reset();
                          });


              // 추가
                     $("#btnAdd").bind("click", function() {
                        addAutoUpdate();
                     });

              // 수정
                $("#btnUpd").bind("click", function() {
                    updateAutoUpdate();
                });


              // 삭제
                  $("#btnDel").bind("click", function() {
                      delAutoUpdate();
                  });


         });

        //등록
         function addAutoUpdate() {

                if ('' == $("#add_name").val()) {
                    alert("이름을 입력하여주세요.");
                    $("#add_name").focus();
                    return;
                }
                if ('' == $("#add_svr_loc").val()) {
                    alert("파일위치(서버)를 입력하여주세요.");
                    $("#add_svr_loc").focus();
                    return;
                }
                if ('' == $("#add_local_loc").val()) {
                    alert("파일위치(로컬)를 입력하여주세요.");
                    $("#add_local_loc").focus();
                    return;
                }      if ('' == $("#add_down_loc").val()) {
                    alert("다운로드 위치를 입력하여주세요.");
                    $("#add_down_loc").focus();
                    return;
                }

                  var datas = $("#addAutoUpdateForm").serialize();
                   $('.contents-area').append('<div class="ajax-loading"></div>');



                 $.ajax({
                     type: "POST",
                     url: "/autoUpdate/autoUpdateAdd.ajax",
                     data: "ajax=true&" + datas,
                     dataType: "json",
                     error: function (e) {
                         alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
                     },
                     success: function (data) {
                      $('.ajax-loading').remove();
                         if (data.result == "1") {
                             alert("정상적으로 추가되었습니다.");
                             document.location.reload();
                         } else if (data.result == "-2") {
                             alert("해당 이름이 존재합니다. 다시 입력하여주세요.");
                         } else {
                             alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
                         }
                     }
                 });
                 return;
             }


        //삭제
        function delAutoUpdate(){

            var p_name = $('#upd_name').val();
            if(!confirm(p_name + "(이)가 삭제됩니다. \n해당 자동 업데이트 관리를 정말 삭제 하시겠습니까?")){
                return;
            }
            $.ajax({
                type : "POST",
                url : "/autoUpdate/autoUpdateDelete.ajax",
                data : "ajax=true&p_name=" + p_name,
                dataType : "json",
                error	: function (e) { alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다."); },
                success : function(data) {
                    if(data.result == "1") {
                        alert("정상적으로 삭제되었습니다.");
                        document.location.reload();
                    }else {
                        alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
                    }
                }
            });
        }

        //paging
        function goPage(pageno) {
          $('.contents-area').append('<div class="ajax-loading"></div>');
            $("#pageno").val(pageno);
            $("#searchConfForm").attr("action", "/conf/conf.do");
            $("#searchConfForm").submit();
        }





        //수정 페이지 이동
         function goUpdate(seq){
                            $.ajax({
                                type : "POST",
                                url : "/autoUpdate/autoUpdateSetUpdateData.ajax",
                                data : "ajax=true&seq=" + seq ,
                                dataType : "json",
                                error	: function (e) { alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다."); },
                                success : function(data) {

                                    $('#upd_name').val(data.autoUpdateVo.p_name);
                                    $('#u_upd_name').text(data.autoUpdateVo.p_name);
                                    $('#upd_version').val(data.autoUpdateVo.version);
                                    $('#upd_file_loc').val(data.autoUpdateVo.file_loc);
                                    $('#upd_local_loc').val(data.autoUpdateVo.local_loc);
                                    $('#upd_down_loc').val(data.autoUpdateVo.down_loc);
                                    $('#upd_svr_proc_flag').val(data.autoUpdateVo.svr_proc_flag);
                                    $('#upd_download_flag').val(data.autoUpdateVo.download_flag);
                                    $('#upd_down_file_svr').val(data.autoUpdateVo.down_file_svr);
                                    $('#upd_down_file_cli').val(data.autoUpdateVo.down_file_cli);
                                    $('#upd_down_file_name').val(data.autoUpdateVo.down_file_name);
                                    $('#upd_cmd_flag').val(data.autoUpdateVo.cmd_flag);
                                    $('#upd_cmd_str').val(data.autoUpdateVo.cmd_str);
                                    $('#upd_script_flag').val(data.autoUpdateVo.script_flag);
                                    $('#upd_script_file').val(data.autoUpdateVo.script_file);

                                    $("#updateModal").modal("show");
                                }
                            });
               }

        //수정
        function updateAutoUpdate() {

             if ('' == $("#upd_svr_loc").val()) {
                    alert("파일위치(서버)를 입력하여주세요.");
                    $("#upd_svr_loc").focus();
                    return;
             }
             if ('' == $("#upd_local_loc").val()) {
                alert("파일위치(로컬)를 입력하여주세요.");
                $("#upd_local_loc").focus();
                return;
             }      if ('' == $("#upd_down_loc").val()) {
                alert("다운로드 위치를 입력하여주세요.");
                $("#upd_down_loc").focus();
                return;
             }


          var datas = $("#updAutoUpdateForm").serialize();
            $('.contents-area').append('<div class="ajax-loading"></div>');

            $.ajax({
                type: "POST",
                url: "/autoUpdate/autoUpdateUpdate.ajax",
                data: "ajax=true&" + datas,
                dataType: "json",
                error: function (e) {
                    alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
                },
                success: function(data) {
                 $('.ajax-loading').remove();
                    if(data.result == "1") {
                        alert("정상적으로 수정되었습니다.");
                        document.location.reload();
                    } else {
                        alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
                    }
                }
            });
            return;
        }

        //paging
        function goPage(pageno) {
            $("#pageno").val(pageno);
            $("#searchAutoUpdateForm").attr("action", "/autoUpdate/autoUpdate.do");
            $("#searchAutoUpdateForm").submit();
        }
