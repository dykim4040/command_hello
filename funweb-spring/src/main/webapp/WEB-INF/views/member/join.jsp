<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="/css/default.css" rel="stylesheet" type="text/css">
<link href="/css/subpage.css" rel="stylesheet" type="text/css">
<!--[if lt IE 9]>
<script src="http://ie7-js.googlecode.com/svn/version/2.1(beta4)/IE9.js" type="text/javascript"></script>
<script src="http://ie7-js.googlecode.com/svn/version/2.1(beta4)/ie7-squish.js" type="text/javascript"></script>
<script src="http://html5shim.googlecode.com/svn/trunk/html5.js" type="text/javascript"></script>
<![endif]-->
<!--[if IE 6]>
 <script src="/script/DD_belatedPNG_0.0.8a.js"></script>
 <script>
   /* EXAMPLE */
   DD_belatedPNG.fix('#wrap');
   DD_belatedPNG.fix('#main_img');   

 </script>
 <![endif]-->
<script src="/script/jquery-3.3.1.min.js"></script>
<script>
$(function () {
	
	$('input[name=id]').on('keyup', function (event) {
		var id = $(this).val();
		console.log('id 변수 타입: ' + typeof id);
		console.log('id: ' + id);
		
		$.ajax({
			url: '/member/joinIdCheckJson',
			data: {userid: id},  // userid=id입력값
			success: function (isDup) {
				console.log('result 변수 타입: ' + typeof(isDup));
				console.log('result: ' + isDup);
				
				if (isDup) {// id중복
					$('span#dupCheck').html('이미 존재하는 아이디입니다.').css('color', 'red');
				} else { // id중복아님
					$('span#dupCheck').html('사용 가능한 아이디입니다.').css('color', 'green');
				}
				
			}
		});
		
	});
	
});
</script>
<script>
function formCheck() {
	// ID는 3글자 이상 입력해야함
	if (frm.id.value.length < 3) { // frm.id.value == ''
		alert('ID는 3글자 이상 입력해야 합니다.');
		frm.id.focus();
		return false;
	}
	
	
	if (frm.password.value != frm.password2.value) {
		alert('패스워드 입력이 서로 다릅니다.\n다시 입력하세요.');
		frm.pass.focus();
		return false;
	}
	if (frm.email.value != frm.email2.value) {
		alert('이메일주소 입력이 서로 다릅니다.\n다시 입력하세요.');
		frm.email.focus();
		return false;
	}
	return true;
}

function idDupCheck() {
	// id값이 공백이면 "아이디 입력하세요." 포커스 주기.
	var id = frm.id.value;  // string 타입
	if (id.length == 0) { // id == ''
		alert('아이디 입력하세요.');
		frm.id.focus();
		return;
	}
	
	// 현재 창 기준으로 새 창 열기
	var childWindow = window.open('/member/joinIdCheck.do?userid='+id, '', 'width=400,height=200');
}
</script>
</head>
<body>
<div id="wrap">
<!-- 헤더들어가는 곳 -->
<jsp:include page="/WEB-INF/views/inc/top.jsp"/>
<!-- 헤더들어가는 곳 -->

<!-- 본문들어가는 곳 -->
<!-- 본문메인이미지 -->
<div id="sub_img_member"></div>
<!-- 본문메인이미지 -->
<!-- 왼쪽메뉴 -->
<nav id="sub_menu">
<ul>
<li><a href="#">Join us</a></li>
<li><a href="#">Privacy policy</a></li>
</ul>
</nav>
<!-- 왼쪽메뉴 -->
<!-- 본문내용 -->
<article>
<h1>Join Us</h1>

<form action="/member/join" method="post" id="join" name="frm" onsubmit="return formCheck();">
<fieldset>
<legend>Basic Info</legend>
<label>User ID</label>
<input type="text" name="id" class="id" required>
<input type="button" value="ID 중복확인" class="dup" onclick="idDupCheck()">
<span id="dupCheck"></span>
<br>
<label>Password</label>
<input type="password" name="password" required><br>
<label>Retype Password</label>
<input type="password" name="password2"><br>
<label>Name</label>
<input type="text" name="name" required><br>
<label>E-Mail</label>
<input type="email" name="email" required><br>
<label>Retype E-Mail</label>
<input type="email" name="email2"><br>
</fieldset>

<fieldset>
<legend>Optional</legend>
<label>생년월일</label>
<input type="date" name="birthday"><br>
<label>성별</label>
<p>
<input type="radio" name="gender" value="여">여자
<input type="radio" name="gender" value="남">남자<br>
</p>
<label>Address</label>
<input type="text" name="address"><br>
<label>Phone Number</label>
<input type="text" name="tel"><br>
<label>Mobile Phone Number</label>
<input type="text" name="mtel"><br>
</fieldset>
<div class="clear"></div>
<div id="buttons">
<input type="submit" value="회원가입" class="submit">
<input type="reset" value="초기화" class="cancel">
</div>
</form>

</article>
<!-- 본문내용 -->
<!-- 본문들어가는 곳 -->

<div class="clear"></div>
<!-- 푸터들어가는 곳 -->
<jsp:include page="/WEB-INF/views/inc/bottom.jsp"/>
<!-- 푸터들어가는 곳 -->
</div>
</body>
</html>