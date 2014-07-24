<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
<code id="pagename" style="display:none">c:index_a:play</code>
<!-- 搜索 -->
<div class="serch">
	<!-- 关键字 -->
    <p class="serch_img"><input type="text" name="keywork" class="serch_text" value="${query.keyword}" />
    <a href="#" class="search_btn"><img src="${ctx}/resource/photo/search_icon_03.png" border="0" />搜索</a>
    </p>
    <!-- 标签 -->
   <p class="category"><a href="#" class="tag_add"><img src="${ctx}/resource/photo/newcode_btn_03.png" width="60" height="20" /></a>
   <c:forEach items="${tagList}" var="item">
   <a href="#" class='tag<c:if test="${query.keyword == item.tagName}"> cur_tag</c:if>'>${item.tagName}</a><a href="#" class="tag_del">删除</a>
   </c:forEach>
   </p>
</div>
<!-- 列表 -->
 <ul class="meeting_vod">
 	<c:forEach items="${fileList}" var="file">
    <li><a href="#"><em></em><span class="guankan_img"><img src="${ctx}/resource/photo/guankan_a.png" ></span><img src="${ctx}/resource/photo/Class_pictures13.jpg" width="230" height="130" alt="img"></a>
        <p>世办足球报道</p>
        <p>2014-06-30</p>
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