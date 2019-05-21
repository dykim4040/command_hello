<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
<h1>Notice [전체글개수: ${pageInfoMap.allRowCount}]</h1>

<table id="notice">
<tr><th class="tno">No.</th>
    <th class="ttitle">Title</th>
    <th class="twrite">Writer</th>
    <th class="tdate">Date</th>
    <th class="tread">Read</th></tr>
    
<c:choose>
<c:when test="${not empty list}"><%-- pageInfoMap.allRowCount gt 0 --%>
	<%-- 
		*forEach 반복할때마다 pageScope 영역객체에 저장
		pageContext.setAttribute("board", list 요소 한개); 
	--%>
	<c:forEach var="board" items="${list}">
		<tr onclick="location.href='/board/detail?num=${board.num}&pageNum=${pageInfoMap.pageNum}'">
			<td>${board.num}</td>
			<td class="left">
			<c:if test="${board.reLev gt 0}"><!-- 답글일때 -->
				<c:set var="wid" value="${board.reLev * 10}" /><%-- 답글 들여쓰기 레벨 값 저장용 --%>
				<img src="/images/center/level.gif" style="width: ${wid}px; height: 13px;">
				<img src="/images/center/re.gif">
			</c:if>
				${board.subject}
			</td>
			<td>${board.name}</td>
			<td>
				<fmt:formatDate value="${board.regDate}" pattern="yyyy.MM.dd"/>
			</td>
			<td>${board.readcount}</td>
		</tr>
	</c:forEach>
</c:when>
<c:otherwise>
	<tr><td colspan="5">게시판 글 없음</td></tr>
</c:otherwise>
</c:choose>
</table>

<!-- 세션에 id값이 있으면 글쓰기 버튼이 보이게 설정 -->
<c:if test="${sessionScope.id ne null}">
	<div id="table_search">
	<input type="button" value="글쓰기" class="btn" onclick="location.href = '/board/write';">
	</div>
</c:if>

<div id="table_search">
<form action="/board/list" method="get">
<input type="text" name="search" class="input_box" value="${search}">
<input type="submit" value="검색" class="btn">
</form>
</div>
<div class="clear"></div>
<div id="page_control">
<c:if test="${pageInfoMap.allRowCount gt 0}">
	<!-- 이전 블록이 존재하는지 확인 -->
	<c:if test="${pageInfoMap.startPage gt pageInfoMap.pageBlockSize}">
		<a href="/board/list?pageNum=${pageInfoMap.startPage - pageInfoMap.pageBlockSize}&search=${search}">[이전]</a>
	</c:if>
	
	<c:forEach var="i" begin="${pageInfoMap.startPage}" end="${pageInfoMap.endPage}" step="1">
		<c:choose>
		<c:when test="${i eq pageInfoMap.pageNum}">
			<a href="/board/list?pageNum=${ i }&search=${search}"><span style="color: blue; font-weight: bold;">[${ i }]</span></a>
		</c:when>
		<c:otherwise>
			<a href="/board/list?pageNum=${ i }&search=${search}">[${ i }]</a>
		</c:otherwise>
		</c:choose>
	</c:forEach>
	
	<!-- 다음 블록이 존재하는지 확인 -->
	<c:if test="${pageInfoMap.endPage lt pageInfoMap.maxPage}">
		<a href="/board/list?pageNum=${pageInfoMap.startPage + pageInfoMap.pageBlockSize}&search=${search}">[다음]</a>
	</c:if>
</c:if>

</div>
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