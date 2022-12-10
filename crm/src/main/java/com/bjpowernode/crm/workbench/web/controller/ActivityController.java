package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.Constants;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        // 调用服务层查询所有用户
        List<User> users = userService.queryAllUsers();
        // 保存到作用域(request)
        request.setAttribute(Constants.USERLIST, users);
        // 请求转发
        return "workbench/activity/index";
    }
}
