<%@page import="com.exam.domain.BoardVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
		<jsp:include page="/WEB-INF/views/inc/top.jsp" />
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
			<h1>File Notice Content</h1>
			<table id="notice">
				<tr>
					<th>글번호</th>
					<td>${board.num}</td>
					<th>조회수</th>
					<td>${board.readcount}</td>
				</tr>
				<tr>
					<th>작성자</th>
					<td>${board.name}</td>
					<th>작성일</th>
					<td>${board.regDate}</td>
				</tr>
				<tr>
					<th>글제목</th>
					<td colspan="3" class="left">${board.subject}</td>
				</tr>
				<tr>
					<th>파일</th>
					<td colspan="3" class="left"><c:if
							test="${not empty attachList}">
							<ul>
								<c:forEach var="attach" items="${attachList}">
									<li><c:if test="${attach.filetype eq 'I'}">
											<a
												href="/upload/${attach.uploadpath}/${attach.uuid}_${attach.filename}">
												<img src="/resources/images/center/p_png_s.gif">
												${attach.filename} <img
												src="/upload/${attach.uploadpath}/s_${attach.uuid}_${attach.filename}">
											</a>
										</c:if> <c:if test="${attach.filetype eq 'O'}">
											<a
												href="javascript:location.href='/board/download?fileName=${attach.uploadpath}/${attach.uuid}_' + encodeURIComponent('${attach.filename}')">
												<img src="/resources/images/center/p_etc_s.gif">
												${attach.filename}
											</a>
										</c:if></li>
								</c:forEach>
							</ul>
						</c:if></td>
				</tr>
				<tr>
					<th>글내용</th>
					<td colspan="3" class="left">${board.content}</td>
				</tr>
			</table>

			<div id="table_search">
				<%
				    String id = (String) session.getAttribute("id");
				    // 세션값 있으면(로그인 했으면)
				    // 수정,삭제,답글쓰기 버튼 보이게 설정
				    if (id != null) {
				%>
				<input type="button" value="글수정" class="btn"
					onclick="location.href='/board/fileModify?num=${board.num}&pageNum=${param.pageNum}';">
				<input type="button" value="글삭제" class="btn"
					onclick="location.href='/board/fileDelete?num=${board.num}&pageNum=${param.pageNum}';">
				<input type="button" value="답글쓰기" class="btn"
					onclick="location.href='/board/reply?reRef=${board.reRef}&reLev=${board.reLev}&reSeq=${board.reSeq}&pageNum=${param.pageNum}';">
				<%
				    }
				%>
				<input type="button" value="글목록" class="btn"
					onclick="location.href='/board/filelist?pageNum=${param.pageNum}';">
			</div>

			<div class="clear"></div>

		</article>
		<!-- 게시판 -->
		<!-- 본문들어가는 곳 -->
		<div class="clear"></div>
		<!-- 푸터들어가는 곳 -->
		<jsp:include page="/WEB-INF/views/inc/bottom.jsp" />
		<!-- 푸터들어가는 곳 -->
	</div>
</body>
</html>