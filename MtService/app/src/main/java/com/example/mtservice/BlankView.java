package com.example.mtservice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

@SuppressLint("ViewConstructor")
public class BlankView extends View {
    private final Paint mPaint = new Paint();

    public BlankView(Context context) {
        super(context);
    }

    @Override
    public void onDrawForeground(final Canvas canvas) {
        canvas.drawARGB(255, 0, 0, 0);
        mPaint.setTextSize(50);
        mPaint.setARGB(255, 32, 128, 255);

        canvas.drawText("This is the service", 0.0f, 50.0f, mPaint);
    }
}
