
 $(document).ready(function(){

        //검색
            $("#btnSearch").on("click", function() {

            //검색조건 저장
            sessionStorage.setItem("searchCompId", $('#searchCompId').val());
            sessionStorage.setItem("searchSCode", $('#searchSCode').val());
            sessionStorage.setItem("searchCrew", $('#searchCrew').val());
    //        sessionStorage.setItem("searchCrewName", $('#searchCrewName').val());
            sessionStorage.setItem("search", "true");


            var crewStatVo = sessionStorage.getItem("crewStatVo") ? JSON.parse(sessionStorage.getItem("crewStatVo")) : {};

            var CompChecked = $('#cs_ref_comp').is(':checked');
            var ShipChecked = $('#cs_ref_ship').is(':checked');
            var crewChecked = $('#cs_ref_crew').is(':checked');
            var crewNameChecked = $('#cs_ref_crewName').is(':checked');

            crewStatVo.cs_ref_comp = CompChecked
            crewStatVo.cs_ref_ship = ShipChecked
            crewStatVo.cs_ref_crew = crewChecked
            crewStatVo.cs_ref_crewName = crewNameChecked

            // Hidden 필드에 값 설정
            $('#hidden_cs_ref_comp').val(CompChecked);
            $('#hidden_cs_ref_ship').val(ShipChecked);
            $('#hidden_cs_ref_crew').val(crewChecked);

            sessionStorage.setItem("cs_ref_comp", CompChecked.toString());
            sessionStorage.setItem("cs_ref_ship", ShipChecked.toString());
            sessionStorage.setItem("cs_ref_crew", crewChecked.toString());


                $("#pageno").val(1);
                $('.contents-area').append('<div class="ajax-loading"></div>');

                // 폼 제출
                $("#searchCrewStatForm").attr("action", "/crewstat/crewstat.do");
                $("#searchCrewStatForm").submit();
          });



          // excel
            $("#btnExcel").bind("click", function() {
                goExcelDown();
            });

});

    //paging
    function goPage(pageno) {

            $('.contents-area').append('<div class="ajax-loading"></div>');
            //검색조건 저장
            sessionStorage.setItem("searchCompId", $('#searchCompId').val());
            sessionStorage.setItem("searchSCode", $('#searchSCode').val());
            sessionStorage.setItem("searchCrew", $('#searchCrew').val());
          //  sessionStorage.setItem("searchCrewName", $('#searchCrewName').val());
            sessionStorage.setItem("search", "true");


            var crewStatVo = sessionStorage.getItem("crewStatVo") ? JSON.parse(sessionStorage.getItem("crewStatVo")) : {};

            var CompChecked = $('#cs_ref_comp').is(':checked');
            var ShipChecked = $('#cs_ref_ship').is(':checked');
            var crewChecked = $('#cs_ref_crew').is(':checked');
            var crewNameChecked = $('#cs_ref_crewName').is(':checked');

            crewStatVo.cs_ref_comp = CompChecked
            crewStatVo.cs_ref_ship = ShipChecked
            crewStatVo.cs_ref_crew = crewChecked
            crewStatVo.cs_ref_crewName = crewNameChecked

            // Hidden 필드에 값 설정
            $('#hidden_cs_ref_comp').val(CompChecked);
            $('#hidden_cs_ref_ship').val(ShipChecked);
            $('#hidden_cs_ref_crew').val(crewChecked);
            $('#hidden_cs_ref_crewName').val(crewNameChecked);
            sessionStorage.setItem("cs_ref_comp", CompChecked.toString());
            sessionStorage.setItem("cs_ref_ship", ShipChecked.toString());
            sessionStorage.setItem("cs_ref_crew", crewChecked.toString());
            sessionStorage.setItem("cs_ref_crewName", crewNameChecked.toString());

            $("#pageno").val(pageno);

            $("#searchCrewStatForm").attr("action", "/crewstat/crewstat.do");
            $("#searchCrewStatForm").submit();
        }


function updateShipNames(comp_id) {
    var shipSelect = $('#searchSCode');
    shipSelect.empty();
    shipSelect.append('<option value="select">' + selectShipName + '</option>');

    // AJAX 요청
    $.ajax({
        type: "POST",
        url: "/crewstat/compList.ajax",
        data: { comp_id: comp_id },
        dataType: "json",
        error: function() {
            alert(error);
        },
        success: function(data) {
            var existsSCode = false;

            data.shipNameList.forEach(function(ship) {
                shipSelect.append('<option value="' + ship.s_code + '">' + ship.comp_name + " - " + ship.ship_name + '</option>');
                if (ship.s_code === sessionStorage.getItem("searchSCode")) {
                    existsSCode = true;
                }
            });

            // 선박명 선택 복원 또는 '선박명' 선택
            var reSCode = sessionStorage.getItem("searchSCode");
            if (reSCode && existsSCode) {
                shipSelect.val(reSCode);
            } else {
                shipSelect.val('select');
            }
        }
    });
}


        //선박 선택시 Crew리스트
//        function updateCrew(s_code) {
//         var reCrew = sessionStorage.getItem("searchCrew");
//         var reCrewName = sessionStorage.getItem("searchCrewName");
//            if(s_code === 'select') {
//                $('#searchCrew').html('<option value="select">선원 ID</option>');
//                $('#searchCrewName').html('<option value="select">선원 별칭</option>');
//                return;
//            }
//            selectedCrew = $('#searchCrew').val();
//
//            $.ajax({
//                type: "POST",
//                url: "/crewStat/crewSelectList.ajax",
//                data: { s_code: s_code },
//                dataType: "json",
//                error: function() {
//                    alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
//                },
//                 success: function(data) {
//                           var crewSelect = $('#searchCrew');
//                           var crewNameSelect = $('#searchCrewName');
//                           crewSelect.empty();
//                           crewNameSelect.empty();
//
//                           crewSelect.append('<option value="select">선원 ID</option>');
//                           crewNameSelect.append('<option value="select">선원 별칭</option>');
//
//                           data.crewSelectList.forEach(function(crew) {
//                               crewSelect.append('<option value="' + crew.crew_id + '">' + crew.crew_id + '</option>');
//                               crewNameSelect.append('<option value="' + crew.crew_name + '">' + crew.crew_name + '</option>');
//                           });
//
//                           if (reCrew && crewSelect.find('option[value="' + reCrew + '"]').length > 0) {
//                               crewSelect.val(reCrew);
//                           }
//                           if (reCrewName && crewNameSelect.find('option[value="' + reCrewName + '"]').length > 0) {
//                               crewNameSelect.val(reCrewName);
//                           }
//                       }
//            });
//        }


       // 엑셀 다운로드
          function goExcelDown() {


              var excel_msg = prompt(downloadExcelMessage);

              if (!excel_msg) {
                  alert(downloadCanceledMessage);
                  return;
              }

            var crewStatVo = sessionStorage.getItem("crewStatVo") ? JSON.parse(sessionStorage.getItem("crewStatVo")) : {};

            var CompChecked = $('#cs_ref_comp').is(':checked');
            var ShipChecked = $('#cs_ref_ship').is(':checked');
            var crewChecked = $('#cs_ref_crew').is(':checked');
            var crewNameChecked = $('#cs_ref_crewName').is(':checked');

            crewStatVo.cs_ref_comp = CompChecked
            crewStatVo.cs_ref_ship = ShipChecked
            crewStatVo.cs_ref_crew = crewChecked
            crewStatVo.cs_ref_crewName = crewNameChecked

            // Hidden 필드에 값 설정
            $('#hidden_cs_ref_comp').val(CompChecked);
            $('#hidden_cs_ref_ship').val(ShipChecked);
            $('#hidden_cs_ref_crew').val(crewChecked);
            $('#hidden_cs_ref_crewName').val(crewNameChecked);

              var form = $("#searchCrewStatForm");


              var input = $('<input>')
                  .attr('type', 'hidden')
                  .attr('name', 'excel_msg')
                  .val(excel_msg);
              form.append(input);
              form.attr("action", "/crewstat/crewStatExcelDownload.do");
              form.submit();
          }

     //날짜 형식 변경
        function formatDateString(dateString) {
            return dateString.substring(0, 4) + '-' + dateString.substring(4, 6) + '-' + dateString.substring(6, 8);
        }

        function convertToyyyyMMdd(dateString) {
            return dateString.replace(/-/g, "");
        }



        function chk_ref() {
                var compCheckbox = document.getElementById("cs_ref_comp");
                var shipCheckbox = document.getElementById("cs_ref_ship");
                var crewCheckbox = document.getElementById("cs_ref_crew");


            if (compCheckbox && shipCheckbox && crewCheckbox) {
                // 체크박스 상태에 따라 입력 필드 활성화/비활성화
                var csComp = document.getElementById("searchCompId");
                var csShip = document.getElementById("searchSCode");
                var csCrew = document.getElementById("searchCrew");
                var csCrewName = document.getElementById("searchCrewName");

                if(csComp) csComp.disabled = !compCheckbox.checked;
                if(csShip) csShip.disabled = !shipCheckbox.checked;
                if(csCrew) csCrew.disabled = !crewCheckbox.checked;
                if(csCrewName) csCrewName.disabled = !crewCheckbox.checked;

                // 체크박스 상태에 따라 값을 설정 (true/false)
                compCheckbox.value = compCheckbox.checked ? "true" : "false";
                shipCheckbox.value = shipCheckbox.checked ? "true" : "false";
                crewCheckbox.value = crewCheckbox.checked ? "true" : "false";
            }
        }
