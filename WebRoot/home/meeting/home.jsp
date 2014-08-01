<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
<code id="pagename" style="display:none">c:index_a:meeting</code>
<form action="" method="post" id="meeting_config">
	<input type="hidden" name="id" value='${meeting.id}' />
	<input type="hidden" name="channel" value='${meeting.channel}' />
	<ul class="meeting">
	    <li>
	        <span class="tit"><fmt:message key="meeting_bitRate" />:</span>
	        <span class="cont">
	        <select name="bitRate" class="rate">
	            <option value="768" <c:if test="${meeting.bitRate == 768}">selected</c:if> >768kpbs</option>
	            <option value="1024" <c:if test="${meeting.bitRate == 1024}">selected</c:if> >1024kpbs</option>
	            <option value="2048" <c:if test="${meeting.bitRate == 2048}">selected</c:if> >2048kpbs</option>
	            <option value="4096" <c:if test="${meeting.bitRate == 4096}">selected</c:if> >4096kpbs</option>
	        </select>
	        </span> </li>
	    <li>
	        <span class="tit"><fmt:message key="meeting_resolution" />:</span>
	        <span class="cont">
	        <select name="resolution" class="rate">
	            <option value="0" <c:if test="${meeting.resolution == 0}">selected</c:if> >720x480</option>
	            <option value="1" <c:if test="${meeting.resolution == 1}">selected</c:if> >1024x768</option>
	            <option value="2" <c:if test="${meeting.resolution == 2}">selected</c:if> >1280x720</option>
	            <option value="3" <c:if test="${meeting.resolution == 3}">selected</c:if> >1920x1024</option>
	        </select>
	        </span></li>
	    <li>
            <span class="tit"><fmt:message key="meeting_maxTime" />:</span>
            <span class="cont">
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
	        <span class="tit"><fmt:message key="meeting_datasource" />:</span>
	        <span class="cont dsList">
	        <a href="#" class="add">+</a><br/>
	        <c:forEach items="${meeting.dsList}" var="ds">
	        
	        <span class="dsItem">
	        <input type="hidden" name="dsId" value="${ds.id}"/>
	        192.168.1.1
	        <c:if test="${ds.isConnected == 1}">
	        <img class="rec_img" src="${ctx}/resource/photo/connected.gif" width="12" height="12" />
	        </c:if>
	        <c:if test="${ds.isConnected == 0}">
	        <img class="rec_img" src="${ctx}/resource/photo/disconnected.gif" width="12" height="12" />
	        </c:if>
	        <a href="#" class="dsEdit">上移</a>
	        <a href="#" class="dsEdit">下移</a>
	        <a href="#" class="dsEdit">修改</a>
	        <a href="#" class="dsDel">删除</a>
	        <br/>
	        </span>
	        
	        </c:forEach>
	        
	        </span>
	    </li>
	    
	    <li>
	        <span class="tit"><fmt:message key="meeting_useRecord" />:</span>
	        <span class="cont"><input name="useRecord" type="checkbox" value="1" <c:if test="${meeting.useRecord == 1}">checked</c:if> /><label><fmt:message key="meeting_record" /></label></span></li>
	    <li>
	        <span class="tit"><fmt:message key="meeting_recordMode" />:</span>
	        <span class="cont" id="layoutContainer">
	        <input type="hidden" name="recordMode" value="${meeting.recordMode}"/>
	        <c:if test="${fn:length(meeting.dsList) == 0}">
	        <!-- 0 + VGA -->
	        <img src="${ctx}/resource/photo/03_68.png" width="71" height="47" id="a1" />
	        </c:if>
			
			<c:if test="${fn:length(meeting.dsList) == 1}">
	        <!-- 1 + VGA -->
	        <img src="${ctx}/resource/photo/03_68.png" width="71" height="47" id="a2" />
	        <img src="${ctx}/resource/photo/03_68.png" width="71" height="47" id="a3" />
	        <img src="${ctx}/resource/photo/03_68.png" width="71" height="47" id="a4" />
	        <img src="${ctx}/resource/photo/03_68.png" width="71" height="47" id="a5" />
	        <img src="${ctx}/resource/photo/03_68.png" width="71" height="47" id="a6" />
	        <img src="${ctx}/resource/photo/03_68.png" width="71" height="47" id="a7" />
	        <img src="${ctx}/resource/photo/03_68.png" width="71" height="47" id="a8" />
	        <img src="${ctx}/resource/photo/03_68.png" width="71" height="47" id="a9" />
	        </c:if>
			
			<c:if test="${fn:length(meeting.dsList) == 2}">
	        <!-- 2 + VGA -->
	        <img src="${ctx}/resource/photo/03_68.png" width="71" height="47" id="a10" />
	        <img src="${ctx}/resource/photo/03_68.png" width="71" height="47" id="a11" />
	        <img src="${ctx}/resource/photo/03_68.png" width="71" height="47" id="a12" />
	        <img src="${ctx}/resource/photo/03_68.png" width="71" height="47" id="a13" />
	        <img src="${ctx}/resource/photo/03_68.png" width="71" height="47" id="a14" />
	        <img src="${ctx}/resource/photo/03_68.png" width="71" height="47" id="a15" />
	        </c:if>
	        
			</span>
	        </li>
	    <li>
	        <span class="tit"><fmt:message key="meeting_showName" />:</span>
	        <span class="cont"><input name="showName" type="text" class="form_text" value="${meeting.showName}" /></span></li>	        
	    <li>
	        <span class="tit"><fmt:message key="meeting_courseName" />:</span>
	        <span class="cont"><input name="courseName" type="text" class="form_text" value="${meeting.courseName}" /></span></li>
	    <li>
	        <span class="tit"><fmt:message key="meeting_teacher" />:</span>
	        <span class="cont"><input name="teacher" type="text" class="form_text" value="${meeting.teacher}" /></span></li>
	    <li>
	        <span class="tit"><fmt:message key="meeting_remark" />:</span>
	        <span class="cont"><textarea name="remark" cols="30" rows="4" class="form_text">${meeting.remark}</textarea></span></li>
	    <li>
	    	<span class="tit"><fmt:message key="meeting_status" />:</span>
	        <span class="cont">
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
	    <li class="canves"<c:if test="${meeting.isLive == 0}">style="display:none;"</c:if>></li>
	</ul>
	<div class="button_panel">
	<c:if test="${meeting.isConnect == 1}"><!-- 跟系统调度能连接上才显示 -->
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
	<c:if test="${meeting.isConnect == 0}">
		<b><fmt:message key="MaxBackupIndex" /></b>
	</c:if>
	</div>
</form>


