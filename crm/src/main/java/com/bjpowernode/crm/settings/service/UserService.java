package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author: 22892
 * @date: 2022.11.26
 * @email: wgqcn@foxmail.com
 */
public interface UserService {
    /**
     * 通过账户密码查询账号
     * @param map
     * @return
     */
    User queryUserByLoginActAndPwd(Map<String, Object> map);

    /**
     * 查询所有用户
     * @return
     */
    List<User> queryAllUsers();
}
