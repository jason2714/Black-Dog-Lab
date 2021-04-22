package com.jason.blackdoglab.utils;

import android.app.Activity;
import android.os.Process;

import java.util.ArrayList;
import java.util.List;

public class ActivityUtils{
    private List<Activity> activityList = new ArrayList<>();
    private static ActivityUtils instance;

    // 单例模式中获取唯一的ExitApplication实例
    public static synchronized ActivityUtils getInstance() {
        if (null == instance) {
            instance = new ActivityUtils();
        }
        return instance;
    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        if (activityList == null)
            activityList = new ArrayList<>();
        activityList.add(activity);
    }

    // 移除Activity
    public void removeActivity(Activity activity) {
        if (activityList != null)
            activityList.remove(activity);
    }

    // traverse所有Activity並finish
    public void exitSystem() {
        for (Activity activity : activityList) {
            if (activity != null)
                activity.finish();
        }
        //
        android.os.Process.killProcess(Process.myPid());
        System.exit(0);
    }

}