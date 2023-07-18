<%--
  Created by IntelliJ IDEA.
  User: WuKouChiao
  Date: 2023/7/17
  Time: 20:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <title>演示文档下载</title>
    <script type="text/javascript">
        $(function (){
            // 下载按钮 点击事件
            $("#fileDownloadBtn").click(function () {
                window.location.href = "workbench/activity/fileDownload.do"
            });
        })
    </script>
</head>
<body>
<input type="button" value="下载" id="fileDownloadBtn">
</body>
</html>
