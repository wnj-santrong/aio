<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
<form action="${ctx}/setting/update.action" method="post" id="setting_update">
	<input type="hidden" name="type" value="0" />
	<dl class="system_set fr">
	    <dt><fmt:message key="setting_update" /></dt>
	    <dd>
	        <p><fmt:message key="setting_selectFile" />:</p><span><input name="file" type="text" class="form_text" required /></span><span class="notice">*</span>
	    </dd>
	    <dd>
	        <p>&nbsp;</p>
	        <span><a href="javascript:void(0);" class="submit"><fmt:message key="text_save" /></a></span>
		</dd>
	</dl>
</form>