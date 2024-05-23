package com.robot.geeui.setting.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.usage.StorageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.RecoverySystem;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

public class SystemUtils {
    private static String sSuAlias = "";

    public SystemUtils() {
    }

    @SuppressLint({"PrivateApi"})
    public static String getProperty(String key, String defaultValue) {
        String value = defaultValue;

        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            value = (String) ((String) get.invoke(c, key, defaultValue));
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return value;
    }

    @SuppressLint({"PrivateApi"})
    public static void setProperty(String key, String value) {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method set = c.getMethod("set", String.class, String.class);
            set.invoke(c, key, value);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public static long getMemSize(Context context, String type) {
        try {
            FileReader fr = new FileReader("/proc/meminfo");
            BufferedReader br = new BufferedReader(fr, 4096);
            String line = null;

            while ((line = br.readLine()) != null && !line.contains(type)) {
            }

            br.close();
            String[] array = line.split("\\s+");
            return Long.parseLong(array[1]) * 1024L;
        } catch (IOException var6) {
            var6.printStackTrace();
            return 0L;
        }
    }

    @RequiresApi(
            api = 26
    )
    public static long getStorageSize(Context context, UUID storageUuid) {
        try {
            StorageStatsManager stats = (StorageStatsManager) context.getSystemService(StorageStatsManager.class);
            return stats.getTotalBytes(storageUuid != null ? storageUuid : StorageManager.UUID_DEFAULT);
        } catch (IOException var3) {
            var3.printStackTrace();
            return 0L;
        }
    }

    public static long getStorageSize() {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return statFs.getTotalBytes();
    }

    public static String getSuAlias() {
        if (TextUtils.isEmpty(sSuAlias)) {
            sSuAlias = getProperty("ro.su_alias", "su");
        }

        return sSuAlias;
    }

    public static boolean isInChinese() {
        String pro = getProperty("persist.sys.region.language", "zh");
        if ("zh".equals(pro)) {
            return true;

        } else {
            return false;
        }
    }

    //自动回充前台
    public static boolean isAutoAppOnTheTop(Context context) {
        String activityName = getTopActivityName(context);
        if (activityName != null && (activityName.equals("top.keepempty.MainActivity"))) {
            return true;
        } else {
            return false;
        }

    }

    public static String getTopActivityName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(1);
        if (runningTasks != null && runningTasks.size() > 0) {
            ActivityManager.RunningTaskInfo taskInfo = runningTasks.get(0);
            ComponentName componentName = taskInfo.topActivity;
            if (componentName != null && componentName.getClassName() != null) {
                return componentName.getClassName();
            }
        }
        return null;
    }

    public static void restoreRobot(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                RecoverySystem.rebootWipeUserData(context.getApplicationContext());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                String packageName = context.getApplicationContext().getPackageName();
                Runtime.getRuntime().exec(new String[]{"su", "-c", "pm clear " + packageName}).waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getRomVersion() {
        //判断ROM版本号
        String displayVersion = Build.DISPLAY;
        String localVersion = "";
        if (displayVersion.startsWith("GeeUITest")) {
            localVersion = displayVersion.replace("GeeUITest.", "");
        } else if (displayVersion.startsWith("GeeUI")) {
            localVersion = displayVersion.replace("GeeUI.", "");
        }
        if (localVersion.endsWith("d")) {
            localVersion = localVersion.replace(".d", "");
        } else {
            localVersion = localVersion.replace(".u", "");
        }
        return localVersion;
    }

}

