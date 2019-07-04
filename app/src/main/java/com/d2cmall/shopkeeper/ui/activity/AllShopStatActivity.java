package com.d2cmall.shopkeeper.ui.activity;

import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.adapter.StatAdapter;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.list.BaseVirtualAdapter;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.binder.ScrollEndBinder;
import com.d2cmall.shopkeeper.holder.DefaultHolder;
import com.d2cmall.shopkeeper.model.StatBean;
import com.d2cmall.shopkeeper.model.StatListBean;
import com.d2cmall.shopkeeper.ui.view.PtrStoreHouseFrameLayout;
import com.d2cmall.shopkeeper.ui.view.StatAddressPop;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.DateUtil;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 作者:Created by sinbara on 2019/6/21.
 * 邮箱:hrb940258169@163.com
 */
public class AllShopStatActivity extends BaseActivity {

    @BindView(R.id.ptr)
    PtrStoreHouseFrameLayout ptr;
    @BindView(R.id.time_tv)
    TextView timeTv;
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.first_content_ll)
    LinearLayout firstContentLl;
    @BindView(R.id.cursor)
    View cursor;

    private StatAddressPop pop;
    private float itemWidth;
    private float divideWidth;
    private String currentStartTime;
    private String currentEndTime;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private StatAdapter statAdapter;
    private List<StatBean> list=new ArrayList<>();
    private BaseVirtualAdapter<DefaultHolder> endAdapter; //列表结束标志
    private boolean hasNext; //是否有下一页数据
    private int p=1;
    private boolean isRefresh;

    @Override
    public int getLayoutId() {
        return R.layout.activity_all_shop_stat;
    }

    @Override
    public void doBusiness() {
        Sofia.with(this).statusBarBackground(getResources().getColor(R.color.normal_blue));
        virtualLayoutManager = new VirtualLayoutManager(this);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager, true);
        recycleView.setLayoutManager(virtualLayoutManager);
        recycleView.setAdapter(delegateAdapter);
        statAdapter = new StatAdapter(this, list);
        delegateAdapter.addAdapter(statAdapter);
        List<String> list = new ArrayList<>();
        list.add("今日");
        list.add("昨日");
        list.add("本周");
        list.add("本月");
        list.add("全部");
        int size = list.size();
        itemWidth=ScreenUtil.getDisplayWidth()/5;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) itemWidth, -2);
        for (int i = 0; i < size; i++) {
            TextView textView = new TextView(this);
            textView.setText(list.get(i));
            textView.setTextColor(getResources().getColor(R.color.color_white));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            textView.setGravity(Gravity.CENTER);
            textView.setOnClickListener(listener);
            firstContentLl.addView(textView, lp);
            if (i == size - 1) {
                divideWidth = textView.getPaint().measureText(list.get(i));
                LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams((int) divideWidth, ScreenUtil.dip2px(2));
                ll.setMargins((int) (itemWidth - divideWidth) / 2, ScreenUtil.dip2px(7), 0, 0);
                cursor.setLayoutParams(ll);
            }
        }
        currentStartTime= DateUtil.getFutureDate(DateUtil.getToday(),0);
        currentEndTime=DateUtil.getFutureDate(DateUtil.getToday(),1);
        loadAllShopStatData();
        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int lastVisPosition=virtualLayoutManager.findLastVisibleItemPosition();
                if (lastVisPosition>delegateAdapter.getItemCount()-3&&!hasNext){
                    if (endAdapter == null) {
                        ScrollEndBinder endBinder = new ScrollEndBinder();
                        endAdapter = new BaseVirtualAdapter<>(endBinder, 100);
                        delegateAdapter.addAdapter(endAdapter);
                    }
                }else {
                    if (endAdapter != null) {
                        delegateAdapter.removeAdapter(endAdapter);
                        endAdapter = null;
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        int last = virtualLayoutManager.findLastVisibleItemPosition();
                        if (last > delegateAdapter.getItemCount() - 3 && hasNext) {
                            p++;
                            loadAllShopStatData();
                        }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }}
        );
        ptr.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refresh();
            }
        });
    }

    private void refresh(){
        p=1;
        isRefresh=true;
        loadAllShopStatData();
    }

    private void loadAllShopStatData(){
        addDisposable(ApiRetrofit.getInstance().getApiService().getStatAllShop(buildListParam()), new BaseObserver<BaseModel<StatListBean>>() {
            @Override
            public void onSuccess(BaseModel<StatListBean> o) {
                if (isRefresh){
                    ptr.refreshComplete();
                    isRefresh=false;
                }
                if (p==1){
                    list.clear();
                }
                hasNext=o.getData().getCurrent()<o.getData().getPages()?true:false;
                if (o.getData()!=null&&o.getData().getRecords().size()>0){
                    list.addAll(o.getData().getRecords());
                }
                statAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int errorCode, String msg) {
                if (isRefresh){
                    ptr.refreshComplete();
                    isRefresh=false;
                }
                super.onError(errorCode, msg);
            }
        });
    }

    private ArrayMap<String,String> buildListParam(){
        ArrayMap<String,String> map= Util.buildListParam(p,30);
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
            loadAllShopStatData();
        }
    }

    @OnClick({R.id.back_iv, R.id.time_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.time_tv:
                showPop();
                break;
        }
    }

    private void showPop() {
        if (pop == null) {
            pop = new StatAddressPop(this);
            pop.setCallBack(new StatAddressPop.CallBack() {
                @Override
                public void callback(String startTime, String endTime) {
                    timeTv.setText(startTime + "\n" + endTime);
                    currentStartTime=startTime+" 00:00:00";
                    currentEndTime=endTime+" 00:00:00";
                    loadAllShopStatData();
                }
            });
        }
        pop.show(getWindow().getDecorView());
    }

}
