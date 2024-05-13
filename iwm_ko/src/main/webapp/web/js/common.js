/* 정규표현식 */
var id_regx = /^[a-z0-9_-]{4,20}$/;
var name_regx = /^[ㄱ-ㅎ|가-힣|a-z|A-Z|\*]+$/;
var email_regx = /([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
var pass_regx = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{6,12}$/; /* 6~12자의 영문, 숫자, 특수문자[ ! @ # $ % ^ * + = - ] 를 조합 */
var tel_regx = /^(0(1[1|6|7|8|9|0]|2|3[1-3]|4[1-4]|5[1-5]|6[1-4]|7[0-4]))-(\d{3,4})-(\d{4})$/;
var nick_regx = /^[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|\s+|\*]+$/;
var num_regx = /^[0-9]*$/;
var minDate = new Date();
var maxDate = new Date();

// for ie8
 document.createElement('header');
 document.createElement('footer');
 document.createElement('section');
 document.createElement('aside');
 document.createElement('nav');
 document.createElement('article');
 document.createElement('hgroup');
 document.createElement('address');

$(function(){


	/* datepicker setting */
	$.datepicker.regional['ko'] = {
		closeText: '닫기',
		prevText: '이전달',
		nextText: '다음달',
		currentText: '오늘',
		monthNames: ['1월(JAN)','2월(FEB)','3월(MAR)','4월(APR)','5월(MAY)','6월(JUN)','7월(JUL)','8월(AUG)','9월(SEP)','10월(OCT)','11월(NOV)','12월(DEC)'],
		monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
		dayNames: ['일','월','화','수','목','금','토'],
		dayNamesShort: ['일','월','화','수','목','금','토'],
		dayNamesMin: ['일','월','화','수','목','금','토'],
		weekHeader: 'Wk',
		dateFormat: 'yy-mm-dd',
		firstDay: 0,
		isRTL: false,
		showMonthAfterYear: true,
		yearSuffix: '',
		numberOfMonths: 1,
		changeMonth: true, // 월을 바꿀수 있는 셀렉트 박스를 표시한다.
		changeYear: true
	};
	$.datepicker.setDefaults($.datepicker.regional['ko']);

	// datepicker
	$(':input.inp-date-picker').datepicker();
});

$(document).ready(function() {

    //scroll
    $(window).scroll(function(){
		var position = $(window).scrollTop();
		var windowH = $(window).height();
		if ( position > 70 && windowH > 960) {
        	$('.gnb > ul').addClass('fixed');
		} else {
			$('.gnb > ul').removeClass('fixed');
		}
	});

	//language
	$('.language > div').bind('mouseenter focusin',function(){
		$(this).children('ul').stop().slideDown(250);
	});
	$('.language > div').bind('mouseleave focusout',function(){
		$(this).children('ul').stop().slideUp(200);
	});

	// for gnb resize
	gnbHeight();
	function gnbHeight() {
		var windowH = $(window).height();
		var headerH = $('.header').height();
		var containerH = $('.container').outerHeight();
		$('.gnb').height(containerH);
	}

	// for resize
	$( window ).resize(function() {
		gnbHeight();
	});


	// data-href
	$('*[data-href]').on('click', function() {
		window.location = $(this).data('href');
		return false;
	});

	/* ajax 팝업
		1. a링크 또는 href 속성이 있는 태그에 아래 .ajax-modal 클래스를 적용하면 팝업이 생성됩니다.
		2. href 속성을 담을 수 없다면 data-target 속성에 지정합니다.
		3. 팝업의 닫기는 생성된 팝업의 HTML 내에 .btn-modal-close 클래스 요소를 클릭하면 닫을 수 있습니다.
	*/
	$(document).on('click', '.ajax-modal', function(ev) {
		ev.preventDefault();
		var targetUrl = $(this).attr('href');

		if (targetUrl == '' || targetUrl == "undefined" || targetUrl == null) {
			targetUrl = $(this).attr('data-target');
		}

		var modalHtml = $('<div class="modal-popup"><div class="modal-wrap"><div class="modal-content"><div class="content-wrap"></div><button type="button" class="btn-modal-close btn-close"><span>닫기</span></button></div></div></div>');

		$.ajax({
			type: 'POST',
			url: targetUrl,
			dataType: 'html',
			success: function (data) {
				if (data) {

					// 팝업을 열기전 이전에 열린 팝업이 있다면 제거한뒤 다시 띄웁니다.
					if ($('.modal-popup').length > 0) {
						closeModal();
					}

					modalHtml.find('.content-wrap').html(data);
					$('body').css({overflow:'hidden', paddingRight:'18px', boxSizing:'border-box'}).append(modalHtml);

				} else {
					alert('처리시 오류가 발생했습니다.');
				}
			},
			error:function(request,status){
				alert('처리시 오류가 발생했습니다.');
			},
			complete:function(request,status){
			}
		});
	});

	/* ajax 팝업 닫기
		1. a 또는 button 등 클릭 될 요소에 .btn-modal-close 클래스를 지정하면 닫힙니다.
	*/
	$(document).on('click', '.btn-modal-close', function(ev) {
		ev.preventDefault();
		closeModal(); // 다른 경우를 대비해서 함수로 닫습니다.
	});


	/* 숫자입력 */
    $(".numeric").keydown(function (e) {
      // Allow: backspace, delete, tab, escape, enter and .
      if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110, 190]) !== -1 ||
           // Allow: Ctrl+A, Command+A
          (e.keyCode === 65 && (e.ctrlKey === true || e.metaKey === true)) ||
           // Allow: home, end, left, right, down, up
          (e.keyCode >= 35 && e.keyCode <= 40)) {
               // let it happen, don't do anything
               return;
      }
      // Ensure that it is a number and stop the keypress
      if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
          e.preventDefault();
      }
    });
});

// ajax 팝업 닫기 함수
function closeModal() {
	if ($('.modal-popup').length > 0) {
		$('.modal-popup').remove();
		$('body').css({overflow:'', paddingRight:'', boxSizing:''});
	}
}

// ajax-modal에서 datepicker가 되지 않을 때
$(function(){
    $("body").on("click", ".inp-date", function(){
        if (!$(this).hasClass("hasDatepicker"))
        {
            $(this).datepicker();
            $(this).datepicker("show");
        }
    });
});