
  $(document).ready(function(){

            //추가 modal 닫을시 초기화
              $('#addModal').on('hidden.bs.modal', function (e) {
                  $('#addShipInfoForm')[0].reset();
              });

           // 검색
                $("#btnSearch").on("click", function() {
                     $("#pageno").val(1);
                    $("#searchShipInfoForm").attr("action", "/shipinfo/shipinfo.do");
                    $("#searchShipInfoForm").submit();
                });


              // 추가
                 $("#btnAdd").bind("click", function() {
                     addShipInfo();
                 });


                // 수정
                $("#btnUpd").bind("click", function() {
                    updateShipInfo();
                });

              // 삭제
                  $("#btnDel").bind("click", function() {
                      delShipInfo();
                  });

             // excel
                $("#btnExcel").bind("click", function() {
                    goExcelDown();
                });


             });



            //paging
            function goPage(pageno) {
                $("#pageno").val(pageno);
                $("#searchShipInfoForm").attr("action", "/shipinfo/shipinfo.do");
                $("#searchShipInfoForm").submit();
            }

             function addShipInfo(){

                  if ('' == $("addCode").val()) {
                      alert(enterShipCode);
                      $("#addCode").focus();
                      return;
                  }
                  if ('' == $("#addName").val()) {
                      alert(enterShipName);
                      $("#addName").focus();
                      return;
                  }
                  if(!/^\d+$/.test($("#addCode").val())){
                      alert(wrongShipCode);
                      $("#addCode").focus();
                      return;
                  }
                if ($("#addCode").val().length !== 5) {
                    alert(wrongShipCodeLength);
                    $("#addCode").focus();
                    return;
                }
                if ($("#addCompId").val() === 'select') {
                    alert(selectShipOwner);
                    $("#addCompId").focus();
                    return;
                }
                        var datas = $("#addShipInfoForm").serialize();
                        $.ajax({
                            type : "POST",
                            url : "/shipinfo/shipInfoAdd.ajax",
                            data : "ajax=true&" + datas,
                            dataType : "json",
                            error	: function (e) { alert(error); },
                            success : function(data) {
                                if(data.result == "1") {
                                    alert(success);
                                    document.location.reload();
                                } else if(data.result == "-2") {
                                    alert(existsShipCode);
                                }else if(data.result == "3"){
                                    alert(existsShipName)
                                }else if(data.result == "4"){
                                    alert(existsShipCode)
                                }
                                else {
                                    alert(error);
                                }
                            }
                        });
                        return;
                    }


    function goUpdate(s_code){
                    $.ajax({
                        type : "POST",
                        url : "/shipinfo/shipInfoSetUpdateData.ajax",
                        data : "ajax=true&s_code=" + s_code,
                        dataType : "json",
                        error	: function (e) { alert(error); },
                        success : function(data) {
                            $('#displayUpdCode').text(data.shipInfoVo.s_code);
                            $('#updCode').val(data.shipInfoVo.s_code);
                            $('#u_updCode').val(data.shipInfoVo.s_code);
                            $('#updName').val(data.shipInfoVo.s_name);
                            $('#u_updName').val(data.shipInfoVo.s_name);
                            $('#updCompName').val(data.shipInfoVo.comp_name);
                            $('#updDescr').val(data.shipInfoVo.descr);
                            $('#u_updDescr').val(data.shipInfoVo.descr);
                            $('#chkDel').val(data.shipInfoVo.chkDel);


                            $('#updCompId').val(data.shipInfoVo.comp_id);


                            $("#updateModal").modal("show");
                        }
                    });
                }


         function updateShipInfo(){
                          if ('' == $("#updName").val()) {
                              alert(enterShipName);
                              $("#updName").focus();
                              return;
                          }
                        if ($("#updCompId").val() === 'select') {
                            alert(selectShipOwner);
                            $("#updCompId").focus();
                            return;
                        }

                        var datas = $("#updShipInfoForm").serialize();
                        $.ajax({
                            type : "POST",
                            url : "/shipinfo/shipInfoUpdate.ajax",
                            data : "ajax=true&" + datas,
                            dataType : "json",
                            error	: function (e) { alert(error); },
                            success : function(data) {
                                if(data.result == "1") {
                                    alert(success);
                                    document.location.reload();
                                }else if(data.result == "3"){
                                   alert(existsShipName);
                                }
                                else {
                                    alert(error);
                                }
                            }
                        });
                        return;
                    }


            function delShipInfo() {
                var s_code = $('#u_updCode').val();
                var chkDel = $('#chkDel').val();

                if(chkDel != "OK" && !confirm(chkDel + deleteActiveManagement )) {
                    return;
                }

                if(!confirm(s_code + " " + deleteShip)) {
                    return;
                }

                // 삭제 작업 진행
                $.ajax({
                    type : "POST",
                    url : "/shipinfo/shipInfoDelete.ajax",
                    data : "ajax=true&s_code=" + s_code,
                    dataType : "json",
                    error : function(e) {
                        alert(error);
                    },
                    success : function(data) {
                        if(data.result == "1") {
                            alert(del);
                            document.location.reload();
                        } else {
                            alert(error);
                        }
                    }
                });
            }



         // 엑셀 다운로드
          function goExcelDown() {
              var excel_msg = prompt(downloadExcelMessage);

              if (!excel_msg) {
                  alert(downloadCanceledMessage);
                  return;
              }

              var form = $("#searchShipInfoForm");

              var input = $('<input>')
                  .attr('type', 'hidden')
                  .attr('name', 'excel_msg')
                  .val(excel_msg);
              form.append(input);

              form.attr("action", "/shipinfo/shipInfoExcelDownload.do");
              form.submit();
          }

