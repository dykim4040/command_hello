<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script>
function useIdAndClose() {
	// 현재창을 띄운 부모창의 참조는 window.opener
	// 현재창 의미하는 window는 일반적으로 생략가능
	opener.document.frm.id.value = document.fr.userid.value;
	
	// 현재 창 닫기  window.close();  close();
	window.close();
}
</script>
</head>
<body>
<%
	String id = request.getParameter("userid");

	boolean isDuplicated = (Boolean) request.getAttribute("isDuplicated");
	
	if (isDuplicated) {
		%>
		사용중인 아이디 입니다.<br>
		<%
	} else {
		%>
		사용가능한 아이디 입니다.
		<input type="button" value="ID사용" onclick="useIdAndClose()">
		<%
	}
%>

<form action="joinIdCheck.do" method="get" name="fr">
	<input type="text" name="userid" value="<%=id %>">
	<input type="submit" value="중복체크">
</form>

</body>
</html>