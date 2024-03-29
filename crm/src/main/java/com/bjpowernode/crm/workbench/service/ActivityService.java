package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author: WGQ
 * @date: 2022.12.10
 * @email: wgqcn@foxmail.com
 */
public interface ActivityService {
    /**
     * 新增市场活动
     * @param activity
     * @return
     */
    public int insertActivity(Activity activity);

    /**
     * 根据条件分页查询市场活动
     * @param map
     * @return
     */
    public List<Activity> selectActivityByConditionForPage(Map<String, Object> map);

    /**
     * 计算市场活动总数
     * @param map
     * @return
     */
    public int selectCountOfActivityByCondition(Map<String, Object> map);
    /**
     * 批量删除市场活动
     * @param ids
     * @return
     */
    public int deleteActivityByIds(String[] ids);
    /**
     *根据id查询市场活动
     * @param id
     * @return
     */
    public Activity selectActivityByid(String id);

    /**
     * 根据id更新市场活动
     * @param activity
     * @return
     */
    public int updateActivityByid(Activity activity);

    /**
     * 查询所有市场活动
     * @return
     */
    public List<Activity> queryAllActivitys();

    /**
     * 批量导入市场活动
     * @param activityList
     * @return
     */
    public int saveCreateActivityByList(List<Activity> activityList);
}
