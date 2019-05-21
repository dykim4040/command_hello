<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="/resources/script/jquery-3.3.1.min.js"></script>
<script>
$(document).ready(function () {
	$('#frm').on('submit', function (e) {
		var str = $('input[type="datetime-local"]').val();
// 		alert(str);
		
		var resDate = new Date(str); // 예약날짜시간
		var nowDate = new Date(); // 현재날짜시간
		
		console.log('resDate.getTime() : ' + resDate.getTime());
		console.log('nowDate.getTime() : ' + nowDate.getTime());
		
		// 예약날짜가 현재시점보다 과거라면
		if (resDate.getTime() <= nowDate.getTime()) {
			alert('현재시점 이후의 날짜시각을 입력하세요.');
			//e.preventDefault();
			return false;
		}
		
		str = str.replace('T', ' ');
		str = 
			console.log('str : ' + str);
		$('#datetime').val(str);
		
		
		var time =  $('input:radio[name="time"]:checked').val();
// 		alert(time);
		var number =  $('input[name="interval"]').val();
// 		alert(number)
		
		var period = 0;  // 밀리초로 설정
		if (time == 'second') {
			period = 1000 * number;
		} else if (time == 'minute') {
			period = 1000 * 60 * number;
		} else if (time == 'hour') {
			period = 1000 * 60 * 60 * number;
		} else if (time == 'date') {
			period = 1000 * 60 * 60 * 24 * number;
		}
// 		alert(period);
		
		$('input#period').val(period);
		
		return true;
	});
});
</script>
</head>
<body>
	<%--
	자바스크립트의 setInterval()  setTimeout()에 해당함
	
	배치(Batch) 프로그램 : 스케줄링하기
	  일정 시간단위로 반복 수행
	  또는 정해진 날짜 시각에 (반복) 수행
	  
	자바 기본API에서는 Timer, TimerTast를 이용가능.
	 --%>
<h1>배치 처리 예약하기</h1>
<form action="/batch/process" method="post" id="frm">
	 예약 날짜시간 : <input type="datetime-local" name="datetime">
	 <input type= "hidden" name = "datetime" id = "datetime"><br>
	 <br>
	 실행간격 단위(일/시간/분/초) 선택 : <br>
	 <input type="radio" name="time" value="date"> 일
	 <input type="radio" name="time" value="hour"> 시간
	 <input type="radio" name="time" value="minute"> 분
	 
	 <input type="radio" name="time" value="second"> 초
	 <br>
	 실행간격 값 : <input type="number" min="0" name="interval">
	 <input type="hidden" name="period" id="period">
	 <button type="submit">예약하기</button>
</form>
</body>
</html>



