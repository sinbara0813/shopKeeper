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
 * 作者:Created by sinbara on 2019/3/13.
 * 邮箱:hrb940258169@163.com
 */

public class ViewPagerBehavior extends HeaderScrollingViewBehavior {

    public ViewPagerBehavior(){};

    public ViewPagerBehavior(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }

    @Override
    public View findFirstDependency(List<View> views) {
        for (View view : views) {
            if (isHeadView(view))
                return view;
        }
        return null;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return isHeadView(dependency);
    }

    @Override
    protected int getScrollRange(View v) {
        if (isHeadView(v)) {
            return v.getMeasuredHeight()-getTitleOffer();
        } else {
            return super.getScrollRange(v);
        }
    }

    private int getTitleOffer(){
        return ScreenUtil.dip2px(40+32);
    }

    public boolean isHeadView(View view){
        return view!=null&&view.getId()== R.id.tabLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        child.setTranslationY(dependency.getTranslationY());
        if (child instanceof ExpandViewPager){
            if (dependency.getTranslationY()==0){
                ((ExpandViewPager)child).canRefresh(true);
            }else {
                ((ExpandViewPager)child).canRefresh(false);
            }
        }
        return super.onDependentViewChanged(parent, child, dependency);
    }
}
