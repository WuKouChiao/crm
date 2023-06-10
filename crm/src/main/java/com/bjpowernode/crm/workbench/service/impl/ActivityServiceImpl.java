package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.mapper.ActivityMapper;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service("activityService")
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    ActivityMapper activityMapper;
    @Override
    public int insertActivity(Activity activity) {
        return activityMapper.insertSelective(activity);
    }

    @Override
    public List<Activity> selectActivityByConditionForPage(Map map) {
        return activityMapper.selectActivityByConditionForPage(map);
    }

    @Override
    public int selectCountOfActivityByCondition(Map<String, Object> map) {
        return activityMapper.selectCountOfActivityByCondition(map);
    }

    @Override
    public int deleteActivityByIds(String[] ids) {
        return activityMapper.deleteActivityByIds(ids);
    }

    @Override
    public Activity selectActivityByid(String id) {
        return activityMapper.selectActivityById(id);
    }

    @Override
    public int updateActivityByid(Activity activity) {
        return activityMapper.updateActivityById(activity);
    }
}
