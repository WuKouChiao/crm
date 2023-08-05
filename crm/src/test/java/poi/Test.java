package poi;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author: WGQ
 * @date: 2023.07.18
 * @email: wgqcn@foxmail.com
 */
public class Test {
    public static void main(String[] args) {
        ActivityServiceImpl activityService = new ActivityServiceImpl();
        List<Activity> activities = activityService.queryAllActivitys();
        System.out.println(activities);
    }
}
