package model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import model.mylocator.PositionerBean;
import model.mylocator.PositionerInfoBean;
import model.mylocator.PositionerRecordingBean;
import model.mylocator.PositionerSetBean;
import model.mylocator.PositionerTrailBean;
import model.mylocator.PositionerWarnRecordBean;
import model.vibrationminix.VibrationMiniXBean;

public class ManagerVibrationMiniX {
	public VibrationMiniXBean caCheminiXVibrationInfo;//定位器列表对象
	// ========================out======================
	private static ManagerVibrationMiniX _instance;
	private ManagerVibrationMiniX() {
		init();
	}
	public static ManagerVibrationMiniX getInstance() {
		if (_instance == null)
			_instance = new ManagerVibrationMiniX();
		return _instance;
	}
	private void init() {
	}
	public void saveVibrationInfo(JsonObject productSet) {
		if (productSet == null) return;
		this.caCheminiXVibrationInfo = VibrationMiniXBean.fromJsonObject(productSet);
	}
}
