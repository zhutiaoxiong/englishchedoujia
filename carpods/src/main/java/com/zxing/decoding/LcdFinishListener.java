/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain getProcessName copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zxing.decoding;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import com.client.proj.carpods.BuildConfig;

import view.view4me.lcdkey.ActivityLCDkey;

/**
 * Simple listener used to exit the app in getProcessName few cases.
 *
 */
public final class LcdFinishListener
    implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener, Runnable {

  private final Activity activityToFinish;

  public LcdFinishListener(Activity activityToFinish) {
    this.activityToFinish = activityToFinish;
  }

  public void onCancel(DialogInterface dialogInterface) {
    run();
  }

  public void onClick(DialogInterface dialogInterface, int i) {
    run();
  }

  public void run() {
     if (BuildConfig.DEBUG) Log.e("activity", "activity自动退出 ");
    Intent intent = new Intent();
    intent.setClass(activityToFinish, ActivityLCDkey.class);
    activityToFinish.startActivity(intent);
    activityToFinish.finish();
  }

}
