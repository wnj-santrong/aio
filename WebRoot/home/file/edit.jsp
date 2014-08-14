<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
<form action="${ctx}/file/fileEdit.action" method="post" id="file_fileEdit" class="layout_form">
	<input type="hidden" name="id" value="${file.id}"/>
	<div class="window">
        <dl>
            <dt><fmt:message key="file_edit"/><a href="javascript:void(0);" class="close">&nbsp;&nbsp;&nbsp;</a></dt>
            <dd><span class="tit"><fmt:message key="file_courseName"/>:</span><span class="cont"><input name="courseName" type="text"  class="form_text" value="${file.courseName}"/></span></dd>
            <dd><span class="tit"><fmt:message key="file_teacher"/>:</span><span class="cont"><input name="teacher" type="text"  class="form_text" value="${file.teacher}" /></span></dd>
            <dd><span class="tit"><fmt:message key="file_remark"/>:</span><span class="cont"><textarea name="remark" cols="30" rows="4" class="form_area">${file.remark}</textarea></span></dd>
        </dl>
	    <div class="win_save"><a href="javascript:void(0);" class="submit"><fmt:message key="text_save"/></a><a href="javascript:void(0);" class="close"><fmt:message key="text_cancel"/></a></div>
	</div>
</form>