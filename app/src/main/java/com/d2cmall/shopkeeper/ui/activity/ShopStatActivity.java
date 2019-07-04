package com.d2cmall.shopkeeper.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Address;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.model.ShopBean;
import com.d2cmall.shopkeeper.model.StatBean;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.StatAddressPop;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.DateUtil;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:Created by sinbara on 2019/6/21.
 * 邮箱:hrb940258169@163.com
 */
public class ShopStatActivity extends BaseActivity {

    @BindView(R.id.preview_tv)
    TextView previewTv;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.address_tv)
    TextView addressTv;
    @BindView(R.id.phone_iv)
    ImageView phoneIv;
    @BindView(R.id.middle_rl)
    RelativeLayout middleRl;
    @BindView(R.id.time_tv)
    TextView timeTv;
    @BindView(R.id.first_content_ll)
    LinearLayout firstContentLl;
    @BindView(R.id.cursor)
    View cursor;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.tv5)
    TextView tv5;
    @BindView(R.id.tv6)
    TextView tv6;
    @BindView(R.id.tv7)
    TextView tv7;
    @BindView(R.id.tv8)
    TextView tv8;
    @BindView(R.id.tv9)
    TextView tv9;
    @BindView(R.id.content_ll)
    LinearLayout contentLl;
    @BindView(R.id.back_iv)
    ImageView backIv;

    private float itemWidth;
    private float divideWidth;
    private StatAddressPop pop;
    private long shopId;
    private String currentStartTime;
    private String currentEndTime;
    private String phone;

    @Override
    public int getLayoutId() {
        return R.layout.activity_shop_stat;
    }

    @Override
    public void doBusiness() {
        Sofia.with(this)
                .invasionStatusBar()
                .statusBarBackground(Color.TRANSPARENT);
        shopId=getIntent().getLongExtra("shopId",0);
        List<String> list=new ArrayList<>();
        list.add("今日");
        list.add("昨日");
        list.add("本周");
        list.add("本月");
        list.add("全部");
        int size=list.size();
        itemWidth=ScreenUtil.getDisplayWidth()/5;
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams((int)itemWidth,-2);
        for (int i=0;i<size;i++){
            TextView textView=new TextView(this);
            textView.setText(list.get(i));
            textView.setTextColor(getResources().getColor(R.color.flash_time_bg_color));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
            textView.setGravity(Gravity.CENTER);
            textView.setOnClickListener(listener);
            firstContentLl.addView(textView,lp);
            if (i==size-1){
                divideWidth=textView.getPaint().measureText(list.get(i));
                LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams((int) divideWidth,ScreenUtil.dip2px(2));
                ll.setMargins((int)(itemWidth-divideWidth)/2,ScreenUtil.dip2px(7),0,0);
                cursor.setLayoutParams(ll);
            }
        }
        timeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop();
            }
        });
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        previewTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopStatActivity.this, WebActivity.class);
                intent.putExtra("title", nameTv.getText().toString().trim());
                intent.putExtra("url", Constants.SHARE_URL + shopId);
                startActivity(intent);
            }
        });
        phoneIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtil.isEmpty(phone)){
                    Intent call = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + phone);
                    call.setData(data);
                    startActivity(call);
                }
            }
        });
        ShadowDrawable.setShadowDrawable(contentLl, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), ScreenUtil.dip2px(10), 0, 0);
        currentStartTime= DateUtil.getFutureDate(DateUtil.getToday(),0);
        currentEndTime=DateUtil.getFutureDate(DateUtil.getToday(),1);
        loadShopStatData();
        loadShop();
    }

    private void loadShop(){
        addDisposable(ApiRetrofit.getInstance().getApiService().getBrandInfo(shopId), new BaseObserver<BaseModel<ShopBean>>() {
            @Override
            public void onSuccess(BaseModel<ShopBean> o) {
                if (o.getData()!=null){
                    ImageLoader.displayImage(ShopStatActivity.this,StringUtil.getD2cPicUrl(o.getData().getLogo()),image);
                    nameTv.setText(o.getData().getName());
                    if (!StringUtil.isEmpty(o.getData().getAddress())) {
                        if (o.getData().getAddress().startsWith("{")){
                            Gson gson=new Gson();
                            Address add=gson.fromJson(o.getData().getAddress(),Address.class);
                            addressTv.setText(add.province+add.city+add.district+add.address);
                        }else {
                            addressTv.setText(o.getData().getAddress());
                        }
                    }
                    phone=o.getData().getTelephone();
                }
            }
        });
    }

    private void showPop() {
        if (pop==null){
            pop=new StatAddressPop(this);
            pop.setCallBack(new StatAddressPop.CallBack() {
                @Override
                public void callback(String startTime, String endTime) {
                    currentStartTime=startTime+" 00:00:00";
                    currentEndTime=endTime+" 00:00:00";
                    timeTv.setText(startTime+"\n"+endTime);
                }
            });
        }
        pop.show(getWindow().getDecorView());
    }

    private MyClickListener listener=new MyClickListener();

    private class MyClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            TextView textView= (TextView) v;
            LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams((int) divideWidth,ScreenUtil.dip2px(2));
            ll.setMargins((int)(textView.getLeft()+(itemWidth-divideWidth)/2),ScreenUtil.dip2px(7),0,0);
            cursor.setLayoutParams(ll);
            String text=textView.getText().toString().trim();
            int index=0;
            Calendar calendar=null;
            switch (text){
                case "今日":
                    currentStartTime= DateUtil.getFutureDate(DateUtil.getToday(),0);
                    currentEndTime=DateUtil.getFutureDate(DateUtil.getToday(),1);
                    break;
                case "昨日":
                    currentStartTime= DateUtil.getFutureDate(DateUtil.getToday(),-1);
                    currentEndTime=DateUtil.getFutureDate(DateUtil.getToday(),0);
                    break;
                case "本周":
                    calendar = Calendar.getInstance();
                    index = calendar.get(Calendar.DAY_OF_WEEK);
                    currentStartTime= DateUtil.getFutureDate(DateUtil.getToday(),-(index-1));
                    currentEndTime=DateUtil.getFutureDate(DateUtil.getToday(),1);
                    break;
                case "本月":
                    calendar = Calendar.getInstance();
                    index = calendar.get(Calendar.DAY_OF_MONTH);
                    currentStartTime= DateUtil.getFutureDate(DateUtil.getToday(),-(index-1));
                    currentEndTime=DateUtil.getFutureDate(DateUtil.getToday(),1);
                    break;
                case "全部":
                    calendar = Calendar.getInstance();
                    index = calendar.get(Calendar.DAY_OF_YEAR);
                    currentStartTime= DateUtil.getFutureDate(DateUtil.getToday(),-(index-1));
                    currentEndTime=DateUtil.getFutureDate(DateUtil.getToday(),1);
                    break;
            }
            loadShopStatData();
        }
    }

    private void loadShopStatData(){
        addDisposable(ApiRetrofit.getInstance().getApiService().getStatShop(buildParam()), new BaseObserver<BaseModel<StatBean>>() {
            @Override
            public void onSuccess(BaseModel<StatBean> o) {
                if (o.getData()!=null){
                    tv1.setText(Util.getNumberFormat(o.getData().getTurnover()));
                    tv2.setText(String.valueOf(o.getData().getOrderCount()));
                    tv3.setText(String.valueOf(o.getData().getBuyCount()));
                    tv4.setText(String.valueOf(o.getData().getPayCount()));
                    tv5.setText(String.valueOf(o.getData().getVisitUserCount()));
                    tv6.setText(String.valueOf(o.getData().getFollowUserCount()));
                    tv7.setText(String.valueOf(o.getData().getNewUserCount()));
                    tv8.setText(String.valueOf(o.getData().getReturnOrderCount()));
                    tv9.setText(Util.getNumberFormat(o.getData().getReturnOrderMoney()));
                }
            }
        });
    }

    private ArrayMap<String,String> buildParam(){
        ArrayMap<String,String> map=new ArrayMap<>();
        map.put("startTime",currentStartTime);
        map.put("endTime",currentEndTime);
        map.put("timeType","d");
        map.put("shopIds",String.valueOf(shopId));
        return map;
    }

    @Override
    protected void onDestroy() {
        if (pop!=null){
            pop.setCallBack(null);
        }
        super.onDestroy();
    }
}
