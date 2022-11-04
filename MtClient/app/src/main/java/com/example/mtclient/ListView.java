package com.example.mtclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ViewConstructor")
public class ListView extends View {
    private final Paint mPaint = new Paint();
    private final List<String> _names = new ArrayList<>();
    private float touchY = 0.0f;
    private float scrollY = 0.0f;
    private float offset = 0.0f;
    private final MainActivity parent;

    public ListView(MainActivity context) {
        super(context);
        parent = context;
    }

    @Override
    public void onDrawForeground(final Canvas canvas) {
        canvas.drawARGB(255, 0,0,0);
        mPaint.setARGB(255, 255, 255, 0);
        mPaint.setTextSize(50);
        //_names.sort(String::compareTo);
        float y = 50 + offset + scrollY;

        if (parent.mService == null){
            canvas.drawText("Service is null", 0.0f, y, mPaint);
            y+=50;
        }
        if (!parent.bound){
            canvas.drawText("Service is not bound", 0.0f, y, mPaint);
            y+=50;
        }

        for(String pkg : _names) {
            if (pkg == null) continue;
            if (pkg.contains("example.mt"))
                mPaint.setARGB(255, 255, 255, 255);
            else
                mPaint.setARGB(255, 255, 128, 32);

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
