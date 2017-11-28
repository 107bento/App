package com.example.aishalien.bento;


import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * 使用單例模式創建 Activity 管理物件
 */

public class ApplicationBar extends Application {
    private List<Activity> activityList = new LinkedList<Activity>();
    private static ApplicationBar instance;
    private ApplicationBar (){
    }
    public static ApplicationBar getInstance(){
        if(null == instance){
            instance = new ApplicationBar();
        }
        return instance;
    }

    public void addActivity(Activity activity){
        activityList.add(activity);
    }
    public void exit(){
        for(Activity activity:activityList){
            activity.finish();
        }
        System.exit(0);
    }
}
