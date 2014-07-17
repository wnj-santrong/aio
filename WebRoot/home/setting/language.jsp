<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
<form action="${ctx}/setting/language.action" method="post" id="setting_language">
	<input type="hidden" name="type" value="0" />
	<dl class="system_set fl">
	    <dt><fmt:message key="setting_language" /></dt>
	    <dd>
            <p class="single">
                <input name="lang" type="radio" value="chinese" />
                <label><fmt:message key="setting_chinese" /></label>
                <input name="lang" type="radio" value="english" />
                <label><fmt:message key="setting_english" /></label>
            </p>	    
	    </dd>
	    <dd>
	        <p>&nbsp;</p>
	        <span><a href="javascript:void(0);" class="submit"><fmt:message key="text_save" /></a></span>
		</dd>
	</dl>
</form>