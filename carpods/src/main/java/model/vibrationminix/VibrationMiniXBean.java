package model.vibrationminix;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 定位器对象
 * */
public class VibrationMiniXBean {
    //编号
    private long id;
    //设备号
    private String terminalNum;
    //用户编号
    private int warnShock=0;
    //用户是否绑定
    private int sensitiveShock=3;

    public int getWarnShock() {
        return warnShock;
    }

    public void setWarnShock(int warnShock) {
        this.warnShock = warnShock;
    }

    public int getSensitiveShock() {
        return sensitiveShock;
    }

    public void setSensitiveShock(int sensitiveShock) {
        this.sensitiveShock = sensitiveShock;
    }

    public static VibrationMiniXBean fromJsonObject(JsonObject obj) {
        Gson gson    = new Gson();
        VibrationMiniXBean thisobj = gson.fromJson(obj, VibrationMiniXBean.class);
        return thisobj;
    }
}
