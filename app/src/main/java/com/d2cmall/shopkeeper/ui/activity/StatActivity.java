package com.d2cmall.shopkeeper.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.util.ArrayMap;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.model.StatBean;
import com.d2cmall.shopkeeper.model.StatListBean;
import com.d2cmall.shopkeeper.model.UserBean;
import com.d2cmall.shopkeeper.ui.view.PtrShopMainFrameLayout;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.StatAddressPop;
import com.d2cmall.shopkeeper.ui.view.TipPop;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.DateUtil;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 作者:Created by sinbara on 2019/6/21.
 * 邮箱:hrb940258169@163.com
 */
public class StatActivity extends BaseActivity {
    @BindView(R.id.status_bar_line)
    View statusBarLine;
    @BindView(R.id.first_line)
    View firstLine;
    @BindView(R.id.logo_iv)
    ImageView logoIv;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.time_tv)
    TextView timeTv;
    @BindView(R.id.first_content_ll)
    LinearLayout firstContentLl;
    @BindView(R.id.cursor)
    View cursor;
    @BindView(R.id.bottom_first_line)
    View bottomFirstLine;
    @BindView(R.id.bottom_second_line)
    View bottomSecondLine;
    @BindView(R.id.bottom_first_guide)
    View bottomFirstGuide;
    @BindView(R.id.bottom_third_line)
    View bottomThirdLine;
    @BindView(R.id.bottom_four_line)
    View bottomFourLine;
    @BindView(R.id.bottom_second_guide)
    View bottomSecondGuide;
    @BindView(R.id.tv_pending_pay)
    TextView tvPendingPay;
    @BindView(R.id.tv_pending_pay_tag)
    TextView tvPendingPayTag;
    @BindView(R.id.tv_today_buy_peoples)
    TextView tvTodayBuyPeoples;
    @BindView(R.id.tv_today_buy_peoples_tag)
    TextView tvTodayBuyPeoplesTag;
    @BindView(R.id.tv_sale_num)
    TextView tvSaleNum;
    @BindView(R.id.tv_sale_num_tag)
    TextView tvSaleNumTag;
    @BindView(R.id.tv_sale_orders_num)
    TextView tvSaleOrdersNum;
    @BindView(R.id.tv_sale_orders_num_tag)
    TextView tvSaleOrdersNumTag;
    @BindView(R.id.tv_today_newcustomer_num)
    TextView tvTodayNewcustomerNum;
    @BindView(R.id.tv_today_newcustomer_num_tag)
    TextView tvTodayNewcustomerNumTag;
    @BindView(R.id.tv_today_visitor_num)
    TextView tvTodayVisitorNum;
    @BindView(R.id.tv_today_visitor_num_tag)
    TextView tvTodayVisitorNumTag;
    @BindView(R.id.stat_ll)
    LinearLayout statLl;
    @BindView(R.id.content_rl)
    RelativeLayout contentRl;
    @BindView(R.id.content_ll)
    LinearLayout contentLl;
    @BindView(R.id.check_all_shop)
    TextView checkAllShop;
    @BindView(R.id.shop_num_tv)
    TextView shopNumTv;
    @BindView(R.id.ptr)
    PtrShopMainFrameLayout ptr;

    private float itemWidth;
    private float divideWidth;
    private StatAddressPop pop;
    private String currentStartTime;
    private String currentEndTime;
    private boolean isRefresh;

    @Override
    public int getLayoutId() {
        return R.layout.activity_stat;
    }

    @Override
    public void doBusiness() {
        UserBean userBean=Session.getInstance().getUserFromFile(this);
        if (userBean!=null){
            nameTv.setText(userBean.getNickname());
            ImageLoader.displayImage(this,userBean.getAvatar(),logoIv);
        }
        Sofia.with(this)
                .invasionStatusBar()
                .statusBarBackground(Color.TRANSPARENT);
        ConstraintLayout.LayoutParams cl= (ConstraintLayout.LayoutParams) statusBarLine.getLayoutParams();
        cl.setMargins(0,ScreenUtil.getStatusBarHeight(this),0,0);
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
            textView.setTextColor(getResources().getColor(R.color.color_white));
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
        checkAllShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StatActivity.this,AllShopStatActivity.class);
                startActivity(intent);
            }
        });
        ptr.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                isRefresh=true;
                loadTopStatData();
                loadAllShopStatData();
            }
        });
        logoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TipPop tipPop=new TipPop(StatActivity.this,false);
                tipPop.setContent("是否退出登录");
                tipPop.setBack(new TipPop.CallBack() {
                    @Override
                    public void sure() {
                        Session.getInstance().logout(StatActivity.this);
                        Util.showToast(StatActivity.this,"退出登录成功");
                        Intent intent = new Intent(StatActivity.this, ShopKeeperActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void cancel() {

                    }
                });
                tipPop.show(getWindow().getDecorView());
            }
        });
        ShadowDrawable.setShadowDrawable(contentRl, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), ScreenUtil.dip2px(10), 0, 0);
        currentStartTime= DateUtil.getFutureDate(DateUtil.getToday(),0);
        currentEndTime=DateUtil.getFutureDate(DateUtil.getToday(),1);
        loadTopStatData();
        loadAllShopStatData();
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
                    loadTopStatData();
                    loadAllShopStatData();
                    cursor.setVisibility(View.INVISIBLE);
                }
            });
        }
        pop.show(getWindow().getDecorView());
    }

    private void loadTopStatData(){
        addDisposable(ApiRetrofit.getInstance().getApiService().getStatAll(buildParam()), new BaseObserver<BaseModel<StatBean>>() {
            @Override
            public void onSuccess(BaseModel<StatBean> o) {
                if (isRefresh){
                    ptr.refreshComplete();
                    isRefresh=false;
                }
                if (o.getData()!=null){
                    tvPendingPay.setText(Util.getNumberFormat(o.getData().getTurnover()));//营业收入
                    tvTodayBuyPeoples.setText(String.valueOf(o.getData().getOrderCount()));//订单数
                    tvSaleNum.setText(String.valueOf(o.getData().getHasOrderShopCount()));//开单门店
                    tvSaleOrdersNum.setText(String.valueOf(o.getData().getFollowUserCount()));//关注粉丝
                    tvTodayNewcustomerNum.setText(String.valueOf(o.getData().getVisitUserCount()));//访客流量
                    tvTodayVisitorNum.setText(String.valueOf(o.getData().getNewUserCount()));//新增会员
                }
            }
        });
    }

    private void loadAllShopStatData(){
        addDisposable(ApiRetrofit.getInstance().getApiService().getStatAllShop(buildListParam()), new BaseObserver<BaseModel<StatListBean>>() {
            @Override
            public void onSuccess(BaseModel<StatListBean> o) {
                if (isRefresh){
                    ptr.refreshComplete();
                    isRefresh=false;
                }
                if (o.getData()!=null&&o.getData().getRecords().size()>0){
                    addShop(o.getData().getRecords(),o.getData().getTotal());
                }
            }
        });
    }

    private void addShop(List<StatBean> list,int total){
        contentLl.removeAllViews();
        View view=null;
        shopNumTv.setText(total+"家门店");
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);
        lp.setMargins(ScreenUtil.dip2px(10),0,ScreenUtil.dip2px(10),0);
        for (int i=0;i<(list.size()>6?6:list.size());i++){
            view= LayoutInflater.from(this).inflate(R.layout.layout_stat_item,new LinearLayout(this),false);
            if (i%2==0){
                view.setBackgroundColor(Color.parseColor("#f2f8fd"));
            }else {
                view.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            TextView name=view.findViewById(R.id.name_tv);
            TextView sell=view.findViewById(R.id.sell_tv);
            TextView order=view.findViewById(R.id.order_tv);
            name.setText(list.get(i).getShopName());
            sell.setText(Util.getNumberFormat(list.get(i).getTurnover()));
            order.setText(list.get(i).getOrderCount()+"");
            long shopId=list.get(i).getShopId();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(StatActivity.this,ShopStatActivity.class);
                    intent.putExtra("shopId",shopId);
                    startActivity(intent);
                }
            });
            contentLl.addView(view,lp);
        }
    }

    private ArrayMap<String,String> buildListParam(){
        ArrayMap<String,String> map=Util.buildListParam(1,6);
        map.put("startTime",currentStartTime);
        map.put("endTime",currentEndTime);
        map.put("timeType","d");
        return map;
    }

    private ArrayMap<String,String> buildParam(){
        ArrayMap<String,String> map=new ArrayMap<>();
        map.put("startTime",currentStartTime);
        map.put("endTime",currentEndTime);
        map.put("timeType","d");
        return map;
    }

    private MyClickListener listener=new MyClickListener();

    private class MyClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            TextView textView= (TextView) v;
            LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams((int) divideWidth,ScreenUtil.dip2px(2));
            ll.setMargins((int)(textView.getLeft()+(itemWidth-divideWidth)/2),ScreenUtil.dip2px(7),0,0);
            cursor.setLayoutParams(ll);
            cursor.setVisibility(View.VISIBLE);
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
            loadTopStatData();
            loadAllShopStatData();
        }
    }

    @Override
    protected void onDestroy() {
        if (pop!=null){
            pop.setCallBack(null);
        }
        super.onDestroy();
    }
}
