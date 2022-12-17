<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript">
	$(function (){
		// 获取到页面对象
		$(window).keydown(function(e){
			// 获取到用户输入, 判断是否为回车
			if(e.keyCode==13){
				$("#loginBtn").click();
			}
		});
		// 登录按钮
		$("#loginBtn").click(function(){
			var loginAct = $.trim($("#loginAct").val());
			var loginPwd = $.trim($("#loginPwd").val());
			var isRemPwd = $("#isRemPwd").prop("checked");
			// 表单验证---判断是否为空, 不为空则发送请求
			if(loginAct == null || loginPwd == null || loginAct == "" || loginPwd == ""){
				alert("账户名或者密码不能为空!");
				return;
			}else{
				$.ajax({
					url: 'pages/settings/qx/user/login.do',
					data: {
						loginAct: loginAct,
						loginPwd: loginPwd,
						isRemPwd: isRemPwd
					},
					type: 'post',
					dataType: 'json',
					success: function(data){
						if(data.code == "1"){
							// 跳转到主页面
							window.location.href="workbench/index.do";
						}else{
							// 提示信息
							$("#msg").text(data.message);
						}
					},
					beforeSend: function(){
						$("#msg").text("正在校验!!!!!!!!");
						return true;
					}
				});
			}
		});
	});
</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2019&nbsp;动力节点</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.html" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input id="loginAct" class="form-control" type="text" value="${cookie.loginAct.value}" placeholder="用户名">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input id="loginPwd" class="form-control" type="password" value="${cookie.loginPwd.value}" placeholder="密码">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						<label>
							<c:if test="${not empty cookie.loginAct and not empty cookie.loginPwd}">
								<input id="isRemPwd" type="checkbox" checked> 十天内免登录
							</c:if>
							<c:if test="${empty cookie.loginAct or empty cookie.loginPwd}">
								<input id="isRemPwd" type="checkbox"> 十天内免登录
							</c:if>
						</label>
						&nbsp;&nbsp;
						<span id="msg"></span>
					</div>
					<button type="button" id="loginBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>