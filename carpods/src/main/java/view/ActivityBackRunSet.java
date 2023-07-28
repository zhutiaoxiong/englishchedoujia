package view;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.client.proj.carpods.R;
import com.kulala.staticsview.ActivityBase;

import view.view4control.FcfrtAppBhUtils;
import view.view4me.set.ClipTitleMeSet;

public class ActivityBackRunSet extends ActivityBase {
    private ClipTitleMeSet title;
    private TextView tv;
    private ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.back_run_set);
        title =findViewById(R.id.title);
        tv=findViewById(R.id.tv);
        iv=findViewById(R.id.iv);
        initViews();
        initEvents();
    }


    @Override
    protected void initViews() {
        if(FcfrtAppBhUtils.isVIVO()){
            iv.setImageResource(R.drawable.permision_guide_vivo);
        }else if(FcfrtAppBhUtils.isOPPO()){
            iv.setImageResource(R.drawable.permision_guide_oppo);
        }else if(FcfrtAppBhUtils.isHuawei()){
            iv.setImageResource(R.drawable.permision_guide_huawei);
        }else if(FcfrtAppBhUtils.isXiaomi()){
            iv.setImageResource(R.drawable.permisionguide_xiaomi);
        }else{
            iv.setImageResource(R.drawable.permisionguide_xiaomi);
        }
    }

    @Override
    protected void initEvents() {
        title.img_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String brand=  Build.BRAND;
               if(FcfrtAppBhUtils.isVIVO()){
                   FcfrtAppBhUtils.VIVO(ActivityBackRunSet.this);
               }else if(FcfrtAppBhUtils.isHuawei()){
                   FcfrtAppBhUtils.goHuaweiSetting(ActivityBackRunSet.this);
               }else if(FcfrtAppBhUtils.isOPPO()){
                    FcfrtAppBhUtils.goOPPO(ActivityBackRunSet.this);
               }else if(FcfrtAppBhUtils.isXiaomi()){
                   FcfrtAppBhUtils.goXiaomiSetting(ActivityBackRunSet.this);
               } else if (FcfrtAppBhUtils.isMeizu()) {
                   //魅族手机
                   FcfrtAppBhUtils.goMeizuSetting(ActivityBackRunSet.this);
               } else if (FcfrtAppBhUtils.isSamsung()) {
                   //三星手机
                   FcfrtAppBhUtils.goSamsungSetting(ActivityBackRunSet.this);
               } else if (FcfrtAppBhUtils.isLeTV()) {
                   //乐视手机
                   FcfrtAppBhUtils.goLetvSetting(ActivityBackRunSet.this);
               } else if (FcfrtAppBhUtils.isSmartisan()) {
                   //锤子手机
                   FcfrtAppBhUtils.goSmartisanSetting(ActivityBackRunSet.this);
               }
            }
        });

    }

    @Override
    protected void invalidateUI() {

    }

    @Override
    protected void popView(int resId) {

    }
}
