<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css"
          rel="stylesheet"/>

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <!--  PAGINATION plugin -->
    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>
    <script type="text/javascript">

        $(function () {
            // 打开创建市场活动模态窗口
            $("#createActivityBtn").click(function () {
                // 重置表单
                $("#createActivityForm").get(0).reset();
                // 弹出窗口
                $("#createActivityModal").modal("show");
            });
            // 创建市场活动
            $("#saveCreateActivityBtn").click(function () {
                // 获取数据
                var owner = $("#create-marketActivityOwner").val();
                var name = $.trim($("#create-marketActivityName").val());
                var startDate = $("#create-startTime").val();
                var endDate = $("#create-endTime").val();
                var cost = $.trim($("#create-cost").val());
                var description = $("#create-describe").val();
                // 校验数据
                if (owner == "") {
                    alert("拥有者不能为空");
                    return;
                }
                if (name == "") {
                    alert("活动名称不能为空");
                    return;
                }
                if (startDate != "" && endDate != "") {
                    // 开始日期和结束日期若不为空, 则结束日期不能在开始日期前
                    if (endDate < startDate) {
                        alert("结束日期不能小于开始日期");
                        return;
                    }
                }
                var regExp = /^(([1-9]\d*)|0)$/;
                if (!regExp.test(cost)) {
                    alert("成本只能为非负整数");
                    return;
                }
                // 发送ajax请求
                $.ajax({
                    url: 'workbench/activity/saveCreateActivity.do',
                    type: 'post',
                    data: {
                        owner: owner,
                        name: name,
                        startDate: startDate,
                        endDate: endDate,
                        cost: cost,
                        description: description
                    },
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == '1') {
                            // 关闭模态窗口
                            $("#createActivityModal").modal("hide");
                            // 刷新列表
                            var pageSize = $('#demo_pag1').bs_pagination('getOption', 'rowsPerPage');
                            queryActivityByConditionForPage(1, pageSize);
                        } else {
                            // 提示错误信息
                            alert(data.message);
                        }
                    },
                    error: function () {

                    }
                });
            });
            $(".mydate").datetimepicker({
                language: 'zh-CN',
                format: 'yyyy-mm-dd',
                minView: 'month',
                initialDate: new Date(),
                autoclose: true,
                todayBtn: true,
                clearBtn: true
            });
            // 进入市场活动页面立刻查询一次
            queryActivityByConditionForPage(1, 10);
            // 查询按钮绑定查询
            $('#queryActivityBtn').click(function () {
                var pageSize = $('#demo_pag1').bs_pagination('getOption', 'rowsPerPage');
                queryActivityByConditionForPage(1, pageSize);
            });
            // 列表全选按钮事件
            $('#checkAll').click(function () {
                $('#tbody input[type=checkbox]').prop('checked', this.checked);
            });
            // 通过.on来实现列表全选
            $("#tBody").on("click", "input[type='checkbox']", function () {
                // 如果列表中的checkbox全选, 则把"全选"按钮也选中
                if ($("#tBody input[type=checkbox]").size() == $("#tBody input[type=checkbox]:checked").size()) {
                    $("#checkAll").prop("checked", true);
                } else {
                    // 若至少有一个没选中, 则把全选按钮取消
                    $("#checkAll").prop("checked", false);
                }
            });
            // 删除按钮
            $("#deleteActivitBtn").click(function () {
                console.log('debug');
                // 获取删除列表
                var checkedIds = $("#tBody input[type=checkbox]:checked");
                var ids = "";
                // 校验是否选中
                if (checkedIds.size() < 1) {
                    alert("至少选中一条市场活动");
                    return;
                }
                // 拼接字符串
                $.each(checkedIds, function () {
                    ids += "id=" + this.value + "&";
                })
                ids = ids.substring(0, Number(ids.length - 1));
                // 提示确认删除
                if (confirm("确认删除?")) {
                    // 发送ajax请求
                    $.ajax({
                        url: 'workbench/activity/deleteActivityIds.do',
                        type: 'post',
                        data: ids,
                        dataType: 'json',
                        success: function (data) {
                            // 处理结果
                            if (data.code == '1') {
                                // 刷新列表
                                queryActivityByConditionForPage(1, $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
                            } else {
                                // 提示错误
                                alert(data.message);
                            }
                        }
                    });
                }
            });
            // 打开修改市场活动模态窗口
            $("#updateActivityBtn").click(function () {
                // 获取删除列表
                var checkedIds = $("#tBody input[type=checkbox]:checked");
                // 校验是否选中
                if (checkedIds.size() < 1) {
                    alert("请选择一条市场活动!");
                    return;
                } else if (checkedIds.size() > 1) {
                    alert("不允许选择多条市场活动!")
                    return;
                }
                var id = checkedIds[0].value;
                // 查询市场活动信息
                $.ajax({
                    url: "workbench/activity/queryActivityByid.do",
                    type: "post",
                    data: {id: id},
                    dataType: "json",
                    success: function (data) {
                        if (data == undefined || data == null || data == "") {
                            alert("未能找到该活动信息, 请联系管理员!");
                            return;
                        }
                        var id = data.id;
                        var owner = data.owner;
                        var name = data.name;
                        var startDate = data.startDate;
                        var endDate = data.endDate;
                        var cost = data.cost;
                        var description = data.description;
                        $("#edit-id").val(id);
                        $("#edit-marketActivityOwner").val(owner);
                        $("#edit-marketActivityName").val(name);
                        $("#edit-startTime").val(startDate);
                        $("#edit-endTime").val(endDate);
                        $("#edit-cost").val(cost);
                        $("#edit-describe").val(description)
                    }
                });
                // 重置表单
                // $("#editActivityModal").get(0).reset();
                // 弹出窗口
                $("#editActivityModal").modal("show");
            });
            // 更新市场活动按钮
            $("#editUpdate").click(function () {
                debugger;
                // 取值
                var id = $("#edit-id").val();
                var owner = $("#edit-marketActivityOwner").val();
                var name = $("#edit-marketActivityName").val();
                var startTime = $("#edit-startTime").val().toString();
                var endTime = $("#edit-endTime").val();
                var cost = $("#edit-cost").val();
                var describe = $("#edit-describe").val()
                // TODO 参数校验
                // 发送ajax请求
                $.ajax({
                    url: 'workbench/activity/updateAcitvityByid.do',
                    type: 'post',
                    data: {
                        id: id,
                        owner: owner,
                        name: name,
                        startDate: startTime,
                        endDate: endTime,
                        cost: cost,
                        describe: describe
                    },
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == 1) {
                            alert(data.message);
                            // 关闭修改模态窗口
                            $("#editActivityModal").modal("hide");
                            // 刷新列表
                            var pageSize = $('#demo_pag1').bs_pagination('getOption', 'rowsPerPage');
                            queryActivityByConditionForPage(1, pageSize);
                        } else {
                            alert(data.message);
                        }
                    }
                });
            });
            // 批量导出按钮点击事件
            $("#exportActivityAllBtn").click(function () {
                // 发送同步请求
                window.location.href = "workbench/activity/exprotAllActivity.do";
            });
            // 导入按钮点击事件
            $("#importActivityBtn").click(function () {
                debugger;
                // 收集参数
                var activityFile = $('#activityFile')[0].files[0]; // 获取文件对象
                var activityFileName = activityFile.name; // 获取文件名
                var suffix = activityFileName.substr(activityFileName.lastIndexOf('.')+1).toLocaleLowerCase();
                if(suffix != 'xls'){
                    alert('只支持xls文件');
                    return;
                }
                // FormData是ajax提供的接口,可以模拟键值对向后台提交参数;
                // FormData最大的优势是不但能提交文本数据，还能提交二进制数据
                var formData = new FormData();
                formData.append('activityFile',activityFile);
                formData.append('userName','KouChiao');
                // 发送ajax请求
                $.ajax({
                    url: 'workbench/activity/importActivity.do',
                    data: formData,
                    processData:false,//设置ajax向后台提交参数之前，是否把参数统一转换成字符串：true--是,false--不是,默认是true
                    contentType:false,//设置ajax向后台提交参数之前，是否把所有的参数统一按urlencoded编码：true--是,false--不是，默认是true
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        if(data.code == '1'){
                            // 成功提示
                            alert("成功导入"+data.retData+"条记录");
                            // 关闭模态窗口
                            $('#importActivityModal').modal("hide");
                            // 刷新活动市场页面, 保持页数不变
                            queryActivityByConditionForPage(1,$('#demo_pag1').bs_pagination('getOption','rowPerPage'));
                        }else{
                            alert(data.message);
                            return;
                        }
                    }
                });
            });

            /**
             * 根据页码和条数查询市场活动
             * @param pageNo 页码
             * @param pageSize 页数
             */
            function queryActivityByConditionForPage(pageNo, pageSize) {
                // 获取数据
                var name = $("#query-name").val();
                var owner = $("#query-owner").val();
                var startDate = $("#query-startDate").val();
                var endDate = $("#query-endDate").val();
                // var pageNo = 1;
                // var pageSize = 10;
                // 发送请求
                $.ajax({
                    url: 'workbench/activity/queryActivityByConditionForPage.do',
                    type: 'post',
                    data: {
                        name: name,
                        owner: owner,
                        startDate: startDate,
                        endDate: endDate,
                        beginNo: pageNo,
                        pageSize: pageSize
                    },
                    dataType: 'json',
                    success: function (data) {
                        // 显示总条数
                        $('#totalRowsB').text(data.totalRows);
                        // 添加记录
                        var htmlStr = '';
                        $.each(data.activityList, function (index, obj) {
                            htmlStr += "<tr class=\"active\">"
                            htmlStr += "	<td><input type=\"checkbox\" value=\"" + obj.id + "\"/></td>"
                            htmlStr += "	<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='detail.html';\">" + obj.name + "</a></td>"
                            htmlStr += "    <td>" + obj.owner + "</td>"
                            htmlStr += "	<td>" + obj.startDate + "</td>"
                            htmlStr += "	<td>" + obj.endDate + "</td>"
                            htmlStr += "</tr>"
                        });
                        $("#tBody").html(htmlStr);

                        // 计算总数
                        var totalPages = 1;
                        if (data.totalRows % pageSize == 0) {
                            totalPages = data.totalRows / pageSize;
                        } else {
                            totalPages = parseInt(data.totalRows / pageSize) + 1;
                        }

                        $("#demo_pag1").bs_pagination({
                            currentPage: pageNo,//当前页号,相当于pageNo

                            rowsPerPage: pageSize,//每页显示条数,相当于pageSize
                            totalRows: 1000,//总条数
                            totalPages: totalPages,  //总页数,必填参数.

                            visiblePageLinks: 5,//最多可以显示的卡片数

                            showGoToPage: true,//是否显示"跳转到"部分,默认true--显示
                            showRowsPerPage: true,//是否显示"每页显示条数"部分。默认true--显示
                            showRowsInfo: true,//是否显示记录的信息，默认true--显示

                            //用户每次切换页号，都自动触发本函数;
                            //每次返回切换页号之后的pageNo和pageSize
                            onChangePage: function (event, pageObj) { // returns page_num and rows_per_page after a link has clicked
                                //js代码
                                var pageNo = pageObj.currentPage;
                                var pageSize = pageObj.rowsPerPage;
                                queryActivityByConditionForPage(pageNo, pageSize);
                                // 取消全选按钮
                                $("#checkAll").prop("checked", false);
                            }
                        });

                    }
                });
            }
        });
    </script>
</head>
<body>

<!-- 创建市场活动的模态窗口 -->
<div class="modal fade" id="createActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" role="form" id="createActivityForm">

                    <div class="form-group">
                        <label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-marketActivityOwner">
                                <c:forEach items="${userList}" var="u">
                                    <option value="${u.id}">${u.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-marketActivityName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control mydate" id="create-startTime" readonly>
                        </div>
                        <label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control mydate" id="create-endTime" readonly>
                        </div>
                    </div>
                    <div class="form-group">

                        <label for="create-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-cost">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="create-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="create-describe"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button id="saveCreateActivityBtn" type="button" class="btn btn-primary">保存</button>
            </div>
        </div>
    </div>
</div>

<!-- 修改市场活动的模态窗口 -->
<div class="modal fade" id="editActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" role="form">

                    <div class="form-group">
                        <input type="hidden" id="edit-id">
                        <label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-marketActivityOwner">
                                <c:forEach items="${userList}" var="u">
                                    <option value="${u.id}">${u.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control mydate" id="edit-startTime" readonly>
                        </div>
                        <label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control mydate" id="edit-endTime" readonly>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-cost" value="5,000">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="edit-describe">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button id="editUpdate" type="button" class="btn btn-primary">更新</button>
            </div>
        </div>
    </div>
</div>

<!-- 导入市场活动的模态窗口 -->
<div class="modal fade" id="importActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
            </div>
            <div class="modal-body" style="height: 350px;">
                <div style="position: relative;top: 20px; left: 50px;">
                    请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                </div>
                <div style="position: relative;top: 40px; left: 50px;">
                    <input type="file" id="activityFile">
                </div>
                <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;">
                    <h3>重要提示</h3>
                    <ul>
                        <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                        <li>给定文件的第一行将视为字段名。</li>
                        <li>请确认您的文件大小不超过5MB。</li>
                        <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                        <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                        <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                        <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                    </ul>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
            </div>
        </div>
    </div>
</div>


<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>市场活动列表</h3>
        </div>
    </div>
</div>
<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">名称</div>
                        <input class="form-control" type="text" id="query-name">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input class="form-control" type="text" id="query-owner">
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">开始日期</div>
                        <input class="form-control" type="text" id="query-startDate"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">结束日期</div>
                        <input class="form-control" type="text" id="query-endDate">
                    </div>
                </div>

                <button type="button" class="btn btn-default" id="queryActivityBtn">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-primary" id="createActivityBtn"><span
                        class="glyphicon glyphicon-plus"></span> 创建
                </button>
                <button id="updateActivityBtn" type="button" class="btn btn-default"><span
                        class="glyphicon glyphicon-pencil"></span> 修改
                </button>
                <button id="deleteActivitBtn" type="button" class="btn btn-danger"><span
                        class="glyphicon glyphicon-minus"></span> 删除
                </button>
            </div>
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal">
                    <span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）
                </button>
                <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span
                        class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）
                </button>
                <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span
                        class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）
                </button>
            </div>
        </div>
        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input id="checkAll" type="checkbox"/></td>
                    <td>名称</td>
                    <td>所有者</td>
                    <td>开始日期</td>
                    <td>结束日期</td>
                </tr>
                </thead>
                <tbody id="tBody">
                <!-- <tr class="active">
                    <td><input type="checkbox" /></td>
                    <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">发传单</a></td>
                    <td>zhangsan</td>
                    <td>2020-10-10</td>
                    <td>2020-10-20</td>
                </tr>
                <tr class="active">
                    <td><input type="checkbox" /></td>
                    <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">发传单</a></td>
                    <td>zhangsan</td>
                    <td>2020-10-10</td>
                    <td>2020-10-20</td>
                </tr> -->
                </tbody>
            </table>
            <div id="demo_pag1"></div>
        </div>

        <%--			<div style="height: 50px; position: relative;top: 30px;">--%>
        <%--				<div>--%>
        <%--					<button type="button" class="btn btn-default" style="cursor: default;">共<b id="totalRowsB">50</b>条记录</button>--%>
        <%--				</div>--%>
        <%--				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">--%>
        <%--					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>--%>
        <%--					<div class="btn-group">--%>
        <%--						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">--%>
        <%--							10--%>
        <%--							<span class="caret"></span>--%>
        <%--						</button>--%>
        <%--						<ul class="dropdown-menu" role="menu">--%>
        <%--							<li><a href="#">20</a></li>--%>
        <%--							<li><a href="#">30</a></li>--%>
        <%--						</ul>--%>
        <%--					</div>--%>
        <%--					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>--%>
        <%--				</div>--%>
        <%--				<div style="position: relative;top: -88px; left: 285px;">--%>
        <%--					<nav>--%>
        <%--						<ul class="pagination">--%>
        <%--							<li class="disabled"><a href="#">首页</a></li>--%>
        <%--							<li class="disabled"><a href="#">上一页</a></li>--%>
        <%--							<li class="active"><a href="#">1</a></li>--%>
        <%--							<li><a href="#">2</a></li>--%>
        <%--							<li><a href="#">3</a></li>--%>
        <%--							<li><a href="#">4</a></li>--%>
        <%--							<li><a href="#">5</a></li>--%>
        <%--							<li><a href="#">下一页</a></li>--%>
        <%--							<li class="disabled"><a href="#">末页</a></li>--%>
        <%--						</ul>--%>
        <%--					</nav>--%>
        <%--				</div>--%>
        <%--			</div>--%>

    </div>

</div>
</body>
</html>