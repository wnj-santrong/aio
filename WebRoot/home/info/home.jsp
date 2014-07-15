<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<fmt:setLocale value="zh_CN" /> 
<fmt:setBundle basename="message" />
<code id="pagename" style="display:none">c:index_a:info</code>
<ul class="meeting">
	<li><p><fmt:message key="info_deviceNo"/>:</p><span>${info.deviceNo}</span></li>
	<li><p><fmt:message key="info_deviceType"/>:</p><span>${info.deviceType}</span></li>
	<li><p><fmt:message key="info_maxPlay"/>:</p><span>${info.uniVodMax}</span></li>
	<li><p><fmt:message key="info_uniCur"/>:</p><span>${info.uniCur}</span></li>
	<li><p><fmt:message key="info_vodCur"/>:</p><span>${info.vodCur}</span></li>
	<li><p><fmt:message key="info_diskSize"/>:</p><span>${info.totalSize}</span></li>
	<li><p><fmt:message key="info_diskFree"/>:</p><span>${info.freeSize}</span></li>
	<c:forEach items="${info.moduleList}" var="item">
	<li><p>${item.name}:</p><span>${item.version}<c:if test="${item.state == 1}"><span class="warn">(<fmt:message key="text_exception"/>)</span></c:if></span></li>
	</c:forEach>
</ul>