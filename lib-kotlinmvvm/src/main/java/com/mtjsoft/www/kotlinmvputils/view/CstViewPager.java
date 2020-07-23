package com.mtjsoft.www.kotlinmvputils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * 解决滑动冲突的ViewPager
 */
public class CstViewPager extends ViewPager {
    private static final String TAG = "zxt/CstViewPager";
    private int mLastX;
    private int mLastY;

    public CstViewPager(Context context) {
        super(context);
    }

    public CstViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int x = (int)ev.getX();
        int y = (int)ev.getY();
        boolean intercept = false;
        switch(ev.getAction()) {
            case 2:
                if (this.isHorizontalScroll(x, y)) {
                    if (this.isReactFirstPage() && this.isScrollRight(x)) {
                        intercept = false;
                    } else if (this.isReachLastPage() && this.isScrollLeft(x)) {
                        intercept = false;
                    } else {
                        intercept = true;
                    }
                }
            case 0:
            case 1:
            default:
                this.mLastX = x;
                this.mLastY = y;
                boolean onInterceptTouchEvent = super.onInterceptTouchEvent(ev);
                return intercept || onInterceptTouchEvent;
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    private boolean isHorizontalScroll(int x, int y) {
        return Math.abs(y - this.mLastY) < Math.abs(x - this.mLastX);
    }

    private boolean isReachLastPage() {
        PagerAdapter adapter = this.getAdapter();
        return null != adapter && adapter.getCount() - 1 == this.getCurrentItem();
    }

    private boolean isReactFirstPage() {
        return this.getCurrentItem() == 0;
    }

    private boolean isScrollLeft(int x) {
        return x - this.mLastX < 0;
    }

    private boolean isScrollRight(int x) {
        return x - this.mLastX > 0;
    }
}
