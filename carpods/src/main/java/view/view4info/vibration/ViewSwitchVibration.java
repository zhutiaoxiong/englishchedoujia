package view.view4info.vibration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.client.proj.carpods.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.linkscarpods.blue.BluePermission;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.toast.ToastTxt;

import java.util.List;

import common.GlobalContext;
import common.blue.BlueLinkNetSwitch;
import common.blue.BlueLinkReceiver;
import ctrl.OCtrlCar;
import ctrl.OCtrlCommon;
import model.ManagerCarList;
import model.ManagerVibrationMiniX;
import model.carlist.DataCarInfo;
import model.status.DataSwitch;
import model.vibrationminix.VibrationMiniXBean;
import view.ResourceUtil;
import view.clip.ClipLineBtnInptxt;
import view.find.OToastButtonBlackStyle;
import view.find.ProItem;
import view.view4me.set.ClipSetItem;
import view.view4me.set.ClipTitleMeSet;
import view.view4me.shake.JiecaoVideoPlayerShakeActivity;

import static android.content.Context.SENSOR_SERVICE;

public class ViewSwitchVibration extends RelativeLayoutBase {
    private ClipTitleMeSet title_head;
    //    private ClipLineBtnTxt txt_selectcar;
    private LinearLayout  lin_blueswitch, lin_blue_shake_tips;
    private TextView txt_info, txt_quite, txt_sure;
    private ImageView img_blueswitch, img_blue_vibration;
//    private ImageView img_blue_vibration_cover;
    private List<DataCarInfo> cacheBlueList;
    private DataCarInfo       cacheShowingCar;
    //    private RelativeLayout re_shake;
    private RelativeLayout    re_windows, lin_blue_vibration,re2_a;
    private Button            btn_know, btn_cancle;
    private ClipLineBtnInptxt txt_shake_product, txt_shake_bind;
    private ClipSetItem txt_shake_set,txt_shake_tips,txt_shake_phone;
    private ProItem shake_level_set;
    private  VibrationMiniXBean cacheBean;

    public ViewSwitchVibration(Context context, AttributeSet attrs) {
        super(context, attrs);//this layout for add and edit
        LayoutInflater.from(context).inflate(R.layout.view_me_switch_vibration, this, true);
        title_head =  findViewById(R.id.title_head);
//        txt_selectcar = (ClipLineBtnTxt) findViewById(R.id.txt_selectcar);
        lin_blueswitch = (LinearLayout) findViewById(R.id.lin_blueswitch);
        lin_blue_vibration = (RelativeLayout) findViewById(R.id.lin_blue_vibration);//震动开关
//        re_shake = (RelativeLayout) findViewById(R.id.re_shake);
        txt_shake_set =  findViewById(R.id.txt_shake_set);
        txt_shake_tips = findViewById(R.id.txt_shake_tips);
        txt_shake_phone =findViewById(R.id.txt_shake_phone);
        txt_shake_product = (ClipLineBtnInptxt) findViewById(R.id.txt_shake_product);
        txt_shake_bind = (ClipLineBtnInptxt) findViewById(R.id.txt_shake_bind);
        img_blueswitch = (ImageView) findViewById(R.id.img_blueswitch);
        img_blue_vibration = (ImageView) findViewById(R.id.img_blue_vibration);//震动开关
//        img_blue_vibration_cover = (ImageView) findViewById(R.id.img_blue_vibration_cover);
        re_windows = (RelativeLayout) findViewById(R.id.re_windows);
        btn_know = (Button) findViewById(R.id.btn_know);
        btn_cancle = (Button) findViewById(R.id.btn_cancle);
        shake_level_set= findViewById(R.id.shake_level_set);
        re2_a= findViewById(R.id.re2_a);
        cacheBean=new VibrationMiniXBean();
        cacheBean.setWarnShock(0);
        cacheBean.setSensitiveShock(3);
        ODispatcher.addEventListener(OEventName.MINIX_VIBRATION_BACK,this);
        ODispatcher.addEventListener(OEventName.MINIX_VIBRATION_SET_BACK,this);
        initViews();
        initEvents();
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }
    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.MINIX_VIBRATION_BACK,this);
        ODispatcher.removeEventListener(OEventName.MINIX_VIBRATION_SET_BACK,this);
        super.onDetachedFromWindow();
    }

    @Override
    public void initViews() {

//        OCtrlCar.getInstance().ccmd11002_zhendongbaojing("62209060004");
             DataCarInfo currentCar=ManagerCarList.getInstance().getCurrentCar();
            if(currentCar!=null&&!TextUtils.isEmpty(currentCar.terminalNum)){
                OCtrlCar.getInstance().ccmd11002_zhendongbaojing(currentCar.terminalNum);
            }
    }
    private long clickTime=0;
    @Override
    public void initEvents() {
        re2_a.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        re_windows.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                re_windows.setVisibility(View.INVISIBLE);
            }
        });
        //back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.activity_kulala_main);

            }
        });
        shake_level_set.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OToastButtonBlackStyle.getInstance().show(title_head, new String[]{ResourceUtil.getResourceStr(getContext(),R.string.page_nokey_highest),ResourceUtil.getResourceStr(getContext(),R.string.page_nokey_high),ResourceUtil.getResourceStr(getContext(),R.string.page_shake_middle), ResourceUtil.getResourceStr(getContext(),R.string.page_nokey_low),ResourceUtil.getResourceStr(getContext(),R.string.page_nokey_lowest)}, "item_vibration_level", ViewSwitchVibration.this, ResourceUtil.getResourceStr(getContext(),R.string.page_vibration_linmin_set));
            }
        });
        //要的开关
        img_blueswitch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTime=System.currentTimeMillis();
                if((currentTime-clickTime)>1000){
                    clickTime=currentTime;
                    int isOpen=0;
                    if(cacheBean.getWarnShock()==0){
                        isOpen=1;
                        img_blueswitch.setImageResource(R.drawable.car_set_on);
                    }else{
                        isOpen=0;
                        img_blueswitch.setImageResource(R.drawable.car_set_off);
                    }
//                OCtrlCar.getInstance().ccmd11001_zhendongbaojing("62209060004",2,1,isOpen,cacheBean.getSensitiveShock());
                    DataCarInfo currentCarInfo=ManagerCarList.getInstance().getCurrentCar();
                    if(currentCarInfo!=null&&!TextUtils.isEmpty(currentCarInfo.terminalNum)){
                        OCtrlCar.getInstance().ccmd11001_zhendongbaojing(currentCarInfo.terminalNum,2,1,isOpen,cacheBean.getSensitiveShock());
                    }
                }else{
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, ResourceUtil.getResourceStr(getContext(),R.string.page_vibration_dong_pinfan));
                }
            }
        });
        btn_cancle.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                OCtrlCommon.getInstance().ccmd1315_setSwitchShakeOpen(ManagerCarList.getInstance().getCurrentCarID(),false);
//                handler.obtainMessage(HANDLER_car_set_off).sendToTarget();
            }
        });
        btn_know.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                re_windows.setVisibility(View.INVISIBLE);
                if(BluePermission.checkPermission(GlobalContext.getCurrentActivity())!=1){
                    BluePermission.openBlueTooth(GlobalContext.getCurrentActivity());
                }else {
                    shake_level_set.setVisibility(View.VISIBLE);
                    OCtrlCommon.getInstance().ccmd1315_setSwitchShakeOpen(ManagerCarList.getInstance().getCurrentCarID(), true);
                }

            }
        });

        txt_shake_set.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
//                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_switch_set);
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_switch_vibration_txt);
            }
        });
        txt_shake_tips.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_switch_vibration_tips);

            }
        });
        txt_shake_phone.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_switch_phone);
            }
        });
        txt_shake_product.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_switch_product);
            }
        });
        txt_shake_bind.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_switch_bind);
            }
        });
    }

    // =====================================================
    @Override
    public void callback(String key, Object value) {
        if (key.equals("item_vibration_level")) {
            String o = (String) value;
            shake_level_set.txt_bottom.setText(o);
            int lingminDu=5;
            if(o.equals(ResourceUtil.getResourceStr(getContext(),R.string.page_nokey_lowest))){
                lingminDu=1;
            }else   if(o.equals(ResourceUtil.getResourceStr(getContext(),R.string.page_nokey_low))){
                lingminDu=2;
            }else   if(o.equals(ResourceUtil.getResourceStr(getContext(),R.string.page_shake_middle))){
                lingminDu=3;
            }else   if(o.equals(ResourceUtil.getResourceStr(getContext(),R.string.page_nokey_high))){
                lingminDu=4;
            }else   if(o.equals(ResourceUtil.getResourceStr(getContext(),R.string.page_nokey_highest))){
                lingminDu=5;
            }
//            OCtrlCar.getInstance().ccmd11001_zhendongbaojing("62209060004",2,1,cacheBean.getWarnShock(),lingminDu);
            DataCarInfo currentCarInfo=ManagerCarList.getInstance().getCurrentCar();
            if(currentCarInfo!=null&&!TextUtils.isEmpty(currentCarInfo.terminalNum)){
                OCtrlCar.getInstance().ccmd11001_zhendongbaojing(currentCarInfo.terminalNum,2,1,cacheBean.getWarnShock(),lingminDu);
            }
        }
        super.callback(key, value);
    }

    @Override
    public void receiveEvent(String key, Object value) {
        super.receiveEvent(key, value);
        if (key.equals(OEventName.MINIX_VIBRATION_BACK)) {
            handleChangeData();
        }else   if (key.equals(OEventName.MINIX_VIBRATION_SET_BACK)) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    DataCarInfo currentCar=ManagerCarList.getInstance().getCurrentCar();
                    if(currentCar!=null&&!TextUtils.isEmpty(currentCar.terminalNum)){
                        OCtrlCar.getInstance().ccmd11002_zhendongbaojing(currentCar.terminalNum);
                    }
                }
            },600);
//            OCtrlCar.getInstance().ccmd11002_zhendongbaojing("62209060004");

        }
    }
    private Handler handler=new Handler(Looper.getMainLooper());

    @Override
    public void invalidateUI() {
        cacheBean= ManagerVibrationMiniX.getInstance().caCheminiXVibrationInfo;
        if(cacheBean==null){
            cacheBean=new VibrationMiniXBean();
            cacheBean.setWarnShock(0);
            cacheBean.setSensitiveShock(3);
        }
        if(cacheBean.getWarnShock()==0){
            img_blueswitch.setImageResource(R.drawable.car_set_off);
        }else{
            img_blueswitch.setImageResource(R.drawable.car_set_on);
        }

        if(cacheBean.getSensitiveShock()==1){
            shake_level_set.txt_bottom.setText(ResourceUtil.getResourceStr(getContext(),R.string.page_nokey_lowest));
        }else if(cacheBean.getSensitiveShock()==2){
            shake_level_set.txt_bottom.setText(ResourceUtil.getResourceStr(getContext(),R.string.page_nokey_low));
        }else if(cacheBean.getSensitiveShock()==3){
            shake_level_set.txt_bottom.setText(ResourceUtil.getResourceStr(getContext(),R.string.page_shake_middle));
        }else if(cacheBean.getSensitiveShock()==4){
            shake_level_set.txt_bottom.setText(ResourceUtil.getResourceStr(getContext(),R.string.page_nokey_high));
        }else if(cacheBean.getSensitiveShock()==5){
            shake_level_set.txt_bottom.setText(ResourceUtil.getResourceStr(getContext(),R.string.page_nokey_highest));
        }
    }
}
