        //paging
        function goPage(pageno) {
          $('.contents-area').append('<div class="ajax-loading"></div>');


            sessionStorage.setItem("searchCompId", $('#searchCompId').val());
            sessionStorage.setItem("searchSCode", $('#searchSCode').val());
            sessionStorage.setItem("searchCrewId", $('#searchCrewId').val());
            sessionStorage.setItem("search", "true");



            $("#pageno").val(pageno);
            $("#searchCrewcdrForm").attr("action", "/crewcdr/crewcdr.do");
            $("#searchCrewcdrForm").submit();
        }

        //회사 선택시 선박명 list
        function updateShipNames(comp_id) {
            var reSCode = sessionStorage.getItem("searchSCode");
            if (comp_id === 'select') {
                $('#searchSCode').html('<option value="select">' + selectShipName + '</option>');
                $('#searchCrewId').html('<option value="select">' + listCrewID + '</option>'); // 선원 ID 초기화
                return;
            }

            selectedShip = $('#searchSCode').val();

            $.ajax({
                type: "POST",
                url: "/crewcdr/compList.ajax",
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
                        shipSelect.append('<option value="' + ship.s_code + '">' + ship.ship_name + '</option>');
                    });

                    if (reSCode && shipSelect.find('option[value="' + reSCode + '"]').length > 0) {
                        shipSelect.val(reSCode);
                        updateCrewId(reSCode);
                    } else {
                        $('#searchCrewId').html('<option value="select">' + listCrewID + '</option>');
                    }
                }
            });
        }


        //선박 선택시 선원 ID list
        function updateCrewId(s_code) {
         var reCrewId = sessionStorage.getItem("searchCrewId");
            if(s_code === 'select') {
                $('#searchCrewId').html('<option value="select">' + listCrewID + '</option>');
                return;
            }
            selectedCrew = $('#searchCrewId').val();

            $.ajax({
                type: "POST",
                url: "/crewcdr/crewIdList.ajax",
                data: { s_code: s_code },
                dataType: "json",
                error: function() {
                    alert(error);
                },
               success: function(data) {
                   var crewSelect = $('#searchCrewId');
                   crewSelect.empty();
                   crewSelect.append('<option value="select">' + listCrewID + '</option>');

                   data.crewIdList.forEach(function(crew) {
                       crewSelect.append('<option value="' + crew.crew_id + '">' + crew.crew_id +"[" +crew.crew_name + "]"+ '</option>');
                   });
                   if (reCrewId && crewSelect.find('option[value="' + reCrewId + '"]').length > 0) {
                       crewSelect.val(reCrewId);
                   }
               }
            });
        }



     //날짜 형식 변경
        function formatDateString(dateString) {

            if (!dateString || dateString.length !== 8) {
                var today = new Date();
                var dd = String(today.getDate()).padStart(2, '0');
                var mm = String(today.getMonth() + 1).padStart(2, '0');
                var yyyy = today.getFullYear();
                return yyyy + '-' + mm + '-' + dd;
            }
            return dateString.substring(0, 4) + '-' + dateString.substring(4, 6) + '-' + dateString.substring(6, 8);
        }

       // 엑셀 다운로드
          function goExcelDown() {
              var excel_msg = prompt(downloadExcelMessage);
              if (!excel_msg) {
                  alert(downloadCanceledMessage);
                  return;
              }
              var form = $("#searchCrewcdrForm");

              var input = $('<input>')
                  .attr('type', 'hidden')
                  .attr('name', 'excel_msg')
                  .val(excel_msg);
              form.append(input);

              form.attr("action", "/crewcdr/crewcdrExcelDownload.do");
              form.submit();
          }