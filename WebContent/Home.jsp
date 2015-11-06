<%@ page language="java" import = "java.util.*,com.sshtools.j2ssh.transport.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>LogManager</title>
</head>
<body>
<form action = "LogManagerServlet?operation=Add" method = "post">
	<input type = "text" name = "UserName" required = "required" placeholder = "请输入用户名"><br>
	<input type = "text" name = "Password" required = "required" placeholder = "请输入登录密码"><br>
	<input type = "text" name = "LogPath" required = "required" size = "40" placeholder = "请输入日志路径"><br>	
	<textarea name = "Ip" rows = "3" cols = "40" placeholder = "请输入要添加的机器Ip，各Ip之间请务必用分号隔开"></textarea><br>
	<input type = "text" name = "Remarks" size = "40" placeholder = "输入Ip对应的机器名，务必用分号隔开"><br>
    <input type = "submit" value = "添加">
</form>	

<form action = "LogManagerServlet?operation=Delete" method = "post">
	<input type = "text" name = "LogPath" size = "40" required = "required" placeholder = "请输入日志路径"><br>
	<textarea name = "Ip" rows = "3" cols = "40" required = "required" placeholder = "请输入要删除的机器Ip，各Ip之间请务必用分号隔开"></textarea><br>
	<input type = "text" name = "Remarks" size = "40" placeholder = "输入Ip对应的机器名，务必用分号隔开"><br>
	<input type = "submit" value = "删除">
</form>

<form action = "LogManagerServlet?operation=Start" method = "post">
	<input type = "submit" value = "开始">
</form>
<div>
	<h3>说明：</h3>
	<ol>
		<li>当点击添加按钮时，将会把输入的机器节点的信息添加至配置文件中</li>
		<li>当点击删除按钮时，将会从配置文件中删除相应的节点</li>
		<li>当点击开始按钮时，程序将会根据配置文件，遍历每一个节点路径下的文件<br>如果找到某一个文件保存时间超过六个月或者其大小超过2G，则该文件将会被删除</li>
		<li>每次可以添加/删除一个日志路径下的多个Ip节点</li>
	</ol>
</div>
</body>
</html>