
      $(document).ready(function(){
	
	//console.log($('#addAllComp').val());

              //추가 modal 닫을시 초기화
                          $('#addModal').on('hidden.bs.modal', function (e) {
                              $('#addCrewInfoForm')[0].reset();
                          });
              // 검색
                    $("#btnSearch").on("click", function() {
                         $("#pageno").val(1);
                         $("#searchBtn").val("true");
                        $("#searchCrewInfoForm").attr("action", "/crewinfo/crewinfo.do");
                         $('.contents-area').append('<div class="ajax-loading"></div>');

                        $("#searchCrewInfoForm").submit();
                    });

              // 추가
                     $("#btnAdd").bind("click", function() {
                        addCrewInfo();
                     });

              // 수정
                $("#btnUpd").bind("click", function() {
                    updateCrewInfo();
                });

              // 일괄 수정
                $("#btnUpdAll").bind("click", function() {
                    updateCrewInfoAll();
                });

              // 삭제
                  $("#btnDel").bind("click", function() {
                      delCrewInfo();
                  });

              // excel
                $("#btnExcel").bind("click", function() {
                    goExcelDown();
                });

               //비밀번호 변경 체크박스
                $('#changePw').change(function() {
                    if ($(this).is(':checked')) {
                        $('#updPw').prop('disabled', false);
                        $('#updPwChk').prop('disabled', false);
                    } else {
                        $('#updPw').prop('disabled', true);
                        $('#updPwChk').prop('disabled', true);
                    }
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

        //등록
         function addCrewInfo() {
                 var addDayFrom = $('#addDayFrom').val();
                 var addDayTo = $('#addDayTo').val();

                 // 날짜 형식 변경
                 addDayFrom = formatDateToyyyyMMdd(addDayFrom);
                 addDayTo = formatDateToyyyyMMdd(addDayTo);

                 var selectedDays = getSelectedDays();

                 // 폼 데이터 수동 조합
                 var datas = $("#addCrewInfoForm").find("input, select, textarea").not("#addDayFrom, #addDayTo").serialize();
                 datas += "&addDayFrom=" + addDayFrom + "&addDayTo=" + addDayTo + "&addApplyDays=" + selectedDays;

                if ('' == $("#addCrewId").val()) {
                    alert(enterCrewID);
                    $("#addCrewId").focus();
                    return;
                }
                if ('' == $("#addCrewName").val()) {
                    alert(enterCrewName);
                    $("#addCrewName").focus();
                    return;
                }
                if ('' == $("#addPw").val()) {
                    alert(enterPassword);
                    $("#addPw").focus();
                    return;
                }
                if ('' == $("#addPwChk").val()) {
                    alert(enterConfirmPassword);
                    $("#addPwChk").focus();
                    return;
                }
                if ($("#addPwChk").val() !== $("#addPw").val()) {
                    alert(wrongPassword);
                    $("#addPw").focus();
                    return;
                }
                if ('' == $("#addSName").val() || 'select' == $("#addSName").val()) {
                    alert(selectShip);
                    $("#addSName").focus();
                    return;
                }
                if ('' == $("#addRdata").val() || 'select' == $("#addRdata").val()) {
                    alert(selectUsagePolicy);
                    $("#addRdata").focus();
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
              if (isNaN($('#addDay').val())) {
                  alert(wrongDailyUse);
                  $("#addDay").focus();
                  return;
              }
              if (isNaN($('#addMin').val())) {
                  alert(wrongContinuousUseTime);
                  $("#addMin").focus();
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
                     url: "/crewinfo/crewInfoAdd.ajax",
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
                             alert(existsID);
                         } else {
                             alert(error);
                         }
                     }
                 });
                 return;
             }

        //POSSOM 일괄 적용
            function addAllCrew() {
                var cs_code = $('#cs_scode').val();
                var chk_code = /^[0-9]+$/;

                if (cs_code == "") {
                    alert(enterShipCode);
                    return;
                }

                if (cs_code.search(chk_code) == -1) {
                    alert(wrongShipCode);
                    return;
                }

                if (cs_code.length != 5) {
                    alert(wrongShipCodeLength);
                    return;
                }

                if (!confirm(runCrewAddBulk)) {
                    return false;
                }




                 $.ajax({
                            type : "POST",
                            url : "/crewinfo/addall.ajax",
                            data : "ajax=true&cs_code=" + cs_code,
                            dataType : "json",
                            error	: function (e) { alert(error); },
                            success : function(data) {
                             $('.ajax-loading').remove();
                                if(data.result == "1") {
                                    alert(success);
                                    document.location.reload();
                                }else if(data.result =="3"){
                                    alert(wrongAgainShipCode);
                                   document.location.reload();
                                } else {
                                    alert(error);
                                }
                            }
                        });
            }
        //삭제
        function delCrewInfo(){

            var s_code = $('#u_updSCode').val();
            var updCrewId = $('#u_updCrewId').val();
            if(!confirm(updCrewId + " " + deleteCrew)){
                return;
            }

            $.ajax({
                type : "POST",
                url : "/crewinfo/crewInfoDelete.ajax",
                data : "ajax=true&s_code=" + s_code + "&updCrewId=" + updCrewId,
                dataType : "json",
                error	: function (e) { alert(error); },
                success : function(data) {
                    if(data.result == "1") {
                    $('.ajax-loading').remove();
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
          $('.contents-area').append('<div class="ajax-loading"></div>');
            $("#pageno").val(pageno);
            $("#searchCrewInfoForm").attr("action", "/crewinfo/crewinfo.do");
            $("#searchCrewInfoForm").submit();
        }

        //회사 선택시 선박명 list
        function updateShipNames(comp_id) {
            if(comp_id === 'select') {
                $('#addSName').html('<option value="select">' + select + '</option>');
                return;
            }

                // AJAX 요청
                $.ajax({
                    type: "POST",
                    url: "/crewinfo/compList.ajax",
                    data: { comp_id: comp_id },
                    dataType: "json",
                    error: function() {
                        alert(error);
                    },
                    success: function(data) {
                        var shipSelect = $('#addSName');
                        shipSelect.empty();
                        shipSelect.append('<option value="select">' + select + '</option>');

                         data.shipNameList.forEach(function(ship) {
                            shipSelect.append('<option value="' + ship.s_code + '">' + ship.ship_name + '</option>');
                        });
                    }
                });
            }

      //선박 선택시 정책list
      function updateRdataNames(s_name) {
                if(s_name === 'select') {
                    $('#addRdata').html('<option value="select">' + select + '</option>');
                    return;
                }
                // AJAX 요청
                $.ajax({
                    type: "POST",
                    url: "/crewinfo/selectRateNameList.ajax",
                    data: { s_name: s_name },
                    dataType: "json",
                    error: function() {
                        alert(error);
                    },
                    success: function(data) {
                        var dataSelect = $('#addRdata');
                        dataSelect.empty();
                        dataSelect.append('<option value="select">' + select + '</option>');

                         data.rateNameList.forEach(function(rdata) {
                            dataSelect.append('<option value="' + rdata.rate_name + '">' + rdata.rate_name + '</option>');
                        });
                    }
                });
            }

          //rateName 선택시 해당 정보
             function valList(rate_name) {
             var s_code = $('#addSName').val();

                       $.ajax({
                           type: "POST",
                           url: "/crewinfo/valList.ajax",
                           data: { rate_name: rate_name ,s_code:s_code  },
                           dataType: "json",
                           error: function() {
                               alert(error);
                           },
                           success: function(data) {
                            if (data.valList && data.valList.length > 0) {
                            var crewInfoVo = data.valList[0];
                                    $('#addMonth').val(crewInfoVo.amt_total_month);
                                   $('#addDay').val(crewInfoVo.limit_day_amt);
                                   $('#addUse').val(crewInfoVo.limit_cont_amt);
                                   $('#addMin').val(crewInfoVo.limit_cont_time);
                                   $('#addTimeFrom').val(crewInfoVo.time_from);
                                   $('#addTimeTo').val(crewInfoVo.time_to);
                                   $('#addTimeTo2').val(crewInfoVo.time_to2);
                                   $('#addTimeTo3').val(crewInfoVo.time_to3);
                                   $('#addTimeFrom2').val(crewInfoVo.time_from2);
                                   $('#addTimeFrom3').val(crewInfoVo.time_from3);
                                   $('#addDayFrom').val(formatDateString(crewInfoVo.day_from));
                                   $('#addDayTo').val(formatDateString(crewInfoVo.day_to));


                                   addApplyDays(crewInfoVo.apply_day);
                           }
                           }
                       });
                   }


   //rateName 선택시 해당 정보
             function updValList(rate_name) {
             var s_code = $('#u_updSCode').val();

                       $.ajax({
                           type: "POST",
                           url: "/crewinfo/valList.ajax",
                           data: { rate_name: rate_name ,s_code:s_code  },
                           dataType: "json",
                           error: function() {
                               alert(error);
                           },
                           success: function(data) {
                            if (data.valList && data.valList.length > 0) {
                            var crewInfoVo = data.valList[0];
                                    $('#updMonth').val(crewInfoVo.amt_total_month);
                                   $('#updDay').val(crewInfoVo.limit_day_amt);
                                   $('#updUse').val(crewInfoVo.limit_cont_amt);
                                   $('#updMin').val(crewInfoVo.limit_cont_time);
                                   $('#updTimeFrom').val(crewInfoVo.time_from);
                                   $('#updTimeTo').val(crewInfoVo.time_to);
                                   $('#updTimeTo2').val(crewInfoVo.time_to2);
                                   $('#updTimeTo3').val(crewInfoVo.time_to3);
                                   $('#updTimeFrom2').val(crewInfoVo.time_from2);
                                   $('#updTimeFrom3').val(crewInfoVo.time_from3);
                                   $('#updDayFrom').val(formatDateString(crewInfoVo.day_from));
                                   $('#updDayTo').val(formatDateString(crewInfoVo.day_to));


                                   updateApplyDays(crewInfoVo.apply_day);
                           }
                           }
                       });
                   }

       //일괄적용 회사 선택시 선주사 list
        function updateShipNamesAll(comp_id) {
            if(comp_id === 'select') {
                $('#allSCode').html('<option value="select">' + select + '</option>');
                return;
            }

            // AJAX 요청
            $.ajax({
                type: "POST",
                url: "/crewinfo/compList.ajax",
                data: { comp_id: comp_id },
                dataType: "json",
                error: function() {
                    alert(error);
                },
                success: function(data) {
                    var shipSelect = $('#allSCode');
                    shipSelect.empty();
                    shipSelect.append('<option value="select">' + select + '</option>');

                     data.shipNameList.forEach(function(ship) {
                        shipSelect.append('<option value="' + ship.s_code + '">' + ship.ship_name + '</option>');
                    });
                }
            });
        }

    //일괄 적용 선박 선택시 정책list
     function updateRdataNamesAll(s_name) {
         if(s_name === 'select') {
             $('#allRdata').html('<option value="select">' + select + '</option>');
             $('#allCrewId').empty();
             return;
         }

         // AJAX 요청
         $.ajax({
             type: "POST",
             url: "/crewinfo/selectRateNameList.ajax",
             data: { s_name: s_name },
             dataType: "json",
             error: function() {
                 alert(error);
             },

            success: function(data) {
                var dataSelect = $('#allRdata');
                var listContainer = $('#crew_checkbox_list');
                dataSelect.empty();
                dataSelect.append('<option value="select">' + select + '</option>');
                listContainer.empty();

                // 전체 선택 체크박스 추가
                var allSelectCheckbox = '<li><span class="check-box1">' +
                                        '<input type="checkbox" id="allSelect" name="allSelect" onclick="selectAllCrew(this);">' +
                                        '<label for="allSelect">' + selectAll + '</label>' +
                                        '</span></li>';
                listContainer.append(allSelectCheckbox);

                // 다른 체크박스 추가
                data.crewIdList.forEach(function(crew) {
                    var listItem = '<li><span class="check-box1">' +
                                   '<input type="checkbox" id="crew_' + crew.crew_id + '" name="cs_wifi_ref" value="' + crew.crew_id + '" onclick="checkIndividualCrew();">' +
                                   '<label for="crew_' + crew.crew_id + '"> ' + crew.crew_id + '</label>' +
                                   '</span></li>';
                    $('#crew_checkbox_list').append(listItem);
                });

                 data.rateNameList.forEach(function(rdata) {
                     dataSelect.append('<option value="' + rdata.rate_name + '">' + rdata.rate_name + '</option>');
                 });

             }
         });
     }

     // 선박 선택시 선원 ID list
        function collectSelectedCrewIds() {
            var selectedCrewIds = [];
            $('#crew_checkbox_list .check-box1 input[type="checkbox"]:checked').each(function() {
                if ($(this).attr('id') !== 'allSelect') {
                    selectedCrewIds.push($(this).val());
                }
            });
                console.log(selectedCrewIds);
                toggleCrewList();
            }


        function toggleCrewList() {
            $('#wifi_crew_list').toggle(); // 표시/숨김 전환
        }

        //선원 전체선택
        function selectAllCrew(checkbox) {
            var isChecked = checkbox.checked;
            $('#crew_checkbox_list .check-box1 input[type="checkbox"]').prop('checked', isChecked);
        }
        // 전체선택 체크 해제
        function checkIndividualCrew() {
            var allChecked = true;
            $('#crew_checkbox_list .check-box1 input[type="checkbox"]').each(function() {
                if (!$(this).is(':checked') && $(this).attr('id') !== 'allSelect') {
                    allChecked = false;
                    return false;
                }
            });

            $('#allSelect').prop('checked', allChecked);
        }

        //수정 페이지 이동
         function goUpdate(s_code,crew_id){
                            $.ajax({
                                type : "POST",
                                url : "/crewinfo/crewInfoSetUpdateData.ajax",
                                data : "ajax=true&s_code=" + s_code + "&crew_id=" + crew_id,
                                dataType : "json",
                                error	: function (e) { alert(error); },
                                success : function(data) {
                                    $('#updCrewId').text(data.crewInfoVo.crew_id);
                                    $('#u_updCrewId').val(data.crewInfoVo.crew_id);
                                    $('#updCrewName').val(data.crewInfoVo.crew_name);
                                    $('#u_updCrewName').val(data.crewInfoVo.crew_name);
                                    $('#updRateName').val(data.crewInfoVo.rate_name);
                                    $('#u_updRateName').val(data.crewInfoVo.rate_name);
                                    $('#u_updCompName').val(data.crewInfoVo.comp_name);
                                    $('#updCompName').text(data.crewInfoVo.comp_name);
                                    $('#updSName').text(data.crewInfoVo.s_name);
                                    $('#u_updSName').val(data.crewInfoVo.s_name);
                                    $('#u_updSCode').val(data.crewInfoVo.s_code);
                                    $('#updMonth').val(data.crewInfoVo.amt_total_month);
                                    $('#updDay').val(data.crewInfoVo.limit_day_amt);
                                    $('#updUse').val(data.crewInfoVo.limit_cont_amt);
                                    $('#updMin').val(data.crewInfoVo.limit_cont_time);
                                    $('#updTimeFrom').val(data.crewInfoVo.time_from);
                                    $('#updTimeTo').val(data.crewInfoVo.time_to);
                                    $('#updTimeFrom2').val(data.crewInfoVo.time_from2);
                                    $('#updTimeTo2').val(data.crewInfoVo.time_to2);
                                    $('#updTimeFrom3').val(data.crewInfoVo.time_from3);
                                    $('#updTimeTo3').val(data.crewInfoVo.time_to3);
                                    $('#updDayFrom').val(formatDateString(data.crewInfoVo.day_from));
                                    $('#updDayTo').val(formatDateString(data.crewInfoVo.day_to));
                                     $('#updRdata').val(data.crewInfoVo.rate_name);
                                    updateApplyDays(data.crewInfoVo.apply_day);
                                    if (data.crewInfoVo.val2 === 'Y') {
                                        $('#updVal2').prop('checked', true);
                                    } else {
                                        $('#updVal2').prop('checked', false);
                                    }


                                    var rdataSelect = $('#updRdata');
                                    rdataSelect.empty();
                                    rdataSelect.append('<option value="select">' + select + '</option>');

                                    var selectedRateName = data.crewInfoVo.rate_name;
                                    var rateNameExistsInList = false;

                                    data.rdataList.forEach(function(rdata) {
                                        rdataSelect.append('<option value="' + rdata.rate_name + '">' + rdata.rate_name + '</option>');
                                        if (rdata.rate_name === selectedRateName) {
                                            rateNameExistsInList = true;
                                        }
                                    });

                                    if (rateNameExistsInList) {
                                        rdataSelect.val(selectedRateName);
                                    } else {
                                        rdataSelect.val('select');
                                    }


                                    var amtTotalMonth = parseInt(data.crewInfoVo.amt_total_month || 0, 10);
                                    var accUseMonth = parseInt(data.crewInfoVo.acc_use_month || 0, 10) / 1000;
                                    var amtRest = amtTotalMonth - accUseMonth;
                                    amtRest = amtRest < 0 ? 0 : amtRest;



                                    $('#updAmtRest').text(amtRest.toLocaleString());


                                    $('#rate_name').val(data.crewInfoVo.rate_name);
                                    $('#amt_total_month').val(data.crewInfoVo.amt_total_month);
                                    $('#limit_day_amt').val(data.crewInfoVo.limit_day_amt);
                                    $('#limit_cont_amt').val(data.crewInfoVo.limit_cont_amt);
                                    $('#limit_cont_time').val(data.crewInfoVo.limit_cont_time);
                                    $('#time_from').val(data.crewInfoVo.time_from);
                                    $('#time_from2').val(data.crewInfoVo.time_from2);
                                    $('#time_from3').val(data.crewInfoVo.time_from3);
                                    $('#time_to').val(data.crewInfoVo.time_to);
                                    $('#time_to2').val(data.crewInfoVo.time_to2);
                                    $('#time_to3').val(data.crewInfoVo.time_to3);
                                    $('#apply_day').val(data.crewInfoVo.apply_day);
                                    $('#day_from').val(data.crewInfoVo.day_from);
                                    $('#day_to').val(data.crewInfoVo.day_to);
                                    $('#crew_name').val(data.crewInfoVo.crew_name);
                                    $('#val2').val(data.crewInfoVo.val2);

                                    $("#updateModal").modal("show");
                                }
                            });
               }

        //수정
        function updateCrewInfo() {
            var updDayFrom = $('#updDayFrom').val();
            var updDayTo = $('#updDayTo').val();

            updDayFrom = formatDateToyyyyMMdd(updDayFrom);
            updDayTo = formatDateToyyyyMMdd(updDayTo);

            var selectedDays = getSelectedUpdateDays();

             // 폼 데이터 수동 조합
             var datas = $("#updCrewInfoForm").find("input, select, textarea").not("#updDayFrom, #updDayTo").serialize();
             datas += "&updDayFrom=" + updDayFrom + "&updDayTo=" + updDayTo + "&updApplyDays=" + selectedDays;


            // 비밀번호 변경 체크박스가 선택되었는지 확인
            if ($('#changePw').is(':checked')) {
                var password = $('#updPw').val();
                var confirmPassword = $('#updPwChk').val();

                if (password.trim() === '' ) {
                    alert(enterPassword);
                    $('#updPw').focus();
                    return;
                }
                if (confirmPassword.trim() === '') {
                    alert(enterConfirmPassword);
                    $('#updPwChk').focus();
                    return;
                }
                if (password !== confirmPassword) {
                    alert(wrongPassword);
                    $('#updPw').focus();
                    return;
                }
            }
           if ('' == $("#updRdata").val() || 'select' == $("#updRdata").val()) {
                alert(selectUsagePolicy);
                $("#updRdata").focus();
                return;
            }
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
              alert(wrongDailyUse);
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
          
          if ($('#updDayFrom').val() === '') {
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
                url: "/crewinfo/crewInfoUpdate.ajax",
                data: "ajax=true&" + datas,
                dataType: "json",
                error: function (e) {
                    alert(error객);
                },
                success: function(data) {
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

        function updateCrewInfoAll() {

              var form = document.getElementById('AllCrewInfoForm');
              var formData = new FormData(form);
              var cs_select = $('#cs_select').val();


              // 선택한 선박명 추가
              var selectedShipName = document.getElementById('allSCode').value;
              formData.append('s_code', selectedShipName);

                var selectedCrewCount = 0;
                // 선택한 선원 ID 추가
                $('#crew_checkbox_list .check-box1 input[type="checkbox"]:checked').each(function() {
                    if ($(this).attr('id') !== 'allSelect') {
                        formData.append('selectedCrewIds', $(this).val());
                        selectedCrewCount++;
                    }
                });

                // 선택된 선원이 없을 경우 경고
                if (selectedCrewCount === 0) {
                    alert(selectShip);
                    return;
                }

                if(cs_select == 'wifi'){
                        var cs_data = $('#cs_data').val();
                        if(cs_data == '0'){
                            if(!confirm(confirmBlockWIFI)){
                                return;
                            }
                        }else if(cs_data =='1'){
                            if(!confirm(confirmUnBlockWIFI)){
                                return;
                            }
                        }
                    }



             if(cs_select == 'rate'){
                  var allRdata = $('#allRdata').val();
                 if(allRdata == 'select'){
                    alert(selectUsagePolicy);
                       return;
                 }else{
                         // 선택한 선원들의 데이터 정책 일괄 적용 확인
                         if (!confirm(confirmPolicyInBulk)) {
                             return;
                         }

                         // 기본 정책 저장 확인
                         if ($("input:checkbox[name=save_flag]").is(":checked")) {
                             if (!confirm(confirmSaveDefaultPolicy)) {
                                 return;
                             }
                         }

                    }

             }
             if(cs_select =='carry_over'){
                var allVal2 = $('#allVal2').val();
                if(allVal2 == 'Y'){
                    if(!confirm(confirmBlockCarryOver)){
                        return;
                    }
                }else if(allVal2 == 'N'){
                    if(!confirm(confirmCarryOverFunction)){
                        return;
                    }
                }

             }
            if(cs_select =='default'){
                if(!confirm(confirmReturnPolicy)){
                    return;
                }
            }



            $.ajax({
                type: "POST",
                url: "/crewinfo/updateCrewInfoAll.ajax",
                data: formData,
                processData: false,
                contentType: false,
                dataType: "json",
                error: function (e) {
                    alert(error);
                },
                success: function(data) {
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


             function formatDateToyyyyMMdd(dateString) {
                 return dateString.replace(/-/g, "");
             }


       function getSelectedUpdateDays() {
            var selectedDays = ',';
            $('input[name="upd_apply_day"]:checked').each(function () {
                selectedDays += $(this).val() + ',';
            });
            return selectedDays;
        }

        //요일 형식 변경
        function days(applyDayStr) {
            const dayNames = [sun, mon, tue, wed, thu, fri, sat];
            return applyDayStr.split(',')
                             .filter(day => day)
                             .map(day => dayNames[parseInt(day, 10) - 1])
                             .join(',');
        }
        //날짜 형식 변경
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

           function getSelectedDays() {
               var selectedDays = ',';
               $('input[name="add_apply_day"]:checked').each(function () {
                   selectedDays += $(this).val() + ',';
               });
               return selectedDays;
           }

        // 수정시 요일 형식 변경
        function updateApplyDays(applyDayStr) {
            $('input[name="upd_apply_day"]').prop('checked', false);

            var days = applyDayStr.split(',').filter(function(day) { return day.trim() !== ""; });

            days.forEach(function(day) {
                $('#upd_apply_day' + day).prop('checked', true);
            });
        }
        // rate_name 선택시 요일 형식 변경
        function addApplyDays(applyDayStr) {
            $('input[name="add_apply_day"]').prop('checked', false);

            var days = applyDayStr.split(',').filter(function(day) { return day.trim() !== ""; });

            days.forEach(function(day) {
                $('#add_apply_day' + day).prop('checked', true);
            });
        }
          //일괄 설정 적용 항목 선택시 메뉴 표시
          function setList(selectedValue) {


            // 모든 옵션을 먼저 숨깁니다.
            document.getElementById('wifi_option').style.display = 'none';
            document.getElementById('rate_option').style.display = 'none';
            document.getElementById('val2_option').style.display = 'none';

            // 선택된 값에 따라 특정 옵션을 표시합니다.
            if (selectedValue === 'wifi') {
              document.getElementById('wifi_option').style.display = '';
            } else if (selectedValue === 'rate') {
              document.getElementById('rate_option').style.display = '';
            } else if (selectedValue === 'carry_over') {
              document.getElementById('val2_option').style.display = '';
            }

           document.getElementById('addAllComp').value = 'select';
           document.getElementById('allSCode').value = 'select';
           document.getElementById('allRdata').value = 'select';

            document.getElementById('cs_select').value = selectedValue;



          }


       // 엑셀 다운로드
          function goExcelDown() {
              var excel_msg = prompt(downloadExcelMessage);

              if (!excel_msg) {
                  alert(downloadCanceledMessage);
                  return;
              }

              var form = $("#searchCrewInfoForm");

              var input = $('<input>')
                  .attr('type', 'hidden')
                  .attr('name', 'excel_msg')
                  .val(excel_msg);
              form.append(input);

              form.attr("action", "/crewinfo/crewInfoExcelDownload.do");
              form.submit();
          }



        function req_import() {
            var formData = new FormData(document.getElementById("excelUploadFrom"));
            formData.append("add_field", document.getElementById("add_field").checked);
            formData.append("add_predel", document.getElementById("add_predel").checked);

            if (!formData.has("add_file") || !formData.get("add_file").name) {
                alert("엑셀 파일을 선택하여주세요.");
                return;
            }

            $('.excelContents-area').append('<div class="ajax-loading"></div>');

            $.ajax({
                url: "excelImport.ajax",
                data: formData,
                type: 'POST',
                processData: false,
                contentType: false,
                dataType: "json",
                success: function(data) {
                    $('.ajax-loading').remove();
                    alert(data.message);
                    location.reload();
                },
                error: function(data) {
                    $('.ajax-loading').remove();
                    alert(data.message);
                }
            });
        }
