<%@page import="com.exam.domain.BoardVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<script src="/resources/script/jquery-3.3.1.min.js"></script>
<script>
$(document).ready(function () {
	$('button#btn').on('click', function () {
		$('div#newUploadFiles').append('<input type="file" name="newFiles" multiple="multiple"><br>');
	});
	
	$('span#del').on('click', function () {
		var $li = $(this).closest('li');
		
		$li.children('input[type="hidden"]').data('del', true);
		$li.children('div.attach-item').remove();
		
// 		var a = $li.children('input[type="hidden"]').data('del');
// 		alert(a);
	});
	
	$('form#frm').on('submit', function (e) {
		e.preventDefault();
		
		$('input[name="oldFiles"]').each(index, function() {
			if ($(this).data('del') != true) {
				$(this).remove();
			}
		});
		
		
	});
	
});
</script>
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
<h1>File Notice Update</h1>
<form id="frm" action="/board/fileModify" method="post" name="frm" enctype="multipart/form-data">
<input type="hidden" name="num" value="${param.num}">
<input type="hidden" name="pageNum" value="${param.pageNum}">
<table id="notice">
<tr><th>작성자명</th><td class="left"><input type="text" name="name" value="${board.name}"></td></tr>
<tr><th>비밀번호</th><td class="left"><input type="password" name="pass"></td></tr>
<tr><th>글제목</th><td class="left"><input type="text" name="subject" value="${board.subject}"></td></tr>
<tr>
	<th>파일</th>
	<td class="left">
	<c:if test="${not empty attachList}">
		<ul>
		<c:forEach var="attach" items="${attachList}">
			<li>
				<div class="attach-item">
					<c:if test="${attach.filetype eq 'I'}">
						<img src="/resources/images/center/p_png_s.gif">
					</c:if>
					<c:if test="${attach.filetype eq 'O'}">
						<img src="/resources/images/center/p_etc_s.gif">
					</c:if>
					${attach.filename}
					<span id="del" style="color: red; font-weight: bold;">X</span>
				</div>
				<input type="hidden" name="oldFiles" value="${attach.uploadpath}/${attach.uuid}_${attach.filename}">
			</li>
		</c:forEach>
		</ul>
	</c:if>
	<button type="button" id="btn">새로 업로드</button>
	<div id="newUploadFiles"></div>
	</td>
</tr>
<tr>
	<th>글내용</th>
	<td class="left"><textarea rows="13" cols="40" name="content">${board.content}</textarea></td>
</tr>
</table>

<div id="table_search">
<input type="submit" value="파일글수정" class="btn">
<input type="reset" value="다시작성" class="btn">
<input type="button" value="글목록" class="btn" onclick="location.href='/board/filelist?pageNum=${param.pageNum}';">
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