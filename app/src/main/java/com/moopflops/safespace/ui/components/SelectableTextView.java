package com.moopflops.safespace.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.moopflops.safespace.R;

/**
 * Created by patrickc on 24/10/15.
 */
public class SelectableTextView extends TextView {

    private boolean mIsSelected = false;

    public SelectableTextView(Context context) {
        super(context);
    }

    public SelectableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSelected(boolean selected){
        mIsSelected = selected;
        if(mIsSelected){
            setTextColor(getResources().getColor(R.color.colorPrimary));
        } else{
            setTextColor(getResources().getColor(R.color.text_grey));
        }
    }

    public boolean isSelected(){
        return mIsSelected;
    }

    public void toggleSelected(){
        setSelected(!mIsSelected);
    }
}
