<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
<form action="${ctx}/tag/tagPost.action" method="post" id="play_tagPost" class="layout_form" style="width:440px; height:230px;">
	<input type="hidden" name="id" value="${tag.id}"/>
	<div class="window">
	    <div>
	        <dl>
	            <dt><fmt:message key="play_addGet"/><a href="#" class="close">&nbsp;&nbsp;&nbsp;</a></dt>
	            <dd><p><fmt:message key="play_tagName"/>:</p><span><input name="tagName" type="text" class="form_text" value="${tag.tagName}" required /></span></dd>
	            <dd><p><fmt:message key="play_priority"/>:</p><span><input name="priority" type="text"  class="form_text" value="${tag.priority}"/></span></dd>
	        </dl>
	    </div>
	    <div class="win_save"><a href="#" class="submit"><fmt:message key="text_save"/></a><a href="#" class="close"><fmt:message key="text_cancel"/></a></div>
	</div>
</form>