package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.Constants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
}
