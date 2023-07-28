package view.view4control;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.carpods.BuildConfig;
import com.client.proj.carpods.R;
import com.kulala.staticsfunc.static_assistant.ButtonBgStyle;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsfunc.time.CountDownTimerMy;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.static_interface.OCallBack;

import common.blue.BlueLinkReceiver;
import model.ManagerCarList;
import model.ManagerNokey;
import model.ManagerSkins;
import model.carlist.DataCarInfo;
import model.skin.DataTempSetup;

import static model.ManagerSkins.DEFAULT_NAME_TEMP;
import static model.ManagerSkins.TRANSPARENT;

public class ClipPopNokey {
    private PopupWindow    popContain;//弹出管理
    private View           parentView;//本对象显示
    private Context        context;
    private RelativeLayout thisView;

    private RelativeLayout lin_base;
    private TextView txt_info;
    private ImageView split_center_line,img_bottom,spiline_x;
    private Button   btn_cancel, btn_confirm;
    private        OCallBack             callback;
    private        String                mark;
    private RelativeLayout re1,re2;
    private LinearLayout li1;
    private ImageView img_open,img_close;
    private boolean isOpen;
    private boolean isClose;
    // ========================out======================
    private static ClipPopNokey _instance;

    public static ClipPopNokey getInstance() {
        if (_instance == null)
            _instance = new ClipPopNokey();
        return _instance;
    }
    private Drawable getImage(String url,String name){
        if(ManagerSkins.TRANSPARENT.equals(name)){
            return ManagerSkins.getInstance().getPngImage(TRANSPARENT);
        }
        return ManagerSkins.getInstance().getPngImage(ManagerSkins.getCacheKey(false,(TextUtils.isEmpty(url) ? DEFAULT_NAME_TEMP : url),name));
    }
    public void show(int heightS,View parentView,String info, String mark, OCallBack callback) {
        this.parentView = parentView;
        this.callback = callback;
        this.mark = mark;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (RelativeLayout) layoutInflater.inflate(R.layout.clip_pop_mokey, null);
        lin_base = (RelativeLayout) thisView.findViewById(R.id.lin_base);
        txt_info = (TextView) thisView.findViewById(R.id.txt_info);
        btn_cancel = (Button) thisView.findViewById(R.id.btn_cancel);
        btn_confirm = (Button) thisView.findViewById(R.id.btn_confirm);
        split_center_line = (ImageView) thisView.findViewById(R.id.split_center_line);
        img_bottom = (ImageView) thisView.findViewById(R.id.img_bottom);
        spiline_x= (ImageView) thisView.findViewById(R.id.spiline_x);
        re1=  thisView.findViewById(R.id.re1);
        re2=  thisView.findViewById(R.id.re2);
        li1=  thisView.findViewById(R.id.li1);
        img_open=  thisView.findViewById(R.id.img_open);
        img_close=  thisView.findViewById(R.id.img_close);
        initViews();
        initEvents();
        txt_info.setText(info);
        txt_info.setCompoundDrawables(null,null,null,null);
        if(mark.equals("closeNokey")){
            re1.setVisibility(View.INVISIBLE);
            re2.setVisibility(View.INVISIBLE);
            li1.setVisibility(View.VISIBLE);
        }else{
            re1.setVisibility(View.VISIBLE);
            re2.setVisibility(View.VISIBLE);
            li1.setVisibility(View.INVISIBLE);
            boolean isOpen= ManagerNokey.getInstance().getSwitchOpen();
            boolean isClose= ManagerNokey.getInstance().getSwitchClose();
            this.isOpen=isOpen;
            this.isClose=isClose;
            int openres=isOpen?R.drawable.car_set_on:R.drawable.car_set_off;
            int closeRes=isClose?R.drawable.car_set_on:R.drawable.car_set_off;
            img_open.setImageResource(openres);
            img_close.setImageResource(closeRes);
        }
        int usedHeight = (heightS!=0) ? heightS:135;
//        if(heightS!=0){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ODipToPx.dipToPx(context,usedHeight));
        lin_base.setLayoutParams(params);
        img_bottom.setLayoutParams(params);
        img_bottom.setScaleType(ImageView.ScaleType.FIT_XY);
        //style
        //再看用什么Action
        DataCarInfo   car = ManagerCarList.getInstance().getCurrentCar();
        String url = "";//使用默认的
        if(car != null && car.skinTemplateInfo!=null){//使用网络的
            url = car.getCarTemplate().url;
        }
        ManagerSkins.getInstance().loadTemp(context,url,"control_normal_0",null);
        //背景
        Drawable bg = getImage(url,"control_bg_confirm");
        if(bg == null){
            setBgAndTextColor("#00000000","#000000");
        }else{
            img_bottom.setImageDrawable(bg);
            DataTempSetup tempSetup = ManagerSkins.getInstance().getTempSetup(ManagerSkins.getTempZipFileName(url));
            if(tempSetup!=null) {
                int[][] states = new int[2][];
                states[0] = new int[]{android.R.attr.state_pressed};
                states[1] = new int[]{};
                int[] colors = new int[]{Color.parseColor(tempSetup.colorPopPress), Color.parseColor(tempSetup.colorPopNomal)};
                ColorStateList selector = new ColorStateList(states, colors);
//                txt_info.setTextColor(Color.parseColor(tempSetup.colorPopNomal));
                btn_confirm.setTextColor(selector);
//                btn_cancel.setTextColor(selector);
            }
            //默认图片
            StateListDrawable btnCancleBg = ButtonBgStyle.createDrawableSelector(context, getImage(url,"img_bg_confirm_btn_normal_l"),getImage(url,"img_bg_confirm_btn_press_l"),getImage(url,"img_bg_confirm_btn_press_l"));
            btn_cancel.setBackground(btnCancleBg);
            StateListDrawable btnConfimBg = ButtonBgStyle.createDrawableSelector(context, getImage(url,"img_bg_confirm_btn_normal_r"),getImage(url,"img_bg_confirm_btn_press_r"),getImage(url,"img_bg_confirm_btn_press_r"));
            btn_confirm.setBackground(btnConfimBg);
        }
        thisView.postInvalidate();
    }
    public void setBgAndTextColor(String bgColor,String textColor){
        btn_confirm.setBackgroundColor(Color.parseColor(bgColor));
        btn_cancel.setBackgroundColor(Color.parseColor(bgColor));
        btn_confirm.setTextColor(Color.parseColor(textColor));
        btn_cancel.setTextColor(Color.parseColor(textColor));
        txt_info.setTextColor(Color.parseColor(textColor));
    }

    public void initViews() {
        if(popContain==null){
            popContain = new PopupWindow();
        }
        popContain.setContentView(thisView);
        popContain.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popContain.setFocusable(true);//设了这个就不能点外面了
        popContain.setTouchable(true);
        popContain.setOutsideTouchable(true);//R.color.background_all
//        Drawable dw = GlobalContext.getContext().getResources().getDrawable(R.color.background_all);//no color no initClick
//        popContain.setBackgroundDrawable(dw);
//        popContain.setAnimationStyle(R.style.WindowEnterExitAnimation);
        popContain.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        popContain.dismiss();
                        callback = null;
                        parentView =null;
                        return true;
                }
                return false;
            }
        });
        popContain.showAtLocation(parentView, Gravity.CENTER, 0, 0);
    }

    public void initEvents() {
        btn_cancel.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                popContain.dismiss();
                callback = null;
                parentView =null;
            }
        });
        btn_confirm.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                if(mark.equals("closeNokey")){
                    BlueLinkReceiver.getInstance().sendMessage(ManagerNokey.CMD_SET_SWITCH_CLOSE_CLOSE, false);
                    new CountDownTimerMy(500,500) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            BlueLinkReceiver.getInstance().sendMessage(ManagerNokey.CMD_SET_SWITCH_OPEN_CLOSE, false);
                        }
                    }.start();
                }else{
                   String cmdOpen=isOpen?ManagerNokey.CMD_SET_SWITCH_OPEN_OPEN:ManagerNokey.CMD_SET_SWITCH_OPEN_CLOSE;
                    BlueLinkReceiver.getInstance().sendMessage(cmdOpen, false);
                    String cmdClose=isClose?ManagerNokey.CMD_SET_SWITCH_CLOSE_OPEN:ManagerNokey.CMD_SET_SWITCH_CLOSE_CLOSE;
                    new CountDownTimerMy(500,500) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            BlueLinkReceiver.getInstance().sendMessage(cmdClose, false);
                        }
                    }.start();


                }
                if(callback!=null){
                    callback.callback(mark, "");
                }
                popContain.dismiss();
                callback = null;
                parentView =null;
            }
        });
        img_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpen=isOpen?false:true;
                int openres=isOpen?R.drawable.car_set_on:R.drawable.car_set_off;
                img_open.setImageResource(openres);
            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClose=isClose?false:true;
                int closeRes=isClose?R.drawable.car_set_on:R.drawable.car_set_off;
                img_close.setImageResource(closeRes);
            }
        });

    }

    private Bitmap getBitmapFromUri(Uri uri)
    {
        try
        {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            return bitmap;
        }
        catch (Exception e)
        {
             if (BuildConfig.DEBUG) Log.e("[Android]", e.getMessage());
             if (BuildConfig.DEBUG) Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }
}

