package com.d2cmall.shopkeeper.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.d2cmall.shopkeeper.BuildConfig;
import com.d2cmall.shopkeeper.R;

import java.util.List;

/**
 * 作者:Created by sinbara on 2019/4/24.
 * 邮箱:hrb940258169@163.com
 */

public class MarketShadowBehavior extends CoordinatorLayout.Behavior {

    public MarketShadowBehavior(){};

    public MarketShadowBehavior(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        /*View headFl=parent.findViewById(R.id.tabLayout);
        if (BuildConfig.DEBUG){
            Log.d("han","headxx.getBottom()="+headFl.getBottom());
        }
        ((CoordinatorLayout.LayoutParams)child.getLayoutParams()).topMargin= headFl.getBottom();*/
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return isDependView(dependency);
    }

    public boolean isDependView(View view){
        return view!=null&&view.getId()== R.id.tabLayout;
    }
}
