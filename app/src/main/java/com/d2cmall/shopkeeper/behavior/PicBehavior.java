package com.d2cmall.shopkeeper.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.utils.ScreenUtil;

/**
 * 作者:Created by sinbara on 2019/3/13.
 * 邮箱:hrb940258169@163.com
 */

public class PicBehavior extends CoordinatorLayout.Behavior{

    public PicBehavior(){}

    public PicBehavior(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        ((CoordinatorLayout.LayoutParams)child.getLayoutParams()).topMargin= ScreenUtil.dip2px(62);
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return isDependOn(dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        child.setTranslationY(dependency.getTranslationY()*getTotalY()/getHeadOffer());
        child.setTranslationX(dependency.getTranslationY()*getTotalX()/getHeadOffer());
        child.setScaleX(getScal(dependency));
        child.setScaleY(getScal(dependency));
        return false;
    }

    private float getScal(View dependency){
      return 1+(float)dependency.getTranslationY()*16/(getHeadOffer()*56);
    }

    private int getTotalX(){
        return ScreenUtil.getDisplayWidth()/2-ScreenUtil.dip2px(48);
    }

    private int getTotalY(){
        return ScreenUtil.dip2px(70);
    }

    private int getHeadOffer(){
        return ScreenUtil.dip2px(100);
    }

    private boolean isDependOn(View dependency) {
        return dependency != null && dependency.getId() == R.id.tabLayout;
    }

}
