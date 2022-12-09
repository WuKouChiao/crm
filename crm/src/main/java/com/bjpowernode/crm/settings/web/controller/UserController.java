package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.Constants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author: 22892
 * @date: 2022.11.20
 * @email: wgqcn@foxmail.com
 */
@Controller
public class UserController {
    @Autowired
    UserService userService;

    /**
     * 转发到登录页面
     * @return
     */
    @RequestMapping("/pages/settings/qx/user/toLogin.do")
    public String toLogin() {
        // 请求转发
        return "settings/qx/user/login";
    }

    /**
     * 登录
     * @param loginAct
     * @param loginPwd
     * @param isRemPwd
     * @param request
     * @return
     */
    @RequestMapping("/pages/settings/qx/user/login.do")
    public @ResponseBody Object queryUserByLoginActAndPwd(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        // 封装参数
        Map<String, Object> map = new HashMap<>(3);
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);
        // 调用service
        User user = userService.queryUserByLoginActAndPwd(map);
        // 返回对象
        ReturnObject returnObject = new ReturnObject();
        // 判断
        if (user == null) {
            // 用户名错误或者密码错误
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("用户名或者密码错误");
        } else {
            String nowStr = DateUtils.formartDateTime(new Date());
            if (nowStr.compareTo(user.getExpireTime()) > 0) {
                // 账户过期
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账户过期");
            } else if (!user.getLockState().equals(Constants.NOT_LOCK_STATE)) {
                // 账户状态被锁定
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账户已被锁定");
            } else if (!user.getAllowIps().contains(request.getRemoteAddr())) {
                // 登录地址受限制
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("ip受限");
            }else{
                // 校验通过, 准备返回值
                session.setAttribute(Constants.SESSION_USER, user);
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                // 记住密码功能
                if(Constants.TRUE_STRING.equals(isRemPwd)){
                    // 记住密码, 则返回保存有账户密码的cookie
                    Cookie c1 = new Cookie(Constants.COOKIE_ACT, user.getLoginAct());
                    Cookie c2 = new Cookie(Constants.COOKIE_PWD, user.getLoginPwd());
                    c1.setMaxAge(Constants.TEN_DAY);
                    c2.setMaxAge(Constants.TEN_DAY);
                    response.addCookie(c1);
                    response.addCookie(c2);
                }else{
                    // 不记住密码, 则返回无数据的cookie, 并且设置过期时间为0
                    Cookie c1 = new Cookie(Constants.COOKIE_ACT, "1");
                    Cookie c2 = new Cookie(Constants.COOKIE_PWD, "1");
                    c1.setMaxAge(Constants.ZERO_DAY);
                    c2.setMaxAge(Constants.ZERO_DAY);
                    response.addCookie(c1);
                    response.addCookie(c2);
                }
            }
        }
        // 返回结果
        return returnObject;
    }

    /**
     * 安全退出
     * @param response
     * @param session
     * @return
     */
    @RequestMapping("/pages/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response, HttpSession session){
        // 清空session
        session.invalidate();
        // 清空cookie
        Cookie c1 = new Cookie(Constants.COOKIE_ACT, "1");
        Cookie c2 = new Cookie(Constants.COOKIE_PWD, "1");
        c1.setMaxAge(Constants.ZERO_DAY);
        c2.setMaxAge(Constants.ZERO_DAY);
        response.addCookie(c1);
        response.addCookie(c2);
        // 跳转回主页(即登录页面)
        return "redirect:/";
    }
}
