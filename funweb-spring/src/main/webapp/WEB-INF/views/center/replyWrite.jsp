<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="/resources/css/default.css" rel="stylesheet" type="text/css">
<link href="/resources/css/subpage.css" rel="stylesheet" type="text/css">
<!--[if lt IE 9]>
<script src="http://ie7-js.googlecode.com/svn/version/2.1(beta4)/IE9.js" type="text/javascript"></script>
<script src="http://ie7-js.googlecode.com/svn/version/2.1(beta4)/ie7-squish.js" type="text/javascript"></script>
<script src="http://html5shim.googlecode.com/svn/trunk/html5.js" type="text/javascript"></script>
<![endif]-->
<!--[if IE 6]>
 <script src="/resources/script/DD_belatedPNG_0.0.8a.js"></script>
 <script>
   /* EXAMPLE */
   DD_belatedPNG.fix('#wrap');
   DD_belatedPNG.fix('#main_img');   

 </script>
 <![endif]-->
</head>
<body>
<div id="wrap">
<!-- 헤더들어가는 곳 -->
<jsp:include page="/WEB-INF/views/inc/top.jsp"/>
<!-- 헤더들어가는 곳 -->

<!-- 본문들어가는 곳 -->
<!-- 메인이미지 -->
<div id="sub_img_center"></div>
<!-- 메인이미지 -->

<!-- 왼쪽메뉴 -->
<nav id="sub_menu">
<ul>
<li><a href="/board/list">Notice</a></li>
<li><a href="#">Public News</a></li>
<li><a href="/board/filelist">Driver Download</a></li>
<li><a href="#">Service Policy</a></li>
</ul>
</nav>
<!-- 왼쪽메뉴 -->

<!-- 게시판 -->
<article>
<h1>Notice Reply Write</h1>

<form action="/board/reply" method="post" name="frm">
<input type="hidden" name="pageNum" value="${param.pageNum}">
<input type="hidden" name="reRef" value="${param.reRef}">
<input type="hidden" name="reLev" value="${param.reLev}">
<input type="hidden" name="reSeq" value="${param.reSeq}">
<table id="notice">
<tr><th>작성자명</th><td><input type="text" name="name"></td></tr>
<tr><th>비밀번호</th><td><input type="password" name="pass"></td></tr>
<tr><th>글제목</th><td><input type="text" name="subject"></td></tr>
<tr>
	<th>글내용</th>
	<td><textarea rows="13" cols="40" name="content"></textarea></td>
</tr>
</table>

<div id="table_search">
<input type="submit" value="답글쓰기" class="btn">
<input type="reset" value="다시작성" class="btn">
<input type="button" value="글목록" class="btn" onclick="location.href='/board/list?pageNum=${param.pageNum}';">
</div>
</form>

<div class="clear"></div>

</article>
<!-- 게시판 -->
<!-- 본문들어가는 곳 -->
<div class="clear"></div>
<!-- 푸터들어가는 곳 -->
<jsp:include page="/WEB-INF/views/inc/bottom.jsp"/>
<!-- 푸터들어가는 곳 -->
</div>
</body>
</html>