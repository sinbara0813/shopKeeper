package com.d2cmall.shopkeeper.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.utils.ScreenUtil;

/**
 * 作者:Created by sinbara on 2019/4/23.
 * 邮箱:hrb940258169@163.com
 */

public class MyHorizontalScrollView extends AutoScrollView {

    private Paint paint;
    private Paint linePaint;
    private int saveDistance;
    private float xPos;
    private float divideWidth=ScreenUtil.dip2px(48);
    private int divideColor=getResources().getColor(R.color.color_blue5);

    public MyHorizontalScrollView(Context context) {
        this(context,null);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(divideColor);
        linePaint=new Paint();
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(getResources().getColor(R.color.line));
        left=getPaddingLeft();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        top=getBottom()- ScreenUtil.dip2px(3);
        right=(int)(left+divideWidth);
        bottom=top+ScreenUtil.dip2px(2);
        if (left>0){
            canvas.drawRect(left,top,right,bottom,paint);
        }
        canvas.drawRect(getPaddingLeft()+getScrollX(),getBottom()-ScreenUtil.dip2px(1),getWidth()+getScrollX(),getBottom(),linePaint);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        float rangeBar = ScreenUtil.getDisplayWidth() - ScreenUtil.dip2px(77);
        float rangeScrollView = getChildAt(0).getMeasuredWidth() - ScreenUtil.getDisplayWidth()+ScreenUtil.dip2px(19);
        xPos = rangeBar/(rangeScrollView * 1.0f);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (saveDistance == l)
            return;
        else
            saveDistance = l;

        //left =getPaddingLeft()+getScrollX()+(int) (getScrollX()*xPos);
        //重新绘制
        //invalidate();
    }

    public void setDivideWidth(float divideWidth) {
        this.divideWidth = divideWidth;
    }

    public void setDivideColor(int divideColor) {
        this.divideColor = divideColor;
    }
}
