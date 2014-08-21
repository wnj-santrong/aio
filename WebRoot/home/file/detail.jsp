<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
<form action="${ctx}/file/fileEdit.action" method="post" id="file_fileEdit" class="layout_form" style="height:600px;">
	<input type="hidden" name="id" value="${file.id}"/>
	<div class="window" style="height:600px;">
        <dl>
            <dt><fmt:message key="file_detail"/><a href="javascript:void(0);" class="close">&nbsp;&nbsp;&nbsp;</a></dt>
            <dd><span class="tit"><fmt:message key="file_fileName"/>:</span><span class="cont">${file.fileName}</span></dd>
            <dd><span class="tit"><fmt:message key="file_courseName"/>:</span><span class="cont">${file.courseName}</span></dd>
            <dd><span class="tit"><fmt:message key="file_teacher"/>:</span><span class="cont">${file.teacher}</span></dd>
            <dd><span class="tit"><fmt:message key="file_duration"/>:</span><span class="cont">${file.duration}</span></dd>
            <dd><span class="tit"><fmt:message key="file_fileSize"/>:</span><span class="cont">${file.fileSizeString}</span></dd>
            <dd>
            	<span class="tit"><fmt:message key="file_status"/>:</span>
            	<span class="cont">
            	<c:if test="${file.status == 0}"><fmt:message key="file_status_recording"/></c:if>
            	<c:if test="${file.status == 1}"><fmt:message key="file_status_recorded"/></c:if>
            	<c:if test="${file.status == 2}"><fmt:message key="file_status_uploading"/></c:if>
            	<c:if test="${file.status == 3}"><fmt:message key="file_status_uploaded"/></c:if>
            	</span>
            </dd>
            <dd>
            	<span class="tit"><fmt:message key="file_level"/>:</span>
            	<span class="cont">
				<c:if test="${file.level == 0}"><fmt:message key="text_yes"/></c:if>
			    <c:if test="${file.level == 1}"><fmt:message key="text_no"/></c:if>
			    </span>
			</dd>
            <dd><span class="tit"><fmt:message key="file_bitRate"/>:</span><span class="cont">${file.bitRate}</span></dd>
            <dd><span class="tit"><fmt:message key="file_resolution"/>:</span><span class="cont">${file.resolutionString}</span></dd>
            <dd><span class="tit"><fmt:message key="file_playCount"/>:</span><span class="cont">${file.playCount}</span></dd>
            <dd><span class="tit"><fmt:message key="file_downloadCount"/>:</span><span class="cont">${file.downloadCount}</span></dd>
            <dd><span class="tit"><fmt:message key="file_recordTime"/>:</span><span class="cont"><fmt:formatDate value="${file.cts}" pattern="yyyy-MM-dd  HH:mm:ss" /></span></dd>
            <dd><span class="tit"><fmt:message key="file_modifyTime"/>:</span><span class="cont"><fmt:formatDate value="${file.uts}" pattern="yyyy-MM-dd  HH:mm:ss" /></span></dd>
            <dd><span class="tit"><fmt:message key="file_remark"/>:</span><span class="cont">${file.remark}</span></dd>
            <c:if test="${file.status != 0}">
            <dd><span class="tit"></span><span class="cont"><a href="javascript:void(0);" class="cplay"><fmt:message key="text_play"/></a></span></dd>
            </c:if>
        </dl>    	    
	</div>
</form>