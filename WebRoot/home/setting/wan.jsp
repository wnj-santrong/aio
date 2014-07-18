<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
<form action="${ctx}/setting/networkPost.action" method="post" id="setting_wan">
	<input type="hidden" name="type" value="1" />
	<dl class="system_set fl">
	    <dt><fmt:message key="setting_wan" /></dt>
	    <dd>
	        <p><fmt:message key="setting_ip" />:</p><span><input name="ip" type="text" class="form_text" required /></span><span class="notice">*</span>
	    </dd>
	    <dd>
	        <p><fmt:message key="setting_mask" />:</p><span><input name="mask" type="text" class="form_text" required /></span><span class="notice">*</span>
	    </dd>
	    <dd>
	        <p><fmt:message key="setting_gateway" />:</p><span><input name="gateway" type="text" class="form_text" required /></span><span class="notice">*</span>
	    </dd>
	    <dd>
	        <p>&nbsp;</p>
	        <span><a href="javascript:void(0);" class="submit"><fmt:message key="text_save" /></a></span>
		</dd>
	</dl>
</form>