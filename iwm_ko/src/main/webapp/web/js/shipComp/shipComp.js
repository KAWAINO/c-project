
  $(document).ready(function(){

            //추가 modal 닫을시 초기화
              $('#addModal').on('hidden.bs.modal', function (e) {
                  $('#addShipCompForm')[0].reset();
              });


            // 검색
                $("#btnSearch").on("click", function() {
                     $("#pageno").val(1);
                    $("#searchShipCompForm").attr("action", "/shipcomp/shipcomp.do");
                    $("#searchShipCompForm").submit();
                });

           // 추가
            $("#btnAdd").bind("click", function() {
                addShipComp();
            });

                // 수정
                $("#btnUpd").bind("click", function() {
                    updateShipComp();
                });
            // 삭제
                $("#btnDel").bind("click", function() {
                    delShipComp();
                });

         // excel
            $("#btnExcel").bind("click", function() {
                goExcelDown();
            });

  });

  function addShipComp(){


                  if ('' == $("#addCompName").val()) {
                      alert(shipOwnerName);
                      $("#addCompName").focus();
                      return;
                  }
                  if ('' == $("#addCompCnt").val()) {
                      alert(numberOfShips);
                      $(numberOfShips).focus();
                      return;
                  }

 $('.contents-area').append('<div class="ajax-loading"></div>');
            var datas = $("#addShipCompForm").serialize();
            $.ajax({
                type : "POST",
                url : "/shipcomp/shipCompAdd.ajax",
                data : "ajax=true&" + datas,
                dataType : "json",
                error	: function (e) { alert(error); },
                success : function(data) {
                    $('.ajax-loading').remove();
                    if(data.result == "1") {
                        alert(success);
                        document.location.reload();
                    } else if(data.result == "-2") {
                        alert(shipOwnerNameAgain);
                    }else {
                        alert(error);
                    }
                }
            });
            return;
        }

    function goUpdate(comp_id){
                    $.ajax({
                        type : "POST",
                        url : "/shipcomp/shipCompSetUpdateData.ajax",
                        data : "ajax=true&comp_id=" + comp_id,
                        dataType : "json",
                        error	: function (e) { alert(error); },
                        success : function(data) {
                            $('#updCompId').val(data.shipCompVo.comp_id);
                            $('#u_updCompName').val(data.shipCompVo.comp_name);
                            $('#updCompName').val(data.shipCompVo.comp_name);
                            $('#updCompHold').val(data.shipCompVo.ship_hold);
                            $('#updDescr').val(data.shipCompVo.descr);
                            $('#chkDel').val(data.shipCompVo.chkDel);

                            $("#updateModal").modal("show");
                        }
                    });
                }


         function updateShipComp(){


                  if ('' == $("updCompName").val()) {
                      alert(shipOwnerName);
                      $("#updCompName").focus();
                      return;
                  }
                  if ('' == $("#updCompHold").val()) {
                      alert(numberOfShips);
                      $("#updCompHold").focus();
                      return;
                  }

 $('.contents-area').append('<div class="ajax-loading"></div>');
                        var datas = $("#updShipCompForm").serialize();
                        $.ajax({
                            type : "POST",
                            url : "/shipcomp/shipCompUpdate.ajax",
                            data : "ajax=true&" + datas,
                            dataType : "json",
                            error	: function (e) { alert(error); },
                            success : function(data) {
                                if(data.result == "1") {
                                    alert(success);
                                    document.location.reload();
                                }else if(data.result == "-2") {
                                     alert(shipOwnerNameAgain);
                                 }else {
                                     alert(error);
                                 }
                            }
                        });
                          $('.ajax-loading').remove();
                        return;
                    }

            function delShipComp(){
                var compName = $('#u_updCompName').val();
                var comp_id = $('#updCompId').val();
                var chkDel = $('#chkDel').val();

                if(chkDel != "OK"){
                    alert(chkDel);
                    return;
                } else if(!confirm(compName + " " + deleteShipOwner))
                {
                    return;
                }


                $.ajax({
                    type : "POST",
                    url : "/shipcomp/shipCompDelete.ajax",
                    data : "ajax=true&comp_name=" + compName,
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

      // 엑셀 다운로드
      function goExcelDown() {
          var excel_msg = prompt(downloadExcelMessage);

          if (!excel_msg) {
              alert(downloadCanceledMessage);
              return;
          }

          var form = $("#searchShipCompForm");

          var input = $('<input>')
              .attr('type', 'hidden')
              .attr('name', 'excel_msg')
              .val(excel_msg);
          form.append(input);

          form.attr("action", "/shipcomp/shipCompExcelDownload.do");
          form.submit();
      }



        //paging
        function goPage(pageno) {
            $("#pageno").val(pageno);
            $("#searchShipCompForm").attr("action", "/shipcomp/shipcomp.do");
            $("#searchShipCompForm").submit();
        }