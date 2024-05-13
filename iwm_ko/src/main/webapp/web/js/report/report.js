

var chart2;

      $(document).ready(function(){

            $('#searchCompId').change(function() {
                updateShipNames(this.value);
            });

             $("#btnSearch").on("click", function() {			
                 report();
             });

             // 현재 날짜에서 한 달 전 날짜를 계산합니다.
                var currentDate = new Date();
                currentDate.setMonth(currentDate.getMonth() - 1);
                var prevYear = currentDate.getFullYear();
                var prevMonth = currentDate.getMonth() + 1; // JavaScript에서 월은 0부터 시작합니다.

                // 월을 두 자리 숫자로 포매팅합니다.
                var formattedPrevMonth = prevMonth < 10 ? '0' + prevMonth : prevMonth;

                // 년도 `<select>`에서 이전 년도를 선택합니다.
                $('#cs_year').val(prevYear);

                // 월 `<select>`에서 이전 월을 선택합니다.
                $('#cs_month').val(formattedPrevMonth);

         });


	    //회사 선택시 선박명 list
	    function updateShipNames(comp_id) {
	        if(comp_id === 'select') {
	            $('#searchSCode').html('<option value="select">선박명</option>');
	            return;
	        }
	            // AJAX 요청
	            $.ajax({
	                type: "POST",
	                url: "/report/SNameList.ajax",
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
	                        shipSelect.append('<option value="' + ship.s_code + '">'+ ship.comp_name + ' - ' + ship.s_name + '</option>');
	                    });
	                }
	
	            });
	        }

        function report() {
            if ($('#searchSCode').val() == 'select') {
                alert(selectVesselName);
                return;
            }
            if ($('#cs_year').val() == "all" || $('#cs_month').val() == "all") {
                alert(selectDateToLookUp);
                return;
            }
                $('.contents-area').append('<div class="ajax-loading"></div>');
            var datas = $("#searchReportForm").serialize();
            $.ajax({
                type: "POST",
                url: "/report/searchReport.ajax",
                data: "ajax=true&" + datas,
                dataType: "json",
                error: function(e) {
                    alert(error);
                },
                success: function(data) {

                    // 로딩 이미지를 최소 3초간 유지 후 삭제
		            setTimeout(function() {
		                $('.ajax-loading').remove();
		            }, 3000);
		            
                    if (data.result == "1") {

                        $('#cs_ship').val(data.reportVo.searchSCode);
                        $('#viewer').val(data.reportVo.viewer);
                        $('#path').val(data.reportVo.path);
                        $('#s_year').val(data.reportVo.cs_year);
                        $('#s_month').val(data.reportVo.cs_month);
                        $('#cs_comp').val(data.reportVo.searchCompId);

                        var apList = "";
                        var dataList = "";
                        var compList = data.compList;

                        for(var i=0; i<compList.length; i++){
                            var mapdata = compList[i];
                            apList += "'" + mapdata.crew_id + "(" + mapdata.crew_name + ")', ";
                            dataList += mapdata.val2 + ", ";
                        }

                          apList = apList.slice(0, -2).split(", ");
                          dataList = dataList.slice(0, -2).split(", ").map(Number);

                          chart2 = Highcharts.chart('container1', {
                                      chart: {
                                          type: 'column',
                                          backgroundColor:null,
                                          width: 1110,
                                          height: 500
                                      },
                                      exporting: {
                                          buttons: {
                                              exportButton: {
                                                  enabled:false
                                              }
                                          }
                                      },
                                      title: {
                                          text: null
                                      },
                                      subtitle: {
                                          text: null
                                      },
                                      xAxis: {
                                          categories:  apList,
                                          //categories: ['Africa', 'America', 'Asia', 'Europe', 'Oceania'],
                                          title: {
                                              text: null
                                          },
                                          labels: {
                                              style: {
                                                  color: '#000000'
                                              }
                                          }
                                      },
                                      yAxis: {
                                          min: 0,
                                          title: {
                                              text: '사용량 (MB)',
                                              style: {
                                                  color: '#000000'
                                              }
                                          },
                                          labels: {
                                              overflow: 'justify',
                                              style: {
                                                  color: '#000000'
                                              }
                                          },
                                          gridLineColor: '#000000'
                                      },
                                      tooltip: {
                                          valueSuffix: null
                                      },
                                      plotOptions: {
                                          column: {
                                              dataLabels: {
                                                  enabled: true,
                                                  color: (Highcharts.theme && Highcharts.theme.dataLabelsColor)
                                              }
                                          },
                                          series: {
                                              pointWidth: 30,
                                              point: {

                                              },
                                              animation: false
                                          }
                                      },
                                      credits: {
                                          enabled: false
                                      },
                                      series: [ {
                                          showInLegend: false,
                                          name: 'yyyyyyyy',
                                          color: 'lightskyblue',
                                          data:  dataList,

                                      } ]
                                  });

                                saveImage(chart2);
                    } else if (data.result == "-2") {
                        alert("해당 기간은 데이터가 존재하지 않습니다. 다시 선택하여주세요.");
                    }else if(data.result == "1"){
                     alert("clear.");

                    } else {
                     alert(error);
                    }
                }
            });       
        }

         function saveImage(chart2){
                	$("#container1").hide();
        	      //  var chart11 = $('#container1').highcharts();

        	        var svg = chart2.getSVG({
        	            exporting: {
        	                sourceWidth: chart2.chartWidth,
        	                sourceHeight: chart2.chartHeight
        	            }
        	        });
        	        var mycanvas = document.createElement('canvas');
        	        canvg(mycanvas, svg);
        	        var image = mycanvas.toDataURL("image/png");

        	        $("#imgData").val(image);
        	          $("#imgForm").off("submit").on("submit", function(event) {
                             event.preventDefault(); // 폼 기본 전송 막기
                             var formData = $(this).serialize();

                             // AJAX를 사용하여 이미지 데이터 전송
                             $.ajax({
                                 type: "POST",
                                 url: "/report/save_image.do", // 이미지를 저장하는 서버의 URL
                                 data: formData,
                                 success: function(response) {
                                     // 성공적으로 이미지가 저장되고, pdfUrl을 받으면 새 창에서 열기
                                     if (response.pdfUrl) {
                                         window.open(response.pdfUrl, '_blank');
                                         //document.location.reload();
                                     }
                                 },
                                 error: function(error) {
                                     console.log("Error:", error);
                                    // document.location.reload();
                                 }
                             });
                         });
                         $("#imgForm").submit(); // 폼 제출 방지 로직 적용 후 제출
                     }


        Highcharts.setOptions({
            lang: {
                thousandsSep: ','
            }
        });



