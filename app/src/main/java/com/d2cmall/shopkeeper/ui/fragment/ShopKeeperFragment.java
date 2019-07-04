package com.d2cmall.shopkeeper.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.ArrayMap;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseTabFragment;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Action;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.common.SharePrefConstant;
import com.d2cmall.shopkeeper.model.HomeBean;
import com.d2cmall.shopkeeper.model.ShopBean;
import com.d2cmall.shopkeeper.model.StatBean;
import com.d2cmall.shopkeeper.model.UserBean;
import com.d2cmall.shopkeeper.ui.activity.CustomerAfterSaleListActivity;
import com.d2cmall.shopkeeper.ui.activity.CustomerManagerActivity;
import com.d2cmall.shopkeeper.ui.activity.FundManagerActivity;
import com.d2cmall.shopkeeper.ui.activity.MarketingUtilActivity;
import com.d2cmall.shopkeeper.ui.activity.OrderManagerActivity;
import com.d2cmall.shopkeeper.ui.activity.ProductManagerActivity;
import com.d2cmall.shopkeeper.ui.activity.PurchaseAllocationWarehousingListActivity;
import com.d2cmall.shopkeeper.ui.activity.ShopUtilActivity;
import com.d2cmall.shopkeeper.ui.activity.StockProductManagertActivity;
import com.d2cmall.shopkeeper.ui.activity.WebActivity;
import com.d2cmall.shopkeeper.ui.view.HomeSharePop;
import com.d2cmall.shopkeeper.ui.view.PtrShopMainFrameLayout;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.SharePop;
import com.d2cmall.shopkeeper.utils.DateUtil;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.SharedPreUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 作者:Created by sinbara on 2019/6/12.
 * 邮箱:hrb940258169@163.com
 */
public class ShopKeeperFragment extends BaseTabFragment {

    @BindView(R.id.status_bar_line)
    View statusBarLine;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.tv_product_num)
    TextView tvProductNum;
    @BindView(R.id.tv_order_num)
    TextView tvOrderNum;
    @BindView(R.id.tv_customer_num)
    TextView tvCustomerNum;
    @BindView(R.id.tv_pending_refund_num)
    TextView tvPendingRefundNum;
    @BindView(R.id.tv_deliver_goods_num)
    TextView tvDeliverGoodsNum;
    @BindView(R.id.tv_pending_writeoff_num)
    TextView tvPendingWriteoffNum;
    @BindView(R.id.tv_pending_pay)
    TextView tvPendingPay;
    @BindView(R.id.tv_today_buy_peoples)
    TextView tvTodayBuyPeoples;
    @BindView(R.id.tv_sale_num)
    TextView tvSaleNum;
    @BindView(R.id.tv_sale_orders_num)
    TextView tvSaleOrdersNum;
    @BindView(R.id.tv_today_newcustomer_num)
    TextView tvTodayNewcustomerNum;
    @BindView(R.id.tv_today_visitor_num)
    TextView tvTodayVisitorNum;
    @BindView(R.id.ptr)
    PtrShopMainFrameLayout ptr;
    @BindView(R.id.ll_pending)
    ConstraintLayout llPending;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.first_content_ll)
    LinearLayout firstContentLl;
    @BindView(R.id.cursor)
    View cursor;
    @BindView(R.id.stock_manager_cl)
    ConstraintLayout stockManagerCl;

    private float divideWidth;
    private float itemWidth;
    private boolean isFirst = true;
    private UserBean userBean;
    private BaseModel<ShopBean> mShopBean;
    private String currentStartTime;
    private String currentEndTime;

    @Override
    public View getRootView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop_keeper2, container, false);
    }

    @Override
    public void doBusiness() {

        ConstraintLayout.LayoutParams cl = (ConstraintLayout.LayoutParams) statusBarLine.getLayoutParams();
        cl.setMargins(0, ScreenUtil.getStatusBarHeight(mContext), 0, 0);
        ShadowDrawable.setShadowDrawable(llPending, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), ScreenUtil.dip2px(16), ScreenUtil.dip2px(16), ScreenUtil.dip2px(16), ScreenUtil.dip2px(5), 0, 0);
        ShadowDrawable.setShadowDrawable(llBottom, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), ScreenUtil.dip2px(16), ScreenUtil.dip2px(16), ScreenUtil.dip2px(16), ScreenUtil.dip2px(16), 0, 0);
        ptr.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                loadHomeInfo();
            }
        });
        List<String> list=new ArrayList<>();
        list.add("今日");
        list.add("昨日");
        list.add("本周");
        list.add("本月");
        list.add("全部");
        int size=list.size();
        itemWidth=(ScreenUtil.getDisplayWidth()-ScreenUtil.dip2px(30))/5;
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams((int)itemWidth,-2);
        for (int i=0;i<size;i++){
            TextView textView=new TextView(getContext());
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
        currentStartTime= DateUtil.getFutureDate(DateUtil.getToday(),0);
        currentEndTime=DateUtil.getFutureDate(DateUtil.getToday(),1);
        loadHomeInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirst  && mContext != null) {
            loadHomeInfo();
        }
        if (isFirst) {
            isFirst = false;
        }
    }

    private void loadHomeInfo() {
        SharedPreUtil.setLong("homeRefreshTime", System.currentTimeMillis());
        userBean = Session.getInstance().getUserFromFile(mContext);
        if (userBean == null) {
            ptr.refreshComplete();
            stockManagerCl.setVisibility(View.VISIBLE);
            tvProductNum.setText("");
            tvOrderNum.setText("");
            tvCustomerNum.setText("");
            tvPendingPay.setText("");
            tvTodayBuyPeoples.setText("");
            tvSaleNum.setText("");
            tvSaleOrdersNum.setText("");
            tvTodayNewcustomerNum.setText("");
            tvTodayVisitorNum.setText("");
            return;
        }
        addDisposable(ApiRetrofit.getInstance().getApiService().getHomeInfo(), new BaseObserver<BaseModel<HomeBean>>() {
            @Override
            public void onSuccess(BaseModel<HomeBean> homeBeanBaseModel) {
                ptr.refreshComplete();
                if (homeBeanBaseModel != null) {
                    //订单
                    if (homeBeanBaseModel.getData().getOrders() != null) {
                        tvPendingRefundNum.setText(String.valueOf(homeBeanBaseModel.getData().getOrders().getWaitPayCount()));//待付款
                        tvPendingWriteoffNum.setText(String.valueOf(homeBeanBaseModel.getData().getOrders().getWaitRefundCount()));//待退款
                        tvDeliverGoodsNum.setText(String.valueOf(homeBeanBaseModel.getData().getOrders().getWaitDeliverCount()));//待发货
                    }
                    //管理
                    if (homeBeanBaseModel.getData().getManage() != null) {
                        tvProductNum.setText(String.valueOf(homeBeanBaseModel.getData().getManage().getProductTotal()));
                        tvOrderNum.setText(String.valueOf(homeBeanBaseModel.getData().getManage().getOrderTotal()));
                        tvCustomerNum.setText(String.valueOf(homeBeanBaseModel.getData().getManage().getMemberTotal()));
                    }
                    //每日数据
                    /*if (homeBeanBaseModel.getData().getDaily() != null) {
                        tvPendingPay.setText(Util.getNumberFormat(homeBeanBaseModel.getData().getDaily().getPaidAmount()));
                        tvTodayBuyPeoples.setText(String.valueOf(homeBeanBaseModel.getData().getDaily().getMemberCount()));
                        tvSaleNum.setText(String.valueOf(homeBeanBaseModel.getData().getDaily().getQuantityCount()));
                        tvSaleOrdersNum.setText(String.valueOf(homeBeanBaseModel.getData().getDaily().getOrderCount()));
                        tvTodayNewcustomerNum.setText(String.valueOf(homeBeanBaseModel.getData().getDaily().getNewlyCount()));
                        tvTodayVisitorNum.setText(String.valueOf(homeBeanBaseModel.getData().getDaily().getVisitorCount()));
                    }*/

                }
            }

            @Override
            public void onError(int errorCode, String msg) {
                ptr.refreshComplete();
                super.onError(errorCode, msg);
            }
        });
        if (userBean!=null&&userBean.getShopId()>0){
            addDisposable(ApiRetrofit.getInstance().getApiService().getBrandInfo(userBean.getShopId()), new BaseObserver<BaseModel<ShopBean>>() {
                @Override
                public void onSuccess(BaseModel<ShopBean> shopBean) {
                    if (shopBean != null) {
                        mShopBean = shopBean;
                        if ("FUNE".equals(mShopBean.getData().getType())){
                            stockManagerCl.setVisibility(View.VISIBLE);
                        }else {
                            if ("BRAND".equals(mShopBean.getData().getType())){
                                userBean.setDef("brand");
                                Session.getInstance().setUser(userBean);
                            }
                            stockManagerCl.setVisibility(View.VISIBLE);
                        }
                        tvShopName.setText(shopBean.getData().getName());
                        if (!StringUtil.isEmpty(shopBean.getData().getBackAddress())){
                            SharedPreUtil.setString(SharePrefConstant.SHOP_BACK_ADDRESS,shopBean.getData().getBackAddress());
                        }
                    }
                }
            });
        }
        loadStatData();
    }

    private void loadStatData(){
        addDisposable(ApiRetrofit.getInstance().getApiService().getStatFormTime(buildParam()), new BaseObserver<BaseModel<StatBean>>() {
            @Override
            public void onSuccess(BaseModel<StatBean> o) {
                if (o.getData()!=null){
                    tvPendingPay.setText(Util.getNumberFormat(o.getData().getTurnover()));
                    tvTodayBuyPeoples.setText(String.valueOf(o.getData().getBuyCount()));
                    tvSaleNum.setText(String.valueOf(o.getData().getPayCount()));
                    tvSaleOrdersNum.setText(String.valueOf(o.getData().getPayOrderCount()));
                    tvTodayNewcustomerNum.setText(String.valueOf(o.getData().getNewUserCount()));
                    tvTodayVisitorNum.setText(String.valueOf(o.getData().getVisitUserCount()));
                }
            }
        });
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
            loadStatData();
        }
    }

    @OnClick({R.id.popular_tv, R.id.preview_tv, R.id.first_chunk, R.id.second_chunk, R.id.third_chunk, R.id.four_chunk, R.id.five_chunk, R.id.six_chunk, R.id.stock_manager_cl, R.id.tv_pending_refund_num, R.id.tv_deliver_goods_num, R.id.tv_pending_writeoff_num, R.id.tv_pending_pay, R.id.tv_today_buy_peoples, R.id.tv_sale_num, R.id.tv_sale_orders_num, R.id.tv_today_newcustomer_num, R.id.tv_today_visitor_num})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.popular_tv://推广
                share(view);
                break;
            case R.id.preview_tv://预览
                //预览店铺
                if (mShopBean == null) return;
                intent = new Intent(mContext, WebActivity.class);
                if (mShopBean.getData().getName() != null) {
                    intent.putExtra("title", mShopBean.getData().getName());
                }
                intent.putExtra("url", Constants.SHARE_URL + mShopBean.getData().getId());
                getActivity().startActivity(intent);
                break;
            case R.id.first_chunk://
                intent=new Intent(mContext, ProductManagerActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.second_chunk:
                intent=new Intent(mContext, OrderManagerActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.third_chunk:
                intent=new Intent(mContext, CustomerManagerActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.four_chunk:
                intent=new Intent(mContext, MarketingUtilActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.five_chunk:
                if (Util.loginChecked((Activity) mContext, 999)) {
                    intent = new Intent(getActivity(), FundManagerActivity.class);
                    getActivity().startActivity(intent);
                }
                break;
            case R.id.six_chunk:
                intent=new Intent(mContext, ShopUtilActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.stock_manager_cl:
                if (Util.loginChecked((Activity) mContext, 999)) {
                    if ("FUNE".equals(mShopBean.getData().getType())){
                        intent = new Intent(getActivity(), StockProductManagertActivity.class);
                        getActivity().startActivity(intent);
                    }else {
                        intent = new Intent(getActivity(), PurchaseAllocationWarehousingListActivity.class);
                        intent.putExtra("second",2);
                        intent.putExtra("special",true);
                        getActivity().startActivity(intent);
                    }
                }
                break;
            case R.id.tv_pending_refund_num:
                //待付款
                if (Util.loginChecked((Activity) mContext, 999)) {
                    intent = new Intent(getActivity(), OrderManagerActivity.class);
                    intent.putExtra("first", 0);
                    intent.putExtra("second", 1);
                    getActivity().startActivity(intent);
                }
                break;
            case R.id.tv_deliver_goods_num:
                //待发货
                if (Util.loginChecked((Activity) mContext, 999)) {
                    intent = new Intent(getActivity(), OrderManagerActivity.class);
                    intent.putExtra("first", 0);
                    intent.putExtra("second", 2);
                    getActivity().startActivity(intent);
                }
                break;
            case R.id.tv_pending_writeoff_num:
                //待退款
                if (Util.loginChecked((Activity) mContext, 999)) {
                    intent = new Intent(getActivity(), CustomerAfterSaleListActivity.class);
                    intent.putExtra("first", 1);
                    intent.putExtra("second", 2);
                    getActivity().startActivity(intent);
                }
                break;
            case R.id.tv_pending_pay:
                break;
            case R.id.tv_today_buy_peoples:
                break;
            case R.id.tv_sale_num:
                break;
            case R.id.tv_sale_orders_num:
                break;
            case R.id.tv_today_newcustomer_num:
                break;
            case R.id.tv_today_visitor_num:
                break;
        }
    }

    private void share(View view){
        if (mShopBean==null)return;
        HomeSharePop pop=new HomeSharePop(mContext,mShopBean.getData());
        pop.show(view);
    }

    @Override
    public void show() {
        super.show();
        if (!isFirst && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getActivity().getWindow();
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
