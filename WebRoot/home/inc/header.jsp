<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	// 拒绝直接访问JSP文件，所有动态请求必须经过过滤器和spring框架，然后才能把控制权转到JSP文件
	Object o = request.getAttribute("passFilter");
	if(o == null){
		//response.sendRedirect("/404.action");
	}
%>
<c:set var="ctx" value="${pageContext.request.contextPath}" ></c:set>

<fmt:setLocale value="zh_CN" /> 
<fmt:setBundle basename="message" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="${ctx}/resource/css/style.css" />
    <script type="text/javascript" src="${ctx}/js/lang/message.zh_CN.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/event.js"></script>
    <script type="text/javascript" src="${ctx}/js/init.js"></script>
