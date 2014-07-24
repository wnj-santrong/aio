<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
<code id="pagename" style="display:none">c:index_a:file</code>
<div class="serch">
	<!-- 关键字 -->
    <p class="serch_img"><input type="text" name="keywork" class="serch_text" value="${query.keyword}" />
    <a href="#" class="search_btn"><img src="${ctx}/resource/photo/search_icon_03.png" border="0" />搜索</a>
    </p>
   <!-- 按钮 -->
   <span class="table_btn" style="position:static;float:left;"><a href="#" id="fileDel">删除</a><a href="#" id="fileDownload">下载</a><a href="#" id="fileEdit">编辑</a></span>
   <span class="table_btn_right" style="position:static;float:right;"><a href="#" id="filePlay"><img src="${ctx}/resource/photo/btn_watching.png"></a> </span>
</div>
<!-- 列表 -->
<table>
    <colgroup>
    <col width="4%" />
    <col width="25%" />
    <col width="15%" />
    <col width="15%" />
    <col width="10%" />
    <col width="10%" />
    <col width="10%" />
    <col width="10%" />
    </colgroup>
    <thead>
        <tr>
            <th>操作</th>
            <th>录制时间</th>
            <th>课程名称</th>
            <th>文件大小</th>
            <th>上传情况</th>
            <th>公开情况</th>
            <th>老师名称</th>
            <th>课程概要</th>
            <!-- 录制时长 -->
        </tr>
    </thead>
    <tbody>
		<c:forEach items="${fileList}" var="file">
		<tr>
		    <td><input type="checkbox" name="CheckboxGroup1" value="${file.id}" id="CheckboxGroup1_0" /></td>
		    <td>${file.duration}</td>
		    <td>${file.courseName}</td>
		    <td>${file.fileSize}</td>
		    <td>${file.status}</td>
		    <td>${file.level}</td>
		    <td>${file.teacher}</td>
		    <td>${file.remark}</td>
		</tr>
		</c:forEach>
    </tbody>
    <tfoot>
        <tr>
            <!-- <td colspan="8"><a href="#">上一页</a> <a href="#">1</a> <a href="#">2</a> <a href="#">3</a> <a href="#">4</a> <a href="#">5</a> <a href="#">下一页</a></td> -->
        </tr>
    </tfoot>
</table>
<!-- 分页 -->
<div id="pagination"></div>
<script type="text/javascript">
var fileCount = ${query.count};
var pageNum = ${query.pageNum};
var pageSize = ${query.pageSize};
var keyword = '${query.keyword}';
</script>


