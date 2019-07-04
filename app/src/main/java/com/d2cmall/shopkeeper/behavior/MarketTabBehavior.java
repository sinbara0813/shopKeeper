package com.d2cmall.shopkeeper.behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.d2cmall.shopkeeper.BuildConfig;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.ui.view.ExpandViewPager;
import com.d2cmall.shopkeeper.utils.ScreenUtil;

import static com.d2cmall.shopkeeper.utils.ScreenUtil.dip2px;

/**
 * 作者:Created by sinbara on 2019/4/22.
 * 邮箱:hrb940258169@163.com
 */

public class MarketTabBehavior extends ViewOffsetBehavior {

    private int totalOffer;
    private int firstOffer;

    public MarketTabBehavior() {
    }

    public MarketTabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void layoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        /*View middlewareFl=parent.findViewById(R.id.middleware_fl);
        View headFl=parent.findViewById(R.id.head_fl);
        firstOffer=headFl.getMeasuredHeight();
        totalOffer=middlewareFl.getMeasuredHeight()+firstOffer;
        if (totalOffer<firstOffer){
            totalOffer+=firstOffer;
        }
        if (BuildConfig.DEBUG){
            Log.d("han","middleFl.getBottom()="+middlewareFl.getMeasuredHeight());
            Log.d("han","headFl.getBottom()="+headFl.getMeasuredHeight());
        }
        ((CoordinatorLayout.LayoutParams) child.getLayoutParams()).topMargin = totalOffer;*/
        super.layoutChild(parent, child, layoutDirection);
    }

    //此方法不会实时执行 当此方法返回true时可以在onNestedPreScroll中将consumed[1] = 0 达到不处理滑动效果
    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0 && canScroll(coordinatorLayout,child, 0);
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        //dy>0 scroll up;dy<0,scroll down
        float halfOfDis = dy/2; //消费掉其中的4分之1，不至于滑动效果太灵敏
        if (canScroll(coordinatorLayout,child, halfOfDis)){
            child.setTranslationY(child.getTranslationY() - halfOfDis);
            consumed[1] = dy;
        }else {
            if (isExpand(coordinatorLayout)){
                child.setTranslationY(halfOfDis > 0 ? getTotalOffsetRange() : 0);
            }
            consumed[1]=0;
        }
    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, float velocityX, float velocityY) {
        return super.onNestedPreFling(coordinatorLayout,child,target,velocityX,velocityY);
    }

    private boolean canScroll(CoordinatorLayout coordinatorLayout, View child, float offer){
        int pendingTranslationY = (int) (child.getTranslationY() - offer);
        if (pendingTranslationY>=getTotalOffsetRange()&&pendingTranslationY<=getFirstOffsetRange()){
            return true;
        }
        if (pendingTranslationY>=getFirstOffsetRange()&&pendingTranslationY<=0&&isExpand(coordinatorLayout)){
            return true;
        }
        return false;
    }

    private int getFirstOffsetRange() {
        return -firstOffer;
    }

    private int getTotalOffsetRange(){
        return -totalOffer;
    }

    private boolean isExpand(CoordinatorLayout coordinatorLayout) {
        RecyclerView recyclerView = coordinatorLayout.findViewById(R.id.recycle_view);
        return !recyclerView.canScrollVertically(-1);
    }

}
