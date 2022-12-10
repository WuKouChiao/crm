package com.bjpowernode.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
public class MainController {
    @RequestMapping("/workbench/main/index.do")
    public String toIndex(){
        return "workbench/main/index";
    }
}
