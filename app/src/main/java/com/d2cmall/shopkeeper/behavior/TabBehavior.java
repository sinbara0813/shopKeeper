package com.d2cmall.shopkeeper.behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import com.d2cmall.shopkeeper.BuildConfig;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.ui.view.ExpandViewPager;
import com.d2cmall.shopkeeper.utils.App;
import com.d2cmall.shopkeeper.utils.ScreenUtil;

/**
 * 作者:Created by sinbara on 2019/3/13.
 * 邮箱:hrb940258169@163.com
 */

public class TabBehavior extends ViewOffsetBehavior {

    private final String TAG = getClass().getSimpleName();
    public static final int DURATION_SHORT = 300;
    private OverScroller mOverScroller;

    public TabBehavior() {
        init();
    }

    public TabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mOverScroller = new OverScroller(App.getContext());
    }

    @Override
    protected void layoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        ((CoordinatorLayout.LayoutParams) child.getLayoutParams()).topMargin = ScreenUtil.dip2px(40 + 100);
        super.layoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0 && canScroll(child, 0) && isExpand(coordinatorLayout);
    }

    private boolean canScroll(View child, float i) {
        int pendingTranslationY = (int) (child.getTranslationY() - i);
        if (pendingTranslationY >= getHeaderOffsetRange() && pendingTranslationY <= 0) {
            return true;
        }
        return false;
    }

    private int getHeaderOffsetRange() {
        return -ScreenUtil.dip2px(100);
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int type) {
        if (child.getTranslationY() > getHeaderOffsetRange() && child.getTranslationY() < 0) {
            handleActionUp(coordinatorLayout, child);
        }
        super.onStopNestedScroll(coordinatorLayout, child, target, type);
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        //dy>0 scroll up;dy<0,scroll down
        float halfOfDis = dy; //消费掉其中的4分之1，不至于滑动效果太灵敏
        if (!isExpand(coordinatorLayout)) {
            consumed[1] = 0;
        } else if (!canScroll(child, halfOfDis)) {
            child.setTranslationY(halfOfDis > 0 ? getHeaderOffsetRange() : 0);
            consumed[1] = 0;
        } else {
            child.setTranslationY(child.getTranslationY() - halfOfDis);
            consumed[1] = dy;
        }
    }

    private boolean isExpand(CoordinatorLayout coordinatorLayout) {
        ExpandViewPager viewPager = (ExpandViewPager) coordinatorLayout.findViewById(R.id.view_pager);
        return viewPager.isExpand();
    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, float velocityX, float velocityY) {
        if (!isExpand(coordinatorLayout)) {
            return false;
        } else {
            if ((child.getTranslationY() == 0 && velocityY > 0) || (child.getTranslationY() == getHeaderOffsetRange() && velocityY < 0)) {
                return false;
            } else {
                return true;
            }
        }
    }

    private void handleActionUp(CoordinatorLayout parent, final View child) {
        if (mFlingRunnable != null) {
            child.removeCallbacks(mFlingRunnable);
            mFlingRunnable = null;
        }
        mFlingRunnable = new FlingRunnable(parent, child);
        if (Math.abs(child.getTranslationY()) < Math.abs(getHeaderOffsetRange() / 3.0f)) {
            mFlingRunnable.scrollToOpen(DURATION_SHORT);
        } else {
            mFlingRunnable.scrollToClosed(DURATION_SHORT);
        }

    }

    private FlingRunnable mFlingRunnable;

    private class FlingRunnable implements Runnable {
        private final CoordinatorLayout mParent;
        private final View mLayout;

        FlingRunnable(CoordinatorLayout parent, View layout) {
            mParent = parent;
            mLayout = layout;
        }

        public void scrollToClosed(int duration) {
            float curTranslationY = mLayout.getTranslationY();
            float dy = getHeaderOffsetRange() - curTranslationY;
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "scrollToClosed:offest:" + getHeaderOffsetRange());
                Log.d(TAG, "scrollToClosed: cur0:" + curTranslationY + ",end0:" + dy);
                Log.d(TAG, "scrollToClosed: cur:" + Math.round(curTranslationY) + ",end:" + Math.round(dy));
                Log.d(TAG, "scrollToClosed: cur1:" + (int) (curTranslationY) + ",end:" + (int) dy);
            }
            mOverScroller.startScroll(0, Math.round(curTranslationY - 0.1f), 0, Math.round(dy + 0.1f), duration);
            start();
        }

        public void scrollToOpen(int duration) {
            float curTranslationY = mLayout.getTranslationY();
            mOverScroller.startScroll(0, (int) curTranslationY, 0, (int) -curTranslationY, duration);
            start();
        }

        private void start() {
            if (mOverScroller.computeScrollOffset()) {
                mFlingRunnable = new FlingRunnable(mParent, mLayout);
                ViewCompat.postOnAnimation(mLayout, mFlingRunnable);
            } else {
            }
        }


        @Override
        public void run() {
            if (mLayout != null && mOverScroller != null) {
                if (mOverScroller.computeScrollOffset()) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "run: " + mOverScroller.getCurrY());
                    }
                    mLayout.setTranslationY(mOverScroller.getCurrY());
                    ViewCompat.postOnAnimation(mLayout, this);
                } else {
                }
            }
        }
    }
}
