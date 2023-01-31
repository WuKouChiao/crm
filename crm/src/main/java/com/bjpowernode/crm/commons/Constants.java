package com.bjpowernode.crm.commons;

/**
 * Created with IntelliJ IDEA.
 * Description: 系统常量
 *
 * @author: WGQ
 * @date: 2022.11.26
 * @email: wgqcn@foxmail.com
 */
public class Constants {
    // 成功状态码
    public final static String RETURN_OBJECT_CODE_SUCCESS = "1";
    // 失败状态码
    public final static String RETURN_OBJECT_CODE_FAIL = "0";
    // 账户不锁定值
    public final static String NOT_LOCK_STATE = "1";
    // 保存当前用户的key
    public final static String SESSION_USER = "sessionUser";
    // 保存cookie的用户名的key
    public static final String COOKIE_ACT = "loginAct";
    // 保存cookie的用户密码的key
    public static final String COOKIE_PWD = "loginPwd";
    // 用于设置账户密码cookie过期时间 10天
    public static final int TEN_DAY = 10 * 24 * 60 * 60;
    // 用于设置账户密码cookie过期时间  0天
    public static final int ZERO_DAY = 0;
    // 字符串true
    public static final String TRUE_STRING = "true";
    // 字符串false
    public static final String FALSE_STRING = "false";
    // 用户列表
    public static final String USERLIST = "userList";
    // 返回信息 - 成功
    public static final String RETURN_MESSAGE_SUCCESS = "成功";
    // 返回信息 - 失败
    public static final String RETURN_MESSAGE_FAIL = "失败";
}
