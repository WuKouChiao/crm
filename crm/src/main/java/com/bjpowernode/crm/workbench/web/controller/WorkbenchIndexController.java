package com.bjpowernode.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author: WGQ
 * @date: 2022.11.29
 * @email: wgqcn@foxmail.com
 */
@Controller
public class WorkbenchIndexController {
    /**
     * 登录成功跳转到主页面
     * @return
     */
    @RequestMapping("/workbench/index.do")
    public String toIndex(){
        return "workbench/index";
    }
}
