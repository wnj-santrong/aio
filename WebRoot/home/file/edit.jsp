<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
<form action="${ctx}/file/fileEdit.action" method="post" id="file_fileEdit" class="layout_form">
	<input type="hidden" name="id" value="${file.id}"/>
	<div class="window">
        <dl>
            <dt><fmt:message key="file_edit"/><a href="#" class="close">&nbsp;&nbsp;&nbsp;</a></dt>
            <dd><p><fmt:message key="file_courseName"/>:</p><span><input name="courseName" type="text"  class="form_text" value="${file.courseName}"/></span></dd>
            <dd><p><fmt:message key="file_teacher"/>:</p><span><input name="teacher" type="text"  class="form_text" value="${file.teacher}" /></span></dd>
            <dd><p><fmt:message key="file_remark"/>:</p><span><textarea name="remark" cols="30" rows="4" class="form_text">${file.remark}</textarea></span></dd>
        </dl>
	    <div class="win_save"><a href="#" class="submit"><fmt:message key="text_save"/></a><a href="#" class="close"><fmt:message key="text_cancel"/></a></div>
	</div>
</form>