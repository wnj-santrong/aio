<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
<form action="${ctx}/tag/tagPost.action" method="post" id="play_tagPost" class="layout_form" style="width:440px; height:230px;">
	<input type="hidden" name="id" value="${tag.id}"/>
	<div class="window">
	    <div>
	        <dl>
	            <dt><fmt:message key="play_addGet"/><a href="javascript:void(0);" class="close">&nbsp;&nbsp;&nbsp;</a></dt>
	            <dd><span class="tit"><fmt:message key="play_tagName"/>:</span><span class="cont"><input name="tagName" type="text" class="form_text" value="${tag.tagName}" required /></span></dd>
	            <dd><span class="tit"><fmt:message key="play_priority"/>:</span><span class="cont"><input name="priority" type="text"  class="form_text" value="${tag.priority}"/></span></dd>
	        </dl>
	    </div>
	    <div class="win_save"><a href="javascript:void(0);" class="submit"><fmt:message key="text_save"/></a><a href="javascript:void(0);" class="close"><fmt:message key="text_cancel"/></a></div>
	</div>
</form>