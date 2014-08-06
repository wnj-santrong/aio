<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="common.jsp"%>
<form action="${ctx}/login.action" method="post" id="index_login" class="layout_form" style="width:440px; height:230px;">
	<div class="window">
	    <div>
	        <dl>
	            <dt><fmt:message key="index_userLogin"/> <a href="#" class="close">&nbsp;&nbsp;&nbsp;</a></dt>
	            <dd><span class="tit"><fmt:message key="index_login_username"/>:</span><span class="cont"><input name="username" type="text" class="form_text" value="" required /></span></dd>
	            <dd><span class="tit"><fmt:message key="index_login_password"/>:</span><span class="cont"><input name="password" type="password" class="form_text" value="" required /></span></dd>
	        </dl>
	    </div>
	    <div class="win_save"><a href="#" class="login_submit"><fmt:message key="index_login_login"/></a><a href="#" class="close"><fmt:message key="text_cancel"/></a></div>
	</div>
</form>