package view.loginreg;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.client.proj.carpods.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.static_view_change.OInputValidation;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.OTime60;
import com.kulala.staticsview.OnClickListenerMy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.GlobalContext;
import common.pinyinzhuanhuan.KeyBoard;
import common.timetick.OTimeSchedule;
import ctrl.OCtrlRegLogin;
import model.ManagerLoginReg;
import model.ManagerPublicData;
import view.clip.ClipPopLoading;
import view.clip.ClipPopSpeechReceive;
import view.view4me.set.ClipTitleMeSet;

public class ViewReg extends LinearLayoutBase {
    private ClipTitleMeSet title_head;
    private TextView      txt_cannot_receive,textView_youxaing;
    private EditText      txt_input_telephone, txt_input_password, txt_input_password_rep, txt_input_verificationcode,txt_input_youxiang;
    private Button btn_getverificationcode, btn_confirm_register;
    private int timeCount;
    private RegData regdata;
    public static boolean IS_REG_COMPLETED = false;

    public ViewReg(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_loginreg_reg, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        txt_cannot_receive = (TextView) findViewById(R.id.txt_cannot_receive);
        txt_input_telephone = (EditText) findViewById(R.id.txt_input_telephone);
        txt_input_password = (EditText) findViewById(R.id.txt_input_password);
        txt_input_password_rep = (EditText) findViewById(R.id.txt_input_password_rep);
        txt_input_verificationcode = (EditText) findViewById(R.id.txt_input_verificationcode);
        btn_getverificationcode = (Button) findViewById(R.id.btn_getverificationcode);
        btn_confirm_register = (Button) findViewById(R.id.btn_confirm_register);
        txt_input_youxiang=findViewById(R.id.txt_input_youxiang);
        textView_youxaing=findViewById(R.id.textView_youxaing);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.REGISTER_SUCCESS, this);
        ODispatcher.addEventListener(OEventName.REG_FAILED,this);
        ODispatcher.addEventListener(OEventName.VERIFICATION_CODE_BACKOK,this);
        ODispatcher.addEventListener(OEventName.VERIFICATION_CODE_EMAIL_BACKOK,this);
    }

    @Override
    public void initViews() {
        ManagerLoginReg.getInstance().saveFristLogin();
        regdata = new RegData();
        handleChangeData();
        OTime60.getInstance().listener(btn_getverificationcode);
        if(ManagerPublicData.isCn){
            txt_input_telephone.setFilters(new InputFilter[] { new InputFilter.LengthFilter(11) });
            txt_cannot_receive.setVisibility(View.VISIBLE);
            textView_youxaing.setVisibility(View.GONE);
            txt_input_youxiang.setVisibility(View.GONE);
            txt_input_verificationcode.setHint("Please enter code");
        }else{
            txt_input_telephone.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
            txt_cannot_receive.setVisibility(View.INVISIBLE);
            textView_youxaing.setVisibility(View.VISIBLE);
            txt_input_youxiang.setVisibility(View.VISIBLE);
            txt_input_verificationcode.setHint("Please enter code");
        }
    }

    @Override
    public void initEvents() {
        txt_input_telephone.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(txt_input_telephone, 0);
                }
            }
        });
        // back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_LOGIN_GOTOVIEW, R.layout.view_loginreg_login);
            }
        });
        // get
        btn_getverificationcode.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                regdata.phoneNum = txt_input_telephone.getText().toString();
                if(ManagerPublicData.isCn){
                    if(regdata.phoneNum.length() == 11 ) {
                        OTimeSchedule.getInstance().init();
                        OTime60.getInstance().startTime();
                        OCtrlRegLogin.getInstance().ccmd1101_getVerificationCode(regdata.phoneNum, 1,1);
                    }else{
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.please_enter_the_complete_phone_number_1));
                    }
                }else{
                    String email=txt_input_youxiang.getText().toString();
                    if(!TextUtils.isEmpty(email)&&isEmail(email)){
                        OTimeSchedule.getInstance().init();
                        OTime60.getInstance().startTime();
                        OCtrlRegLogin.getInstance().ccmd1603_getVerificationCodeByEmaol(email);
                    }else{
                        txt_input_youxiang.setText("");
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,"Please enter the correct email format");
                    }
                }
            }
        });
        // confirm
        btn_confirm_register.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                if(ManagerPublicData.isCn){
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(txt_input_password.getWindowToken(), 0);
                    String result = checkConfirmResult();
                    if (result.equals("")) {
                        handleermesscircleshow();
                        OCtrlRegLogin.getInstance().ccmd1102_Reg(regdata.phoneNum, regdata.password, regdata.verifyCode);
                    } else {
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,result);
                    }
                }else{
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(txt_input_password.getWindowToken(), 0);
                    String result = checkConfirmResultNotCN();
                    if (result.equals("")) {
                        String email=txt_input_youxiang.getText().toString();
                        if(!TextUtils.isEmpty(email)&&isEmail(email)){
                            handleermesscircleshow();
                            OCtrlRegLogin.getInstance().ccmd1604_Reg(email,regdata.phoneNum, regdata.password, regdata.verifyCode);
                        }else{
                            txt_input_youxiang.setText("");
                            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,"Please enter the correct email format");
                        }
                    } else {
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,result);
                    }
                }
            }
        });
        // ���ص�¼���
        txt_cannot_receive.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                KeyBoard.hintKb();
                regdata.phoneNum = txt_input_telephone.getText().toString();
                if(regdata.phoneNum.length() == 11 ) {
                    ClipPopSpeechReceive.phoneNum = regdata.phoneNum;
                    ClipPopSpeechReceive.getInstance().show(txt_cannot_receive,1);
                }else{
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.please_enter_the_complete_phone_number_1));
                }
            }
        });
    }

    @Override
    protected void invalidateUI() {
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.REGISTER_SUCCESS)) {
            timeCount = 0;
            ODispatcher.removeEventListener(OEventName.TIME_TICK_SECOND,ViewReg.this);
            handlemessstopcircle();
            ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("finishInfo", String.valueOf(false));
            IS_REG_COMPLETED = true;
            ODispatcher.dispatchEvent(OEventName.ACTIVITY_LOGIN_GOTOVIEW, R.layout.view_loginreg_login);
        }else if(OEventName.REG_FAILED.equals(eventName)){
            timeCount = 0;
            ODispatcher.removeEventListener(OEventName.TIME_TICK_SECOND,ViewReg.this);
//            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.registration_failed_please_try_again));
            handlemessstopcircle();
        }else if(eventName.equals(OEventName.VERIFICATION_CODE_BACKOK)){
            OTime60.getInstance().endTime();
        }else if(eventName.equals(OEventName.VERIFICATION_CODE_EMAIL_BACKOK)){
            OTime60.getInstance().endTime();
        }else if(eventName.equals(OEventName.TIME_TICK_SECOND)){
            timeCount++;
            if(timeCount>=5){
                ODispatcher.removeEventListener(OEventName.TIME_TICK_SECOND,ViewReg.this);
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.registration_failed_please_try_again));
                handlemessstopcircle();
            }
        }
    }
    // =====================================================
    // 判断格式是否为email
    public boolean isEmail(String email) {
        String str = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }
    /**
     * 检测点击confirm
     **/
    private String checkConfirmResult() {
        // �ֻ���
        regdata.phoneNum = txt_input_telephone.getText().toString();
        if (!OInputValidation.chkInputPhoneNum(regdata.phoneNum)) {
            txt_input_telephone.setText("");
            return getResources().getString(R.string.the_phone_number_wrong);
        }
        // ����
        regdata.password = txt_input_password.getText().toString();
        if (!OInputValidation.chkInputPassword(regdata.password)) {
            txt_input_password.setText("");
            return getResources().getString(R.string.password_is_wrong_please_input_english_numbers_underscore_combination);
        }
        // �ظ�����
        String reppass = txt_input_password_rep.getText().toString();
        if (!OInputValidation.chkRepWords(reppass, regdata.password)) {
            txt_input_password_rep.setText("");
            return getResources().getString(R.string.repeated_password_is_wrong );
        }
        // ��֤��
        regdata.verifyCode = txt_input_verificationcode.getText().toString();
        if (regdata.verifyCode.length() < 6) {
            txt_input_verificationcode.setText("");
            return getResources().getString(R.string.verification_code_wrong_please_enter_the_six_digit_verification_code);
        }
        return "";
    }
    /**
     * 检测点击confirm国外
     **/
    private String checkConfirmResultNotCN() {
        // �ֻ���
        regdata.phoneNum = txt_input_telephone.getText().toString();
//        if (!OInputValidation.isPhone(regdata.phoneNum)) {
//            txt_input_telephone.setText("");
//            return getResources().getString(R.string.the_phone_number_wrong);
//        }
        // ����
        regdata.password = txt_input_password.getText().toString();
        if (!OInputValidation.chkInputPassword(regdata.password)) {
            txt_input_password.setText("");
            return getResources().getString(R.string.password_is_wrong_please_input_english_numbers_underscore_combination);
        }
        // �ظ�����
        String reppass = txt_input_password_rep.getText().toString();
        if (!OInputValidation.chkRepWords(reppass, regdata.password)) {
            txt_input_password_rep.setText("");
            return getResources().getString(R.string.repeated_password_is_wrong );
        }
        // ��֤��
        regdata.verifyCode = txt_input_verificationcode.getText().toString();
        if (regdata.verifyCode.length() < 6) {
            txt_input_verificationcode.setText("");
            return getResources().getString(R.string.verification_code_wrong_please_enter_the_six_digit_verification_code);
        }
        return "";
    }

    // ==================================================================
    private class RegData {
        public String phoneNum   = "";
        public String password   = "";
        public String verifyCode = "";
    }

    @Override
    public void callback(String key, Object value) {
        super.callback(key, value);
    }

    @Override
    protected void onDetachedFromWindow() {
        OTime60.getInstance().clearButton();
        ODispatcher.removeEventListener(OEventName.REGISTER_SUCCESS, this);
        ODispatcher.removeEventListener(OEventName.REG_FAILED, this);
        ODispatcher.removeEventListener(OEventName.VERIFICATION_CODE_BACKOK, this);
        ODispatcher.removeEventListener(OEventName.TIME_TICK_SECOND, this);
        ODispatcher.removeEventListener(OEventName.VERIFICATION_CODE_EMAIL_BACKOK, this);
        super.onDetachedFromWindow();
    }
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 110:
                    timeCount=0;
                    ClipPopLoading.getInstance().stopLoading();
                    break;
                case 111:
                    ClipPopLoading.getInstance().show(title_head);
                    timeCount=0;
                    OTimeSchedule.getInstance().init();
                    ODispatcher.addEventListener(OEventName.TIME_TICK_SECOND,ViewReg.this);
                    break;
            }
        }
    };

    public void handlemessstopcircle() {
        Message message = Message.obtain();
        message.what = 110;
        handler.sendEmptyMessage(110);
    }

    public void handleermesscircleshow() {
        Message message = Message.obtain();
        message.what = 111;
        handler.sendEmptyMessage(111);
    }
    // =======================================================================
}
