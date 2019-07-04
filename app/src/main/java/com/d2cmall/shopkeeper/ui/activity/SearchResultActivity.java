package com.d2cmall.shopkeeper.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.adapter.MarketProductAdapter;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.list.BaseVirtualAdapter;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.binder.ScrollEndBinder;
import com.d2cmall.shopkeeper.holder.DefaultHolder;
import com.d2cmall.shopkeeper.model.MarketProductListBean;
import com.d2cmall.shopkeeper.model.ProductBean;
import com.d2cmall.shopkeeper.ui.view.PtrStoreHouseFrameLayout;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 作者:Created by sinbara on 2019/6/19.
 * 邮箱:hrb940258169@163.com
 */
public class SearchResultActivity extends BaseActivity {

    @BindView(R.id.select_tv)
    TextView selectTv;
    @BindView(R.id.first_chunk)
    View firstChunk;
    @BindView(R.id.second_chunk)
    View secondChunk;
    @BindView(R.id.third_chunk)
    View thirdChunk;
    @BindView(R.id.four_chunk)
    View fourChunk;
    @BindView(R.id.syn_tv)
    TextView synTv;
    @BindView(R.id.profit_tv)
    TextView profitTv;
    @BindView(R.id.supply_price_tv)
    TextView supplyPriceTv;
    @BindView(R.id.allot_tv)
    TextView allotTv;
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.ptr)
    PtrStoreHouseFrameLayout ptr;
    @BindView(R.id.select_ll)
    View selectLl;
    @BindView(R.id.search_tv)
    TextView searchTv;
    @BindView(R.id.empty_tv)
    TextView emptyTv;

    private VirtualLayoutManager layoutManager;
    private DelegateAdapter delegateAdapter;
    private long currentClassId;//当前二级id
    private int profitAsc = -1; //0是降序 1是升序
    private int supplyPriceAsc = -1; //0是降序 1是升序
    private boolean isCreateDate;
    private boolean isAllot;
    private boolean isRefresh;
    private String name;
    private String title;
    private View lastClick;
    private boolean search;

    private List<ProductBean> list = new ArrayList<>();
    private MarketProductAdapter marketProductAdapter;
    private BaseVirtualAdapter<DefaultHolder> endAdapter; //列表结束标志
    private int p=1;
    private boolean hasNext;

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_result;
    }

    @Override
    public void doBusiness() {
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        getDataFromIntent();
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
        layoutManager = new VirtualLayoutManager(this);
        delegateAdapter = new DelegateAdapter(layoutManager);
        recycleView.setLayoutManager(layoutManager);
        marketProductAdapter = new MarketProductAdapter(this, list,true);
        delegateAdapter.addAdapter(marketProductAdapter);
        recycleView.setAdapter(delegateAdapter);
        firstChunk.setSelected(true);
        lastClick = firstChunk;
        changeTextViewColor();
        loadProductList();
        initListener();
    }

    private void initListener() {
        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        int last = layoutManager.findLastVisibleItemPosition();
                        if (last > delegateAdapter.getItemCount() - 3 && hasNext) {
                            p++;
                            loadProductList();
                        }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int lastVisPosition = layoutManager.findLastVisibleItemPosition();
                int itemCount = layoutManager.getItemCount();
                if (lastVisPosition >= itemCount - 3 && layoutManager.findFirstVisibleItemPosition() > 1&&!hasNext) {
                    if (endAdapter == null) {
                        ScrollEndBinder endBinder = new ScrollEndBinder();
                        endAdapter = new BaseVirtualAdapter<>(endBinder, 100);
                        delegateAdapter.addAdapter(endAdapter);
                    }
                } else {
                    if (endAdapter != null) {
                        delegateAdapter.removeAdapter(endAdapter);
                        endAdapter = null;
                    }
                }
            }
        });
    }

    private void getDataFromIntent(){
        name=getIntent().getStringExtra("name");
        title=getIntent().getStringExtra("title");
        search=getIntent().getBooleanExtra("search",true);
        if (!search){
            selectTv.setText(title);
            selectTv.setVisibility(View.VISIBLE);
            selectLl.setVisibility(View.GONE);
            searchTv.setVisibility(View.GONE);
            emptyTv.setVisibility(View.GONE);
        }else {
            if (StringUtil.isEmpty(name)){
                emptyTv.setVisibility(View.VISIBLE);
                searchTv.setVisibility(View.GONE);
            }else {
                emptyTv.setVisibility(View.GONE);
                searchTv.setText(name);
                searchTv.setVisibility(View.VISIBLE);
            }
            selectLl.setVisibility(View.VISIBLE);
            selectTv.setVisibility(View.GONE);
        }
        currentClassId=getIntent().getLongExtra("id",0);
        isCreateDate=getIntent().getBooleanExtra("date",false);
        isAllot=getIntent().getBooleanExtra("allot",false);
        profitAsc=getIntent().getIntExtra("profit",-1);
    }

    private void refresh(){
        isRefresh=true;
        loadProductList();
    }

    private void loadProductList() {
        addDisposable(ApiRetrofit.getInstance().getApiService().getMarketProductList(buildParam()), new BaseObserver<BaseModel<MarketProductListBean>>() {
            @Override
            public void onSuccess(BaseModel<MarketProductListBean> o) {
                if (isRefresh){
                    ptr.refreshComplete();
                    isRefresh=false;
                }
                if (p==1){
                    list.clear();
                }
                hasNext=o.getData().getCurrent()<o.getData().getPages()?true:false;
                if (o.getData() != null && o.getData().getRecords() != null && o.getData().getRecords().size() > 0) {
                    list.addAll(o.getData().getRecords());
                    if (list.size() % 2 == 1) {
                        list.add(new ProductBean());
                    }
                }
                marketProductAdapter.notifyDataSetChanged();
            }
                    @Override
                    public void onError(int errorCode, String msg) {
                        super.onError(errorCode, msg);
                        if (isRefresh){
                            ptr.refreshComplete();
                            isRefresh=false;
                        }
                    }
                }
        );
    }

    private ArrayMap<String, String> buildParam() {
        ArrayMap<String, String> map = Util.buildListParam(p, 50);
        if (currentClassId != 0) {
            map.put("classifyId", String.valueOf(currentClassId));
        }
        if (isCreateDate) {
            map.put("desc", "create_date");
        }
        if (profitAsc == 1) {
            map.put("asc", "profit");
        } else if (profitAsc == 0) {
            map.put("desc", "profit");
        }
        if (supplyPriceAsc == 1) {
            map.put("asc", "supply_price");
        } else if (supplyPriceAsc == 0) {
            map.put("desc", "supply_price");
        }
        if (isAllot) {
            map.put("allot", "1");
        }
        if (!StringUtil.isEmpty(name)){
            map.put("name",name);
        }
        return map;
    }

    @OnClick({R.id.back_iv, R.id.cart_iv, R.id.category_iv, R.id.first_chunk, R.id.second_chunk, R.id.third_chunk, R.id.four_chunk,R.id.select_ll})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.select_ll:
                intent=new Intent(this,SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.back_iv:
                finish();
                break;
            case R.id.cart_iv:
                intent=new Intent(this,CartActivity.class);
                startActivity(intent);
                break;
            case R.id.category_iv:
                intent=new Intent(this,SearchCategoryActivity.class);
                startActivityForResult(intent,100);
                break;
            case R.id.first_chunk:
                if (firstChunk != lastClick) {
                    lastClick.setSelected(false);
                    changeTextViewColor();
                }
                if (secondChunk == lastClick) {
                    profitTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_paixu, 0);
                }
                if (thirdChunk == lastClick) {
                    supplyPriceTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_paixu, 0);
                }
                firstChunk.setSelected(!firstChunk.isSelected());
                synTv.setSelected(firstChunk.isSelected());
                lastClick = firstChunk;
                isCreateDate = firstChunk.isSelected();
                profitAsc = -1;
                supplyPriceAsc = -1;
                isAllot = false;
                loadProductList();
                break;
            case R.id.second_chunk:
                if (secondChunk != lastClick) {
                    lastClick.setSelected(false);
                    changeTextViewColor();
                }
                if (thirdChunk == lastClick) {
                    supplyPriceTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_paixu, 0);
                }
                if (!secondChunk.isSelected()) {
                    secondChunk.setSelected(true);
                }
                profitAsc = profitAsc == -1 ? 0 : profitAsc == 0 ? 1 : 0;
                profitTv.setSelected(secondChunk.isSelected());
                if (profitAsc == 1) {
                    profitTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_paixu_sheng, 0);
                } else {
                    profitTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_paixu_jiang, 0);
                }
                lastClick = secondChunk;
                isCreateDate = false;
                isAllot = false;
                supplyPriceAsc = -1;
                loadProductList();
                break;
            case R.id.third_chunk:
                if (thirdChunk != lastClick) {
                    lastClick.setSelected(false);
                    changeTextViewColor();
                }
                if (secondChunk == lastClick) {
                    profitTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_paixu, 0);
                }
                if (!thirdChunk.isSelected()) {
                    thirdChunk.setSelected(true);
                }
                supplyPriceTv.setSelected(thirdChunk.isSelected());
                supplyPriceAsc = supplyPriceAsc == -1 ? 0 : supplyPriceAsc == 0 ? 1 : 0;
                if (supplyPriceAsc == 1) {
                    supplyPriceTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_paixu_sheng, 0);
                } else {
                    supplyPriceTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_paixu_jiang, 0);
                }
                lastClick = thirdChunk;
                isCreateDate = false;
                isAllot = false;
                profitAsc = -1;
                loadProductList();
                break;
            case R.id.four_chunk:
                if (fourChunk != lastClick) {
                    lastClick.setSelected(false);
                    changeTextViewColor();
                }
                if (secondChunk == lastClick) {
                    profitTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_paixu, 0);
                }
                if (thirdChunk == lastClick) {
                    supplyPriceTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_paixu, 0);
                }
                fourChunk.setSelected(!fourChunk.isSelected());
                allotTv.setSelected(fourChunk.isSelected());
                lastClick = fourChunk;
                isCreateDate = false;
                profitAsc = -1;
                supplyPriceAsc = -1;
                isAllot = fourChunk.isSelected();
                loadProductList();
                break;
        }
    }

    private void changeTextViewColor() {
        if (lastClick == firstChunk) {
            synTv.setSelected(lastClick.isSelected());
        } else if (lastClick == secondChunk) {
            profitTv.setSelected(lastClick.isSelected());
        } else if (lastClick == thirdChunk) {
            supplyPriceTv.setSelected(lastClick.isSelected());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        getDataFromIntent();
        loadProductList();
        super.onNewIntent(intent);
    }
}
