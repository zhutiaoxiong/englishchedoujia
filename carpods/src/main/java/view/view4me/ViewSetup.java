package view.view4me;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.client.proj.carpods.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.toast.ToastTxt;


import common.GlobalContext;
import ctrl.OCtrlRegLogin;
import view.ActivityPermisionSet;
import view.EquipmentManager;
import view.ResourceUtil;
import view.view4me.set.ClipSetItem;
import view.view4me.set.ClipTitleMeSet;

public class ViewSetup extends LinearLayoutBase {
    private final ClipTitleMeSet title_head;
    private final ClipSetItem txt_safety;
    private final ClipSetItem txt_switch;
    private final ClipSetItem txt_private;
    private final ClipSetItem txt_tipsswitch;
    private final ClipSetItem txt_sound;
    private final ClipSetItem txt_findpasswordway;
    private final ClipSetItem txt_about;
    private final ClipSetItem txt_contact;
    private final ClipSetItem txt_back_run_permision_set;
    private final TextView txt_exit;
    public ViewSetup(Context context, AttributeSet attrs) {
        super(context, attrs);//this layout for add and edit
        LayoutInflater.from(context).inflate(R.layout.view_me_setup, this, true);
        title_head = findViewById(R.id.title_head);
        txt_safety =  findViewById(R.id.txt_safety);
        txt_findpasswordway = findViewById(R.id.txt_findpasswordway);
        txt_switch = findViewById(R.id.txt_switch);
        txt_private = findViewById(R.id.txt_private);
        txt_tipsswitch =  findViewById(R.id.txt_tipsswitch);
        txt_sound = findViewById(R.id.txt_sound);
        txt_exit = findViewById(R.id.txt_exit);
        txt_contact =  findViewById(R.id.txt_contact);
        txt_about = findViewById(R.id.txt_about);
        txt_back_run_permision_set= findViewById(R.id.txt_back_run_permision_set);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.LANGUAGE_CHANGE, this);

    }
    @Override
    public void initViews() {

    }

    @Override
    public void initEvents() {
        //back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.activity_kulala_main);
            }
        });
        txt_back_run_permision_set.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                Intent intent=new Intent(getContext(), ActivityPermisionSet.class);
                getContext().startActivity(intent);
            }
        });
        txt_switch.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                if(EquipmentManager.isMini()){
                        new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo(getResources().getString(R.string.page_car_not_sopport)).quicklyShow();
                        return;
                }
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_switch_message);
            }
        });
        txt_private.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                if(EquipmentManager.isMini()){
                    new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo(getResources().getString(R.string.page_car_not_sopport)).quicklyShow();
                    return;
                }
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_switch_private);
            }
        });
        txt_tipsswitch.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_switch_confirm);
            }
        });
        txt_sound.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                if(EquipmentManager.isMini()){
                    new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo(getResources().getString(R.string.page_car_not_sopport)).quicklyShow();
                    return;
                }
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_switch_voice);
            }
        });
        txt_safety.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_safety);
            }
        });
        txt_exit.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                        .withTitle(ResourceUtil.getResourceStr(getContext(),R.string.exit_login))
                        .withInfo(ResourceUtil.getResourceStr(getContext(),R.string.are_you_sure_you_want_to_log_out))
                        .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                            @Override
                            public void onClickConfirm(boolean isClickConfirm) {
                                if(isClickConfirm)OCtrlRegLogin.getInstance().ccmd1109_exitlogin();
                            }
                        }).show();
            }
        });
        txt_findpasswordway.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_findpasswordway);
            }
        });

        txt_contact.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClick(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_contactus);
            }
        });
        txt_about.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClick(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_about);
            }
        });
    }
    @Override
    public void receiveEvent(String eventName, Object paramObj) {
   if (eventName.equals(OEventName.LANGUAGE_CHANGE)) {
            handleChangeData();
        }
    }
    @Override
    public void callback(String key, Object value) {
        super.callback(key, value);
    }
    @Override
    public void invalidateUI() {
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.LANGUAGE_CHANGE, this);
        super.onDetachedFromWindow();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK  && event.getRepeatCount() == 0) {
            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.activity_kulala_main);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
