<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
<form action="${ctx}/file/fileEdit.action" method="post" id="file_fileEdit">
	<div class="window">
	    <div>
	        <dl>
	            <dt>文件编辑 <a href="#">&nbsp;&nbsp;&nbsp;</a></dt>
	            <dd><p>课程名称:</p><span><input name="text" type="text"  class="form_text"/></span></dd>
	            <dd><p>录制时间:</p><span><input name="text" type="text"  class="form_text"/></span></dd>
	            <dd><p>老师名称:</p><span><input name="text" type="text"  class="form_text"/></span></dd>
	            <dd><p>老师名称:</p><span><input name="text" type="text"  class="form_text"/></span></dd>
	            <dd><p>文件大小:</p><span><textarea name="textarea" cols="30" rows="4" class="form_text"></textarea></span></dd>
	            <dd><p>备注:</p><span><textarea name="textarea" cols="30" rows="2" class="form_text"></textarea></span></dd>
	        </dl>
	    </div>
	    <div class="clr"></div>
	    <div class="win_save"><a href="#" class="submmit">保存</a><a href="#">取消</a></div>
	</div>
</form>
