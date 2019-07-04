package com.d2cmall.shopkeeper.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.ui.view.ExpandViewPager;
import com.d2cmall.shopkeeper.utils.ScreenUtil;

import java.util.List;

/**
 * 作者:Created by sinbara on 2019/4/22.
 * 邮箱:hrb940258169@163.com
 */

public class MarketRecyclerViewBehavior extends HeaderScrollingViewBehavior  {

    public MarketRecyclerViewBehavior(){};

    public MarketRecyclerViewBehavior(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }

    @Override
    public View findFirstDependency(List<View> views) {
        for (View view : views) {
            if (isDependView(view))
                return view;
        }
        return null;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return isDependView(dependency);
    }

    @Override
    protected int getScrollRange(View v) {
        if (isDependView(v)) {
            return 0;
        } else {
        }
        return super.getScrollRange(v);
    }

    public boolean isDependView(View view){
        return view!=null&&view.getId()== R.id.tabLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        child.setTranslationY(dependency.getTranslationY());
        return super.onDependentViewChanged(parent, child, dependency);
    }
}
