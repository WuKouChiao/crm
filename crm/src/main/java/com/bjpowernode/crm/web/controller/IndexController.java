package com.bjpowernode.crm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author: 22892
 * @date: 2022.11.20
 * @email: wgqcn@foxmail.com
 */
@Controller
public class IndexController {
    /**
     * 请求转发到欢迎页面
     * @return
     */
    @RequestMapping("/")
    public String index(){
        // 视图解析器已经配置前缀和后缀, 实际路径为 /WEB-INF/pages/index.jsp
        // 这里不用带斜杠是因为视图解析器中已经有了斜杠
        return "index";
    }
}
