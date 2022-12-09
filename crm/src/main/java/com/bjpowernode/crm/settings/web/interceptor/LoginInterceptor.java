package com.bjpowernode.crm.settings.web.interceptor;

import com.bjpowernode.crm.commons.Constants;
import com.bjpowernode.crm.settings.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author: WGQ
 * @date: 2022.12.08
 * @email: wgqcn@foxmail.com
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        // 获取session
        HttpSession session = httpServletRequest.getSession();
        // 判断session是否为空
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        if(user == null){
            // 自行重定向
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath());
            return false;
        }
        // 不为空则放行
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
