package com.moopflops.safespace.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;

import com.moopflops.safespace.R;

/**
 * Created by patrickc on 24/10/15.
 */
public class SlidyView extends FrameLayout {

    private float mPreviewHeight;
    private float initialY;
    private float initialTranslation;

    enum SwipeDirection {
        UP,
        DOWN
    }

    private SwipeDirection lastDirection;

    public SlidyView(Context context) {
        super(context);
        init();
    }

    public SlidyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlidyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        addView(LayoutInflater.from(getContext()).inflate(R.layout.slidy_view, null));

        mPreviewHeight = getResources().getDimension(R.dimen.preview_height);


        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setTranslationY(getHeight() - mPreviewHeight);
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Y = (int) motionEvent.getRawY();

                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        initialY = Y;
                        initialTranslation = getTranslationY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if(lastDirection == SwipeDirection.DOWN) {
                            animate().translationY(getHeight() - mPreviewHeight).setInterpolator(new DecelerateInterpolator()).setDuration(200).start();
                        } else {
                            animate().translationY(0).setInterpolator(new DecelerateInterpolator()).setDuration(200).start();
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float dy = (Y - initialY);
                        lastDirection = dy > 0 ? SwipeDirection.DOWN : SwipeDirection.UP;
                        float newY = initialTranslation + dy;
                        if(newY <= 0 || newY >= getHeight() - mPreviewHeight) break;
                        setTranslationY(newY);
                        break;
                }


                return true;
            }
        });
    }

}
