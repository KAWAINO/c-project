<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<script type="text/javascript">

window.onLoad = getAlarmHeader();

	var timer = null;
	
	function getAlarmHeader(){  
		   	$.ajax({
		   	    url : "/alarmSys/getAlarmHeader.ajax",
		   		type : "POST", 
		   		dataType : "JSON",
		   	    chech : false,
		   	    success : function(result){
		   	 		$('#alarm_critical').html(result[0].s_cnt+'건');
		   	 		$('#alarm_major').html(result[1].s_cnt+'건');
		   	 		$('#alarm_minor').html(result[2].s_cnt+'건');	
		   	    }
		   	});
		   	
		   	if(timer = null){
		     	timer = setTimeout("getAlarmHeader()",5000);	  
			}else{
				clearTimeout(timer);
				timer = setTimeout("getAlarmHeader()",5000);	 
			}
	}

</script>

<!-- header -->
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">

  <div class="container-fluid">
    <a class="navbar-brand" href="/chart/chart.do">RADIUS</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarColor01" aria-controls="navbarColor01" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarColor01">
      <ul class="navbar-nav me-auto">
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">관리</a>
          	<ul class="dropdown-menu">
          		<li><a class="dropdown-item" href="/suser/suser.do" >운영자 관리</a></li>
          		<li><a class="dropdown-item" href="/alarmConf/alarmConf.do" >알람 설정</a></li>
          		<li><a class="dropdown-item" href="/inmarsat/inmarsat.do" >inmarsat 설정 관리</a></li>
          	</ul>
        </li>
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">이력</a>
          	<ul class="dropdown-menu">
          		<li><a class="dropdown-item" href="/athntHistory/athntHistory.do">인증 이력</a></li>
          		<li><a class="dropdown-item" href="/alarmHis/alarmHis.do">알람 이력</a></li>
          	</ul>
        </li>
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">통계</a>
          	<ul class="dropdown-menu">
          		<li><a class="dropdown-item" href="/athntStat/athntStat.do">인증 통계</a></li>
          	</ul>
        </li>
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">상태</a>
          	<ul class="dropdown-menu">
          		<li><a class="dropdown-item" href="/radacctInfo/radacctInfo.do">인증 현황</a></li>       	
          		<li><a class="dropdown-item" href="/alarmSys/alarmSys.do">알람 현황</a></li>
          	</ul>
        </li>
      </ul>
      
      <ul class="nav navbar-nav navbar-right">
	  	<li class="nav-item">
		 	<a href="/alarmSys/alarmSys.do" class="nav-link text-danger">
		 		<span class="badge rounded-pill bg-danger">CRITICAL</span>
		 		<span id="alarm_critical">&nbsp;0건</span>
		 	</a>
		</li>
		<li class="nav-item">
			<a href="/alarmSys/alarmSys.do" class="nav-link text-warning"  >
				<span class="badge rounded-pill bg-warning">MAJOR</span>
				<span id="alarm_major">&nbsp;0건</span>
			</a>
		</li>
		<li class="nav-item">
			<a href="/alarmSys/alarmSys.do" class="nav-link text-success" id="alarm_minor" >
				<span class="badge rounded-pill bg-success">MINOR</span>
				<span id="alarm_minor" >&nbsp;0건</span>
			</a>
		</li>
      	<c:choose>
	      	<c:when test="${sessionScope.user_id == null }">
		      	<li class="nav-item">
		      		<a href="/" class="nav-link">
		              <img class="bi mx-auto mb-1" id="logout" width="18" height="18" src="/web/images/icon/power.svg"/>
					  LOGIN
		            </a>
		      	</li>
	      	</c:when>
	      	<c:otherwise>
		      	<li class="nav-item">
		      		<a href="/logout.do" class="nav-link" >
		              <img class="bi mx-auto mb-1" id="logout" width="18" height="18" src="/web/images/icon/power.svg"/>
					  LOGOUT
		            </a>
		      	</li>
	      	</c:otherwise>
      	</c:choose>
      </ul>
     </div>
  </div>
</nav>
<!-- //header -->