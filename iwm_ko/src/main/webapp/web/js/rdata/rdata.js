
          $(document).ready(function(){

              //추가 modal 닫을시 초기화
                          $('#addModal').on('hidden.bs.modal', function (e) {
                              $('#addRdataForm')[0].reset();
                          });
               // 검색
                    $("#btnSearch").on("click", function() {
                         $("#pageno").val(1);
                         $('.contents-area').append('<div class="ajax-loading"></div>');
                        $("#searchRdataForm").attr("action", "/rdata/rdata.do");
                        $("#searchRdataForm").submit();
                    });

              // 추가
                 $("#btnAdd").bind("click", function() {
                     addRdata();
                 });

                // 수정
                $("#btnUpd").bind("click", function() {
                    updateRdata();
                });

                   // 삭제
                  $("#btnDel").bind("click", function() {
                      delRdata();
                  });

                 // excel
                    $("#btnExcel").bind("click", function() {
                        goExcelDown();
                    });

                // 추가 부분 날짜 설정
                   var today = new Date();
                    var dateIn30Days = new Date();
                    dateIn30Days.setDate(today.getDate() + 30);

                    // 날짜를 YYYY-MM-DD 형식으로 변환하는 함수
                    function formatDate(date) {
                        var d = new Date(date),
                            month = '' + (d.getMonth() + 1),
                            day = '' + d.getDate(),
                            year = d.getFullYear();

                        if (month.length < 2)
                            month = '0' + month;
                        if (day.length < 2)
                            day = '0' + day;

                        return [year, month, day].join('-');
                    }

                    // 날짜 입력 필드에 값 설정
                    $('#addDayFrom').val(formatDate(today));
                    $('#addDayTo').val(formatDate(dateIn30Days));


         });


         function addRdata() {
                 var addDayFrom = $('#addDayFrom').val();
                 var addDayTo = $('#addDayTo').val();

                 // 날짜 형식 변경
                 addDayFrom = formatDateToyyyyMMdd(addDayFrom);
                 addDayTo = formatDateToyyyyMMdd(addDayTo);

                 var selectedDays = getSelectedDays();

                 // 폼 데이터 수동 조합
                 var datas = $("#addRdataForm").find("input, select, textarea").not("#addDayFrom, #addDayTo").serialize();
                 datas += "&addDayFrom=" + addDayFrom + "&addDayTo=" + addDayTo + "&addApplyDays=" + selectedDays;

                if ('' == $("#addRateName").val()) {
                    alert(enterDataUsagePolicy);
                    $("#addRateName").focus();
                    return;
                }
                if ('' == $("#addName").val() || 'select' == $("#addName").val()) {
                    alert(selectShip);
                    $("#addName").focus();
                    return;
                }

                if ('' == $("#addMonth").val()) {
                    alert(enterMonthlySupply);
                    $("#addMonth").focus();
                    return;
                }

              if (isNaN($('#addMonth').val())) {
                  alert(wrongMonthlySupply);
                  $('#addMonth').focus();
                  return;
              }


              if (Number($("#addMonth").val()) < 1) {
                  alert(wrongMonthlySupplyUpTo);
                  $("#addMonth").focus();
                  return;
              }

              if (isNaN($('#addUse').val())) {
                  alert(wrongContinuousUse);
                  $("#addUse").focus();
                  return;
              }

              if (isNaN($('#addMin').val())) {
                  alert(wrongContinuousUseTime);
                  $("#addMin").focus();
                  return;
              }

                if (isNaN($('#addDay').val())) {
                    alert(wrongdailyUse);
                    $("#addDay").focus();
                    return;
                }

              if ($('#addDayFrom').val() === '') {
                  alert(selectStartPeriod);
                  return;
              }
              
              if ($('#addDayTo').val() === '') {
                  alert(selectEndPeriod);
                  return;
              }

              var startDate = new Date($('#addDayFrom').val());
              var endDate = new Date($('#addDayTo').val());

              if (startDate > endDate) {
                  alert(wrongPeriodOfUse);
                  $('#addDayFrom').focus();
                  return;
              }


                 $.ajax({
                     type: "POST",
                     url: "/rdata/rdataAdd.ajax",
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
                             alert(existsPolicyName);
                         } else {
                             alert(error);
                         }
                     }
                 });
                 return;
             }



         function goUpdate(s_code,rate_name){
                            $.ajax({
                                type : "POST",
                                url : "/rdata/rdataSetUpdateData.ajax",
                                data : "ajax=true&s_code=" + s_code + "&rate_name=" + rate_name,
                                dataType : "json",
                                error	: function (e) { alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다."); },
                                success : function(data) {
                                    $('#updSCode').val(data.rdataVo.s_code);
                                    $('#updRateName').text(data.rdataVo.rate_name);
                                    $('#u_updRateName').val(data.rdataVo.rate_name);
                                    $('#u_updComp').val(data.rdataVo.comp_name);
                                    $('#updComp').text(data.rdataVo.comp_name);
                                    $('#u_updName').val(data.rdataVo.s_name);
                                    $('#updName').text(data.rdataVo.s_name);
                                    $('#updMonth').val(data.rdataVo.amt_total_month);
                                    $('#updDay').val(data.rdataVo.limit_day_amt);
                                    $('#updUse').val(data.rdataVo.limit_cont_amt);
                                    $('#updMin').val(data.rdataVo.limit_cont_time);
                                    $('#updTimeFrom').val(data.rdataVo.time_from);
                                    $('#updTimeTo').val(data.rdataVo.time_to);
                                    $('#updTimeFrom2').val(data.rdataVo.time_from2);
                                    $('#updTimeTo2').val(data.rdataVo.time_to2);
                                    $('#updTimeFrom3').val(data.rdataVo.time_from3);
                                    $('#updTimeTo3').val(data.rdataVo.time_to3);
                                    $('#updDayFrom').val(formatDateString(data.rdataVo.day_from));
                                    $('#updDayTo').val(formatDateString(data.rdataVo.day_to));
                                    $('#chkDel').val(data.rdataVo.chKDel);
                                    updateApplyDays(data.rdataVo.apply_day);



                                    $('#rate_name').val(data.rdataVo.rate_name);
                                    $('#amt_total_month').val(data.rdataVo.amt_total_month);
                                    $('#limit_day_amt').val(data.rdataVo.limit_day_amt);
                                    $('#limit_cont_amt').val(data.rdataVo.limit_cont_amt);
                                    $('#limit_cont_time').val(data.rdataVo.limit_cont_time);
                                    $('#time_from').val(data.rdataVo.time_from);
                                    $('#time_from2').val(data.rdataVo.time_from2);
                                    $('#time_from3').val(data.rdataVo.time_from3);
                                    $('#time_to').val(data.rdataVo.time_to);
                                    $('#time_to2').val(data.rdataVo.time_to2);
                                    $('#time_to3').val(data.rdataVo.time_to3);
                                    $('#apply_day').val(data.rdataVo.apply_day);
                                    $('#day_from').val(data.rdataVo.day_from);
                                    $('#day_to').val(data.rdataVo.day_to);

                                    $("#updateModal").modal("show");
                                }
                            });
               }



            function updateRdata() {
                var updDayFrom = $('#updDayFrom').val();
                var updDayTo = $('#updDayTo').val();

                updDayFrom = formatDateToyyyyMMdd(updDayFrom);
                updDayTo = formatDateToyyyyMMdd(updDayTo);

                var selectedDays = getSelectedUpdateDays();

                 // 폼 데이터 수동 조합
                 var datas = $("#updRdataForm").find("input, select, textarea").not("#updDayFrom, #updDayTo").serialize();
                 datas += "&updDayFrom=" + updDayFrom + "&updDayTo=" + updDayTo + "&updApplyDays=" + selectedDays;


                if ('' == $("#updMonth").val()) {
                    alert(enterMonthlySupply);
                    $("#updMonth").focus();
                    return;
                }

              if (isNaN($('#updMonth').val())) {
                  alert(wrongMonthlySupply);
                  $('#updMonth').focus();
                  return;
              }


              if (Number($("#updMonth").val()) < 1) {
                  alert(wrongMonthlySupplyUpTo);
                  $("#updMonth").focus();
                  return;
              }

                if (isNaN($('#updDay').val())) {
                    alert(wrongdailyUse);
                    $("#updDay").focus();
                    return;
                }
              if (isNaN($('#updUse').val())) {
                  alert(wrongContinuousUse);
                  $("#updUse").focus();
                  return;
              }

              if (isNaN($('#updMin').val())) {
                  alert(wrongContinuousUseTime);
                  $("#updMin").focus();
                  return;
              }

              if ($('#updDayFrom').val() === '') {
                  alert(selectStartPeriod);
                  return;
              }
              if ($('#updDayTo').val() === '') {
                  alert(selectEndPeriod);
                  return;
              }

              var startDate = new Date($('#updDayFrom').val());
              var endDate = new Date($('#updDayTo').val());

              if (startDate > endDate) {
                  alert(wrongPeriodOfUse);
                  $('#updDayFrom').focus();
                  return;
              }
                $.ajax({
                    type: "POST",
                    url: "/rdata/rdataUpdate.ajax",
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



            function updateShipNames(compId) {
                if(compId === 'select') {
                    $('#addName').html('<option value="select">' + select + '</option>');
                    return;
                }

                // AJAX 요청
                $.ajax({
                    type: "POST",
                    url: "/rdata/compList.ajax",
                    data: { compId: compId },
                    dataType: "json",
                    error: function() {
                        alert(error);
                    },
                    success: function(data) {
                        var shipSelect = $('#addName');
                        shipSelect.empty();
                        shipSelect.append('<option value="select">' + select + '</option>');

                         data.shipNameList.forEach(function(ship) {
                            shipSelect.append('<option value="' + ship.s_code + '">' + ship.ship_name + '</option>');
                        });
                    }
                });
            }


                function delRdata(){

                    var s_code = $('#updSCode').val();
                    var rate_name = $('#u_updRateName').val();
                    var chkDel = $('#chkDel').val();

                    if(chkDel != "OK"){
                        alert(chkDel);
                        return;
                    }

                    if(!confirm(rate_name + " " + deleteDataUsagePolicy)){
                        return;
                    }


                    $.ajax({
                        type : "POST",
                        url : "/rdata/rdataDelete.ajax",
                        data : "ajax=true&s_code=" + s_code + "&rate_name=" + rate_name,
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
            $("#searchRdataForm").attr("action", "/rdata/rdata.do");
            $("#searchRdataForm").submit();
        }

             function formatDateToyyyyMMdd(dateString) {
                 return dateString.replace(/-/g, "");
             }


           function getSelectedDays() {
               var selectedDays = ',';
               $('input[name="add_apply_day"]:checked').each(function () {
                   selectedDays += $(this).val() + ',';
               });
               return selectedDays;
           }

           function getSelectedUpdateDays() {
                var selectedDays = ',';
                $('input[name="upd_apply_day"]:checked').each(function () {
                    selectedDays += $(this).val() + ',';
                });
                return selectedDays;
            }
        // 요일 출력 숫자 -> 한글변경
            function days(applyDayStr) {
                if (!applyDayStr || applyDayStr.split(',').every(s => s === '')) {
                    return noDayOfUse;
                }

                const dayNames = [sun, mon, tue, wed, thu, fri, sat];
                return applyDayStr.split(',')
                                 .filter(day => day)
                                 .map(day => dayNames[parseInt(day, 10) - 1])
                                 .join(',');
            }

        function formatDateString(dateString) {
            return dateString.substring(0, 4) + '-' + dateString.substring(4, 6) + '-' + dateString.substring(6, 8);
        }

        document.addEventListener('DOMContentLoaded', function() {
            var applyDaysElements = document.querySelectorAll('[id^="applyDay_"]');
            applyDaysElements.forEach(function(element) {
                var applyDayText = days(element.innerHTML);
                element.innerHTML = applyDayText;
            });

            // 날짜 변환
            var dateElements = document.querySelectorAll('[id^="dateRange_"]');
            dateElements.forEach(function(element) {
                var dateRange = element.innerHTML.split(' ~ ');
                if(dateRange.length === 2) {
                    var formattedDateFrom = formatDateString(dateRange[0]);
                    var formattedDateTo = formatDateString(dateRange[1]);
                    element.innerHTML = formattedDateFrom + ' ~ ' + formattedDateTo;
                }
            });
        });

            function updateApplyDays(applyDayStr) {
                $('input[name="upd_apply_day"]').prop('checked', false);

                var days = applyDayStr.split(',').filter(function(day) { return day.trim() !== ""; });

                days.forEach(function(day) {
                    $('#upd_apply_day' + day).prop('checked', true);
                });
            }


       // 엑셀 다운로드
          function goExcelDown() {
              var excel_msg = prompt(downloadExcelMessage);

              if (!excel_msg) {
                  alert(downloadCanceledMessage);
                  return;
              }

              var form = $("#searchRdataForm");

              var input = $('<input>')
                  .attr('type', 'hidden')
                  .attr('name', 'excel_msg')
                  .val(excel_msg);
              form.append(input);

              form.attr("action", "/rdata/rdataExcelDownload.do");
              form.submit();
          }
