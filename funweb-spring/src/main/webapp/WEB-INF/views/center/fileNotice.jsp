<%@page import="com.exam.domain.BoardVO"%>
<%@page import="java.util.Map"%>
<%@page import="java.text.SimpleDateFormat"%>

<%@page import="java.util.List"%>
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

<%
List<BoardVO> list = (List<BoardVO>) request.getAttribute("list");

Map<String, Integer> map = (Map<String, Integer>) request.getAttribute("pageInfoMap");
int allRowCount = map.get("allRowCount");
int pageNum = map.get("pageNum");

String search = (String) request.getAttribute("search"); // 검색어
%>

<!-- 게시판 -->
<article>
<h1>File Notice [전체글개수: <%=allRowCount %>]</h1>

<table id="notice">
<tr><th class="tno">No.</th>
    <th class="ttitle">Title</th>
    <th class="twrite">Writer</th>
    <th class="tdate">Date</th>
    <th class="tread">Read</th></tr>
<%
SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");

if (allRowCount > 0) {
	for (BoardVO board : list) {
		%>
		<tr onclick="location.href='/board/fileDetail?num=<%=board.getNum() %>&pageNum=<%=pageNum%>'">
			<td><%=board.getNum() %></td>
			<td class="left">
			<%
			int wid = 0; // 답글 들여쓰기 레벨 값 저장용
			if (board.getReLev() > 0) { // 답글일때
				wid = board.getReLev() * 10;
				%>
				<img src="/resources/images/center/level.gif" style="width: <%=wid%>px; height: 13px;">
				<img src="/resources/images/center/re.gif">
				<%
			}
			%>
				<%=board.getSubject() %>
			</td>
			<td><%=board.getName() %></td>
			<td><%=sdf.format(board.getRegDate()) %></td>
			<td><%=board.getReadcount() %></td>
		</tr>
		<%
	}
} else { // list.size() == 0
	%><tr><td colspan="5">게시판 글 없음</td></tr><%
}
%>
</table>

<%
// 세션값 가져오기
String id = (String) session.getAttribute("id");
// 세션값이 있으면 글쓰기 버튼이 보이게 설정
if (id != null) {
	%>
<div id="table_search">
<input type="button" value="파일 글쓰기" class="btn" onclick="location.href = '/board/fileWrite';">
</div>
	<%
}
%>

<div id="table_search">
<form action="/board/filelist" method="get">
<input type="text" name="search" class="input_box" value="${search}">
<input type="submit" value="검색" class="btn">
</form>
</div>
<div class="clear"></div>
<div id="page_control">
<%
int startPage = map.get("startPage");
int endPage = map.get("endPage");
int pageBlockSize = map.get("pageBlockSize");
int maxPage = map.get("maxPage");


if (allRowCount > 0) {
	if (startPage > pageBlockSize) { // 이전 블록이 존재하는지 확인
		%> <a href="/board/filelist?pageNum=<%=startPage - pageBlockSize %>&search=${search}">[이전]</a> <%
	}
	for (int i=startPage; i<=endPage; i++) {
		if (i == pageNum) {
			%> <a href="/board/filelist?pageNum=<%=i %>&search=${search}"><span style="color: blue; font-weight: bold;">[<%=i %>]</span></a>	<%
		} else {
			%> <a href="/board/filelist?pageNum=<%=i %>&search=${search}">[<%=i %>]</a>	<%
		}
	}
	if (endPage < maxPage) { // 다음 블록이 존재하는지 확인
		%> <a href="/board/filelist?pageNum=<%=startPage + pageBlockSize %>&search=${search}">[다음]</a> <%
	}
}
%>
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