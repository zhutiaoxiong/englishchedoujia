package view;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.annotation.Nullable;

import com.client.proj.carpods.R;
import com.kulala.staticsview.ActivityBase;

import view.view4control.FcfrtAppBhUtils;
import view.view4me.ClipPermisonSetItem;
import view.view4me.set.ClipTitleMeSet;

public class ActivityPermisionSet extends ActivityBase {
    private ClipPermisonSetItem ignore_battery,back_run_set,battry_mode_save;
    private ClipTitleMeSet title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permisonset);
        ignore_battery =findViewById(R.id.ignore_battery);
        back_run_set =findViewById(R.id.back_run_set);
        battry_mode_save =findViewById(R.id.battry_mode_save);
        title =findViewById(R.id.title);
        initViews();
        initEvents();
    }


    @Override
    protected void initViews() {
        if(FcfrtAppBhUtils.isVIVO()){
            battry_mode_save.setVisibility(View.VISIBLE);
        }else{
            battry_mode_save.setVisibility(View.INVISIBLE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(FcfrtAppBhUtils.isIgnoringBatteryOptimizations(this)){
                ignore_battery.to_set.setBackgroundResource(R.drawable.button_selector_oval_green);
                ignore_battery.to_set.setEnabled(false);
                ignore_battery.to_set.setText(getResources().getString(R.string.page_set_yihulue));
            }else{
                ignore_battery.to_set.setBackgroundResource(R.drawable.button_selector_oval);
                ignore_battery.to_set.setEnabled(true);
                ignore_battery.to_set.setText(getResources().getString(R.string.page_user_to_set));
            }
        }
    }

    @Override
    protected void initEvents() {
        ignore_battery.to_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent,100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        back_run_set.to_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ActivityPermisionSet.this,ActivityBackRunSet.class);
                startActivity(intent);
            }
        });
        battry_mode_save.to_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ActivityPermisionSet.this,ActivityBatteyModeSet.class);
                startActivity(intent);
            }
        });
        title.img_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(FcfrtAppBhUtils.isIgnoringBatteryOptimizations(this)){
                    ignore_battery.to_set.setBackgroundResource(R.drawable.button_selector_oval_green);
                    ignore_battery.to_set.setEnabled(false);
                    ignore_battery.to_set.setText(getResources().getString(R.string.page_set_yihulue));
                }else{
                    ignore_battery.to_set.setBackgroundResource(R.drawable.button_selector_oval);
                    ignore_battery.to_set.setEnabled(true);
                    ignore_battery.to_set.setText(getResources().getString(R.string.page_user_to_set));
                }
            }
        }
    }

    @Override
    protected void invalidateUI() {

    }

    @Override
    protected void popView(int resId) {

    }
}
