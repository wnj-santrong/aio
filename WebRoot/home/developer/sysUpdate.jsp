<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<link rel="stylesheet" type="text/css" href="${ctx}/resource/css/reset.css" />
<style>
table {width:1030px; border-collapse:collapse; margin:10px 0 0 5px;}
table tr {border:1px #CCC solid; height:32px; line-height:32px;}
table th {height:30px; text-align:center;line-height:32px; font-size:16px; color:#333; background:#e2eef8;}
table tr td {text-align:center; color:#666; border:1px #ccc solid; overflow:hidden;}
</style>
</head>
<body>
	<table>
		<tr>
			<th>升级来源</th>
			<th>正在获取</th>
			<th>获取进度</th>
			<th>获取结果</th>
			<th>正在升级</th>
			<th>升级进度</th>
			<th>升级结果</th>
		</tr>
		<tr>
			<td>${updateSource}</td>
			<td>${uploading}</td>
			<td>${uploadPercent}</td>
			<td>${uploadResult}</td>
			<td>${updating}</td>
			<td>${updatePercent}</td>
			<td>${updateResult}</td>
		</tr>
	</table>
</body>
</html>