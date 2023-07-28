package view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.client.proj.carpods.R;
import com.kulala.staticsview.ActivityBase;

import view.view4control.FcfrtAppBhUtils;
import view.view4me.set.ClipTitleMeSet;

public class ActivityBatteyModeSet extends ActivityBase {
    private ClipTitleMeSet title;
    private TextView tv;
    private ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battery_mode_set);
        title =findViewById(R.id.title);
        tv=findViewById(R.id.tv);
        iv=findViewById(R.id.iv);
        initViews();
        initEvents();
    }


    @Override
    protected void initViews() {
        if(FcfrtAppBhUtils.isVIVO()){
            iv.setImageResource(R.drawable.battery_guide_vivo);
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
                if(FcfrtAppBhUtils.isVIVO()){
                    FcfrtAppBhUtils.goAndroidSetting(ActivityBatteyModeSet.this);
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
