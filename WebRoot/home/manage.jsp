<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
Globals.vedioCount = <%=Global.VedioCount%>;
</script>
</head>
<body>
<code id="pagename" style="display:none">c:index_a:manage</code>
<div class="mainbav">
<div class="topnav">
    <div class="topnav_logo">
        <p><img src="${ctx}/resource/photo/logo.png" /></p>
        <h2><%=Global.Title%></h2>
        <span><fmt:message key="index_welcome"/>${sessionScope.loginUser.username}<a href="#" class="logout_submit"><fmt:message key="index_logout"/></a></span>
	</div>
</div>
<div class="column">
    <div class="title_sid">
        <ul class="navigator">
        	<c:forEach items="${navigator}" var="item"><li><a href='javascript:void(0);' rel='${ctx}/${item.pageUrl}'><fmt:message key="menu_${item.menuName}" /></a></li></c:forEach>
        </ul>
    </div>
    <div class="submenu">
        <div class="sub_top"></div>
        <div class="sub_content">
        
        </div>
    </div>
</div>
</div>
</body>
</html>