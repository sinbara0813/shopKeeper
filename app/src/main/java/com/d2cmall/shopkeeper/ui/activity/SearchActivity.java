package com.d2cmall.shopkeeper.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.common.SharePrefConstant;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.SharedPreUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/6/19.
 * 邮箱:hrb940258169@163.com
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.content_ll)
    LinearLayout contentLl;
    @BindView(R.id.clear_tv)
    TextView clearTv;
    @BindView(R.id.search_et)
    EditText searchEt;

    private List<String> history=new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void doBusiness() {
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        String value=SharedPreUtil.getString(SharePrefConstant.SEARCH_HISTORY,"");
        String[] v=value.split(",");
        for (String s: v) {
            if (!StringUtil.isEmpty(s)){
                history.add(s);
            }
        }
        addView();
        if (history.size()>0){
            clearTv.setVisibility(View.VISIBLE);
        }
        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //点击搜索的时候隐藏软键盘
                    hideKeyboard(v);
                    String value=searchEt.getText().toString().trim();
                    if (StringUtil.isEmpty(value)){
                        Util.showToast(SearchActivity.this,"请输入搜索内容");
                        return false;
                    }
                    history.add(value);
                    Intent intent=new Intent(SearchActivity.this,SearchResultActivity.class);
                    intent.putExtra("name",value);
                    startActivity(intent);
                    return true;
                }

                return false;
            }
        });
        searchEt.requestFocus();
    }

    public void hideKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void addView(){
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1, ScreenUtil.dip2px(42));
        int size=history.size();
        for (int i=0;i<size;i++){
            LinearLayout linearLayout=new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER_VERTICAL);
            addItem(linearLayout,i);
            contentLl.addView(linearLayout,lp);
            View view=new View(this);
            LinearLayout.LayoutParams lineLp=new LinearLayout.LayoutParams(-1, ScreenUtil.dip2px(0.5f));
            view.setBackgroundColor(ContextCompat.getColor(this,R.color.bg_color));
            contentLl.addView(view,lineLp);
        }
    }

    private void addItem(LinearLayout layout,int index){
        TextView textView=new TextView(this);
        textView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.yuncang_sousuo_lishi,0,0,0);
        textView.setCompoundDrawablePadding(ScreenUtil.dip2px(5));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
        textView.setTextColor(getResources().getColor(R.color.color_black3));
        textView.setText(history.get(index));
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-2,-2,1);
        lp.setMarginStart(ScreenUtil.dip2px(25));
        layout.addView(textView,lp);
        textView.setOnClickListener(this::onClick);
        lp=new LinearLayout.LayoutParams(-2,-2);
        lp.setMarginEnd(ScreenUtil.dip2px(20));
        ImageView imageView=new ImageView(this);
        imageView.setOnClickListener(this::onClick);
        imageView.setTag(index);
        imageView.setImageResource(R.mipmap.yuncang_sousuo_close);
        layout.addView(imageView,lp);
    }

    @OnClick({R.id.search_tv, R.id.clear_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_tv:
                finish();
                break;
            case R.id.clear_tv:
                contentLl.removeAllViews();
                clearTv.setVisibility(View.GONE);
                history.clear();
                SharedPreUtil.setString(SharePrefConstant.SEARCH_HISTORY,"");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof ImageView){
            contentLl.removeView((View) v.getParent());
            int index= (int) v.getTag();
            history.remove(index);
        }else if (v instanceof TextView){
            String value=((TextView)v).getText().toString().trim();
            Intent intent=new Intent(this,SearchResultActivity.class);
            intent.putExtra("name",value);
            startActivity(intent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        StringBuilder builder=new StringBuilder();
        for (String s: history) {
            builder.append(s);
            builder.append(",");
        }
        String result=builder.toString();
        if (!StringUtil.isEmpty(result)){
            result=result.substring(0,result.length()-1);
        }
        SharedPreUtil.setString(SharePrefConstant.SEARCH_HISTORY,result);
    }
}
