<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
<form action="${ctx}/setting/ftp.action" method="post" id="setting_ftp">
	<input type="hidden" name="type" value="0" />
	<dl class="system_set fr">
	    <dt><fmt:message key="setting_ftp" /></dt>
	    <dd>
	        <p><fmt:message key="setting_useFtp" />:</p><span><input name="useFtp" type="text" class="form_text" required /></span><span class="notice">*</span>
	    </dd>
	    <dd>
	        <p><fmt:message key="setting_ftpHost" />:</p><span><input name="host" type="text" class="form_text" required_Ip /></span><span class="notice">*</span>
	    </dd>
	    <dd>
	        <p><fmt:message key="setting_ftpPort" />:</p><span><input name="port" type="text" class="form_text" required_Number /></span><span class="notice">*</span>
	    </dd>
	    <dd>
	        <p><fmt:message key="setting_ftpUser" />:</p><span><input name="username" type="text" class="form_text" required /></span><span class="notice">*</span>
	    </dd>
	    <dd>
	        <p><fmt:message key="setting_ftpPwd" />:</p><span><input name="password" type="text" class="form_text" required /></span><span class="notice">*</span>
	    </dd>
	    <dd>
	        <p><fmt:message key="setting_ftpDuration" />:</p><span><input name="duration1" type="text" class="form_text"/></span><span class="notice">*</span>
	    </dd>	    	    	    
	    <dd>
	        <p>&nbsp;</p>
	        <span><a href="javascript:void(0);" class="submit"><fmt:message key="text_save" /></a></span>
		</dd>
	</dl>
</form>