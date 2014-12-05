<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
<div class="file_list">
	<table>
	    <colgroup>
	    <col width="5%" />
	    <col width="23%" />
	    <col width="10%" />
	    <col width="10%" />
	    <col width="10%" />
	    <col width="10%" />
	    <col width="17%" />
	    <col width="8%" />
	    <col width="6%" />
	    </colgroup>
	    <thead>
	        <tr>
	            <th><fmt:message key="text_index"/></th>
	            <th><fmt:message key="file_courseName"/></th>
	            <th><fmt:message key="file_teacher"/></th>
	            <th><fmt:message key="plt_push_account"/></th>
	            <th><fmt:message key="file_duration"/></th>
	            <th><fmt:message key="file_fileSize"/></th>
	            <th><fmt:message key="file_recordTime"/></th>
	            <th><fmt:message key="plt_push_status"/></th>
	            <th><fmt:message key="text_opt"/></th>
	        </tr>
	    </thead>
	    <tbody>
			<c:forEach items="${pushList}" var="file" varStatus="st">
			<tr>
			    <td>${query.pageNum*query.pageSize+st.index+1}</td>
			    <td>
				<c:choose>  
				    <c:when test="${fn:length(file.courseName) > 15}">  
				        <c:out value="${fn:substring(file.courseName, 0, 15)}..." />  
				    </c:when>  
				   <c:otherwise>  
				      <c:out value="${file.courseName}" />  
				    </c:otherwise>  
				</c:choose>
			    </td>
			    <td>${file.teacher}</td>
			    <td>${file.username}</td>
			    <td>${file.duration}</td>
			    <td>${file.fileSizeString}</td>
			    <td><fmt:formatDate value="${file.cts}" pattern="yyyy-MM-dd HH:mm" /></td>
			    <td>
            		<c:if test="${file.status == 0}"><fmt:message key="plt_push_status_waiting"/></c:if>
	            	<c:if test="${file.status == 1}"><fmt:message key="plt_push_status_pushing"/></c:if>
				</td>
			    <td>
			    	<c:if test="${file.status == 0}"><a href="javascript:void(0);" class="cancel" rel="i_${file.id}"><fmt:message key="text_cancel"/></a></c:if>
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
</script>
