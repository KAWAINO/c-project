<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- header -->
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
  <div class="container-fluid">
    <a class="navbar-brand" href="/suser/suser.do">APMS</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarColor01" aria-controls="navbarColor01" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarColor01">
      <ul class="navbar-nav me-auto">
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">setting</a>
          <ul class="dropdown-menu">
            <li><a class="dropdown-item" href="/suser/suser.do">OWM 운영자 관리</a></li>
          </ul>
        </li>
      </ul>
      <ul class="nav navbar-nav navbar-right">
      	<li class="nav-item">
			<a href="#" class="nav-link text-danger">
			<span class="badge rounded-pill bg-danger">Danger</span>
			&nbsp; 0</a>
      	</li>
      	<li class="nav-item">
			<a href="#" class="nav-link text-warning">
			<span class="badge rounded-pill bg-warning">Warning</span>
			&nbsp; 0</a>
      	</li>
      	<li class="nav-item">
			<a href="#" class="nav-link text-success">
      		<span class="badge rounded-pill bg-success">Success</span>
      		&nbsp; 0</a>
      	</li>
      	<li class="nav-item">
      		<a href="#" class="nav-link" data-bs-container="body" data-bs-toggle="popover" data-bs-placement="bottom" data-bs-html="true"
			data-bs-content="-Alarm1<br>-Alarm2<br>-<a href='#'>Alarm3</a>" data-bs-original-title="Alarm Title">
              <img class="bi mx-auto mb-1" width="18" height="18" src="/web/images/icon/bell-fill.svg"/>
              Alarm
            </a>
      	</li>
      	<li class="nav-item">
      		<a href="#" class="nav-link">
              <img class="bi mx-auto mb-1" width="18" height="18" src="/web/images/icon/power.svg"/>
              Logout
            </a>
      	</li>
      </ul>
    </div>
  </div>
</nav>
<!-- //header -->