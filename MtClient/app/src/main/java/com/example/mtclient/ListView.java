package com.example.mtclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

@SuppressLint("ViewConstructor")
public class ListView extends View {
    private final Paint mPaint = new Paint();
    private final List<String> _names = new LinkedList<>();
    private float touchY = 0.0f;
    private float scrollY = 0.0f;
    private float offset = 0.0f;

    public ListView(Context context) {
        super(context);
    }

    @Override
    public void onDrawForeground(final Canvas canvas) {
        canvas.drawARGB(255, 0,0,0);
        mPaint.setARGB(255, 255, 128, 32);
        mPaint.setTextSize(50);

        float y = offset + scrollY;
        for(String pkg : _names) {
            canvas.drawText(pkg, 0.0f, y, mPaint);
            y+=50;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = event.getY();
                scrollY = moveY - touchY;
                break;
            case MotionEvent.ACTION_UP:
                offset += scrollY;
                scrollY = 0.0f;
                break;
            default:
        }

        invalidate(); // draw a frame
        return true; // event handled
    }

    public void add(String packageName) {
        _names.add(packageName);
    }
}
