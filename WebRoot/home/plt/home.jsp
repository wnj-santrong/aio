<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
<code id="pagename" style="display:none">c:index_a:plt</code>
<form action="${ctx}/plt/save.action" method="post">
	<ul class="meeting">
		    <li class="line">
		        <span class="tit"><fmt:message key="plt_username" />:</span>
		        <span class="cont"><input name="username" type="text" class="form_text" value="${username}" required /></span>
		    </li>
		    <li class="line">
		        <span class="tit"><fmt:message key="plt_password" />:</span>
		        <span class="cont"><input name="password" type="password" class="form_text" value="" required /></span>
		    </li>
		    <li class="line">
		        <span class="tit"><fmt:message key="plt_conStatus" />:</span>
		        <span class="cont">
			        <c:if test="${conSuccess}"><fmt:message key="text_success" /></c:if>
			        <c:if test="${!conSuccess}"><fmt:message key="text_fail" /></c:if>
		        </span>
		    </li>		    
	</ul>
	<div class="button_panel">
		<a href="javascript:void(0);" class="submit"><fmt:message key="text_save" /></a>
	</div>
</form>