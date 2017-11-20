package com.freelift.service;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by ADMIN on 13-Feb-17.
 */

public class Helper {

    public static boolean isAppRunning(Context context, String packageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        for (int i = 0; i < procInfos.size(); i++) {
            if (procInfos.get(i).processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

}