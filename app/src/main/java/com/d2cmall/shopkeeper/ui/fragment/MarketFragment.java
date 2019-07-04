package com.d2cmall.shopkeeper.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.adapter.MarketProductAdapter;
import com.d2cmall.shopkeeper.adapter.MarketProductAdapter1;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseTabFragment;
import com.d2cmall.shopkeeper.base.list.BaseVirtualAdapter;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.binder.ScrollEndBinder;
import com.d2cmall.shopkeeper.common.Action;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.common.SharePrefConstant;
import com.d2cmall.shopkeeper.holder.DefaultHolder;
import com.d2cmall.shopkeeper.model.BannerBean;
import com.d2cmall.shopkeeper.model.CategoryBean;
import com.d2cmall.shopkeeper.model.MarketProductListBean;
import com.d2cmall.shopkeeper.model.ProductBean;
import com.d2cmall.shopkeeper.model.ShopBean;
import com.d2cmall.shopkeeper.model.UserBean;
import com.d2cmall.shopkeeper.ui.activity.CartActivity;
import com.d2cmall.shopkeeper.ui.activity.FundManagerActivity;
import com.d2cmall.shopkeeper.ui.activity.SearchActivity;
import com.d2cmall.shopkeeper.ui.activity.SearchCategoryActivity;
import com.d2cmall.shopkeeper.ui.activity.SearchResultActivity;
import com.d2cmall.shopkeeper.ui.activity.WebActivity;
import com.d2cmall.shopkeeper.ui.view.AutoScrollView;
import com.d2cmall.shopkeeper.ui.view.Banner;
import com.d2cmall.shopkeeper.ui.view.PtrStoreHouseFrameLayout;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.StatusView;
import com.d2cmall.shopkeeper.ui.view.headerViewPager.HeaderScrollHelper;
import com.d2cmall.shopkeeper.ui.view.headerViewPager.HeaderViewPager;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.SharedPreUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;
import com.example.bannerlibrary.DefineBaseBanner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 作者:Created by sinbara on 2019/6/18.
 * 邮箱:hrb940258169@163.com
 */
public class MarketFragment extends BaseTabFragment implements HeaderScrollHelper.ScrollableContainer {
    @BindView(R.id.status_view)
    StatusView statusView;
    @BindView(R.id.cart_iv)
    ImageView cartIv;
    @BindView(R.id.category_iv)
    ImageView categoryIv;
    @BindView(R.id.banner_iv)
    Banner bannerIv;
    @BindView(R.id.group)
    Group group;
    @BindView(R.id.second_content_ll)
    LinearLayout secondContentLl;
    @BindView(R.id.auto_scroll_view)
    AutoScrollView autoScrollView;
    @BindView(R.id.syn_tv)
    TextView synTv;
    @BindView(R.id.profit_tv)
    TextView profitTv;
    @BindView(R.id.supply_price_tv)
    TextView supplyPriceTv;
    @BindView(R.id.filter_group)
    Group filterGroup;
    @BindView(R.id.head_view_pager)
    HeaderViewPager headViewPager;
    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;
    @BindView(R.id.select_ll)
    View selectLl;
    @BindView(R.id.select_tv)
    TextView selectTv;
    @BindView(R.id.ptr)
    PtrStoreHouseFrameLayout ptr;
    @BindView(R.id.first_chunk)
    View firstChunk;
    @BindView(R.id.second_chunk)
    View secondChunk;
    @BindView(R.id.third_chunk)
    View thirdChunk;
    @BindView(R.id.show_type_tv)
    TextView showTypeTv;

    private VirtualLayoutManager layoutManager;
    private DelegateAdapter delegateAdapter;

    private boolean isFirst = true;
    //private BadgeView badgeView;
    private List<ProductBean> list = new ArrayList<>();
    private MarketProductAdapter marketProductAdapter;
    private MarketProductAdapter1 marketProductAdapter1;
    private long currentClassId;//当前二级id
    private List<CategoryBean> categoryBeans = new ArrayList<>();
    private int currentIndex = -1;//当前一级下标
    private TextView currentText;//当前textview
    private View lastClick;
    private int profitAsc = -1; //0是降序 1是升序
    private int supplyPriceAsc = -1; //0是降序 1是升序
    private boolean isCreateDate;
    private boolean isAllot;
    private int cartCount;
    private boolean isRefresh;
    private boolean isBrand;//是否是品牌店
    private List<String> titles=new ArrayList<>();
    private List<Long> ids=new ArrayList<>();
    private boolean isLinear;
    private BaseVirtualAdapter<DefaultHolder> endAdapter; //列表结束标志
    private int p=1;
    private boolean hasNext;
    private boolean hasEmpty;

    @Override
    public View getRootView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_market1, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        boolean hasStatus = true;
        if (getArguments() != null) {
            hasStatus = getArguments().getBoolean("hasStatus");
        }
        if (!hasStatus) {
            statusView.setVisibility(View.GONE);
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            statusView.setBackgroundColor(getResources().getColor(R.color.color_black3));
        } else {
            Sofia.with(getActivity()).statusBarDarkFont();
        }
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void doBusiness() {
        initView();
        ptr.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return headViewPager.canPtr();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refresh();
            }
        });
        layoutManager = new VirtualLayoutManager(getActivity());
        delegateAdapter = new DelegateAdapter(layoutManager);
        recyclerView.setLayoutManager(layoutManager);
        marketProductAdapter = new MarketProductAdapter(getActivity(), list);
        marketProductAdapter1=new MarketProductAdapter1(getActivity(),list);
        boolean is= SharedPreUtil.getBoolean(SharePrefConstant.IS_LINEAR,false);
        if (is){
            showTypeTv.setText("图文模式");
            showTypeTv.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.yuncang_home_icon_tuwen,0);
            delegateAdapter.addAdapter(marketProductAdapter1);
            isLinear=true;
        }else {
            showTypeTv.setText("货架模式");
            showTypeTv.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.yunchang_home_icon_huojia,0);
            delegateAdapter.addAdapter(marketProductAdapter);
            isLinear=false;
        }
        recyclerView.setAdapter(delegateAdapter);
        if (isBrand){
            loadCategory();
        }else {
            loadProductList();
        }
        firstChunk.setSelected(true);
        lastClick = firstChunk;
        changeTextViewColor();
        getBanner();
        iniListener();
    }

    private void iniListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private void initView() {
        if (Session.getInstance().getUser()!=null){
            isBrand="brand".equals(Session.getInstance().getUser().getDef());
        }
        if (isBrand) {
            selectLl.setVisibility(View.GONE);
            selectTv.setVisibility(View.GONE);
            categoryIv.setVisibility(View.GONE);
            group.setVisibility(View.GONE);
            filterGroup.setVisibility(View.VISIBLE);
        } else {
            selectLl.setVisibility(View.VISIBLE);
            selectTv.setVisibility(View.VISIBLE);
            categoryIv.setVisibility(View.VISIBLE);
            group.setVisibility(View.VISIBLE);
            filterGroup.setVisibility(View.GONE);
        }
        headViewPager.setCurrentScrollableContainer(this::getScrollableView);
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

    private void refresh() {
        isRefresh = true;
        if (isBrand){
            loadCategory();
        }else {
            loadProductList();
        }
    }

    private void loadCategory() {
        ArrayMap<String,String> map=Util.buildListParam(1,20);
        map.put("type","1");
        addDisposable(ApiRetrofit.getInstance().getApiService().getClassifyList(map), new BaseObserver<BaseModel<List<CategoryBean>>>() {
            @Override
            public void onSuccess(BaseModel<List<CategoryBean>> o) {
                if (isRefresh){
                    ptr.refreshComplete();
                }
                int size = o.getData().size();
                titles.clear();
                ids.clear();
                for (int i = 0; i < size; i++) {
                    int num = o.getData().get(i).getChildren().size();
                    if (num==0){
                        titles.add(o.getData().get(i).getName());
                        ids.add(o.getData().get(i).getId());
                    }else {
                        for (int j = 0; j < num; j++) {
                            titles.add(o.getData().get(i).getChildren().get(j).getName());
                            ids.add(o.getData().get(i).getChildren().get(j).getId());
                        }
                    }
                }
                addSecondTab();
                isRefresh=false;
            }

            @Override
            public void onError(int errorCode, String msg) {
                super.onError(errorCode, msg);
                if (isRefresh){
                    ptr.refreshComplete();
                    isRefresh=false;
                }
            }
        });
    }

    private void addSecondTab() {
        int size = titles.size();
        if (size>0){
            autoScrollView.setVisibility(View.VISIBLE);
            secondContentLl.removeAllViews();
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -2);
            lp.setMargins(0, 0, ScreenUtil.dip2px(24), 0);
            for (int i = 0; i < size; i++) {
                TextView textView = new TextView(mContext);
                if (i == 0) {
                    textView.setTextColor(getResources().getColor(R.color.flash_time_bg_color));
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                    currentClassId = ids.get(i);
                    currentText = textView;
                } else {
                    textView.setTextColor(getResources().getColor(R.color.color_black4));
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
                }
                textView.setOnClickListener(new MyClickListener(2));
                textView.setTag(ids.get(i));
                textView.setText(titles.get(i));
                secondContentLl.addView(textView, lp);
            }
            loadProductList();
        }else {
            list.clear();
            if (!isLinear){
                marketProductAdapter.notifyDataSetChanged();
            }else {
                marketProductAdapter1.notifyDataSetChanged();
            }
            autoScrollView.setVisibility(View.GONE);
        }
    }

    public class MyClickListener implements View.OnClickListener {

        int type;//1一级 2二级

        public MyClickListener(int type) {
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            if (type == 1) {
            } else if (type == 2) {
                if (v instanceof TextView) {
                    if (currentText != null) {
                        currentText.setTextColor(getResources().getColor(R.color.color_black4));
                        currentText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
                    }
                    TextView textView = (TextView) v;
                    textView.setTextColor(getResources().getColor(R.color.flash_time_bg_color));
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                    if ((long) v.getTag() != currentClassId) {
                        currentClassId = (long) v.getTag();
                        loadProductList();
                    }
                    currentText = textView;
                }
            }
        }
    }

    private void getBanner() {
        addDisposable(ApiRetrofit.getInstance().getApiService().getBanner(), new BaseObserver<BaseModel<BannerBean>>() {
            @Override
            public void onSuccess(BaseModel<BannerBean> o) {
                if (o.getData() != null) {
                    try {
                        String value = o.getData().getValue();
                        JSONArray jsonObject = new JSONArray(value);
                        List<String> pics = new ArrayList<>();
                        List<String> urls = new ArrayList<>();
                        for (int i = 0; i < jsonObject.length(); i++) {
                            JSONObject object = jsonObject.getJSONObject(i);
                            String pic = object.getString("pic");
                            String url = object.getString("url");
                            if (pic.startsWith(Constants.IMG_HOST) || pic.startsWith(Constants.IMG_HOST1)) {
                                if (pic.contains("!/")) {
                                    if (!pic.contains(Constants.WEBP)) {
                                        pic = pic + Constants.WEBP;
                                        //Log.e("han","type=4");
                                    }
                                } else {
                                    pic = pic + String.format(Constants.PHOTO_URL2, ScreenUtil.getDisplayWidth());
                                }
                            }
                            pics.add(pic);
                            urls.add(url);
                        }
                        if (jsonObject.length() == 1 || jsonObject.length() == 2) {
                            bannerIv.setLoop(false);
                        }
                        bannerIv.setOnItemClickL(new DefineBaseBanner.OnItemClickL() {
                            @Override
                            public void onItemClick(int position) {

                            }
                        });
                        bannerIv.setItemClick(new Banner.ItemClick() {
                            @Override
                            public void onItemClick(int position) {
                                if (!StringUtil.isEmpty(urls.get(position))) {
                                    Intent intent = new Intent(mContext, WebActivity.class);
                                    intent.putExtra("url", urls.get(position % urls.size()));
                                    mContext.startActivity(intent);
                                }
                            }
                        });
                        bannerIv.setSource(pics).startScroll();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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
                }
                if (isLinear){
                    marketProductAdapter1.notifyDataSetChanged();
                }else {
                    marketProductAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(int errorCode, String msg) {
                super.onError(errorCode, msg);
                if (isRefresh){
                    ptr.refreshComplete();
                    isRefresh=false;
                }
            }
        });
    }

    private ArrayMap<String, String> buildParam() {
        ArrayMap<String, String> map = Util.buildListParam(p, 50);
        if (currentClassId != 0) {
            map.put("classifyIds", String.valueOf(currentClassId));
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
        return map;
    }

    @Override
    public View getScrollableView() {
        return recyclerView;
    }

    @OnClick({R.id.show_type_tv, R.id.select_ll, R.id.cart_iv, R.id.category_iv, R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4, R.id.first_chunk, R.id.second_chunk, R.id.third_chunk})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.show_type_tv://展示模式 图文或货架
                if (!isLinear){
                    delegateAdapter.clear();
                    delegateAdapter.addAdapter(marketProductAdapter1);
                    marketProductAdapter1.notifyDataSetChanged();
                    showTypeTv.setText("图文模式");
                    showTypeTv.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.yuncang_home_icon_tuwen,0);
                    SharedPreUtil.setBoolean(SharePrefConstant.IS_LINEAR,true);
                }else {
                    delegateAdapter.clear();
                    delegateAdapter.addAdapter(marketProductAdapter);
                    marketProductAdapter.notifyDataSetChanged();
                    showTypeTv.setText("货架模式");
                    showTypeTv.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.yunchang_home_icon_huojia,0);
                    SharedPreUtil.setBoolean(SharePrefConstant.IS_LINEAR,false);
                }
                isLinear=!isLinear;
                break;
            case R.id.select_ll://搜索
                intent = new Intent(mContext, SearchActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.cart_iv://清单
                if (Util.loginChecked((Activity) mContext, 999)) {
                    intent = new Intent(getActivity(), CartActivity.class);
                    getActivity().startActivity(intent);
                }
                break;
            case R.id.category_iv://分类
                intent =new Intent(mContext, SearchCategoryActivity.class);
                getActivity().startActivityForResult(intent,100);
                break;
            case R.id.tv1://搜索 高利
                intent=new Intent(mContext, SearchResultActivity.class);
                intent.putExtra("profit",0);
                getActivity().startActivity(intent);
                break;
            case R.id.tv2: //免费拿货
                intent=new Intent(mContext, SearchResultActivity.class);
                intent.putExtra("allot",true);
                getActivity().startActivity(intent);
                break;
            case R.id.tv3: //新品推荐
                intent=new Intent(mContext, SearchResultActivity.class);
                intent.putExtra("date",true);
                getActivity().startActivity(intent);
                break;
            case R.id.tv4: //全部商品
                intent=new Intent(mContext, SearchResultActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.first_chunk://综合
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
            case R.id.second_chunk://利
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
            case R.id.third_chunk://进价
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
        }
    }

    @Override
    public void onResume() {
        getCartCount();
        super.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Action action){
        if (action.type==Constants.ActionType.LOGIN_OK){
            loadShop();
        }
    }

    private void loadShop(){
        if (Session.getInstance().getUserFromFile(mContext)==null){
            return;
        }
        UserBean userBean=Session.getInstance().getUserFromFile(mContext);
        addDisposable(ApiRetrofit.getInstance().getApiService().getBrandInfo(Session.getInstance().getUser().getShopId()), new BaseObserver<BaseModel<ShopBean>>() {
            @Override
            public void onSuccess(BaseModel<ShopBean> o) {
                if (o.getData()!=null){
                    if ("BRAND".equals(o.getData().getType())){
                        userBean.setDef("brand");
                        Session.getInstance().setUser(userBean);
                        isBrand=true;
                    }else {
                        isBrand=false;
                    }
                }
                if (isBrand) {
                    selectLl.setVisibility(View.GONE);
                    selectTv.setVisibility(View.GONE);
                    categoryIv.setVisibility(View.GONE);
                    group.setVisibility(View.GONE);
                    filterGroup.setVisibility(View.VISIBLE);
                } else {
                    currentClassId=0;
                    selectLl.setVisibility(View.VISIBLE);
                    selectTv.setVisibility(View.VISIBLE);
                    categoryIv.setVisibility(View.VISIBLE);
                    group.setVisibility(View.VISIBLE);
                    filterGroup.setVisibility(View.GONE);
                }
                getBanner();
                if (isBrand){
                    loadCategory();
                }else {
                    loadProductList();
                }
            }
        });
    }

    private void getCartCount() {
        /*addDisposable(ApiRetrofit.getInstance().getApiService().getCartCount(), new BaseObserver<BaseModel<String>>() {
            @Override
            public void onSuccess(BaseModel<String> o) {
                if (!StringUtil.isEmpty(o.getData())) {
                    try {
                        if (Long.valueOf(o.getData()) < 10) {
                            if (badgeView!=null){
                                badgeView.unbind();
                                badgeView=null;
                            }
                            badgeView = BadgeFactory.createCircle(mContext).setBadgeCount(o.getData()).bind(cartTv);
                        } else {
                            if (badgeView!=null){
                                badgeView.unbind();
                                badgeView=null;
                            }
                            badgeView = BadgeFactory.createOval(mContext).setBadgeCount(o.getData()).bind(cartTv);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });*/
    }

    @Override
    public void show() {
        super.show();
        if (isFirst) {
            isFirst = false;
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            statusView.setBackgroundColor(getResources().getColor(R.color.color_black3));
        } else {
            if (!isFirst) {
                Sofia.with(getActivity()).statusBarDarkFont();
            } else {
                isFirst = false;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==Activity.RESULT_OK&&requestCode==100){
            currentClassId=data.getLongExtra("id",0);
            loadProductList();
        }
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
