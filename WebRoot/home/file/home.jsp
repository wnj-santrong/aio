<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
<code id="pagename" style="display:none">c:index_a:file</code>
<object id="RecCtrl1" classid="clsid:27671653-7A2D-4F23-92CF-76C7984F2CD5" class="hide" VIEWASTEXT></object>
<div class="search">
	<!-- 关键字 -->
    <span class="search_img"><input type="text" name="keywork" class="search_text" value="${query.keyword}" />
    	<a href="#" class="search_btn"><fmt:message key="text_search"/></a>
    </span>
   <!-- 按钮 -->
   <span class="btns">
	   <a href="#" id="fileEdit"><fmt:message key="text_edit"/></a>
	   <a href="#" id="fileDownload"><fmt:message key="text_download"/></a>
	   <a href="#" id="fileOpen"><fmt:message key="file_open"/></a>
	   <a href="#" id="fileClose"><fmt:message key="file_close"/></a>
	   <a href="#" id="fileDel"><fmt:message key="text_del"/></a>
   </span>
   <a href="#" id="filePlay"><img src="${ctx}/resource/photo/btn_watching.png"></a>
</div>
<!-- 列表 -->
<div class="file_list">
	<table>
	    <colgroup>
	    <col width="4%" />
	    <col width="25%" />
	    <col width="10%" />
	    <col width="10%" />
	    <col width="10%" />
	    <col width="10%" />
	    <col width="10%" />
	    <col width="10%" />
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
			    <a href="#" class="cdetail">
			    <c:if test="${file.courseName != '' }">
			    ${file.courseName}
			    </c:if>
			    <c:if test="${file.courseName == '' }">
			    -
			    </c:if>			    
			    </a>
			    </td>
			    <td>${file.teacher}</td>
			    <td>${file.cts}</td>
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


