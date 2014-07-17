<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
<form action="${ctx}/setting/user.action" method="post" id="setting_form">
	<dl class="system_set fr">
	    <dt><fmt:message key="setting_user" /></dt>
	    <dd>
	        <p><fmt:message key="setting_newname" />:</p>
	        <span><input name="newname" type="text" class="form_text" required /></span><span class="notice">*</span>
		</dd>
	    <dd>
	        <p><fmt:message key="setting_newpwd" />:</p>
	        <span><input name="newpwd" type="password" class="form_text" required /></span><span class="notice">*</span>
		</dd>
	    <dd>
	        <p><fmt:message key="setting_agpwd" />:</p>
	        <span><input name="agpwd" type="password" class="form_text" required equalTo="newpwd"/></span><span class="notice">*</span>
	    </dd>
	    <dd>
	        <p><fmt:message key="setting_oldpwd" />:</p>
	        <span><input name="oldpwd" type="password" class="form_text" required /></span><span class="notice">*</span>
		</dd>
	    <dd>
	        <p>&nbsp;</p>
	        <span><a href="javascript:void(0);" class="submit"><fmt:message key="text_save" /></a></span>
		</dd>
	</dl>
</form>
