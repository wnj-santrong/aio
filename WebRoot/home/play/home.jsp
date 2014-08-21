<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
<code id="pagename" style="display:none">c:index_a:play</code>
<div style="display:none" id="pageSource">${source}</div>
<!-- 搜索 -->
<div class="search">
	<!-- 关键字 -->
    <span class="search_img"><input type="text" name="keywork" class="search_text" value="${query.keyword}" />
    	<a href="javascript:void(0);" class="search_btn"><fmt:message key="text_search"/></a>
    </span>
    <!-- 标签 -->
    <span class="tags">
    <c:forEach items="${tagList}" var="item">
    	<span class="tag" opt="${item.id}">
    	<span  class="tsd"><a href="javascript:void(0);">${item.tagName}</a></span>
    	<span class="tmd hide">
            <a href="javascript:void(0);" class="tag_edit"><fmt:message key="text_edit"/></a>
            <a href="javascript:void(0);" class="tag_del"><fmt:message key="text_del"/></a>
        </span>
    	</span>
    </c:forEach>
        <a href="javascript:void(0);" class="tag_add hide"><img src="${ctx}/resource/photo/tag_add.png" width="54" height="20" /></a>
    </span>
</div>
<!-- 列表 -->
<ul class="meeting_vod">
	<c:if test="${query.pageNum == 0}" >
 	<c:forEach items="${liveList}" var="live">
    <li><a href="javascript:void(0);" rel="${live.id}" type="1"><em></em><span class="guankan_img"><img src="${ctx}/resource/photo/guankan_a.png" ></span><img src="${ctx}/resource/photo/video01.jpg" width="230" height="130" /></a>
        <p>
      	<c:choose>
		    <c:when test="${fn:length(live.courseName) > 15}">  
		        <c:out value="${fn:substring(live.courseName, 0, 15)}..." />  
		    </c:when>  
		   <c:otherwise>  
		      <c:out value="${live.courseName}" />  
		    </c:otherwise>  
		</c:choose>
        </p>
        <p>${live.teacher}</p>
    </li>
    </c:forEach>
    </c:if>
    
 	<c:forEach items="${fileList}" var="file">
    <li><a href="javascript:void(0);" rel="${file.id}" type="0"><em></em><span class="guankan_img"></span><img src="${ctx}${file.thumbnail[0]}" width="230" height="130" /></a>
        <p>
      	<c:choose>  
		    <c:when test="${fn:length(file.courseName) > 15}">  
		        <c:out value="${fn:substring(file.courseName, 0, 15)}..." />
		    </c:when>  
		   <c:otherwise>  
		      <c:out value="${file.courseName}" />  
		    </c:otherwise>  
		</c:choose>
		</p>
        <p><fmt:formatDate value="${file.cts}" pattern="yyyy-MM-dd HH:mm" /></p>
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