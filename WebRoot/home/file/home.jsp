<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
<code id="pagename" style="display:none">c:index_a:file</code>
<table>
    <caption>
    <div class="table_btn"><a href="#" id="fileDel">删除</a><a href="#" id="fileDownload">下载</a><a href="#" id="fileEdit">编辑</a></div>
    <span class="table_btn_right"><a href="#" id="filePlay"><img src="${ctx}/resource/photo/btn_watching.png"></a> </span>
    </caption>
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
    <tbody id="fileList">

    </tbody>
    <tfoot>
        <tr>
            <!-- <td colspan="8"><a href="#">上一页</a> <a href="#">1</a> <a href="#">2</a> <a href="#">3</a> <a href="#">4</a> <a href="#">5</a> <a href="#">下一页</a></td> -->
        </tr>
    </tfoot>
</table>
<div id="pagination"></div>
<input type="hidden" name="fileCount" value="${query.count}" />
<input type="hidden" name="pageSize" value="${query.pageSize}" />


