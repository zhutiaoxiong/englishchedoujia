package view.loginreg;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.Locale;

public class CountryJudgeUtils {

/**
 * 方法一
 * */
    public static boolean isCN(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String countryIso = tm.getSimCountryIso();
        boolean isCN = false;//判断是不是大陆
        if (!TextUtils.isEmpty(countryIso)) {
            countryIso = countryIso.toUpperCase(Locale.US);
            if (countryIso.contains("CN")) {
                isCN = true;
            }
        }
        return isCN;

    }
    /**
     * 方法二
     * */
    /** 查询手机的 MCC+MNC */
    private static String getSimOperator(Context c) {
        TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            return tm.getSimOperator();
        } catch (Exception e) {

        }
        return null;
    }


    /** 因为发现像华为Y300，联想双卡的手机，会返回 "null" "null,null" 的字符串 */
    private static boolean isOperatorEmpty(String operator) {
        if (operator == null) {
            return true;
        }
        if (operator.equals("") || operator.toLowerCase(Locale.US).contains("null")) {
            return true;
        }
        return false;
    }

    /** 判断是否是国内的 SIM 卡，优先判断注册时的mcc */
    public static boolean isChinaSimCard(Context c) {
        String mcc = getSimOperator(c);
        if (isOperatorEmpty(mcc)) {
            return false;
        } else {
//            return false;
            return mcc.startsWith("460");
        }
    }
    public static boolean isChinaSim(Context context){
        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = telManager.getSubscriberId();
        if(imsi!=null){
            if(imsi.startsWith("46000") || imsi.startsWith("46002")){//因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号
//中国移动
            }else if(imsi.startsWith("46001")){
//中国联通
            }else if(imsi.startsWith("46003")){
//中国电信
            }
        }
        return true;
    }

}
