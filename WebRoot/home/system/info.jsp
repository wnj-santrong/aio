<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${ctx}/resource/js/lang/message.zh_CN.js"></script>
<title></title>
</head>
<body>
	<div>
	</div>
	<div>
		<p><span><fmt:message key="system_deviceNo" /></span>:<span></span></p>
		<p><span><fmt:message key="system_deviceType" /></span>:<span></span></p>
		<p><span><fmt:message key="system_maxPlay" /></span>:<span>${info.uniVodMax }</span></p>
		<p><span><fmt:message key="system_diskSize" /></span>:<span></span></p>
		<p><span><fmt:message key="system_diskFree" /></span>:<span>${info.freeSize }</span></p>
		<p><span><fmt:message key="system_packageV" /></span>:<span></span></p>
		<p><span><fmt:message key="system_systemV" /></span>:<span></span></p>
		<p><span><fmt:message key="system_webV" /></span>:<span></span></p>
		<p><span><fmt:message key="system_cpuUse" /></span>:<span></span></p>
		<p><span><fmt:message key="system_memoryUse" /></span>:<span></span></p>
		<p><span><fmt:message key="system_lanUse" /></span>:<span></span></p>
		<p><span><fmt:message key="system_wanUse" /></span>:<span></span></p>
	</div>
</body>
</html>