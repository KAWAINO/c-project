

        $(document).ready(function(){

              //추가 modal 닫을시 초기화
                      $('#addModal').on('hidden.bs.modal', function (e) {
                          $('#addSwUpdateForm')[0].reset();
                      });

              // 추가
                 $("#btnAdd").bind("click", function() {
                     addSwUpdate();
                 });

             // 수정
             $("#btnUpd").bind("click", function() {
                 updateSwUpdate();
             });

            // 삭제
                $("#btnDel").bind("click", function() {
                    delSwUpdate();
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
                url: "/swupdate/compList.ajax",
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



  function addSwUpdate(){

            var datas = $("#addSwUpdateForm").serialize();



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
            $.ajax({
                type : "POST",
                url : "/swupdate/swUpdateAdd.ajax",
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



    function goUpdate(seq_sw_sch){
                    $.ajax({
                        type : "POST",
                        url : "/swupdate/swUpdateSetUpdateData.ajax",
                        data : "ajax=true&seq_sw_sch=" + seq_sw_sch,
                        dataType : "json",
                        error	: function (e) { alert(error); },
                        success : function(data) {
                            $('#updComp').val(data.swUpdateVo.comp_name);
                            $('#u_updComp').text(data.swUpdateVo.comp_name);
                            $('#updSCode').val(data.swUpdateVo.ship_name);
                            $('#u_updSCode').text(data.swUpdateVo.ship_name);
                            $('#upd_sch').val(data.swUpdateVo.sch_name);
                            $('#u_upd_sch').text(data.swUpdateVo.sch_name);
                            $('#upd_unit').val(data.swUpdateVo.sch_unit);
                            $('#upd_interval').val(data.swUpdateVo.sch_interval);
                            $('#upd_min').val(data.swUpdateVo.sch_min);
                            $('#upd_hour').val(data.swUpdateVo.sch_hour);
                            $('#seq_sw_sch').val(data.swUpdateVo.seq_sw_sch);

                            updateOptionsUpdLoad(data);
                            $("#updateModal").modal("show");
                        }
                    });
                }




         function updateSwUpdate(){

            var datas = $("#updSwUpdateForm").serialize();
            $.ajax({
                type : "POST",
                url : "/swupdate/swUpdateUpdate.ajax",
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



            function delSwUpdate(){
                var seq_sw_sch = $('#seq_sw_sch').val();
                var upd_sch = $('#upd_sch').val();


                if(!confirm(upd_sch + " " + deleteSchedule)){
                    return;
                }

                $.ajax({
                    type : "POST",
                    url : "/swupdate/swUpdateDelete.ajax",
                    data : "ajax=true&seq_sw_sch=" + seq_sw_sch,
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
            }


        function updateOptionsUpdLoad(data) {

            var unit = data.swUpdateVo.sch_unit;
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

            // AJAX 응답 데이터를 기반으로 선택 값 설정
               $('#upd_unit').val(unit);
               $('#upd_interval').val(data.swUpdateVo.sch_interval.toString());
               $('#upd_hour').val(data.swUpdateVo.sch_hour < 10 ? '0' + data.swUpdateVo.sch_hour : data.swUpdateVo.sch_hour.toString());
               $('#upd_min').val(data.swUpdateVo.sch_min < 10 ? '0' + data.swUpdateVo.sch_min : data.swUpdateVo.sch_min.toString());
        }
