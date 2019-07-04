package com.d2cmall.shopkeeper.ui.view;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.utils.ScreenUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/4/29.
 * 邮箱:hrb940258169@163.com
 */

public class FilterPop implements TransparentPop.InvokeListener,View.OnClickListener {

    @BindView(R.id.product_style_ll)
    LinearLayout productStyleLl;
    @BindView(R.id.product_source_ll)
    LinearLayout productSourceLl;
    @BindView(R.id.product_promotion_ll)
    LinearLayout productPromotionLl;
    private Context context;
    private View rootView;
    private TransparentPop pop;
    private SelectListener listener;
    private TextView currentStyle;
    private TextView currentPromotion;
    private int allot,buyout,crowd;

    public FilterPop(Context context, String[][] data,SelectListener listener) {
        this.context=context.getApplicationContext();
        this.listener=listener;
        rootView = LayoutInflater.from(context).inflate(R.layout.layout_filter, null);
        ButterKnife.bind(this,rootView);
        int height = ScreenUtil.screenHeight - ScreenUtil.statusbarheight - ScreenUtil.dip2px(85);
        pop = new TransparentPop(context, -1, height, true, this);
        pop.setFocusable(false);
        pop.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.top_slide_in));
        pop.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.top_slide_out));
        setData(context,data);
    }

    private void setData(Context context,String[][] data){
        for (int i=0;i<data.length;i++){
            String[] itemData=data[i];
            LinearLayout content=null;
            int id=0;
            if (i==0){
                content=productPromotionLl;
                id=300;
            }
            addItemView(context,itemData,content,id);
        }
    }

    public void setListener(SelectListener listener){
        this.listener=listener;
    }

    private void addItemView(Context context,String[] itemData,LinearLayout content,int id){
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ScreenUtil.dip2px(54),ScreenUtil.dip2px(25));
        lp.setMargins(0,0,ScreenUtil.dip2px(14),0);
        TextView textView=new TextView(context);
        textView.setTextColor(context.getResources().getColor(R.color.color_white));
        textView.setBackgroundColor(context.getResources().getColor(R.color.filter_select));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
        textView.setText("全部");
        textView.setId(id);
        textView.setOnClickListener(this);
        textView.setSelected(true);
        textView.setGravity(Gravity.CENTER);
        content.addView(textView,lp);
        if (id==100){
            currentStyle=textView;
        }else if (id==300){
            currentPromotion=textView;
        }
        for (int i=0;i<itemData.length;i++){
            textView=new TextView(context);
            textView.setId(id+i+1);
            textView.setOnClickListener(this);
            textView.setTextColor(context.getResources().getColor(R.color.color_black3));
            textView.setBackgroundColor(context.getResources().getColor(R.color.filter_un_select));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
            textView.setGravity(Gravity.CENTER);
            textView.setText(itemData[i]);
            content.addView(textView,lp);
        }
    }

    public void show(View view) {
        pop.show(view, -1, ScreenUtil.dip2px(85), true);
    }

    public void dismiss() {
        pop.dismiss(true);
    }

    public boolean isShow() {
        if (pop != null) {
            return pop.isShowing();
        }
        return false;
    }

    @Override
    public void invokeView(LinearLayout v) {
        v.setGravity(Gravity.TOP);
        v.addView(rootView);
    }

    @Override
    public void releaseView(LinearLayout v) {
        listener=null;
        v.removeAllViews();
        rootView = null;
    }

    @OnClick({R.id.cancel_tv, R.id.sure_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel_tv:
                dismiss();
                break;
            case R.id.sure_tv:
                if (listener!=null){
                    listener.select(allot,buyout,crowd);
                }
                dismiss();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case 100:
                allot=0;
                buyout=0;
                selectView((TextView) v,1);
                break;
            case 101:
                allot=1;
                buyout=0;
                selectView((TextView) v,1);
                break;
            case 102:
                allot=0;
                buyout=1;
                selectView((TextView) v,1);
                break;
            case 300:
                crowd=0;
                selectView((TextView)v,3);
                break;
            case 301:
                crowd=1;
                selectView((TextView)v,3);
                break;
        }
    }

    private void selectView(TextView textView,int type){
        if (type==1){
            if (currentStyle!=null){
                currentStyle.setTextColor(context.getResources().getColor(R.color.color_black3));
                currentStyle.setBackgroundColor(context.getResources().getColor(R.color.filter_un_select));
            }
            currentStyle=textView;
        }else if (type==3){
            if (currentPromotion!=null){
                currentPromotion.setTextColor(context.getResources().getColor(R.color.color_black3));
                currentPromotion.setBackgroundColor(context.getResources().getColor(R.color.filter_un_select));
            }
            currentPromotion=textView;
        }
        textView.setTextColor(context.getResources().getColor(R.color.color_white));
        textView.setBackgroundColor(context.getResources().getColor(R.color.filter_select));
    }

    public interface SelectListener{
        void select(int... value);
    }
}
