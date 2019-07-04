package com.d2cmall.shopkeeper.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.constraint.Guideline;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.View;
import android.widget.ImageView;
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
import com.d2cmall.shopkeeper.common.Address;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.holder.DefaultHolder;
import com.d2cmall.shopkeeper.model.MarketProductListBean;
import com.d2cmall.shopkeeper.model.ProductBean;
import com.d2cmall.shopkeeper.model.ShopBean;
import com.d2cmall.shopkeeper.ui.view.PtrStoreHouseFrameLayout;
import com.d2cmall.shopkeeper.ui.view.ScanCodePop;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.ui.view.headerViewPager.HeaderScrollHelper;
import com.d2cmall.shopkeeper.ui.view.headerViewPager.HeaderViewPager;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/6/20.
 * 邮箱:hrb940258169@163.com
 */
public class BrandDetailActivity extends BaseActivity implements HeaderScrollHelper.ScrollableContainer {

    @BindView(R.id.first_line)
    Guideline firstLine;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.cart_iv)
    ImageView cartIv;
    @BindView(R.id.banner_iv)
    ImageView bannerIv;
    @BindView(R.id.logo_iv)
    ImageView logoIv;
    @BindView(R.id.brand_name_tv)
    TextView brandNameTv;
    @BindView(R.id.second_line)
    View secondLine;
    @BindView(R.id.third_line)
    View thirdLine;
    @BindView(R.id.four_line)
    View fourLine;
    @BindView(R.id.category_tv)
    TextView categoryTv;
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
    @BindView(R.id.head_view_pager)
    HeaderViewPager headViewPager;
    @BindView(R.id.address_tv)
    TextView addressTv;

    private long shopId;

    private VirtualLayoutManager layoutManager;
    private DelegateAdapter delegateAdapter;
    private List<ProductBean> list = new ArrayList<>();
    private MarketProductAdapter marketProductAdapter;

    private View lastClick;
    private int profitAsc = -1; //0是降序 1是升序
    private int supplyPriceAsc = -1; //0是降序 1是升序
    private boolean isCreateDate;
    private boolean isAllot;
    private ShopBean bean;
    private BaseVirtualAdapter<DefaultHolder> endAdapter; //列表结束标志
    private int p=1;
    private boolean hasNext;

    @Override
    public int getLayoutId() {
        return R.layout.activity_brand_detail;
    }

    @Override
    public void doBusiness() {
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        shopId = getIntent().getLongExtra("shopId", 0);
        layoutManager = new VirtualLayoutManager(this);
        delegateAdapter = new DelegateAdapter(layoutManager);
        recycleView.setLayoutManager(layoutManager);
        marketProductAdapter = new MarketProductAdapter(this, list,true);
        delegateAdapter.addAdapter(marketProductAdapter);
        recycleView.setAdapter(delegateAdapter);
        ptr.setEnabled(false);
        firstChunk.setSelected(true);
        lastClick = firstChunk;
        headViewPager.setCurrentScrollableContainer(this);
        changeTextViewColor();
        loadBrandInfo();
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

    private void changeTextViewColor() {
        if (lastClick == firstChunk) {
            synTv.setSelected(lastClick.isSelected());
        } else if (lastClick == secondChunk) {
            profitTv.setSelected(lastClick.isSelected());
        } else if (lastClick == thirdChunk) {
            supplyPriceTv.setSelected(lastClick.isSelected());
        }
    }

    private void loadBrandInfo() {
        addDisposable(ApiRetrofit.getInstance().getApiService().getBrandInfo(shopId), new BaseObserver<BaseModel<ShopBean>>() {
            @Override
            public void onSuccess(BaseModel<ShopBean> shopBean) {
                bean=shopBean.getData();
                ImageLoader.displayImage(BrandDetailActivity.this,bean.getBanner(),bannerIv);
                ImageLoader.displayImage(BrandDetailActivity.this,bean.getLogo(),logoIv);
                titleTv.setText(bean.getName());
                brandNameTv.setText(bean.getName());
                if (!StringUtil.isEmpty(bean.getAddress())) {
                    if (bean.getAddress().startsWith("{")){
                        Gson gson=new Gson();
                        Address address=gson.fromJson(bean.getAddress(),Address.class);
                        addressTv.setText(address.province+address.city+address.district+address.address);
                    }else {
                        addressTv.setText(shopBean.getData().getAddress());
                    }
                }
            }
        });
    }

    private void loadProductList() {
        addDisposable(ApiRetrofit.getInstance().getApiService().getMarketProductList(buildParam()), new BaseObserver<BaseModel<MarketProductListBean>>() {
            @Override
            public void onSuccess(BaseModel<MarketProductListBean> o) {
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
        });
    }

    private ArrayMap<String, String> buildParam() {
        ArrayMap<String, String> map = Util.buildListParam(p, 50);
        map.put("shopId",String.valueOf(shopId));
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
        return map;
    }



    @OnClick({R.id.phone_tv,R.id.wx_tv,R.id.back_iv, R.id.cart_iv, R.id.logo_iv, R.id.category_tv, R.id.first_chunk, R.id.second_chunk, R.id.third_chunk, R.id.four_chunk})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.phone_tv:
                if (bean==null) return;
                Intent call = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + bean.getTelephone());
                call.setData(data);
                startActivity(call);
                break;
            case R.id.wx_tv:
                showScanCode(view);
                break;
            case R.id.back_iv:
                finish();
                break;
            case R.id.cart_iv:
                if (Util.loginChecked(this, 999)) {
                    intent = new Intent(this, CartActivity.class);
                    this.startActivity(intent);
                }
                break;
            case R.id.logo_iv:
                break;
            case R.id.category_tv:
                intent =new Intent(this, SearchCategoryActivity.class);
                this.startActivity(intent);
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

    private void showScanCode(View view){
        if (bean==null)return;
        ScanCodePop codePop=new ScanCodePop(this, bean.getWechat(),true);
        codePop.show(view);
    }

    @Override
    public View getScrollableView() {
        return recycleView;
    }
}
