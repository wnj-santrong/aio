<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
<form action="${ctx}/datasource/dsPost.action" method="post" id="datasource_dsPost">
	<input type="hidden" name="id" value="${ds.id}"/>
	<input type="hidden" name="meetingId" value="${ds.meetingId}"/>
	<div class="window">
	    <div>
	        <dl>
	            <dt><fmt:message key="meeting_datasource_config"/> <a href="#" class="close">&nbsp;&nbsp;&nbsp;</a></dt>
	            <dd><p><fmt:message key="meeting_datasource_addr"/>:</p><span><input name="addr" type="text"  class="form_text" value="${ds.addr}"/></span></dd>
	            <dd><p><fmt:message key="meeting_datasource_port"/>:</p><span><input name="port" type="text"  class="form_text" value="${ds.port}" /></span></dd>
	            <dd><p><fmt:message key="meeting_datasource_username"/>:</p><span><input name="username" type="text"  class="form_text" value="${ds.username}" /></span></dd>
	            <dd><p><fmt:message key="meeting_datasource_password"/>:</p><span><input name="password" type="text"  class="form_text" value="${ds.password}" /></span></dd>
	            <dd><p><fmt:message key="meeting_datasource_priority"/>:</p><span><input name="priority" type="text"  class="form_text" value="${ds.priority}" /></span></dd>
	        </dl>
	    </div>
	    <div class="clr"></div>
	    <div class="win_save"><a href="#" class="submit"><fmt:message key="text_save"/></a><a href="#" class="close"><fmt:message key="text_cancel"/></a></div>
	</div>
</form>