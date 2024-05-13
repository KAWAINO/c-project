
$(document).ready(function(){
	
	 /* 언어 변경 설정 */
		// localStorage의 언어명과 클래스 변수에 저장
		let textStorage = localStorage.getItem('selectedLanguage');
		let flagStorage = localStorage.getItem('langFlag');				
		// localStorage가 비어있는 경우 디폴트값 설정
	    if (!textStorage || !flagStorage) {
	        textStorage = "한국어"; // 기본 언어 설정
	        flagStorage = "korean"; // 기본 언어 플래그 설정
	        /* self.location.replace("/monitor/monitor.do?lang=ko"); */
	        localStorage.setItem('selectedLanguage', textStorage);
	        localStorage.setItem('langFlag', flagStorage);
	    } 
	    // localStorage의 언어명과 클래스 적용
		document.querySelector("#selectedLanguage").innerHTML  = textStorage;	
		document.querySelector("#selectedLanguage").classList.add(flagStorage);

		// 언어 변경 이벤트
		$("#language a").click(function() {
        	
	        //event.preventDefault(); 
	        // 링크 클릭 시 페이지 이동을 막음

	        let selectedLanguage = document.querySelector("#selectedLanguage");
	        let langCode = this.getAttribute("data-lang");
	        $('#langTemp').val(langCode);
	        let langText = '';
	        let langFlag = '';

	        if (langCode === "ko") {
	        	langText = "한국어";
	        	langFlag = "korean";
	            selectedLanguage.textContent = langText;
	            selectedLanguage.classList.add(langFlag);
	        } else {
	        	langText = "English";
	        	langFlag = "eng";
	            selectedLanguage.textContent = langText;
	            selectedLanguage.classList.add(langFlag);
	        } 
	        localStorage.setItem('selectedLanguage', selectedLanguage.innerHTML);
	        localStorage.setItem('langFlag', langFlag); 
	    });
});


        var onloadCallback = function() {
            grecaptcha.render('recaptcha-widget', {
                'sitekey' : '6LeRlh0pAAAAAAP7cUX8occWnRcoReTPs3JOnv33',
                'callback' : verifyCallback,
                'expired-callback' : expiredCallback,
            });
        };
                  //	인증 성공 시
          var verifyCallback = function(response) {
            $("#loginBtn").removeClass("disabled-btn");
            $("#loginBtn").attr("disabled", false);
          };

          //	인증 만료 시
          var expiredCallback = function(response) {
            $("#loginBtn").addClass("disabled-btn");
            $("#loginBtn").attr("disabled", true);
          }

          //	g-recaptcha 리셋
            var resetCallback = function() {
            grecaptcha.reset();
          }

        jQuery(document).ready(function(){

            if(localStorage.getItem("savedId")) {
                $('#user_id').val(localStorage.getItem("savedId"));
                $('#passwd').val(localStorage.getItem("savedPw"));
                $('#chkId').prop('checked', true);
            }


            // 로그인 버튼 클릭시
            $(document).on("click", "#btnLogin", function(e){
                e.preventDefault();
                var userId = $('#user_id').val();
                var passwd = $('#passwd').val();
                var saveIdChecked = $('#chkId').is(':checked');

                if(saveIdChecked) {
                    localStorage.setItem("savedId", userId);
                    localStorage.setItem("savedPw", passwd);
                } else {
                    localStorage.removeItem("savedId");
                    localStorage.removeItem("savedPw");
                }

                fn_login();
            });
        });

       function fn_login() {
           if(jQuery.trim($("#user_id").val()) == ""){
               alert("아이디를 입력해주세요.");
               $("#user_id").focus();
               return;
           }
           if(jQuery.trim($("#passwd").val()) == ""){
               alert("비밀번호를 입력해주세요.");
               $("#passwd").focus();
               return;
           }

           var recaptchaResponse = grecaptcha.getResponse();
           var param = {
               user_id: $("#user_id").val(),
               passwd: $("#passwd").val(),
               'g-recaptcha-response': recaptchaResponse
           };

           $.ajax({
               type: 'post',
               url: '/login/loginProc.do',
               data: param,
               success: function(data){
                   var code = data.code;
                   var message = data.message;

                   switch(code) {
                       case "SUCCESS":
                           location.href = '/monitor/monitor.do';
                           break;
                       case "ACCOUNT_LOCKED":
                           alert(message );
                            location.reload();
                           break;
                       case "LOGIN_FAILED":
                           alert(message );
                           location.reload();
                           break;
                       case "LOGIN_FAILED_FIRST_TIME":
                           alert(message );
                           location.reload();
                           break;
                       case "recaptcha error":
                           alert("자동 가입 방지를 체크해주세요.");
                            location.reload();
                           break;

                       default:
                           alert("로그인 처리 중 오류가 발생했습니다.");
                            location.reload();
                   }
               },
               error: function(xhr, status, error) {
                   alert("로그인 요청 처리 중 오류가 발생했습니다.");
               }
           });       
       }
       