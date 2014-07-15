<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<form action="/system/admin.action" method="post">
	<p><span><fmt:message key="user_newname" /></span>:<span><input type="text" name="newname" /></span></p>
	<p><span><fmt:message key="user_newpwd" /></span>:<span><input type="text" name="newpwd" /></span></p>
	<p><span><fmt:message key="user_agpwd" /></span>:<span><input type="text" name="agpwd" /></span></p>
	<p><span><fmt:message key="user_oldpwd" /></span>:<span><input type="text" name="oldpwd" /></span></p>
	<p><input type="submit" value='<fmt:message key="text_confirm" />' /></p>
</form>