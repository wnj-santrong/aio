<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
<code id="pagename" style="display:none">c:index_a:meeting</code>
<form action="" method="post" id="meeting_config">
	<input type="hidden" name="id" value='${meeting.id}' />
	<input type="hidden" name="channel" value='${meeting.channel}' />
	<ul class="meeting">
	    <li>
	        <p><fmt:message key="meeting_bitRate" />:</p>
	        <span>
	        <select name="bitRate" class="rate">
	            <option value="768" <c:if test="${meeting.bitRate == 768}">selected</c:if> >768kpbs</option>
	            <option value="1024" <c:if test="${meeting.bitRate == 1024}">selected</c:if> >1024kpbs</option>
	            <option value="2048" <c:if test="${meeting.bitRate == 2048}">selected</c:if> >2048kpbs</option>
	            <option value="4096" <c:if test="${meeting.bitRate == 4096}">selected</c:if> >4096kpbs</option>
	        </select>
	        </span> </li>
	    <li>
	        <p><fmt:message key="meeting_resolution" />:</p>
	        <span>
	        <select name="resolution" class="rate">
	            <option value="0" <c:if test="${meeting.resolution == 0}">selected</c:if> >720x480</option>
	            <option value="1" <c:if test="${meeting.resolution == 1}">selected</c:if> >1024x768</option>
	            <option value="2" <c:if test="${meeting.resolution == 2}">selected</c:if> >1280x720</option>
	            <option value="3" <c:if test="${meeting.resolution == 3}">selected</c:if> >1920x1024</option>
	        </select>
	        </span></li>
	    <li>
            <p><fmt:message key="meeting_maxTime" />:</p>
            <span>
            <select name="maxTime" class="rate">
                <option value="60" <c:if test="${meeting.maxTime == 60}">selected</c:if> >1小时</option>
                <option value="120" <c:if test="${meeting.maxTime == 120}">selected</c:if> >2小时</option>
                <option value="180" <c:if test="${meeting.maxTime == 180}">selected</c:if> >3小时</option>
                <option value="240" <c:if test="${meeting.maxTime == 240}">selected</c:if> >4小时</option>
                <option value="300" <c:if test="${meeting.maxTime == 300}">selected</c:if> >5小时</option>
                <option value="360" <c:if test="${meeting.maxTime == 360}">selected</c:if> >6小时</option>
                <option value="420" <c:if test="${meeting.maxTime == 420}">selected</c:if> >7小时</option>
                <option value="480" <c:if test="${meeting.maxTime == 480}">selected</c:if> >8小时</option>
            </select>
            </span></li>
	    <li>
	        <p><fmt:message key="meeting_datasource" />:</p>
	        <span><input name="datasource1" type="text" value="" class="Data_address"/></span>
	        <span class="rec_img"><img src="${ctx}/resource/photo/0222.gif" width="12" height="12" /></span>
	        <a href="#" class="add"><fmt:message key="text_add" /></a>
	    </li>
	    
	    <li>
	        <p><fmt:message key="meeting_useRecord" />:</p>
	        <span><input name="useRecord" type="checkbox" value="1" <c:if test="${meeting.useRecord == 1}">checked</c:if> /><label><fmt:message key="meeting_record" /></label></span></li>
	    <li>
	        <p><fmt:message key="meeting_recordMode" />:</p>
	        <span>
	        <img src="${ctx}/resource/photo/03_68.png" width="71" height="47" /><img src="${ctx}/resource/photo/03_70.png" width="71" height="47" /><img src="${ctx}/resource/photo/03_72.png" width="71" height="47" /> </span>
	        </li>
	    <li>
	        <p><fmt:message key="meeting_showName" />:</p>
	        <span><input name="showName" type="text" class="form_text" value="${meeting.showName}" /></span></li>	        
	    <li>
	        <p><fmt:message key="meeting_courseName" />:</p>
	        <span><input name="courseName" type="text" class="form_text" value="${meeting.courseName}" /></span></li>
	    <li>
	        <p><fmt:message key="meeting_teacher" />:</p>
	        <span><input name="teacher" type="text" class="form_text" value="${meeting.teacher}" /></span></li>
	    <li>
	        <p><fmt:message key="meeting_remark" />:</p>
	        <span><textarea name="remark" cols="30" rows="4" class="form_text">${meeting.remark}</textarea></span></li>
	    <li>
	    	<p><fmt:message key="meeting_status" />:</p>
	        <span>
	        <b>
	        <c:if test="${meeting.isLive == 0}">
	        	<fmt:message key="meeting_not_inMeeting" />
	        </c:if>
	        <c:if test="${meeting.isLive == 1 && meeting.isRecord == 0}">
	        	<fmt:message key="meeting_inMeeting" />
	        </c:if>
	        <c:if test="${meeting.isLive == 1 && meeting.isRecord == 1}">
	        	<fmt:message key="meeting_inRecord" />
	        </c:if>
	        </b>
	        </span>
	    </li>
	</ul>
	<div class="button_panel">
		<span>
		<c:if test="${meeting.isConnected == 1}"><!-- 跟系统调度能连接上才显示 -->
		<c:if test="${meeting.isLive == 0}">
			<a href="#" class="save"><fmt:message key="text_save" /></a>
			<a href="#" class="openLive"><fmt:message key="meeting_openLive" /></a>
		</c:if>
		<c:if test="${meeting.isLive == 1}">
			<a href="#" class="closeLive"><fmt:message key="meeting_closeLive" /></a>
		</c:if>
		<c:if test="${meeting.isLive == 1 && meeting.isRecord == 0}">
			<a href="#" class="startRecord"><fmt:message key="meeting_startRecord" /></a>
		</c:if>
		<c:if test="${meeting.isLive == 1 && meeting.isRecord == 1}">
			<a href="#" class="stopRecord"><fmt:message key="meeting_stopRecord" /></a>
		</c:if>
		</c:if>
		</span>
	</div>
</form>
<script type="text/javascript">
var isLive = ${meeting.isLive};
</script>


