package com.d2cmall.shopkeeper.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.utils.ScreenUtil;

/**
 * 作者:Created by sinbara on 2019/6/13.
 * 邮箱:hrb940258169@163.com
 */
public class MyLinearLayout extends FrameLayout implements View.OnClickListener {

    private Paint paint;
    private Paint linePaint;
    public float left,top,right,bottom;
    private int childCount;
    private float dividerWidth;
    private float itemWidth;
    private String data;
    private int childColor;
    private int divideColor;
    private float childSize;

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context,AttributeSet attrs) {
        super(context, attrs);
        if (attrs==null)return;
        TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.MyLinearLayout);
        data=a.getString(R.styleable.MyLinearLayout_data);
        divideColor=a.getColor(R.styleable.MyLinearLayout_divide_color,getResources().getColor(R.color.color_black3));
        childColor=a.getColor(R.styleable.MyLinearLayout_child_color,getResources().getColor(R.color.flash_time_bg_color));
        childSize=a.getDimension(R.styleable.MyLinearLayout_child_size,ScreenUtil.dip2px(14));
        a.recycle();
        initPaint();

        addView();
    }

    private void initPaint() {
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(divideColor);
        linePaint=new Paint();
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(getResources().getColor(R.color.line));
    }

    private void addView(){
        String[] strings=data.split(",");
        int length=strings.length;
        itemWidth=(ScreenUtil.getDisplayWidth())/length;
        for (int i=0;i<length;i++){
            TextView textView=new TextView(getContext());
            textView.setText(strings[i]);
            textView.setTextColor(childColor);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,childSize);
            textView.setGravity(Gravity.CENTER);
            textView.setOnClickListener(this);
            FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams((int)itemWidth,-2);
            lp.setMargins((int)(i*itemWidth),0,0,0);
            addView(textView,lp);
            if (i==length-1){
                TextPaint textPaint=textView.getPaint();
                if (textPaint!=null){
                    dividerWidth=textPaint.measureText(strings[i]);
                    Log.d("MyLinearLayout","dividerWidth="+dividerWidth);
                    left=(itemWidth-dividerWidth)/2;
                    invalidate();
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("MyLinearLayout","onDraw is done");
        top=getBottom()- ScreenUtil.dip2px(3);
        right=left+dividerWidth;
        bottom=top+ScreenUtil.dip2px(2);
        if (left>0){
            canvas.drawRect(left,top,right,bottom,paint);
        }
        canvas.drawRect(getPaddingLeft(),getBottom()-ScreenUtil.dip2px(1),getWidth()-getPaddingLeft()-getPaddingRight(),getBottom(),linePaint);
    }

    @Override
    public void onClick(View v) {
        TextView textView= (TextView) v;
        left=textView.getLeft()+(itemWidth-dividerWidth)/2;
        invalidate();
        if (itemClickListener!=null){
            itemClickListener.itemClick(textView.getText().toString().trim());
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener{
        void itemClick(String value);
    }
}
