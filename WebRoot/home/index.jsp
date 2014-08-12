<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.santrong.system.Global"%>
<%@ include file="inc/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<link rel="stylesheet" type="text/css" href="${ctx}/resource/css/reset.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/resource/css/boxy.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/resource/css/blue.css" />
<script type="text/javascript" src="${ctx}/resource/js/lang/message.zh_CN.js"></script>
<script type="text/javascript" src="${ctx}/resource/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/resource/js/jquery.pagination.js"></script>
<script type="text/javascript" src="${ctx}/resource/js/jquery.form.js"></script>
<script type="text/javascript" src="${ctx}/resource/js/jquery.boxy.js"></script>
<script type="text/javascript" src="${ctx}/resource/js/event.js"></script>
<script type="text/javascript" src="${ctx}/resource/js/init.js"></script>
<script type="text/javascript">
var Globals = {};
Globals.define = {};
Globals.ctx = "${ctx}";
</script>
</head>
<body>
<code id="pagename" style="display:none">c:index_a:index</code>
<div class="mainbav">
<div class="topnav">
    <div class="topnav_logo">
        <p><img src="${ctx}/resource/photo/logo.png" /></p>
        <h2><%=Global.Title%></h2>
        <span><a href="#" class="user_login"><fmt:message key="index_admin_login"/></a></span>
	</div>
</div>
<div class="column">
	<div class="sub_content">

	</div>
</div>
</div>
</body>
</html>