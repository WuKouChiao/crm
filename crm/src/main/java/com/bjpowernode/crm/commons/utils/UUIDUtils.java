package com.bjpowernode.crm.commons.utils;

import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author: WGQ
 * @date: 2022.12.11
 * @email: wgqcn@foxmail.com
 */
public class UUIDUtils {
    public static String createUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
