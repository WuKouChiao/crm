package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.Constants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.settings.service.UserService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author: WGQ
 * @date: 2022.12.10
 * @email: wgqcn@foxmail.com
 */
@Controller
public class ActivityController {
    @Autowired
    UserService userService;
    @Autowired
    ActivityService activityService;

    /**
     * 跳转到市场活动页面
     *
     * @param request
     * @return
     */
    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request) {
        // 调用服务层查询所有用户
        List<User> users = userService.queryAllUsers();
        // 保存到作用域(request)
        request.setAttribute(Constants.USERLIST, users);
        // 请求转发
        return "workbench/activity/index";
    }

    /**
     * 新增市场活动
     *
     * @param activity
     * @return
     */
    @ResponseBody
    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    public Object saveCreateActivity(Activity activity, HttpSession session) {
        // 保存参数 - id 创建者 更新者 创建时间 更新时间
        activity.setId(UUIDUtils.createUUID());
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        activity.setCreateBy(user.getId());
        activity.setEditBy(user.getId());
        String data = DateUtils.formartDateTime(new Date());
        activity.setCreateTime(data);
        activity.setEditTime(data);
        ReturnObject returnObject = new ReturnObject();
        try {
            // 执行新增
            int result = activityService.insertActivity(activity);
            // 判断是否成功
            if (result > 0) {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setMessage(Constants.RETURN_MESSAGE_SUCCESS);
            } else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage(Constants.RETURN_MESSAGE_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage(Constants.RETURN_MESSAGE_FAIL);
        }
        // 返回
        return returnObject;
    }

    /**
     * 查询市场活动
     *
     * @param name
     * @param owner
     * @param startDate
     * @param endDate
     * @param beginNo
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    public Map queryActivityByConditionForPage(String name, String owner, String startDate, String endDate,
                                               int beginNo, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("beginNo", (beginNo - 1) * pageSize);
        map.put("pageSize", pageSize);
        int i = activityService.selectCountOfActivityByCondition(map);
        List<Activity> activities = activityService.selectActivityByConditionForPage(map);
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("activityList", activities);
        returnMap.put("totalRows", i);
        return returnMap;
    }

    /**
     * 批量删除市场活动
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/workbench/activity/deleteActivityIds.do")
    public Object deleteActivityIds(String[] id) {
        ReturnObject returnObject = new ReturnObject();
        try {
            int i = activityService.deleteActivityByIds(id);
            if (i > 0) {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage(Constants.RETURN_MESSAGE_DELETE_ACTIVITY_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage(Constants.RETURN_MESSAGE_DELETE_ACTIVITY_FAIL);
        }
        return returnObject;
    }

    /**
     * 根据id查询市场活动信息
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/workbench/activity/queryActivityByid.do")
    public Object queryActivityByid(String id) {
        Activity activity = activityService.selectActivityByid(id);
        return activity;
    }

    @ResponseBody
    @RequestMapping("/workbench/activity/updateAcitvityByid.do")
    public Object updateActivityByid(Activity activity) {
        ReturnObject returnObject = new ReturnObject();
        int i = activityService.updateActivityByid(activity);
        if (i == 1) {
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setMessage(Constants.RETURN_MESSAGE_SUCCESS);
        } else {
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage(Constants.RETURN_MESSAGE_FAIL);
        }
        return returnObject;
    }

    /**
     * 导出文件测试
     *
     * @param response
     * @throws Exception
     */
    @RequestMapping("/workbench/activity/fileDownload.do")
    public void fileDownload(HttpServletResponse response) throws Exception {
        // 设置响应类型
        response.setContentType("application/octet-stream;charest=UTF-8");
        // 获取输入流
        ServletOutputStream outputStream = response.getOutputStream();
        // 浏览器接收到响应信息后, 默认情况下, 直接在显示窗口中打开响应信息; 即使打不开, 也会调用应用程序打开
        response.addHeader("Content-Disposition", "attachment;filename=mystudentList.xls");
        // 读取excel文件(InputStream), 把输出到浏览器(OutputStream)
        FileInputStream fileInputStream =
                new FileInputStream("D:\\WorkSpace\\crm\\crm\\src\\test\\java\\poi\\student.xls");
        byte[] bytes = new byte[256];
        int len = 0;
        while ((len = fileInputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, len);
        }
        // 关闭资源
        fileInputStream.close();
        // 不需要关闭, 因为不是此处创建的, tomcat会自己关闭, 但是需要刷新, 需要把写入缓存中的数据刷新, 防止影响其他功能
        outputStream.flush();
    }

    /**
     * 导出全部市场活动
     * @param response
     * @throws Exception
     */
    @RequestMapping("/workbench/activity/exprotAllActivity.do")
    public void exprotAllActivity(HttpServletResponse response) throws Exception {
        // 查询市场活动
        List<Activity> activities = activityService.queryAllActivitys();
        // 创建HSSFWork对象
        HSSFWorkbook sheets = new HSSFWorkbook();
        // 创建HSSFSheet对象
        HSSFSheet sheet = sheets.createSheet("市场活动列表");
        // 创建HSSFRow对象
        HSSFRow row = sheet.createRow(0);
        // 保存excel表格第一行
        String[] strings = {"名称", "所有者", "开始日期", "结束日期", "成本", "描述"};
        for (int i = 0; i < strings.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(strings[i]);
        }
        // 循环保存数据
        for (int i = 1; i < activities.size(); i++) {
            HSSFRow row1 = sheet.createRow(i);
            Activity activity = activities.get(i);
            HSSFCell cell = row1.createCell(0);
            cell.setCellValue(activity.getName());
            cell = row1.createCell(1);
            cell.setCellValue(activity.getOwner());
            cell = row1.createCell(2);
            cell.setCellValue(activity.getStartDate());
            cell = row1.createCell(3);
            cell.setCellValue(activity.getEndDate());
            cell = row1.createCell(4);
            cell.setCellValue(activity.getCost());
            cell = row1.createCell(5);
            cell.setCellValue(activity.getDescription());
        }
        FileOutputStream os =
                new FileOutputStream("D:\\WorkSpace\\crm\\crm\\src\\test\\excelFile\\activityList.xls");
        sheets.write(os);
        sheets.close();
        os.close();

        // 设置响应类型
        response.setContentType("application/octet-stream;charest=UTF-8");
        // 获取输入流
        ServletOutputStream outputStream = response.getOutputStream();
        // 浏览器接收到响应信息后, 默认情况下, 直接在显示窗口中打开响应信息; 即使打不开, 也会调用应用程序打开
        response.addHeader("Content-Disposition", "attachment;filename=mystudentList.xls");
        // 读取excel文件(InputStream), 把输出到浏览器(OutputStream)
        FileInputStream fileInputStream =
                new FileInputStream("D:\\WorkSpace\\crm\\crm\\src\\test\\excelFile\\activityList.xls");
        byte[] bytes = new byte[256];
        int len = 0;
        while ((len = fileInputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, len);
        }
        // 关闭资源
        fileInputStream.close();
        // 不需要关闭, 因为不是此处创建的, tomcat会自己关闭, 但是需要刷新, 需要把写入缓存中的数据刷新, 防止影响其他功能
        outputStream.flush();
    }
}
