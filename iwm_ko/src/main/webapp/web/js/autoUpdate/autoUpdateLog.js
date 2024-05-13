


      $(document).ready(function(){


        //검색
            $("#btnSearch").on("click", function() {

            //검색조건 저장
            sessionStorage.setItem("searchCompId", $('#searchCompId').val());
            sessionStorage.setItem("searchSCode", $('#searchSCode').val());
            sessionStorage.setItem("search", "true");

     var autoUpdateLogVo = sessionStorage.getItem("autoUpdateLogVo") ? JSON.parse(sessionStorage.getItem("autoUpdateLogVo")) : {};

            var updChecked = $('#cs_upd').is(':checked');

            autoUpdateLogVo.cs_upd = updChecked;

            // Hidden 필드에 값 설정
            $('#hidden_cs_upd').val(updChecked);

            sessionStorage.setItem("cs_upd", updChecked.toString());

                $("#pageno").val(1);
                  $('.contents-area').append('<div class="ajax-loading"></div>');
                // 폼 제출
                $("#searchAutoUpdateLogForm").attr("action", "/autoUpdateLog/autoUpdateLog.do");
                $("#searchAutoUpdateLogForm").submit();
          });


         });


        //paging
        function goPage(pageno) {

        //검색 조건 저장
            sessionStorage.setItem("searchCompId", $('#searchCompId').val());
            sessionStorage.setItem("searchSCode", $('#searchSCode').val());
            sessionStorage.setItem("search", "true");


            var iscs_upd = $('#cs_upd').is(':checked');

            sessionStorage.setItem("cs_upd", iscs_upd.toString());
            $("#pageno").val(pageno);
            $("#searchAutoUpdateLogForm").attr("action", "/autoUpdateLog/autoUpdateLog.do");
            $("#searchAutoUpdateLogForm").submit();
        }

        //회사 선택시 선박명 list
        function updateShipNames(comp_id) {
            if(comp_id === 'select') {
                $('#searchSCode').html('<option value="select">선박명</option>');
                return;
            }
                // AJAX 요청
                $.ajax({
                    type: "POST",
                    url: "/autoUpdateLog/compList.ajax",
                    data: { comp_id: comp_id },
                    dataType: "json",
                    error: function() {
                        alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
                    },
                    success: function(data) {
                        var shipSelect = $('#searchSCode');
                        shipSelect.empty();
                        shipSelect.append('<option value="select">선박명</option>');

                         data.shipNameList.forEach(function(ship) {
                            shipSelect.append('<option value="' + ship.s_code + '">' + ship.s_name + '</option>');

                        });


                        // 선박명 선택
                        var reSCode = sessionStorage.getItem("searchSCode");
                        if (reSCode) {
                            $('#searchSCode').val(reSCode);
                        }
                    }
                });
            }

