package com.gmail.ivan.synopsis.ui.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class ThesisViewPager extends ViewPager {

    private boolean swipeEnabled;

    public ThesisViewPager(@NonNull Context context) {
        super(context);
    }

    public ThesisViewPager(@NonNull Context context,
                           @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (swipeEnabled) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (swipeEnabled) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    public void setSwipeEnabled(boolean swipeEnabled) {
        this.swipeEnabled = swipeEnabled;
    }
}
