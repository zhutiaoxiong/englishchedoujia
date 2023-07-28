package view.view4control;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

import static com.tencent.open.utils.Global.getPackageName;


/**
 * 自启动跳转界面清单
 */

public class AutoStartAppUtil {
    public static final Intent[] POWERMANAGER_INTENTS = {
            new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
            new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
//            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
//            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity")),
            //new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.privacypermissionsentry.PermissionTopActivity")),
//            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity")),
//            new Intent().setComponent(new ComponentName("com.huawei.systemmanager",
//                    "com.huawei.systemmanager.mainscreen.MainScreenActivity")),
            new Intent().setComponent(new ComponentName( "com.android.settings","com.android.settings.BatteryInfo")),

            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
            new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
            new Intent().setComponent(new ComponentName("com.samsung.android.lool", "com.samsung.android.sm.battery.ui.BatteryActivity")),
            new Intent().setComponent(new ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity")),
            new Intent().setComponent(new ComponentName("com.htc.pitroad", "com.htc.pitroad.landingpage.activity.LandingPageActivity")),
            new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.MainActivity")),
            new Intent().setComponent(new ComponentName("com.transsion.phonemanager", "com.itel.autobootmanager.activity.AutoBootMgrActivity")),
            new Intent().setComponent(new ComponentName("com.meizu.safe", "com.meizu.safe.permission.SmartBGActivity")),
            new Intent().setComponent(new ComponentName("cn.nubia.security2", "cn.nubia.security.appmanage.selfstart.ui.SelfStartActivity")),
            new Intent().setComponent(new ComponentName("com.transsion.phonemaster", "com.cyin.himgr.applicationmanager.view.activities.AutoStartActivity")),
    };
    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 原生Android系统 开启后台运行调用这里
     */
    public void requestIgnoreBatteryOptimizations(Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 确定应用程序是否已经忽略电池优化
     *
     * @return
     */
    public static boolean isIgnoringBatteryOptimizations(Context context) {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String pakagename=context.getPackageName();
                isIgnoring = powerManager.isIgnoringBatteryOptimizations(context.getPackageName());
            }
        }
        return isIgnoring;
    }
    public static void jumpActivityTest(int i,Context context){
        Intent intent=new Intent();
        switch (i){
            case 1:
                //应用管理
                intent.setAction(Settings.ACTION_APPLICATION_SETTINGS);
                break;
            case 2:
                //应用管理
                intent.setAction(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                break;
            case 3:
                //系统应用管理
                intent.setAction(Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS);
                break;
            case 4:
                //关于手机界面
                intent.setAction(Settings.ACTION_DEVICE_INFO_SETTINGS);
                break;
            case 5:
                //关根据包名跳转到该app的应用信息界面
                Uri packageURI = Uri.parse("package:" + "com.client.proj.carpods");
//                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent=new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,packageURI);
//                intent.setPackage("com.client.proj.carpods");
                break;
            case 6:
                //设置界面
                intent.setAction(Settings.ACTION_SETTINGS);
                break;
            case 7:
                //安全设置界面
                intent.setAction(Settings.ACTION_SECURITY_SETTINGS);
                break;
            case 8:
                //安全设置界面
                intent.setAction(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS);
                break;
            case 9:
                //默认应用界面，指：短信的默认应用，浏览器的默认应用等
                intent.setAction(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS);
                break;
            case 10:
                //隐私管理界面
                intent.setAction(Settings.ACTION_PRIVACY_SETTINGS);
                break;
            case 11:
                //设置管理界面
                intent.setAction(Settings.ACTION_SEARCH_SETTINGS);
                break;
            case 12:
                //设置管理界面
                intent.setAction(Settings.ACTION_SEARCH_SETTINGS);
                break;
        }
        context.startActivity(intent);
    }

}
