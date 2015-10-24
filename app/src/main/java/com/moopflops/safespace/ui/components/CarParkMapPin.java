package com.moopflops.safespace.ui.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.moopflops.safespace.R;

/**
 * Created by patrickc on 24/10/15.
 */
public class CarParkMapPin extends FrameLayout {

    public CarParkMapPin(Context context, int colour, String rating) {
        super(context);
        addView(LayoutInflater.from(getContext()).inflate(R.layout.map_pin_layout, null));

//        ((ImageView) findViewById(R.id.map_pin_background)).setBackground(new PinBackground(getContext(), colour));

        Drawable tintedDrawable = getResources().getDrawable(R.drawable.map_pin).getConstantState().newDrawable().mutate();
        tintedDrawable.setColorFilter(new PorterDuffColorFilter(colour, PorterDuff.Mode.MULTIPLY));
        ((ImageView) findViewById(R.id.map_pin_background)).setBackground(tintedDrawable);


        ((TextView) findViewById(R.id.spaces_free)).setText(rating);
    }

    public class PinBackground extends Drawable{

        Paint mPaint = new Paint();
        Paint mWhitePaint = new Paint();

        int whiteWidth;

        int width;
        int height;

        public PinBackground(Context context, int colour){
            mPaint.setColor(colour);
            mPaint.setAntiAlias(true);
            mWhitePaint.setColor(Color.WHITE);
            mWhitePaint.setAntiAlias(true);

            whiteWidth = (int) context.getResources().getDimension(R.dimen.white_width);
            width = (int) context.getResources().getDimension(R.dimen.map_pin_width);
            height = (int) context.getResources().getDimension(R.dimen.map_pin_height);
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawCircle(width / 2, width / 2, width / 2, mPaint);
            Path mColouredTriangle = new Path();

            mColouredTriangle.moveTo(width / 2, height);
            mColouredTriangle.lineTo((int) (0.2 * width), height / 2);
            mColouredTriangle.lineTo((int) (0.8 * width), height / 2);
            mColouredTriangle.lineTo(width / 2, height);

            canvas.drawPath(mColouredTriangle, mPaint);

            canvas.drawCircle(width / 2, width / 2, width / 2 - whiteWidth, mWhitePaint);
            Path mWhiteTrianglePath = new Path();

            mWhiteTrianglePath.moveTo(width / 2, height - 2 * whiteWidth);
            mWhiteTrianglePath.lineTo((int) (0.2 * width), height / 2 - 2 * whiteWidth);
            mWhiteTrianglePath.lineTo((int) (0.8 * width), height / 2 - 2 * whiteWidth);
            mWhiteTrianglePath.lineTo(width / 2, height - 2 * whiteWidth);

            canvas.drawPath(mWhiteTrianglePath, mWhitePaint);


        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return 0;
        }
    }




}
