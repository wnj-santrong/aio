<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
<div id="setting_update">
	<dl class="system_set fr">
	    <dt><fmt:message key="setting_update" /></dt>
	    <dd>
	    	<form action="${ctx}/setting/updateLocal.action" method="post" enctype="multipart/form-data" id="local_update">
		        <p><fmt:message key="setting_localUpdate" />:</p>
		        <span>
		        	<input name="localfile" type="file" class="file_update"  required />
		        </span>
		        <span>
		        	<a href="javascript:void(0);" class="updateLocal"><fmt:message key="setting_update_do" /></a>
		        </span>
	        </form>
	    </dd>    
	    <dd>
	    	<form action="${ctx}/setting/updateOnlinePost.action" method="post" id="online_update">
		        <p><fmt:message key="setting_onlineUpdate" />:</p>
		        <span>
		        	<label><fmt:message key="setting_update_use" /></label>
		        	<input type="checkbox" name="autoUpdate" class="small" value="1"/>
		        </span>
		        <span>
					<select name="hours">
					    <option value="00">00</option><option value="01">01</option><option value="02">02</option><option value="03">03</option>
					    <option value="04">04</option><option value="05">05</option><option value="06">06</option><option value="07">07</option>
					    <option value="08">08</option><option value="09">09</option><option value="10">10</option><option value="11">11</option>
					    <option value="12">12</option><option value="13">13</option><option value="14">14</option><option value="15">15</option>
					    <option value="16">16</option><option value="17">17</option><option value="18">18</option><option value="19">19</option>
					    <option value="20">20</option><option value="21">21</option><option value="22">22</option><option value="23">23</option>
					</select>
					<label><fmt:message key="text_hours" /></label>
					<select name="minutes">
					    <option value="00">00</option>
					    <option value="05">05</option>
					    <option value="10">10</option>
					    <option value="15">15</option>
					    <option value="20">20</option>
					    <option value="25">25</option>
					    <option value="30">30</option>
					    <option value="35">35</option>
					    <option value="40">40</option>
					    <option value="45">45</option>
					    <option value="50">50</option>
					    <option value="55">55</option>
					</select>
					<label><fmt:message key="text_minutes" /></label>
		        </span>
		        <span>
		        	<a href="javascript:void(0);" class="submit"><fmt:message key="text_save" /></a>
		        </span>
	        </form>
	    </dd>
	    <dd>
	        <p><fmt:message key="setting_onlineUpdate2" />:</p>
	        <span>
	        	<a href="javascript:void(0);" class="updateNow"><fmt:message key="setting_update_check" /></a>
	        	<a href="javascript:void(0);" class="reboot"><fmt:message key="setting_reboot" /></a>
	        </span>
	    </dd>	    
	</dl>
</div>