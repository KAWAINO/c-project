


      $(document).ready(function(){

        //검색
            $("#btnSearch").on("click", function() {

            //검색조건 저장
            sessionStorage.setItem("searchCompId", $('#searchCompId').val());
            sessionStorage.setItem("searchSCode", $('#searchSCode').val());
            sessionStorage.setItem("search", "true");

             var shipStatVo = sessionStorage.getItem("shipStatVo") ? JSON.parse(sessionStorage.getItem("shipStatVo")) : {};

            var CompChecked = $('#cs_ref_comp').is(':checked');
            var ShipChecked = $('#cs_ref_ship').is(':checked');

            shipStatVo.cs_ref_comp = CompChecked
            shipStatVo.cs_ref_ship = ShipChecked

            // Hidden 필드에 값 설정
            $('#hidden_cs_ref_comp').val(CompChecked);
            $('#hidden_cs_ref_ship').val(ShipChecked);

            sessionStorage.setItem("cs_ref_comp", CompChecked.toString());
            sessionStorage.setItem("cs_ref_ship", ShipChecked.toString());

                $("#pageno").val(1);
                 $('.contents-area').append('<div class="ajax-loading"></div>');

                // 폼 제출
                $("#searchShipStatForm").attr("action", "/shipstat/shipstat.do");
                $("#searchShipStatForm").submit();
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
            sessionStorage.setItem("search", "true");

            var shipStatVo = sessionStorage.getItem("shipStatVo") ? JSON.parse(sessionStorage.getItem("shipStatVo")) : {};

            var CompChecked = $('#cs_ref_comp').is(':checked');
            var ShipChecked = $('#cs_ref_ship').is(':checked');

            shipStatVo.cs_ref_comp = CompChecked
            shipStatVo.cs_ref_ship = ShipChecked

            // Hidden 필드에 값 설정
            $('#hidden_cs_ref_comp').val(CompChecked);
            $('#hidden_cs_ref_ship').val(ShipChecked);

            sessionStorage.setItem("cs_ref_comp", CompChecked.toString());
            sessionStorage.setItem("cs_ref_ship", ShipChecked.toString());

            $("#pageno").val(pageno);
            $("#searchShipStatForm").attr("action", "/shipstat/shipstat.do");
            $("#searchShipStatForm").submit();
        }

        //회사 선택시 선박명 list
        function updateShipNames(comp_id) {

                $('#searchSCode').html('<option value="select">' + selectShipName + '</option>');

                // AJAX 요청
                $.ajax({
                    type: "POST",
                    url: "/shipstat/compList.ajax",
                    data: { comp_id: comp_id },
                    dataType: "json",
                    error: function() {
                        alert(error);
                    },
                    success: function(data) {
                        var shipSelect = $('#searchSCode');
                        shipSelect.empty();
                        shipSelect.append('<option value="select">' + selectShipName + '</option>');

                         data.shipNameList.forEach(function(ship) {
                            shipSelect.append('<option value="' + ship.s_code + '">' + ship.comp_name + " - "+ ship.s_name + '</option>');
                        });

                        // 선박명 선택
                        var reSCode = sessionStorage.getItem("searchSCode");
                        if (reSCode) {
                            $('#searchSCode').val(reSCode);
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

              var form = $("#searchShipStatForm");


              var input = $('<input>')
                  .attr('type', 'hidden')
                  .attr('name', 'excel_msg')
                  .val(excel_msg);
              form.append(input);

              form.attr("action", "/shipstat/shipStatExcelDownload.do");
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

            if (compCheckbox && shipCheckbox ) {
                // 체크박스 상태에 따라 입력 필드 활성화/비활성화
                var csComp = document.getElementById("searchCompId");
                var csShip = document.getElementById("searchSCode");

                if(csComp) csComp.disabled = !compCheckbox.checked;
                if(csShip) csShip.disabled = !shipCheckbox.checked;

                // 체크박스 상태에 따라 값을 설정 (true/false)
                compCheckbox.value = compCheckbox.checked ? "true" : "false";
                shipCheckbox.value = shipCheckbox.checked ? "true" : "false";
            }
        }
