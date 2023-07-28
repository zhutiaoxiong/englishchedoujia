package view.view4control;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class FcfrtAppBhUtils {
    /**
     * 判断我们的应用是否在白名单中
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isIgnoringBatteryOptimizations(Context context) {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(context.getPackageName());
        }
        return isIgnoring;
    }

    /**
     * 申请加入白名单
     * @param context
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void requestIgnoreBatteryOptimizations(Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到指定应用的首页
     */
    private static void showActivity(Context context,@NonNull String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        context.startActivity(intent);
    }

    /**
     * 跳转到指定应用的指定页面
     */
    private static void showActivity(Context context, @NonNull String packageName, @NonNull String activityDir) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, activityDir));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 华为厂商判断
     * @return
     */
    public static boolean isHuawei() {
        if (Build.BRAND == null) {
            return false;
        } else {
            return Build.BRAND.toLowerCase().equals("huawei") || Build.BRAND.toLowerCase().equals("honor");
        }
    }

    /**
     * 小米厂商判断
     * @return
     */
    public static boolean isXiaomi() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("xiaomi");
    }
    /**
     * OPPO厂商判断
     * @return
     */
    public static boolean isOPPO() {
        return (Build.BRAND != null && Build.BRAND.toLowerCase().equals("oppo"))||(Build.BRAND != null && Build.BRAND.toLowerCase().equals("realme"));
    }
    /**
     * VIVO厂商判断
     * @return
     */
    public static boolean isVIVO() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("vivo");
    }

    /**
     * 魅族手机
     * @return
     */
    public static boolean isMeizu() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("meizu");
    }

    /**
     * 三星手机
     * @return
     */
    public static boolean isSamsung() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("samsung");
    }

    /**
     * 乐视手机
     * @return
     */
    public static boolean isLeTV() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("letv");
    }

    /**
     * 锤子手机
     * @return
     */
    public static boolean isSmartisan() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("smartisan");
    }

    /**
     * 跳转华为手机管家的启动管理页
     * 操作步骤：应用启动管理 -> 关闭应用开关 -> 打开允许自启动
     * @param context
     */
    public static void goHuaweiSetting(Context context) {
        try {
            showActivity(context,"com.huawei.systemmanager",
                    "com.huawei.systemmanager.mainscreen.MainScreenActivity");
        } catch (Exception e) {
            showActivity(context,"com.android.settings");
        }
    }

    /**
     * 跳转小米安全中心的自启动管理页面
     * 操作步骤：授权管理 -> 自启动管理 -> 允许应用自启动
     * @param context
     */
    public static void goXiaomiSetting(Context context) {
        try {
            showActivity(context,"com.miui.securitycenter",
                    "com.miui.permcenter.autostart.AutoStartManagementActivity");
        } catch (Exception e) {
            showActivity(context,"com.android.settings");
        }
    }

    /**
     * 跳转 OPPO 手机管家
     * 操作步骤：权限隐私 -> 自启动管理 -> 允许应用自启动
     * @param context
     */
    public static void goOPPOSetting(Context context) {
        try {
            showActivity(context,"com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity");
        } catch (Exception e1) {
            try {
                showActivity(context,"com.oppo.safe");
            } catch (Exception e2) {
                try {
                    showActivity(context,"com.coloros.oppoguardelf");
                } catch (Exception e3) {
                    showActivity(context,"com.coloros.safecenter");
                }
            }
        }
    }
    /**
     * 跳转 OPPO 手机管家
     * 操作步骤：权限隐私 -> 自启动管理 -> 允许应用自启动
     * @param context
     */
    public static void goOPPO(Context context) {
        try {
            Uri packageURI = Uri.parse("package:" + "com.client.proj.carpods");
            Intent  intent=new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,packageURI);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e1) {
            Log.e("","");
        }
    }


//                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);


    public static void VIVO(Activity activity) {
        Intent localIntent;
        try{
            if (((Build.MODEL.contains("Y85")) && (!Build.MODEL.contains("Y85A"))) || (Build.MODEL.contains("vivo Y53L"))) {
                localIntent = new Intent();
                localIntent.setClassName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.PurviewTabActivity");
                localIntent.putExtra("packagename", activity.getPackageName());
                localIntent.putExtra("tabId", "1");
                activity.startActivity(localIntent);
            } else {
                localIntent = new Intent();
                localIntent.setClassName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.SoftPermissionDetailActivity");
                localIntent.setAction("secure.intent.action.softPermissionDetail");
                localIntent.putExtra("packagename", activity.getPackageName());
                activity.startActivity(localIntent);
            }
        }catch (Exception e){
            showActivity(activity,"com.android.settings");
        }
    }


    /**
     * 跳转 VIVO 手机管家
     * 操作步骤：权限管理 -> 自启动 -> 允许应用自启动
     * @param context
     */
    public static void goVIVOSetting(Context context) {
        try {
            showActivity(context,"com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity");
        }catch (Exception e){
            showActivity(context,"com.android.settings");
        }
    }
    public static void goAndroidSetting(Context context) {
            showActivity(context,"com.android.settings");
    }


    /**
     * 跳转魅族手机管家
     * 操作步骤：权限管理 -> 后台管理 -> 点击应用 -> 允许后台运行
     * @param context
     */
    public static void goMeizuSetting(Context context) {
        try {
            showActivity(context,"com.meizu.safe");
        } catch (Exception e) {
            showActivity(context,"com.android.settings");
        }
    }

    /**
     * 跳转三星智能管理器
     * 操作步骤：自动运行应用程序 -> 打开应用开关 -> 电池管理 -> 未监视的应用程序 -> 添加应用
     * @param context
     */
    public static void goSamsungSetting(Context context) {
        try {
            showActivity(context,"com.samsung.android.sm_cn", "com.samsung.android.sm_cn.com.samsung.android.sm.ui.ram.AutoRunActivity");
        } catch (Exception e) {
            showActivity(context,"com.android.settings");
        }
    }

    /**
     *跳转乐视手机管家
     * 操作步骤：自启动管理 -> 允许应用自启动
     * @param context
     */
    public static void goLetvSetting(Context context) {
        try {
            showActivity(context,"com.letv.android.letvsafe",
                    "com.letv.android.letvsafe.AutobootManageActivity");
        } catch (Exception e) {
            showActivity(context,"com.android.settings");
        }
    }

    /**
     * 锤子手机
     * @param context
     */
    public static void goSmartisanSetting(Context context) {
        try {
            showActivity(context,"com.smartisanos.security");
        } catch (Exception e) {
            showActivity(context,"com.android.settings");
        }
    }
    public  static void toAutoStart( Context context){
        try {
            //打开白名单
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //
//                if (!FcfrtAppBhUtils.isIgnoringBatteryOptimizations(context)) {
                    //不在白名单中
                    //打开后台运行权限

                    FcfrtAppBhUtils.requestIgnoreBatteryOptimizations(context);


                    //判断是哪个厂家，打开对应手机管家设置，手动设置APP为白名单
                    if (FcfrtAppBhUtils.isXiaomi()) {
                        //小米手机
                        FcfrtAppBhUtils.goXiaomiSetting(context);
                    } else if (FcfrtAppBhUtils.isOPPO()) {
                        //oppo手机
                        FcfrtAppBhUtils.goOPPOSetting(context);
                    } else if (FcfrtAppBhUtils.isMeizu()) {
                        //魅族手机
                        FcfrtAppBhUtils.goMeizuSetting(context);
                    } else if (FcfrtAppBhUtils.isSamsung()) {
                        //三星手机
                        FcfrtAppBhUtils.goSamsungSetting(context);
                    } else if (FcfrtAppBhUtils.isLeTV()) {
                        //乐视手机
                        FcfrtAppBhUtils.goLetvSetting(context);
                    } else if (FcfrtAppBhUtils.isVIVO()) {
                        //vivo手机
                        FcfrtAppBhUtils.goVIVOSetting(context);
                    } else if (FcfrtAppBhUtils.isHuawei()) {
                        //华为手机
                        FcfrtAppBhUtils.goHuaweiSetting(context);
                    } else if (FcfrtAppBhUtils.isSmartisan()) {
                        //锤子手机
                        FcfrtAppBhUtils.goSmartisanSetting(context);
                    }
//                }else{
//                    Toast.makeText(context, "已经设置了电池白名单", Toast.LENGTH_SHORT).show();
//                }
            }
        }catch (Exception exception){
            Log.e("错误","错了");
        }
    }
}
