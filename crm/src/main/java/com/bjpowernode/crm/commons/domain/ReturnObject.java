package com.bjpowernode.crm.commons.domain;

/**
 * Created with IntelliJ IDEA.
 * Description: 返回数据给前端的工具类
 *
 * @author: WGQ
 * @date: 2022.11.26
 * @email: wgqcn@foxmail.com
 */
public class ReturnObject {
    // 处理成功或者失败的标记: 1--成功  0--失败
    public String code;
    // 提示信息
    public String message;
    // 其他数据
    public Object returnObject;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getReturnObject() {
        return returnObject;
    }

    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }
}
