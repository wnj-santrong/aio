<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
<code id="pagename" style="display:none">c:index_a:file</code>
<div class="search">
	<!-- 关键字 -->
    <span class="search_img"><input type="text" name="keywork" class="search_text" value="${query.keyword}" />
    	<a href="javascript:void(0);" class="search_btn"><fmt:message key="text_search"/></a>
    </span>
   <!-- 按钮 -->
   <span class="btns">
	   <a href="javascript:void(0);" id="fileEdit"><fmt:message key="text_edit"/></a>
	   <a href="javascript:void(0);" id="fileDownload"><fmt:message key="text_download"/></a>
	   <a href="javascript:void(0);" id="fileOpen"><fmt:message key="file_open"/></a>
	   <a href="javascript:void(0);" id="fileClose"><fmt:message key="file_close"/></a>
	   <a href="javascript:void(0);" id="fileDel"><fmt:message key="text_del"/></a>
   </span>
   <a href="javascript:void(0);" id="filePlay"><img src="${ctx}/resource/photo/btn_watching.png"></a>
</div>
<!-- 列表 -->
<div class="file_list">
	<table>
	    <colgroup>
	    <col width="4%" />
	    <col width="23%" />
	    <col width="10%" />
	    <col width="17%" />
	    <col width="10%" />
	    <col width="10%" />
	    <col width="10%" />
	    <col width="5%" />
	    <col width="10%" />
	    </colgroup>
	    <thead>
	        <tr>
	            <th><input type="checkbox" class="checkAll" /></th>
	            <th><fmt:message key="file_courseName"/></th>
	            <th><fmt:message key="file_teacher"/></th>
	            <th><fmt:message key="file_recordTime"/></th>
	            <th><fmt:message key="file_duration"/></th>
	            <th><fmt:message key="file_fileSize"/></th>
	            <th><fmt:message key="file_playCount"/></th>
	            <th><fmt:message key="file_level"/></th>
	            <th><fmt:message key="file_status"/></th>
	        </tr>
	    </thead>
	    <tbody>
			<c:forEach items="${fileList}" var="file">
			<tr>
			    <td><input type="checkbox" name="CheckboxGroup1" value="${file.id}" id="CheckboxGroup1_0" st="${file.status}" /></td>
			    <td>
			    <a href="javascript:void(0);" class="cdetail" title="${file.courseName}">
				<c:choose>  
				    <c:when test="${fn:length(file.courseName) > 15}">  
				        <c:out value="${fn:substring(file.courseName, 0, 15)}..." />  
				    </c:when>  
				   <c:otherwise>  
				      <c:out value="${file.courseName}" />  
				    </c:otherwise>  
				</c:choose>
			    </a>
			    </td>
			    <td>${file.teacher}</td>
			    <td><fmt:formatDate value="${file.cts}" pattern="yyyy-MM-dd HH:mm" /></td>
			    <td>${file.duration}</td>
			    <td>${file.fileSizeString}</td>
			    <td>${file.playCount}</td>
			    <td>
			    	<c:if test="${file.level == 0}"><fmt:message key="text_yes"/></c:if>
			    	<c:if test="${file.level == 1}"><fmt:message key="text_no"/></c:if>
			    </td>
			    <td>
            		<c:if test="${file.status == 0}"><fmt:message key="file_status_recording"/></c:if>
	            	<c:if test="${file.status == 1}"><fmt:message key="file_status_recorded"/></c:if>
	            	<c:if test="${file.status == 2}"><fmt:message key="file_status_uploading"/></c:if>
	            	<c:if test="${file.status == 3}"><fmt:message key="file_status_uploaded"/></c:if>				    
				</td>
			</tr>
			</c:forEach>
	    </tbody>
	</table>
</div>
<!-- 分页 -->
<div id="pagination"></div>
<script type="text/javascript">
var fileCount = ${query.count};
var pageNum = ${query.pageNum};
var pageSize = ${query.pageSize};
var keyword = '${query.keyword}';
</script>


