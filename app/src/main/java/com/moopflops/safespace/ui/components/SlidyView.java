package com.moopflops.safespace.ui.components;

import android.animation.ObjectAnimator;
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
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moopflops.safespace.R;
import com.moopflops.safespace.engine.RatingUtils;
import com.moopflops.safespace.engine.model.RatedCarPark;

/**
 * Created by patrickc on 24/10/15.
 */
public class SlidyView extends FrameLayout {

    private static int DURATION = 200;

    private float mPreviewHeight;
    private float initialY;
    private float initialTranslation;
    private boolean mEnabled;

    RelativeLayout mExpanded;
    TextView mTitle;
    TextView mCapacity;
    TextView mPredictedSpaces;
    TextView mSafetyRating;
    TextView mAvailableSpaces;
    RelativeLayout mSafetyCircleLayout;


    RelativeLayout mPreview;
    TextView mPreviewTitle;
    TextView mPreviewSpaces;
    TextView mPreviewSpacesAvailableTitle;
    TextView mPreviewSafetyRating;
    RelativeLayout mPreviewSaferyCircleLayout;

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

        mExpanded = (RelativeLayout) findViewById(R.id.expanded_layout);
        mPreview = (RelativeLayout) findViewById(R.id.preview_layout);

        mTitle = (TextView) findViewById(R.id.carpark_name);
        mCapacity = (TextView) findViewById(R.id.capacity);
        mPredictedSpaces = (TextView) findViewById(R.id.predicted_spaces);
        mSafetyRating = (TextView) findViewById(R.id.safety_rating);
        mAvailableSpaces = (TextView) findViewById(R.id.spaces_available);
        mSafetyCircleLayout = (RelativeLayout) findViewById(R.id.safety_circle_background);

        mPreviewTitle = (TextView) findViewById(R.id.preview_carpark_name);
        mPreviewSpacesAvailableTitle = (TextView) findViewById(R.id.preview_spaces_available_title);
        mPreviewSpaces = (TextView) findViewById(R.id.preview_spaces_available);
        mPreviewSafetyRating = (TextView) findViewById(R.id.preview_safety_rating);
        mPreviewSaferyCircleLayout = (RelativeLayout) findViewById(R.id.preview_safety_circle_background);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setTranslationY(getHeight());
                mExpanded.setAlpha(0.0f);
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(!mEnabled) return true;

                final int Y = (int) motionEvent.getRawY();

                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        initialY = Y;
                        initialTranslation = getTranslationY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (lastDirection == SwipeDirection.DOWN) {
                            animateTheShitter(getTranslationY(), getHeight() - mPreviewHeight);
                        } else {
                            animateTheShitter(getTranslationY(), 0);
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(mEnabled) {
                            float dy = (Y - initialY);
                            lastDirection = dy > 0 ? SwipeDirection.DOWN : SwipeDirection.UP;
                            float newY = initialTranslation + dy;
                            if (newY <= 0 || newY >= getHeight() - mPreviewHeight) break;
                            setTranslationY(newY);
                        }
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void setTranslationY(float translationY) {
        super.setTranslationY(translationY);

        double showFactor = (translationY / (getHeight() - mPreviewHeight));

        mPreview.setAlpha((float) showFactor);
        mExpanded.setAlpha(1f - (float) showFactor);
    }

    private void animateTheShitter(float start, float end) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, start, end);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(200);
        animator.start();
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

    public boolean isOpen(){
        return getTranslationY() == 0;
    }

    public float getVisibleHeight(){
        return isOpen() ? getHeight() : mPreviewHeight;
    }

    public void setEnabled(boolean enabled){
        mEnabled = enabled;
    }

    public void close() {
        animateTheShitter(getTranslationY(), getHeight() - mPreviewHeight);
    }

    public void setMyLocationData(int rating) {
        setEnabled(false);
        mPreviewTitle.setText("My Location");
        Drawable tintedDrawable = getResources().getDrawable(R.drawable.safety_circle).getConstantState().newDrawable().mutate();
        tintedDrawable.setColorFilter(new PorterDuffColorFilter(RatingUtils.getColour(getContext(), rating), PorterDuff.Mode.MULTIPLY));

        mPreviewSaferyCircleLayout.setBackground(tintedDrawable);
        mPreviewSafetyRating.setText(RatingUtils.getRating(rating));
        mPreviewSpaces.setText("");
        mPreviewSpacesAvailableTitle.setVisibility(View.GONE);

        if(getTranslationY() == getHeight()) {
            animateTheShitter(getTranslationY(), getHeight()-mPreviewHeight);
        }
    }

    public void setData(RatedCarPark ratedCarPark) {
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        mTitle.setText(ratedCarPark.carPark.name);
        mCapacity.setText(String.valueOf(ratedCarPark.carPark.capacity));
        mAvailableSpaces.setText(String.valueOf(ratedCarPark.carPark.spacesNow));
        mPredictedSpaces.setText(String.valueOf(ratedCarPark.carPark.spaces30));
        mSafetyRating.setText(RatingUtils.getRating(ratedCarPark.rating));

        mPreviewTitle.setText(ratedCarPark.carPark.name);
        mPreviewSpaces.setText(String.valueOf(ratedCarPark.carPark.spacesNow));
        mPreviewSpacesAvailableTitle.setVisibility(View.VISIBLE);
        mPreviewSafetyRating.setText(RatingUtils.getRating(ratedCarPark.rating));

        Drawable tintedDrawable = getResources().getDrawable(R.drawable.safety_circle).getConstantState().newDrawable().mutate();
        tintedDrawable.setColorFilter(new PorterDuffColorFilter(RatingUtils.getColour(getContext(), ratedCarPark.rating), PorterDuff.Mode.MULTIPLY));

        mSafetyCircleLayout.setBackground(tintedDrawable);
        mPreviewSaferyCircleLayout.setBackground(tintedDrawable);

        if(getTranslationY() == getHeight()) {
            animateTheShitter(getTranslationY(), getHeight()-mPreviewHeight);
        }

    }

}
