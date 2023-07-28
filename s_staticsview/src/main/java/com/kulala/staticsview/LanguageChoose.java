package com.kulala.staticsview;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.util.Log;

import com.kulala.staticsfunc.dbHelper.ODBHelper;

import java.util.Locale;

/**
 * Created by Nathan_Lee on 2016/11/17.
 * 选择不同语言
 */

public class LanguageChoose {
    //示例：LanguageChoose.choose(Locale.US);
//    public static void choose(Locale language) {
//        Resources      resources = GlobalContext.getContext().getResources();
//        DisplayMetrics dm        = resources.getDisplayMetrics();
//        Configuration  config    = resources.getConfiguration();
//        // 应用用户选择语言
//        config.setLocale(language);//Locale.US
//        resources.updateConfiguration(config, dm);
//        //以下事件发送其它页面，语言改变了，只需四个主页面动态改，其它页面自动刷新时获取
//        ODispatcher.dispatchEvent(OEventName.LANGUAGE_CHANGE);
//    }

    public static Context setApplicationLanguage(Context context,Locale locale) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList localeList = new LocaleList(locale);
            LocaleList.setDefault(localeList);
            config.setLocales(localeList);
            Locale.setDefault(locale);
            context.createConfigurationContext(config);
          Locale locale1=  LocaleList.getDefault().get(0);
            Log.e("麻痹操蛋","locale1"+locale1.getLanguage());
        } else {
            config.setLocale(locale);//Locale.US
        }
        resources.updateConfiguration(config, dm);
        return context;
    }
    public static Context setLanguage(Context context){
       return  setApplicationLanguage(context,Locale.US);
    }
    public static boolean isZh(Context context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            LocaleList list=    context.getResources().getConfiguration().getLocales();
//            for (int i = 0; i < list.size(); i++) {
//                Log.e("123455","list"+list.get(i).getLanguage());
//            }
//            locale = context.getResources().getConfiguration().getLocales().get(0);
//        } else {
//            locale = context.getResources().getConfiguration().locale;
//        }
        String language =Locale.getDefault().getLanguage();
//        String language = locale.getLanguage();
//        String contry=locale.getDisplayCountry();
        if (language.endsWith("zh")) {
            return true;
        } else {
            return false;
        }
    }


}
