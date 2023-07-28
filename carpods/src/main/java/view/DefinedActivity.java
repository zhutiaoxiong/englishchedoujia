/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package view;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.client.proj.carpods.R;
import com.google.gson.JsonObject;
import com.huawei.hms.hmsscankit.OnLightVisibleCallBack;
import com.huawei.hms.hmsscankit.OnResultCallback;
import com.huawei.hms.hmsscankit.RemoteView;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.static_interface.OCallBack;
import com.kulala.staticsview.toast.OToastInput;

import java.io.IOException;

import view.view4me.carmanage.ViewCarmanModelBind;
import view.view4me.set.ClipTitleMeSet;

public class DefinedActivity extends Activity implements OCallBack {
    private FrameLayout frameLayout;
    private RemoteView remoteView;
    private ImageView backBtn;
    private ImageView imgBtn;
    private ImageView flushBtn;
    int mScreenWidth;
    int mScreenHeight;
    //The width and height of scan_view_finder is both 240 dp.
    final int SCAN_FRAME_SIZE = 240;

    private int[] img = {R.drawable.flashlight_on, R.drawable.flashlight_off};
    private static final String TAG = "DefinedActivity";

    //Declare the key. It is used to obtain the value returned from Scan Kit.
    public static final String SCAN_RESULT = "scanResult";
    public static final int REQUEST_CODE_PHOTO = 0X1113;
    private ClipTitleMeSet title_head;
    private TextView txt_input;
    public static int useFor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_defined);

        // Bind the camera preview screen.
        frameLayout = findViewById(R.id.rim);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        txt_input=  findViewById(R.id.txt_input);
        if(useFor==0){
            txt_input.setVisibility(View.INVISIBLE);
        }else{
            txt_input.setVisibility(View.VISIBLE);
        }
        //1. Obtain the screen density to calculate the viewfinder's rectangle.
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float density = dm.density;
        //2. Obtain the screen size.
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getResources().getDisplayMetrics().heightPixels;

        int scanFrameSize = (int) (SCAN_FRAME_SIZE * density);

        //3. Calculate the viewfinder's rectangle, which in the middle of the layout.
        //Set the scanning area. (Optional. Rect can be null. If no settings are specified, it will be located in the middle of the layout.)
        Rect rect = new Rect();
        rect.left = mScreenWidth / 2 - scanFrameSize / 2;
        rect.right = mScreenWidth / 2 + scanFrameSize / 2;
        rect.top = mScreenHeight / 2 - scanFrameSize / 2;
        rect.bottom = mScreenHeight / 2 + scanFrameSize / 2;


        //Initialize the RemoteView instance, and set callback for the scanning result.
        remoteView = new RemoteView.Builder().setContext(this).setBoundingBox(rect).setFormat(HmsScan.ALL_SCAN_TYPE).build();
        // When the light is dim, this API is called back to display the flashlight switch.
        flushBtn = findViewById(R.id.flush_btn);
        remoteView.setOnLightVisibleCallback(new OnLightVisibleCallBack() {
            @Override
            public void onVisibleChanged(boolean visible) {
                if(visible){
                    flushBtn.setVisibility(View.VISIBLE);
                }
            }
        });
        // Subscribe to the scanning result callback event.
        remoteView.setOnResultCallback(new OnResultCallback() {
            @Override
            public void onResult(HmsScan[] result) {
                //Check the result.
                if (result != null && result.length > 0 && result[0] != null && !TextUtils.isEmpty(result[0].getOriginalValue())) {
//                    Intent intent = new Intent();
//                    intent.putExtra(SCAN_RESULT, result[0]);
//                    setResult(RESULT_OK, intent);
//                    DefinedActivity.this.finish();
                    HmsScan hmsScan=result[0];
                    String scanResult=hmsScan.getOriginalValue();
                    ODispatcher.dispatchEvent(OEventName.SCAN_RESULT_BACK, scanResult);
                    finish();
                    useFor=0;
                }
            }
        });
        // Load the customized view to the activity.
        remoteView.onCreate(savedInstanceState);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        frameLayout.addView(remoteView, params);
        // Set the back, photo scanning, and flashlight operations.
        setBackOperation();
        setPictureScanOperation();
        setFlashOperation();
        txt_input.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
//                OToastInput.getInstance().showInput(txt_input, "请输入模组号", "请输入模组号:", new String[]{OToastInput.OTHER_NUMBER}, "mozuhao", DefinedActivity.this );
				ViewCarmanModelBind.equipmentNunber="";
				finish();
				ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.carman_model_set_bind);
                useFor=0;
            }
        });
    }

    /**
     * Call the lifecycle management method of the remoteView activity.
     */
    private void setPictureScanOperation() {
//        imgBtn = findViewById(R.id.img_btn);
        title_head.txt_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                DefinedActivity.this.startActivityForResult(pickIntent, REQUEST_CODE_PHOTO);

            }
        });
    }

    private void setFlashOperation() {
        flushBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (remoteView.getLightStatus()) {
                    remoteView.switchLight();
                    flushBtn.setImageResource(img[1]);
                } else {
                    remoteView.switchLight();
                    flushBtn.setImageResource(img[0]);
                }
            }
        });
    }

    private void setBackOperation() {
//        backBtn = findViewById(R.id.back_img);
        title_head.img_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefinedActivity.this.finish();
            }
        });
    }

    /**
     * Call the lifecycle management method of the remoteView activity.
     */
    @Override
    protected void onStart() {
        super.onStart();
        remoteView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        remoteView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        remoteView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        remoteView.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        remoteView.onStop();
    }

    /**
     * Handle the return results from the album.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PHOTO) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                HmsScan[] hmsScans = ScanUtil.decodeWithBitmap(DefinedActivity.this, bitmap, new HmsScanAnalyzerOptions.Creator().setPhotoMode(true).create());
                if (hmsScans != null && hmsScans.length > 0 && hmsScans[0] != null && !TextUtils.isEmpty(hmsScans[0].getOriginalValue())) {
//                    Intent intent = new Intent();
//                    intent.putExtra(SCAN_RESULT, hmsScans[0]);
//                    setResult(RESULT_OK, intent);
//                    DefinedActivity.this.finish();
                    HmsScan hmsScan=hmsScans[0];
                    String scanResult=hmsScan.getOriginalValue();
                    ODispatcher.dispatchEvent(OEventName.SCAN_RESULT_BACK, scanResult);
                    finish();
                    useFor=0;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void callback(String key, Object value) {
        if (key.equals("mozuhao")) {
            //点击验证密码确定框
            JsonObject obj = (JsonObject) value;
            String mozuhao = OJsonGet.getString(obj, OToastInput.OTHER_NUMBER);
            ODispatcher.dispatchEvent(OEventName.SCAN_RESULT_BACK, mozuhao);
            finish();
        }
    }
}
