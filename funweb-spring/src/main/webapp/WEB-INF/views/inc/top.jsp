<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
// 세션값 가져오기
String id = (String) session.getAttribute("id");
%>
<header>
<div id="login">
<%
// 세션값 없으면 [로그인]
// 세션값 있으면 id님 반가워요~ [로그아웃]
if (id == null) {
	%><a href="/member/login">로그인</a><%
} else { // id != null
	%><%=id %>님 반가워요~ <a href="/member/logout">로그아웃</a><%
}
%>
 | <a href="/member/join">회원가입</a>
</div>
<div class="clear"></div>
<!-- 로고들어가는 곳 -->
<div id="logo"><img src="/resources/images/logo.gif" width="265" height="62" alt="Fun Web"></div>
<!-- 로고들어가는 곳 -->
<nav id="top_menu">
<ul>
	<li><a href="/">HOME</a></li>
	<li><a href="/welcome">COMPANY</a></li>
	<li><a href="/batch/form">SOLUTIONS</a></li>
	<li><a href="/board/list">CUSTOMER CENTER</a></li>
	<li><a href="#">CONTACT US</a></li>
</ul>
</nav>
</header>