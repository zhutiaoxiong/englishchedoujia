package view.view4me;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.carpods.R;

/**
 * Default : No Left Image,Center txt , Right No Arrow, Down Line
 */
public class ClipPermisonSetItem extends RelativeLayout {
    public TextView txt_top,txt_bottom,to_set;
    public View bottom_line;


    public ClipPermisonSetItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.me_permisonset_item, this, true);
        txt_top = (TextView) findViewById(R.id.txt_top);
        txt_bottom= (TextView) findViewById(R.id.txt_bottom);
        to_set= (TextView) findViewById(R.id.to_set);
        bottom_line=  findViewById(R.id.bottom_line);
        @SuppressLint("CustomViewStyleable")
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        String topText = ta.getString(R.styleable.androidMe_topText);
        String bottomText = ta.getString(R.styleable.androidMe_bottomTxt);
        boolean splitLine=ta.getBoolean(R.styleable.androidMe_splitLine,true);
        if (topText != null && !topText.equals("")) {
            txt_top.setText(topText);
        }
        if (bottomText != null && !bottomText.equals("")) {
            txt_bottom.setText(bottomText);
        }
        if(splitLine){
            bottom_line.setVisibility(View.VISIBLE );
        }else{
            bottom_line.setVisibility(View.INVISIBLE );
        }
        ta.recycle();
    }
}
