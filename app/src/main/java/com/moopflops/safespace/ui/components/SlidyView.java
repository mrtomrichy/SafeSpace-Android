package com.moopflops.safespace.ui.components;

import android.content.Context;
import android.support.percent.PercentFrameLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by patrickc on 24/10/15.
 */
public class SlidyView extends FrameLayout implements View.OnTouchListener{

    public SlidyView(Context context) {
        super(context);
    }

    public SlidyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

        }


        return false;
    }
}
