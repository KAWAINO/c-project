
        $(document).ready(function(){

              //추가 modal 닫을시 초기화
                          $('#addModal').on('hidden.bs.modal', function (e) {
                              $('#addTrafficForm')[0].reset();
                          });

              // 추가
                 $("#btnAdd").bind("click", function() {
                     addTraffic();
                 });


             // 수정
             $("#btnUpd").bind("click", function() {
                 updateTraffic();
             });

            // 삭제
                $("#btnDel").bind("click", function() {
                    delTraffic();
                });

         });





        //회사 선택시 선박명 list
        function updateShipNames(comp_id) {
            if (comp_id === 'select') {
                $('#addSCode').html('<option value="select">' + select + '</option>');
                return;
            }

            selectedShip = $('#addSCode').val();

            $.ajax({
                type: "POST",
                url: "/traffic/compList.ajax",
                data: { comp_id: comp_id },
                dataType: "json",
                error: function() {
                    alert(error);
                },
                success: function(data) {
                    var shipSelect = $('#addSCode');
                    shipSelect.empty();
                    shipSelect.append('<option value="select">' + select + '</option>');

                    data.shipNameList.forEach(function(ship) {
                        shipSelect.append('<option value="' + ship.s_code + '">' + ship.ship_name + '</option>');
                    });

                }
            });
        }

        function updateOptions() {
            var unit = document.getElementById('add_unit').value;
            var intervalSelect = document.getElementById('add_interval');
            var intervalLabel = document.getElementById('intervalLabel');
            var timeDiv = document.getElementById('time');

            while (intervalSelect.options.length > 0) {
                intervalSelect.remove(0);
            }
            if (unit === '1') {
                intervalLabel.innerHTML = every + ' <select name="add_interval" id="add_interval"></select> ' + hours;

                for (var i = 1; i <= 12; i++) {
                    var value = i;
                     document.getElementById('add_interval').options.add(new Option(value, value));

                }

                timeDiv.innerHTML = '<select name="add_min" id="add_min"></select> ' + minute;
                for (var i = 0; i < 60; i++) {
                    var value = i < 10 ? '0' + i : i;
                    document.getElementById('add_min').options.add(new Option(value, value));
                }
            } else if (unit === '2') {
                intervalLabel.innerHTML = every + ' <select name="add_interval" id="add_interval"></select> ' + days;

                for (var i = 1; i <= 15; i++) {
                   var value = i;
                document.getElementById('add_interval').options.add(new Option(value, value));

                }

                timeDiv.innerHTML = '<select name="add_hour" id="add_hour"></select> ' + hour + ' <select name="add_min" id="add_min"></select> ' + minute;
                for (var i = 0; i < 24; i++) {
                    var hourValue = i < 10 ? '0' + i : i;
                    document.getElementById('add_hour').options.add(new Option(hourValue , hourValue));
                }
                for (var i = 0; i < 60; i++) {
                    var minValue = i < 10 ? '0' + i : i;
                    document.getElementById('add_min').options.add(new Option(minValue, minValue));
                }
            }
        }


        function updateOptionsUpd() {
            var unit = document.getElementById('upd_unit').value;
            var intervalSelect = document.getElementById('upd_interval');
            var intervalLabel = document.getElementById('intervalLabelUpd');
            var timeDiv = document.getElementById('timeUpd');

            while (intervalSelect.options.length > 0) {
                intervalSelect.remove(0);
            }
            if (unit === '1') {
                intervalLabel.innerHTML = every + ' <select name="upd_interval" id="upd_interval"></select> ' + hours;

                for (var i = 1; i <= 12; i++) {
                    var value = i;
                     document.getElementById('upd_interval').options.add(new Option(value, value));

                }

                timeDiv.innerHTML = '<select name="upd_min" id="upd_min"></select> ' + minute;
                for (var i = 0; i < 60; i++) {
                    var value = i < 10 ? '0' + i : i;
                    document.getElementById('upd_min').options.add(new Option(value, value));
                }
            } else if (unit === '2') {
                intervalLabel.innerHTML = every + ' <select name="upd_interval" id="upd_interval"></select> ' + days;

                for (var i = 1; i <= 15; i++) {
                   var value = i;
                document.getElementById('upd_interval').options.add(new Option(value, value));

                }

                timeDiv.innerHTML = '<select name="upd_hour" id="upd_hour"></select> ' + hour + ' <select name="upd_min" id="upd_min"></select> ' + minute;
                for (var i = 0; i < 24; i++) {
                    var hourValue = i < 10 ? '0' + i : i;
                    document.getElementById('upd_hour').options.add(new Option(hourValue , hourValue));
                }
                for (var i = 0; i < 60; i++) {
                    var minValue = i < 10 ? '0' + i : i;
                    document.getElementById('upd_min').options.add(new Option(minValue, minValue));
                }
            }
        }


        function updateOptionsUpdLoad(data) {

            var unit = data.trafficVo.sch_unit;
            var intervalSelect = document.getElementById('upd_interval');
            var intervalLabel = document.getElementById('intervalLabelUpd');
            var timeDiv = document.getElementById('timeUpd');

            while (intervalSelect.options.length > 0) {
                intervalSelect.remove(0);
            }
            if (unit === '1') {
                intervalLabel.innerHTML =  every + ' <select name="upd_interval" id="upd_interval"></select> ' + hours;

                for (var i = 1; i <= 12; i++) {
                    var value = i;
                     document.getElementById('upd_interval').options.add(new Option(value, value));

                }

                timeDiv.innerHTML = '<select name="upd_min" id="upd_min"></select> ' + minute;
                for (var i = 0; i < 60; i++) {
                    var value = i < 10 ? '0' + i : i;
                    document.getElementById('upd_min').options.add(new Option(value, value));
                }
            } else if (unit === '2') {
                intervalLabel.innerHTML = every + ' <select name="upd_interval" id="upd_interval"></select> ' + days;

                for (var i = 1; i <= 15; i++) {
                   var value = i;
                document.getElementById('upd_interval').options.add(new Option(value, value));

                }

                timeDiv.innerHTML = '<select name="upd_hour" id="upd_hour"></select> ' + hour + ' <select name="upd_min" id="upd_min"></select> ' + minute;
                for (var i = 0; i < 24; i++) {
                    var hourValue = i < 10 ? '0' + i : i;
                    document.getElementById('upd_hour').options.add(new Option(hourValue , hourValue));
                }
                for (var i = 0; i < 60; i++) {
                    var minValue = i < 10 ? '0' + i : i;
                    document.getElementById('upd_min').options.add(new Option(minValue, minValue));
                }
            }

            // AJAX 응답 데이터를 기반으로 선택 값 설정
               $('#upd_unit').val(unit);
               $('#upd_interval').val(data.trafficVo.sch_interval.toString());
               $('#upd_hour').val(data.trafficVo.sch_hour < 10 ? '0' + data.trafficVo.sch_hour : data.trafficVo.sch_hour.toString());
               $('#upd_min').val(data.trafficVo.sch_min < 10 ? '0' + data.trafficVo.sch_min : data.trafficVo.sch_min.toString());
        }

  function addTraffic(){

            if ('select' == $("#addComp").val()) {
                alert(selectShipOwner);
                $("#addComp").focus();
                return;
            }

            if ('select' == $("#addSCode").val()) {
                alert(selectShip);
                $("#addSCode").focus();
                return;
            }
            if ('' == $("#add_sch").val()) {
                alert(enterScheduleName);
                $("#add_sch").focus();
                return;
            }

            var datas = $("#addTrafficForm").serialize();
            $.ajax({
                type : "POST",
                url : "/traffic/trafficAdd.ajax",
                data : "ajax=true&" + datas,
                dataType : "json",
                error	: function (e) { alert(error); },
                success : function(data) {
                    if(data.result == "1") {
                        alert(success);
                        document.location.reload();
                    } else if(data.result == "-2") {
                        alert(existsScheduleName);
                    }else {
                        alert(error);
                    }
                }
            });
            return;
        }



    function goUpdate(seq_traf_sch){
                    $.ajax({
                        type : "POST",
                        url : "/traffic/trafficSetUpdateData.ajax",
                        data : "ajax=true&seq_traf_sch=" + seq_traf_sch,
                        dataType : "json",
                        error	: function (e) { alert(error); },
                        success : function(data) {

                            $('#u_updComp').text(data.trafficVo.comp_name);
                            $('#updComp').val(data.trafficVo.comp_name);
                            $('#u_updSCode').text(data.trafficVo.ship_name);
                            $('#updSCode').val(data.trafficVo.ship_name);
                            $('#upd_sch').val(data.trafficVo.sch_name);
                            $('#u_upd_sch').text(data.trafficVo.sch_name);
                            $('#upd_unit').val(data.trafficVo.sch_unit);
                            $('#upd_interval').val(data.trafficVo.sch_interval);
                            $('#upd_min').val(data.trafficVo.sch_min);
                            $('#upd_hour').val(data.trafficVo.sch_hour);
                            $('#seq_traf_sch').val(data.trafficVo.seq_traf_sch);

                            updateOptionsUpdLoad(data);
                            $("#updateModal").modal("show");
                        }
                    });
                }




         function updateTraffic(){

            var datas = $("#updTrafficForm").serialize();


            $.ajax({
                type : "POST",
                url : "/traffic/trafficUpdate.ajax",
                data : "ajax=true&" + datas,
                dataType : "json",
                error	: function (e) { alert(error); },
                success : function(data) {
                    if(data.result == "1") {
                        alert(success);
                        document.location.reload();
                    }else {
                        alert(error);
                    }
                }
            });
            return;
        }



            function delTraffic(){
                var seq_traf_sch = $('#seq_traf_sch').val();
                var upd_sch = $('#upd_sch').val();


                if(!confirm(upd_sch + " " + deleteSchedule)){
                    return;
                }

                $.ajax({
                    type : "POST",
                    url : "/traffic/trafficDelete.ajax",
                    data : "ajax=true&seq_traf_sch=" + seq_traf_sch,
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
