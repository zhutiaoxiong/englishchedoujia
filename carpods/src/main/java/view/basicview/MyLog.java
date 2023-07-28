package view.basicview;

import android.util.Log;

import com.client.proj.carpods.BuildConfig;

public class MyLog {
    public static void loge(String tag,String msg){
        if(BuildConfig.DEBUG){
            Log.e(tag,msg);
        }
    }
}
