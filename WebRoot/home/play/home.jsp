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
    <span class="tags">
    <c:forEach items="${tagList}" var="item">
    	<span class="tag" opt="${item.id}">
    	<span  class="tsd"><a href="#">${item.tagName}</a></span>
    	<span class="tmd hide">
            <a href="#" class="tag_edit"><fmt:message key="text_edit"/></a>
            <a href="#" class="tag_del"><fmt:message key="text_del"/></a>
        </span>
    	</span>
    </c:forEach>
        <a href="#" class="tag_add hide"><img src="${ctx}/resource/photo/tag_add.png" width="54" height="20" /></a>
    </span>
</div>
<!-- 列表 -->
<ul class="meeting_vod">
	<c:if test="${query.pageNum == 0}" >
 	<c:forEach items="${liveList}" var="live">
    <li><a href="#" rel="${live.id}" type="1"><em></em><span class="guankan_img"><img src="${ctx}/resource/photo/guankan_a.png" ></span><img src="${ctx}/resource/photo/Class_pictures13.jpg" width="230" height="130" alt="img"></a>
        <p>${live.courseName}</p>
        <p><fmt:message key="play_liveing" /></p>
    </li>
    </c:forEach>
    </c:if>
    
 	<c:forEach items="${fileList}" var="file">
    <li><a href="#" rel="${file.id}" type="0"><em></em><span class="guankan_img"></span><img src="${ctx}/resource/photo/Class_pictures13.jpg" width="230" height="130" alt="img"></a>
        <p>${file.courseName}</p>
        <p><fmt:formatDate value="${file.cts}" pattern="yyyy/MM/dd  HH:mm:ss" /></p>
    </li>
    </c:forEach>
</ul>
<!-- 分页 -->
<div id="pagination"></div>
<script type="text/javascript">
var fileCount = ${query.count} + ${query.prevInsert};
var pageNum = ${query.pageNum};
var pageSize = ${query.pageSize};
var keyword = '${query.keyword}';
</script>