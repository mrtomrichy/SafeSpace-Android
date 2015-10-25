package com.moopflops.safespace.ui.components;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moopflops.safespace.R;
import com.moopflops.safespace.engine.model.RatedCarPark;

/**
 * Created by patrickc on 24/10/15.
 */
public class SlidyView extends FrameLayout {

    private static int DURATION = 200;

    private float mPreviewHeight;
    private float initialY;
    private float initialTranslation;

    TextView mTitle;
    TextView mCapacity;
    TextView mPredictedSpaces;
    TextView mSafetyRating;
    TextView mAvailableSpaces;
    RelativeLayout mSafetyCircleLayout;

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

        mTitle = (TextView) findViewById(R.id.carpark_name);
        mCapacity = (TextView) findViewById(R.id.capacity);
        mPredictedSpaces = (TextView) findViewById(R.id.predicted_spaces);
        mSafetyRating = (TextView) findViewById(R.id.safety_rating);
        mAvailableSpaces = (TextView) findViewById(R.id.spaces_available);
        mSafetyCircleLayout = (RelativeLayout) findViewById(R.id.safety_circle_background);

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
                        if (lastDirection == SwipeDirection.DOWN) {
                            animate().translationY(getHeight() - mPreviewHeight).setInterpolator(new DecelerateInterpolator()).setDuration(DURATION).start();
                        } else {
                            animate().translationY(0).setInterpolator(new DecelerateInterpolator()).setDuration(DURATION).start();
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
                        if (newY <= 0 || newY >= getHeight() - mPreviewHeight) break;
                        setTranslationY(newY);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP && isOpen()) {
            animate().translationY(getHeight() - mPreviewHeight).setInterpolator(new DecelerateInterpolator()).setDuration(DURATION).start();
            return true;
        } else {
            return super.onKeyPreIme(keyCode, event);
        }
    }

    private boolean isOpen(){
        return getTranslationY() == 0;
    }

    public void setData(RatedCarPark ratedCarPark){
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        mTitle.setText(ratedCarPark.carPark.name);
        mCapacity.setText(String.valueOf(ratedCarPark.carPark.capacity));
        mAvailableSpaces.setText(String.valueOf(ratedCarPark.carPark.spacesNow));
        mPredictedSpaces.setText(String.valueOf(ratedCarPark.carPark.spaces30));
        mSafetyRating.setText(ratedCarPark.getRating());

        Drawable tintedDrawable = getResources().getDrawable(R.drawable.safety_circle).getConstantState().newDrawable().mutate();
        tintedDrawable.setColorFilter(new PorterDuffColorFilter(ratedCarPark.getColour(getContext()), PorterDuff.Mode.MULTIPLY));

        mSafetyCircleLayout.setBackground(tintedDrawable);
    }

}
