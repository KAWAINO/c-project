<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
		<!-- //contents -->
	</section>
	<!-- // container -->

	<!-- footer -->
	<footer class="footer">
		<p class="copy">Copyright &copy 2017 KT SAT All rights reserved.</p>
	</footer>
	<!-- // footer -->
</div>
<script>
$(document).ready(function() {
	// multiple checkbox
	$('.multiple-checkbox .selected').click(function(){
		$('.selectbox').toggle();
		$('.multiple-checkbox .cancel').click(function(){
			$('.selectbox').hide();
		});
	});
	
	$('.tab-wrap').tabs(); 
});
</script>