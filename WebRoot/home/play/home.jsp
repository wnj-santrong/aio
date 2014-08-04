<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
<code id="pagename" style="display:none">c:index_a:play</code>
<div style="display:none" id="pageSource">${source}</div>
<object id="RecCtrl1" classid="clsid:27671653-7A2D-4F23-92CF-76C7984F2CD5" class="hide" VIEWASTEXT></object>
<!-- 搜索 -->
<div class="search">
	<!-- 关键字 -->
    <span class="search_img"><input type="text" name="keywork" class="search_text" value="${query.keyword}" />
    	<a href="#" class="search_btn"><fmt:message key="text_search"/></a>
    </span>
    <!-- 标签 -->
    <c:set var="isLogin" value="${sessionScope.loginUser != null}"/>
    <span class="tags">
    <c:forEach items="${tagList}" var="item">
    	<a href="#" class='tag<c:if test="${query.keyword == item.tagName}"> cur_tag</c:if>'>${item.tagName}</a>
    <c:if test="${isLogin}">
    	<a href="#" class="tag_del"><fmt:message key="text_del"/></a>
    </c:if>
    </c:forEach>
    <c:if test="${isLogin}">
        <a href="#" class="tag_add"><img src="${ctx}/resource/photo/tag_add.png" width="54" height="20" /></a>
    </c:if>
    </span>
</div>
<!-- 列表 -->
<ul class="meeting_vod">
 	<c:forEach items="${fileList}" var="file">
    <li><a href="#" rel="${file.id}"><em></em><span class="guankan_img"><img src="${ctx}/resource/photo/guankan_a.png" ></span><img src="${ctx}/resource/photo/Class_pictures13.jpg" width="230" height="130" alt="img"></a>
        <p>${file.courseName}</p>
        <p><fmt:formatDate value="${file.cts}" pattern="yyyy/MM/dd  HH:mm:ss" /></p>
    </li>
    </c:forEach>
</ul>
<!-- 分页 -->
<div id="pagination"></div>
<script type="text/javascript">
var fileCount = ${query.count};
var pageNum = ${query.pageNum};
var pageSize = ${query.pageSize};
var keyword = '${query.keyword}';
</script>