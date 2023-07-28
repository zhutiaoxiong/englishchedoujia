package view.view4app.lendcartemporary;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.carpods.R;
import com.client.proj.carpods.wxapi.WXEntryActivity;
import com.google.gson.JsonArray;
import com.kulala.TimePickerViewNoAnim;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsfunc.static_system.ODateTime;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.picker.WheelNumView;
import com.tencent.tauth.TencentCommon;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import common.global.OWXShare;
import ctrl.OCtrlAuthorization;
import model.ManagerCarList;
import model.ManagerLoginReg;
import model.ManagerPublicData;
import model.carlist.DataCarInfo;
import view.view4me.set.ClipTitleMeSet;

/**
 * 临时借车主页面
 */
public class ActivityLendCarTemporary extends Activity implements OEventObject {
    private ClipTitleMeSet title_head;
    private TextView txt_tips_success, txt_tips_cancel;
    private TextView btn_confirm;
    private WheelNumView wheel_num;
    private RelativeLayout lin_img_share,lin_wheel;
    private ImageView img_share_wechat, img_share_firend, img_share_qq;
    private int selectNum = 2;
    private ImageView code_layout;
    private ImageView iv_code;
    private TextView txt_code;
    private TimePickerViewNoAnim timeView;
    private long timeStart;
    private long timeEnd;
    private TextView txt_selecttime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend_car_temporary);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        txt_tips_success = (TextView) findViewById(R.id.txt_tips_success);
        txt_tips_cancel = (TextView) findViewById(R.id.txt_tips_cancel);
        btn_confirm = (TextView) findViewById(R.id.btn_confirm);
        wheel_num = (WheelNumView) findViewById(R.id.wheel_num);
        lin_wheel = (RelativeLayout) findViewById(R.id.lin_wheel);
        lin_img_share = (RelativeLayout) findViewById(R.id.lin_img_share);
        img_share_wechat = (ImageView) findViewById(R.id.img_share_wechat);
        img_share_firend = (ImageView) findViewById(R.id.img_share_firend);
        img_share_qq = (ImageView) findViewById(R.id.img_share_qq);
        code_layout =  findViewById(R.id.code_layout);
        iv_code =  findViewById(R.id.iv_code);
        txt_code =  findViewById(R.id.txt_code);
        timeView =  findViewById(R.id.time_view);
        txt_selecttime=  findViewById(R.id.txt_selecttime);
        ArrayList<String> data = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            data.add("" + i);
        }
        wheel_num.setData(data);
        wheel_num.setSelected(selectNum - 2);
        wheel_num.setOnSelectListener(new WheelNumView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectNum = Integer.parseInt(text);
//                SoundHelper.playSoundPool(getContext(),R.raw.voice_move_num);
            }
        });
        selectNum = 1;//默认选1
        txt_tips_success.setVisibility(View.INVISIBLE);
        txt_tips_cancel.setVisibility(View.INVISIBLE);
        lin_img_share.setVisibility(View.INVISIBLE);
        changeDisplay();
        initTimePicker();
        initEvents();
        ODispatcher.addEventListener(OEventName.AUTHOR_CODRIVER_RESULTBACK,this);
        ODispatcher.addEventListener(OEventName.AUTHORIZATION_USER_STOPED,this);
        ODispatcher.addEventListener(OEventName.APPLECODE_RESULTBACK,this);
    }
    private void initTimePicker(){
        Calendar calendar = Calendar.getInstance();
        timeView.setRange(calendar.get(Calendar.YEAR) , calendar.get(Calendar.YEAR) +20);//要在setTime 之前才有效果哦
        timeView.setTime(new Date());
        timeView.setCyclic(true);
        timeView.setMark("timefrom");
        timeView.setOnTimeSelectListener(new TimePickerViewNoAnim.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date datee, String mark) {
                if (mark.equals("timefrom")) {
                    timeStart=System.currentTimeMillis();
                    timeEnd=datee.getTime();
                    Log.e("看看小布","开始时间"+timeStart+"---结束时间"+timeEnd);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        ODispatcher.removeEventListener(OEventName.AUTHOR_CODRIVER_RESULTBACK,this);
        ODispatcher.removeEventListener(OEventName.AUTHORIZATION_USER_STOPED,this);
        ODispatcher.removeEventListener(OEventName.APPLECODE_RESULTBACK,this);
        super.onDestroy();
    }

    //状态变了，修改显示
    private void changeDisplay() {
        DataCarInfo currentCar = ManagerCarList.getInstance().getCurrentCar();
        if (currentCar.authorityEndTime1<System.currentTimeMillis()) {//未授时的显示
            btn_confirm.setText("Generate Key");
        } else {//已授时的显示
            OCtrlAuthorization.getInstance().ccmd1601_getAuthCode(currentCar.ide);
            lin_img_share.setVisibility(View.VISIBLE);
            btn_confirm.setText("Cancel key");
            lin_wheel.setVisibility(View.INVISIBLE);
            timeView.setVisibility(View.INVISIBLE);
            txt_selecttime.setVisibility(View.INVISIBLE);
            String showTime="";
            long ms = currentCar.authorityEndTime1-System.currentTimeMillis();
            long nd = 1000 * 24 * 60 * 60;
            long nh = 1000 * 60 * 60;
            long nm = 1000 * 60;
            // long ns = 1000;
            // 获得两个时间的毫秒时间差异
            // 计算差多少天
            long day = ms / nd;
            // 计算差多少小时
            long hour = ms % nd / nh;
            // 计算差多少分钟
            long min = ms % nd % nh / nm;
            Long msss = Long.valueOf(""+ms);
            if(msss < 60*60*1000L){
                showTime= "<font color='#FF0000'>"+min+"</font>minute";
            }else if(msss>=60*60*1000L&&msss<48*60*60*1000L){
                showTime= "<font color='#FF0000'>"+hour+"</font>hour"+ "<font color='#FF0000'>"+min+"</font>minute";;
            }else if(msss>=48*60*60*1000L){
                showTime= "<font color='#FF0000'>"+day+"</font>day"+"<font color='#FF0000'>"+hour+"</font>hour"+ "<font color='#FF0000'>"+min+"</font>minute";;
            }
            String str="Currently, there are still temporary car loans left "+showTime;
            txt_tips_cancel.setText(android.text.Html.fromHtml(str));
            txt_tips_cancel.setVisibility(View.VISIBLE);
        }
    }

    private void initEvents() {
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                finish();
            }
        });
        btn_confirm.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                String txt = btn_confirm.getText().toString();
                if ("Generate Key".equals(txt)) {
                    DataCarInfo currentCar = ManagerCarList.getInstance().getCurrentCar();
                    timeView.getTime();
                    OCtrlAuthorization.getInstance().ccmd1206_giveauthor(currentCar.ide, new JsonArray(), "", timeStart, timeEnd);
//                    OCtrlAuthorization.getInstance().ccmd1206_giveauthor(currentCar.ide, new JsonArray(), "", System.currentTimeMillis(), System.currentTimeMillis() + selectNum * 60 * 60 * 1000);
                }else if ("Cancel key".equals(txt)) {
                    long carId = ManagerCarList.getInstance().getCurrentCarID();
                    long userId = ManagerLoginReg.getInstance().getCurrentUser().userId;
                    OCtrlAuthorization.getInstance().ccmd1207_stopauthor(userId, carId, 1);
                }
            }
        });
        img_share_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare(1);
            }
        });
        img_share_firend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showShare(2);
                if(!TextUtils.isEmpty(ManagerPublicData.authCode)){
                    OCtrlAuthorization.getInstance().ccmd1602_getWXerweiCode(ManagerPublicData.authCode);
                }
            }
        });
        img_share_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare(3);

            }
        });
        code_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code_layout.setVisibility(View.INVISIBLE);
                iv_code.setVisibility(View.INVISIBLE);
                txt_code.setVisibility(View.INVISIBLE);
            }
        });
    }


    /**
     * 收回钥匙
     * (currentCar.isActive == 0)才可执行
     */
    private void stopKey() {

//        long carId = ManagerCarList.getInstance().getCurrentCarID();
//        if (carId == 0) return;
//        OCtrlAuthorization.getInstance().ccmd1207_stopauthor(user.userId, carId, 1);
    }

    private void showShare(int pos) {
            String authCode = ManagerPublicData.authCode;//临时授权码
            String shareUrl = "http://kc.kcmoco.com/share/borrow?authCode=" + authCode;
        if (pos == 1) {
            WXEntryActivity.NEED_WXSHARE_RESULT = true;
            OWXShare.ShareWXSmallApp("A car has already been lent to you", "Please click to learn more details", shareUrl,authCode);
            ManagerPublicData.isNotPopGusture = true;

//            WXEntryActivity.NEED_WXSHARE_RESULT = true;
//            OWXShare.ShareFriendURL("有辆车已经借给您了", "请点击了解详细内容", shareUrl);
//            ManagerPublicData.isNotPopGusture = true;
        } else if (pos == 2) {
            OWXShare.ShareTimeLineURL("A car has already been lent to you", "Please click to learn more details", shareUrl);
            ManagerPublicData.isNotPopGusture = true;
        } else if (pos == 3) {
            TencentCommon.toTencent(ActivityLendCarTemporary.this, "A car has already been lent to you", "Please click to learn more details", shareUrl, 0, "");
            ManagerPublicData.isNotPopGusture = true;
        }
    }

    @Override
    public void receiveEvent(final String s, Object o) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DataCarInfo currentCar = ManagerCarList.getInstance().getCurrentCar();
                if(s.equals(OEventName.AUTHOR_CODRIVER_RESULTBACK)){//生成key OK
                    btn_confirm.setText("Cancel key");
                    //                    String showTime;
                    String showTimeOne= ODateTime.time2StringWithHH(currentCar.authorityEndTime1);
//                    long ms = currentCar.authorityEndTime1-System.currentTimeMillis();
//                    double msss = Double.valueOf(""+ms);
//                    if(msss < 60*60*1000L){
//                        showTime = "<font color='#FF0000'>"+Math.round(msss/60/1000)+"</font>分钟";
//                    }else{
//                        showTime = "<font color='#FF0000'>"+(int)Math.ceil(msss/60/60/1000)+"</font>小时";
//                    }
                    String str="Your car "+"<font color='#FF0000'>"+currentCar.num+"</font>"+" Now a virtual key has been generated，"+showTimeOne+"Automatically expires after expiration. After receiving it, the other party can only use their phone to switch the lock. At the same time, remind the other party not to send the virtual key to others for use." ;
                    txt_tips_success.setText(android.text.Html.fromHtml(str));
                    txt_tips_success.setVisibility(View.VISIBLE);
                    lin_img_share.setVisibility(View.VISIBLE);
                    timeView.setVisibility(View.INVISIBLE);
                    txt_selecttime.setVisibility(View.INVISIBLE);
                }else if(s.equals(OEventName.AUTHORIZATION_USER_STOPED)){//取消key OK
                    txt_tips_success.setVisibility(View.INVISIBLE);
                    txt_tips_cancel.setVisibility(View.INVISIBLE);
                    lin_img_share.setVisibility(View.INVISIBLE);
                    //                    lin_wheel.setVisibility(View.VISIBLE);
                    lin_wheel.setVisibility(View.INVISIBLE);

                    timeView.setVisibility(View.VISIBLE);
                    txt_selecttime.setVisibility(View.VISIBLE);
                    btn_confirm.setText("Generate Key");
                }else if(s.equals(OEventName.APPLECODE_RESULTBACK)){
                    code_layout.setVisibility(View.VISIBLE);
                    iv_code.setVisibility(View.VISIBLE);
                    txt_code.setVisibility(View.VISIBLE);
                    //Base64编码地址(地址太长，省略)
                    String base64String = ManagerPublicData.appleCode;

                    //将Base64编码字符串解码成Bitmap

                    byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);

                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    //设置ImageView图片

                    iv_code.setImageBitmap(decodedByte);
                }
            }
        });
    }
}
