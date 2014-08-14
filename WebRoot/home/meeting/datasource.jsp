<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
<form action="${ctx}/datasource/dsPost.action" method="post" id="datasource_dsPost" class="layout_form">
	<input type="hidden" name="id" value="${ds.id}"/>
	<input type="hidden" name="meetingId" value="${ds.meetingId}"/>
	<div class="window">
	    <div>
	        <dl>
	            <dt><fmt:message key="meeting_datasource_config"/> <a href="javascript:void(0);" class="close">&nbsp;&nbsp;&nbsp;</a></dt>
	            <dd><span class="tit"><fmt:message key="meeting_datasource_addr"/>:</span><span class="cont"><input name="addr" type="text"  class="form_text" value="${ds.addr}" required_Ip /></span></dd>
	            <dd><span class="tit"><fmt:message key="meeting_datasource_port"/>:</span><span class="cont"><input name="port" type="text"  class="form_text" value="${ds.port}" /></span></dd>
	            <dd><span class="tit"><fmt:message key="meeting_datasource_username"/>:</span><span class="cont"><input name="username" type="text"  class="form_text" value="${ds.username}" /></span></dd>
	            <dd><span class="tit"><fmt:message key="meeting_datasource_password"/>:</span><span class="cont"><input name="password" type="text"  class="form_text" value="${ds.password}" /></span></dd>
	        </dl>
	    </div>
	    <div class="win_save"><a href="javascript:void(0);" class="submit"><fmt:message key="text_save"/></a><a href="javascript:void(0);" class="close"><fmt:message key="text_cancel"/></a></div>
	</div>
</form>