<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
<code id="pagename" style="display:none">c:index_a:meeting</code>
<form action="" method="post" id="meeting_config">
	<input type="hidden" name="id" value='${meeting.id}' />
	<input type="hidden" name="channel" value='${meeting.channel}' />
	<ul class="meeting">
		<!-- 视频码率 -->
	    <li class="line">
	        <span class="tit"><fmt:message key="meeting_bitRate" />:</span>
	        <span class="cont">
	        <select name="bitRate" class="rate">
	            <option value="768" <c:if test="${meeting.bitRate == 768}">selected</c:if> >768kpbs</option>
	            <option value="1024" <c:if test="${meeting.bitRate == 1024}">selected</c:if> >1024kpbs</option>
	            <option value="2048" <c:if test="${meeting.bitRate == 2048}">selected</c:if> >2048kpbs</option>
	            <option value="4096" <c:if test="${meeting.bitRate == 4096}">selected</c:if> >4096kpbs</option>
	        </select>
	        </span> </li>
	        
	    <!-- 视频分辨率 -->
	    <li class="line">
	        <span class="tit"><fmt:message key="meeting_resolution" />:</span>
	        <span class="cont">
	        <select name="resolution" class="rate">
	            <option value="0" <c:if test="${meeting.resolution == 0}">selected</c:if> >720x480</option>
	            <option value="1" <c:if test="${meeting.resolution == 1}">selected</c:if> >1024x768</option>
	            <option value="2" <c:if test="${meeting.resolution == 2}">selected</c:if> >1280x720</option>
	            <option value="3" <c:if test="${meeting.resolution == 3}">selected</c:if> >1920x1024</option>
	        </select>
	        </span></li>
	    
	    <!-- 最大录制时间 -->
	    <li class="line">
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
            
        <!-- 数据源 -->
	    <li class="line datasource">
	        <span class="tit"><fmt:message key="meeting_datasource" />:</span>
	        <span class="cont">
	        
	        <p><a href="#" class="ds_add"><img src="${ctx}/resource/photo/add.png" onmousemove="this.src='${ctx}/resource/photo/add.png'" onmouseout="this.src='${ctx}/resource/photo/add_hover.png'" /></a></p>
	        
	        <ul id="dsList">
	        <c:forEach items="${meeting.dsList}" var="ds">
	        <li class="dsItem">
		        <input type="hidden" name="dsId" value="${ds.id}"/>
		        <input type="hidden" name="addr" value="${ds.addr}"/>
		        <input type="hidden" name="port" value="${ds.port}"/>
		        <input type="hidden" name="username" value="${ds.username}"/>
		        <input type="hidden" name="password" value="${ds.password}"/>
		        <input type="hidden" name="priority" value="${ds.priority}"/>
		        <span class="addr">${ds.addr}</span>
		        <c:if test="${ds.isConnect == 1}">
		        <img class="status" src="${ctx}/resource/photo/connected.gif" width="12" height="12" />
		        </c:if>
		        <c:if test="${ds.isConnect == 0}">
		        <img class="status" src="${ctx}/resource/photo/disconnected.gif" width="12" height="12" />
		        </c:if>
		        <img class="opert dsEdit" src="${ctx}/resource/photo/draw-freehand.png" />
		        <img class="opert dsDel" src="${ctx}/resource/photo/syicon_net.png" />
	        </li>
	        </c:forEach>
	        <li class="dsItem">
	        	<span class="addr">VGA</span>
	        </li>
	        </ul>
	        
	        </span>
	    </li>
	    
	    <!-- 是否录制 -->
	    <li class="line">
	        <span class="tit"><fmt:message key="meeting_useRecord" />:</span>
	        <span class="cont"><input name="useRecord" type="checkbox" value="1" <c:if test="${meeting.useRecord == 1}">checked</c:if> /><label><fmt:message key="meeting_record" /></label></span></li>
	        
	    <!-- 录制模式 -->
	    <li class="line">
	        <span class="tit"><fmt:message key="meeting_recordMode" />:</span>
	        <span class="cont layoutContainer">
	        <input type="hidden" name="recordMode" value="${meeting.recordMode}"/>
	        <img src="${ctx}/resource/photo/1-1.png" class="mode1 hide" id="v1"/>
	        
			<img src="${ctx}/resource/photo/2-1.png" class="mode2 hide" id="v2"/>
			<img src="${ctx}/resource/photo/2-2.png" class="mode2 hide" id="v3"/>
			<img src="${ctx}/resource/photo/2-5.png" class="mode2 hide" id="v6"/>
			<img src="${ctx}/resource/photo/2-6.png" class="mode2 hide" id="v7"/>
			<img src="${ctx}/resource/photo/2-7.png" class="mode2 hide" id="v8"/>
			<img src="${ctx}/resource/photo/2-8.png" class="mode2 hide" id="v9"/>
			
			<img src="${ctx}/resource/photo/3-1.png" class="mode3 hide" id="v10"/>
			<img src="${ctx}/resource/photo/3-2.png" class="mode3 hide" id="v11"/>
			<img src="${ctx}/resource/photo/3-3.png" class="mode3 hide" id="v12"/>
			<img src="${ctx}/resource/photo/3-4.png" class="mode3 hide" id="v13"/>
			<img src="${ctx}/resource/photo/3-5.png" class="mode3 hide" id="v14"/>
			<img src="${ctx}/resource/photo/3-6.png" class="mode3 hide" id="v15"/>
			</span>
	        </li>      
	    
	    <!-- 课程名称 -->
	    <li class="line">
	        <span class="tit"><fmt:message key="meeting_courseName" />:</span>
	        <span class="cont"><input name="courseName" type="text" class="form_text" value="${meeting.courseName}" /></span></li>
	    <li class="line">
	        <span class="tit"><fmt:message key="meeting_teacher" />:</span>
	        <span class="cont"><input name="teacher" type="text" class="form_text" value="${meeting.teacher}" /></span></li>
	    <li class="line">
	        <span class="tit"><fmt:message key="meeting_remark" />:</span>
	        <span class="cont"><textarea name="remark" cols="30" rows="4" class="form_text">${meeting.remark}</textarea></span></li>
	    <li class="line">
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
	
	<!-- 按钮集合 -->
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
		<b><fmt:message key="meeting_controller_disconnect" /></b>
	</c:if>
	</div>
</form>


