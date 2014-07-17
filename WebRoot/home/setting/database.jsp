<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
	<dl class="system_set fr">
	    <dt><fmt:message key="setting_database" /></dt>
	    <dd>
	        <a href="#" id="dbbackup"><fmt:message key="setting_backup" /></a>
		</dd>
	    <dd>
	        <c:forEach items="${dbList}" var="item">
	        <ul>
	        	<li><span>${item.name}</span>-----<a href="#" class="restore">还原</a>---<a href="#" class="deldb">删除</a></li>
	        </ul>
	        </c:forEach>
		</dd>
	</dl>
