<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
<code id="pagename" style="display:none">c:index_a:info</code>
<ul class="meeting">
	<li class="line"><span class="tit"><fmt:message key="info_deviceNo"/>:</span><span class="cont">${info.model}</span></li>
	<li class="line"><span class="tit"><fmt:message key="info_deviceType"/>:</span><span class="cont">${info.serialNo}</span></li>
	<li class="line"><span class="tit"><fmt:message key="info_maxPlay"/>:</span><span class="cont">${info.uniVodMax}</span></li>
	<li class="line"><span class="tit"><fmt:message key="info_uniCur"/>:</span><span class="cont">${info.uniCur}</span></li>
	<li class="line"><span class="tit"><fmt:message key="info_vodCur"/>:</span><span class="cont">${info.vodCur}</span></li>
	<li class="line"><span class="tit"><fmt:message key="info_diskSize"/>:</span><span class="cont">${info.totalSize}</span></li>
	<li class="line"><span class="tit"><fmt:message key="info_diskFree"/>:</span><span class="cont">${info.freeSize}</span></li>
	<li class="line"><span class="tit"><fmt:message key="info_systemVersion"/>:</span><span class="cont">${info.systemVersion}</span></li>
	<li class="line"><span class="tit"><fmt:message key="info_webVersion"/>:</span><span class="cont">${info.webVersion}</span></li>
	<c:forEach items="${info.moduleList}" var="item">
	<li class="line"><span class="tit">${item.name}:</span><span class="cont">${item.version}</span><c:if test="${item.state == 1}"><span class="warn">(<fmt:message key="text_exception"/>)</span></c:if></li>
	</c:forEach>
</ul>