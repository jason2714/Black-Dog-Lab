package com.jason.blackdoglab.utils;

import android.app.Activity;
import android.os.Process;
import android.util.Log;

import com.jason.blackdoglab.DialogActivity;
import com.jason.blackdoglab.customclass.Dialog;
import com.jason.blackdoglab.loginpage.DailyLoginActivity;

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

    // 移除Activity
    public void cleanActivity(Activity currentActivity) {
//        不能在內直接remove null activity,因為for in 不能再回圈內更改
        for (Activity activity : activityList) {
            if (activity != null){
                if(activity != currentActivity)
                    activity.finish();
            }
        }
    }

    public void cleanActivityBesidesDialog(Activity currentActivity) {
//        不能在內直接remove null activity,因為for in 不能再回圈內更改
        for (Activity activity : activityList) {
            if (activity != null ){
                if(activity != currentActivity && !(activity instanceof DialogActivity))
                    activity.finish();
            }
        }
    }
    public void backToDialog() {
//        不能在內直接remove null activity,因為for in 不能再回圈內更改
        for (Activity activity : activityList) {
            if (activity != null && !(activity instanceof DialogActivity))
                    activity.finish();
        }
    }

    public void printActivity(){
        for (Activity activity : activityList) {
            if (activity != null)
                Utils.setLog(activity.getComponentName().getClassName());
        }
    }

    // traverse所有Activity並finish
    public void exitSystem() {
        cleanActivity(null);
        //
        android.os.Process.killProcess(Process.myPid());
        System.exit(0);
    }

    public DialogActivity getDialogActivity(){
        for(Activity activity : activityList)
            if(activity instanceof DialogActivity)
                return (DialogActivity)activity;
        return null;
    }

}