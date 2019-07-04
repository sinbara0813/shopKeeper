package com.d2cmall.shopkeeper.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.utils.ScreenUtil;

/**
 * 作者:Created by sinbara on 2019/4/26.
 * 邮箱:hrb940258169@163.com
 */

public class AutoScrollView extends HorizontalScrollView {

    public int left,top,right,bottom;
    public float marginLeft;

    public AutoScrollView(Context context) {
        this(context,null);
    }

    public AutoScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.autoScrollView);
        marginLeft=a.getDimension(R.styleable.autoScrollView_mar_left,0);
        a.recycle();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_UP:
                View view=findViewByEvent(ev.getX()+getScrollX());
                if (view!=null){
                    Rect rect=new Rect();
                    view.getGlobalVisibleRect(rect);
                    smoothScrollBy(rect.right-(ScreenUtil.getDisplayWidth()-(int)marginLeft)/2,0);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private View findViewByEvent(float x){
        ViewGroup content= (ViewGroup) getChildAt(0);
        int count=content.getChildCount();
        for (int i=0;i<count;i++){
            if (content.getChildAt(i).getLeft()<x&&content.getChildAt(i).getRight()>x){
                return content.getChildAt(i);
            }
        }
        return null;
    }
}
