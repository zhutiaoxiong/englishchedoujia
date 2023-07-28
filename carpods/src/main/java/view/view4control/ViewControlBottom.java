package view.view4control;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.carpods.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;

import java.text.DecimalFormat;

import model.ManagerCarList;
import model.carlist.DataCarInfo;
import model.carlist.DataCarStatus;
import view.basicview.CheckForgroundUtils;


/**
 * 车身
 */
public class ViewControlBottom extends LinearLayout implements OEventObject {
    private RelativeLayout tab_1;
    private RelativeLayout tab_2;
    private RelativeLayout tab_0;
    private TextView t1;
    private TextView t2;
    private TextView t3;
    private TextView t5;
    private TextView t4;
    private TextView t6;

    public ViewControlBottom(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_control_bottom, this, true);
        tab_0 = (RelativeLayout) findViewById(R.id.tab_0);
        tab_1 = (RelativeLayout) findViewById(R.id.tab_1);
        tab_2 = (RelativeLayout) findViewById(R.id.tab_2);
        t1 = tab_0.findViewById(R.id.txt_top);
        t2 = tab_0.findViewById(R.id.txt_bottom);
        t3 = tab_1.findViewById(R.id.txt_top);
        t4 = tab_1.findViewById(R.id.txt_bottom);
        t5 = tab_2.findViewById(R.id.txt_top);
        t6 = tab_2.findViewById(R.id.txt_bottom);
        t2.setText("Voltage");
        t4.setText("License plate number");
        t6.setText("Mileage traveled");
        DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
        if(!TextUtils.isEmpty(car.num)) {
            t3.setText(car.num);
        }
        ODispatcher.addEventListener(OEventName.CAR_STATUS_SECOND_CHANGE, this);
    }


    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.CAR_STATUS_SECOND_CHANGE, this);
        super.onDetachedFromWindow();
    }

    @Override
    public void receiveEvent(String eventName, Object o) {
        if (eventName.equals(OEventName.CAR_STATUS_SECOND_CHANGE)) {
            if (CheckForgroundUtils.isAppForeground()) {
                handler.sendEmptyMessage(1);
            }
        }
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
                DataCarStatus status = ManagerCarList.getInstance().getCurrentCarStatus();
                if (car == null ) {//car null
                    t1.setText("--");
                    t3.setText("--");
                    t5.setText("--");
                }else{
                   if(!TextUtils.isEmpty(car.num)) {
                       t3.setText(car.num);
                   }
                }
                if (status != null) {
                    if (status.voltage == 0) {
                        t1.setText("--");
                    } else {
//                        DecimalFormat df = new DecimalFormat("##.0");
                        t1.setText(status.voltage + "V");
                    }
                    if (status.miles == 0) {
                        t5.setText("--");
                    } else {
                        t5.setText(String.valueOf(status.miles));
                    }
                }
            }
        }
    };
}
