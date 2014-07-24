<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="inc/common.jsp"%>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<title><fmt:message key="index_userLogin"/></title>
<link rel="stylesheet" type="text/css" href="${ctx}/resource/css/boxy.css" />
<script type="text/javascript" src="${ctx}/resource/js/lang/message.zh_CN.js"></script>
<script type="text/javascript" src="${ctx}/resource/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/resource/js/jquery.form.js"></script>
<script type="text/javascript" src="${ctx}/resource/js/jquery.boxy.js"></script>
<script type="text/javascript" src="${ctx}/resource/js/event.js"></script>
<script type="text/javascript" src="${ctx}/resource/js/init.js"></script>
<script type="text/javascript">
Globals = {};
Globals.define = {};
Globals.ctx = "${ctx}";
</script>
<style>
* {	margin: 0;	padding: 0;}
body, html {margin: 0;padding: 0;background: url(${ctx}/resource/photo/login_bg.png); text-align:center; color:#fff;}
.login {width:730px;height:400px;background: url(${ctx}/resource/photo/login_bg_2.png) no-repeat left bottom;	margin: 0 auto;	position: absolute;	top: 50%;left: 50%;	overflow: hidden;	margin-top: -200px;	margin-left: -365px;}

.lonin_con { position:relative; width:730px; height:400px;}
.login_titile　h1{ width:730px; height:60px; color:#fff; text-align:center; overflow:hidden;}
.login_logo {position:absolute;	left:50px;	top:200px;	width: 240px; height:150px;}
.login_name {position:absolute;	left:300px;	top:170px;	width:360px;}
.login_name p { margin:15px 0; height:36px;}
.login_name label{ float:left; display:block; width:120px; height:36px; line-height:32px; text-align:right; color:#000;}
.login_name .login_text {float:left; display:block;background:none;background:url(${ctx}/resource/photo/text_bg.png) no-repeat left top; width:198px; height:36px;  border:none; text-indent:10px; line-height:32px;}
.login_name .login_a { margin-top:20px;}
.login_name .login_a a{ width:112px; height:36px; background:url(${ctx}/resource/photo/login_btn.png) no-repeat left top; display:block; text-align:center; line-height:38px; font-size:20px; letter-spacing:2px; text-decoration:none; color:#222; margin-left:120px;}
.login_name .login_a a:hover{ background:url(${ctx}/resource/photo/login_btn_hove.png) no-repeat left top; color:#fff; font-size:22px; font-weight:600;}
</style>
</head>
<body>
<code id="pagename" style="display:none">c:index_a:login</code>
<form action="${ctx}/login.action" method="post" id="index_login">
	<div class="login">
	    <div class="lonin_con">
	    <div class="login_titile"><h1>xxxx统工程系统工程系统工程系统工程</h1></div>
	        <div class="login_logo"><img src="${ctx}/resource/photo/login_logo.png" width="230" height="101"></div>
	        <div class="login_name">
	            <p><label><fmt:message key="index_login_username"/>：</label>
	            <input type="text" name="username" class="login_text"/></p>
	            
	            <p><label><fmt:message key="index_login_password"/>：</label>
	            <input type="text" name="password" class="login_text"/></p>
	            <p class="login_a"><a href="#" class="login_submit"><fmt:message key="index_login_login"/></a></p>
	        </div>
	    </div>
	</div>
</form>
</body>
</html>
