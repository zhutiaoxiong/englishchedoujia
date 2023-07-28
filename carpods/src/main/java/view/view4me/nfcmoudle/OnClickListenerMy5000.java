package view.view4me.nfcmoudle;

import android.view.View;
import android.widget.Toast;

import com.client.proj.carpods.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;

import common.GlobalContext;
import view.ResourceUtil;

public class OnClickListenerMy5000 implements View.OnClickListener{

	private static long beforeTime = 0;
	@Override
	public void onClick(View v) {
		long time = System.currentTimeMillis();
		if(Math.abs(time-beforeTime)<5000L){
			ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, ResourceUtil.getResourceStr(v.getContext(), R.string.page_nfc_five_reget));
			return;
		}
		beforeTime = time;
		onClickNoFast(v);
	}

	public void onClickNoFast(View v){

	}
}
