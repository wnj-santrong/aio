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
	<h3>控制层</h3>
	<table>
		<tr>
			<th>IP</th>
			<th>端口</th>
			<th>用户名</th>
			<th>密码</th>
			<th>排序</th>
			<th>连接状态</th>
		</tr>
	</table>
	<h3>数据库</h3>
	<table>
		<tr>
			<th>IP</th>
			<th>端口</th>
			<th>用户名</th>
			<th>密码</th>
			<th>排序</th>
			<th>连接状态</th>
		</tr>
		<c:forEach items="${dsList}" var="item">
		<tr>
			<td>${item.addr}</td>
			<td>${item.port}</td>
			<td>${item.username}</td>
			<td>${item.password}</td>
			<td>${item.priority}</td>
			<td>${item.isConnect}</td>
		</tr>
		</c:forEach>
	</table>	
</body>
</html>