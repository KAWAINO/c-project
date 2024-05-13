
      $(document).ready(function(){

        //검색
            $("#btnSearch").on("click", function() {

            //검색조건 저장
            sessionStorage.setItem("searchCompId", $('#searchCompId').val());
            sessionStorage.setItem("searchSCode", $('#searchSCode').val());
            sessionStorage.setItem("searchAp", $('#searchAp').val());
            sessionStorage.setItem("search", "true");

            var wapStatVo = sessionStorage.getItem("wapStatVo") ? JSON.parse(sessionStorage.getItem("wapStatVo")) : {};

            var CompChecked = $('#cs_ref_comp').is(':checked');
            var ShipChecked = $('#cs_ref_ship').is(':checked');
            var ApChecked = $('#cs_ref_ap').is(':checked');

            wapStatVo.cs_ref_comp = CompChecked
            wapStatVo.cs_ref_ship = ShipChecked
            wapStatVo.cs_ref_ap = ApChecked

            // Hidden 필드에 값 설정
            $('#hidden_cs_ref_comp').val(CompChecked);
            $('#hidden_cs_ref_ship').val(ShipChecked);
            $('#hidden_cs_ref_ap').val(ApChecked);

            sessionStorage.setItem("cs_ref_comp", CompChecked.toString());
            sessionStorage.setItem("cs_ref_ship", ShipChecked.toString());
            sessionStorage.setItem("cs_ref_ap", ApChecked.toString());

                $("#pageno").val(1);
                 $('.contents-area').append('<div class="ajax-loading"></div>');

                // 폼 제출
                $("#searchWapStatForm").attr("action", "/wapstat/wapstat.do");
                $("#searchWapStatForm").submit();
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
              sessionStorage.setItem("searchAp", $('#searchAp').val());
              sessionStorage.setItem("search", "true");


              var wapStatVo = sessionStorage.getItem("wapStatVo") ? JSON.parse(sessionStorage.getItem("wapStatVo")) : {};

              var CompChecked = $('#cs_ref_comp').is(':checked');
              var ShipChecked = $('#cs_ref_ship').is(':checked');
              var ApChecked = $('#cs_ref_ap').is(':checked');

              wapStatVo.cs_ref_comp = CompChecked
              wapStatVo.cs_ref_ship = ShipChecked
              wapStatVo.cs_ref_ap = ApChecked

              // Hidden 필드에 값 설정
              $('#hidden_cs_ref_comp').val(CompChecked);
              $('#hidden_cs_ref_ship').val(ShipChecked);
              $('#hidden_cs_ref_ap').val(ApChecked);

              sessionStorage.setItem("cs_ref_comp", CompChecked.toString());
              sessionStorage.setItem("cs_ref_ship", ShipChecked.toString());
              sessionStorage.setItem("cs_ref_ap", ApChecked.toString());

            $("#pageno").val(pageno);
            $("#searchWapStatForm").attr("action", "/wapstat/wapstat.do");
            $("#searchWapStatForm").submit();
        }

        //회사 선택시 선박명 list
            function updateShipNames(comp_id) {
                var shipSelect = $('#searchSCode');
                var wapSelect = $('#searchAp');

                // 셀렉트 박스 초기화
                shipSelect.empty();
                wapSelect.empty();

                // 기본 옵션 추가
                shipSelect.append('<option value="select">' + selectShipName + '</option>');
                wapSelect.append('<option value="select">WAP</option>');

                if(comp_id === 'select') {
                    return;
                }

                // AJAX 요청
                $.ajax({
                    type: "POST",
                    url: "/wapstat/compList.ajax",
                    data: { comp_id: comp_id },
                    dataType: "json",
                    error: function() {
                        alert(error);
                    },
                    success: function(data) {

                     var existsSCode = false;
                     var existsWap = false;


                        // WAP 리스트 업데이트
                        data.wapSelectList.forEach(function(wap) {
                            wapSelect.append('<option value="' + wap.ap_name + '">' + wap.ap_name + '</option>');
                            if (wap.ap_name === sessionStorage.getItem("searchAp")) {
                               existsWap = true;
                           }
                        });

                        // 선박명 리스트 업데이트
                        data.shipNameList.forEach(function(ship) {
                            shipSelect.append('<option value="' + ship.s_code + '">' + ship.comp_name + " - " +  ship.s_name + '</option>');
                         if (ship.s_code === sessionStorage.getItem("searchSCode")) {
                                    existsSCode = true;
                                }

                        });

                        // WAP 선택 복원
                        var reWapName = sessionStorage.getItem("searchAp");
                        if (reWapName && existsWap) {
                            wapSelect.val(reWapName);
                        }else{
                            wapSelect.val('select');
                        }

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


        //선박 선택시 AP리스트
        function updateWap(s_code) {
         var reWap = sessionStorage.getItem("searchAp");

            if(s_code === 'select') {
                $('#searchAp').html('<option value="select">' + selectWAP + '</option>');
                return;
            }
            selectedCrew = $('#searchAp').val();

            $.ajax({
                type: "POST",
                url: "/wapstat/wapSelectList.ajax",
                data: { s_code: s_code },
                dataType: "json",
                error: function() {
                    alert(error);
                },
                  success: function(data) {
                      var wapSelect = $('#searchAp');
                      wapSelect.empty();
                      wapSelect.append('<option value="select">' + selectWAP + '</option>');
                      
                      

                      data.wapSelectList.forEach(function(wap) {
                          wapSelect.append('<option value="' + wap.ap_name + '">' + wap.ap_name + '</option>');
                      });
                      if (reWap && wapSelect.find('option[value="' + reWap + '"]').length > 0) {
                          wapSelect.val(reWap);
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

              var form = $("#searchWapStatForm");


              var input = $('<input>')
                  .attr('type', 'hidden')
                  .attr('name', 'excel_msg')
                  .val(excel_msg);
              form.append(input);

              form.attr("action", "/wapstat/wapStatExcelDownload.do");
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
                var apCheckbox = document.getElementById("cs_ref_ap");


            if (compCheckbox && shipCheckbox && apCheckbox) {
                // 체크박스 상태에 따라 입력 필드 활성화/비활성화
                var csComp = document.getElementById("searchCompId");
                var csShip = document.getElementById("searchSCode");
                var csAp = document.getElementById("searchAp");


                if(csComp) csComp.disabled = !compCheckbox.checked;
                if(csShip) csShip.disabled = !shipCheckbox.checked;
                if(csAp) csAp.disabled = !apCheckbox.checked;


                // 체크박스 상태에 따라 값을 설정 (true/false)
                compCheckbox.value = compCheckbox.checked ? "true" : "false";
                shipCheckbox.value = shipCheckbox.checked ? "true" : "false";
                apCheckbox.value = apCheckbox.checked ? "true" : "false";
            }
        }
