package com.zhangke.shizhong.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;

import com.zhangke.zlog.ZLog;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * APP 操作工具类
 * <p>
 * Created by ZhangKe on 2018/4/26.
 */

public class ApplicationInfoUtil {
    public static final int DEFAULT = 0; // 默认 所有应用
    public static final int SYSTEM_APP = DEFAULT + 1; // 系统应用
    public static final int NONSYSTEM_APP = DEFAULT + 2; // 非系统应用

    private static final String TAG = "ApplicationInfoUtil";

    /**
     * 根据包名获取相应的应用信息
     *
     * @return 返回包名所对应的应用程序的名称。
     */
    public static String getProgramNameByPackageName(Context context,
                                                     String packageName) {
        PackageManager pm = context.getPackageManager();
        String name = null;
        try {
            name = pm.getApplicationLabel(
                    pm.getApplicationInfo(packageName,
                            PackageManager.GET_META_DATA)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 获取手机所有应用信息
     */
    public static List<com.zhangke.shizhong.db.ApplicationInfo> getAllProgramInfo(Context context) {
        List<com.zhangke.shizhong.db.ApplicationInfo> list = new ArrayList<>();
        getAllProgramInfo(list, context, DEFAULT);
        return list;
    }

    /**
     * 获取手机所有应用信息
     *
     * @param type 标识符 是否区分系统和非系统应用
     */
    public static void getAllProgramInfo(List<com.zhangke.shizhong.db.ApplicationInfo> applist,
                                         Context context, int type) {
        List<PackageInfo> packages = context.getPackageManager()
                .getInstalledPackages(0);

        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            com.zhangke.shizhong.db.ApplicationInfo tmpInfo = new com.zhangke.shizhong.db.ApplicationInfo();
            tmpInfo.setAppName(packageInfo.applicationInfo.loadLabel(
                    context.getPackageManager()).toString());
            tmpInfo.setPackageName(packageInfo.packageName);
            tmpInfo.setVersionName(packageInfo.versionName);
            tmpInfo.setVersionCode(packageInfo.versionCode);
            tmpInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(context.getPackageManager()));
            switch (type) {
                case NONSYSTEM_APP:
                    if (!isSystemAPP(packageInfo)) {
                        tmpInfo.setIsSystemApp(false);
                        applist.add(tmpInfo);
                    }
                    break;
                case SYSTEM_APP:
                    if (isSystemAPP(packageInfo)) {
                        tmpInfo.setIsSystemApp(true);
                        applist.add(tmpInfo);
                    }
                    break;
                default:
                    tmpInfo.setIsSystemApp(isSystemAPP(packageInfo));
                    applist.add(tmpInfo);
                    break;
            }

            ArrayList<HanziToPinyin.Token> sort = HanziToPinyin.getInstance().get(tmpInfo.getAppName());
            if (sort == null || sort.isEmpty()) {
                tmpInfo.setSortTarget(tmpInfo.getAppName());
            } else {
                StringBuilder sbSort = new StringBuilder();
                for (HanziToPinyin.Token token : sort) {
                    if (!TextUtils.isEmpty(token.target)) {
                        sbSort.append(token.target.substring(0, 1));
                    }
                }
                tmpInfo.setSortTarget(sbSort.toString());
            }
        }
    }

    /**
     * 获取所有系统应用信息
     */
    public static List<com.zhangke.shizhong.db.ApplicationInfo> getAllSystemProgramInfo(Context context) {
        List<com.zhangke.shizhong.db.ApplicationInfo> systemAppList = new ArrayList<com.zhangke.shizhong.db.ApplicationInfo>();
        getAllProgramInfo(systemAppList, context, SYSTEM_APP);
        return systemAppList;
    }

    /**
     * 获取所有非系统应用信息
     */
    public static List<com.zhangke.shizhong.db.ApplicationInfo> getAllNoSystemProgramInfo(Context context) {
        List<com.zhangke.shizhong.db.ApplicationInfo> noSystemAppList = new ArrayList<com.zhangke.shizhong.db.ApplicationInfo>();
        getAllProgramInfo(noSystemAppList, context, NONSYSTEM_APP);
        return noSystemAppList;
    }

    /**
     * 判断是否是系统应用
     */
    public static Boolean isSystemAPP(PackageInfo packageInfo) {
        if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) { // 非系统应用
            return false;
        } else { // 系统应用
            return true;
        }
    }

    public static void doStartApplicationWithPackageName(Context context, String packageName) {
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        List<ResolveInfo> resolveinfoList = context.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            String pn = resolveinfo.activityInfo.packageName;
            String className = resolveinfo.activityInfo.name;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName(pn, className);
            intent.setComponent(cn);
            context.startActivity(intent);
        }
    }

    /**
     * 判断手机是否ROOT
     */
    public static boolean isRoot() {
        boolean root = false;
        try {
            if ((!new File("/system/bin/su").exists())
                    && (!new File("/system/xbin/su").exists())) {
                root = false;
            } else {
                root = true;
            }
        } catch (Exception e) {
        }
        return root;
    }

    /**
     * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
     *
     * @param command 命令： String apkRoot="chmod 777 "+getPackageCodePath();
     *                RootCommand(apkRoot);
     * @return 应用程序是/否获取Root权限
     */
    public static boolean RootCommand(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            ZLog.e("*** DEBUG ***", "ROOT REE" + e.getMessage());
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        ZLog.e("*** DEBUG ***", "Root SUC ");
        return true;
    }

    /**
     * 执行 shell 命令
     *
     * @param command shell 命令
     * @return 返回值
     */
    public static String execCommand(String command) {
        String result;
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(command);
            if (proc.waitFor() != 0) {
                result = "exit value = " + proc.exitValue();
                ZLog.e(TAG, result);
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            String line = null;
            while ((line = in.readLine()) != null) {
                stringBuffer.append(line + "-");
            }
            result = stringBuffer.toString();
        } catch (InterruptedException e) {
            result = "error: " + e.getMessage();
        } catch (IOException e) {
            result = "error:" + e.getMessage();
        }
        return result;
    }
}
