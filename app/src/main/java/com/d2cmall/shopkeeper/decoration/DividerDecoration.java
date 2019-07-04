package com.d2cmall.shopkeeper.decoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.d2cmall.shopkeeper.utils.ScreenUtil;

/**
 * 作者:Created by sinbara on 2019/5/8.
 * 邮箱:hrb940258169@163.com
 */

public class DividerDecoration extends RecyclerView.ItemDecoration {

    private Paint paint;
    private boolean hasBottom;
    private int bottom;

    public DividerDecoration(int color, boolean hasBottom){
        paint=new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        this.hasBottom=hasBottom;
        this.bottom=ScreenUtil.dip2px(1);
    }

    public DividerDecoration(int color, int bottom){
        paint=new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        this.bottom=bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (view instanceof AppCompatTextView){
            if (((AppCompatTextView)view).getText().equals("亲，到底了哦~")){
                outRect.bottom= 0;
            }else {
                outRect.bottom=bottom;
            }
        }else {
            outRect.bottom= bottom;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount=parent.getChildCount();
        for (int i=0;i<childCount;i++){
            if (i==childCount-1&&!hasBottom){
                continue;
            }
            int left=parent.getPaddingLeft();
            int right=parent.getWidth()-parent.getPaddingRight();
            int top=parent.getChildAt(i).getBottom();
            c.drawRect(left,top,right,top+bottom,paint);
        }
    }
}
