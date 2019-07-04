package com.d2cmall.shopkeeper.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.adapter.MarketProductDetailAdapter;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Action;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.holder.MarketProductTopInfoHolder;
import com.d2cmall.shopkeeper.model.ProductBean;
import com.d2cmall.shopkeeper.model.ShopBean;
import com.d2cmall.shopkeeper.model.UserBean;
import com.d2cmall.shopkeeper.ui.view.InfoPop;
import com.d2cmall.shopkeeper.ui.view.ProductSharePop;
import com.d2cmall.shopkeeper.ui.view.PtrStoreHouseFrameLayout;
import com.d2cmall.shopkeeper.ui.view.SelectStandardPop;
import com.d2cmall.shopkeeper.ui.view.SharePop;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.ui.view.nicevideo.NiceVideoPlayer;
import com.d2cmall.shopkeeper.ui.view.nicevideo.NiceVideoPlayerManager;
import com.d2cmall.shopkeeper.utils.FileUtil;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by LWJ on 2019/5/6.
 * Description : MarketProductDetialActivity
 * 选货市场商品详情界面
 */

public class MarketProductDetialActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.ptr)
    PtrStoreHouseFrameLayout ptr;
    @BindView(R.id.img_hint)
    ImageView imgHint;
    @BindView(R.id.btn_reload)
    TextView btnReload;
    @BindView(R.id.empty_hint_layout)
    LinearLayout emptyHintLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.iv_cart)
    ImageView ivCart;
    @BindView(R.id.tv_add_cart)
    TextView tvAddCart;
    @BindView(R.id.btn_buy_free)
    TextView btnBuyFree;
    @BindView(R.id.id_division)
    View idDivision;
    @BindView(R.id.btn_buy_now)
    TextView btnBuyNow;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    private long productId;
    private VirtualLayoutManager virtualLayoutManager;
    private MarketProductDetailAdapter marketProductDetailAdapter;
    private DelegateAdapter delegateAdapter;
    private ProductBean mProductBean;
    private SelectStandardPop selectStandardPop;
    private UserBean userBean;
    private ShopBean mShopBean;

    private boolean downing;
    private Handler mHandle;
    private int count;
    private int index;

    @Override
    public int getLayoutId() {
        return R.layout.activity_market_product_detial;
    }

    @Override
    public void doBusiness() {
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        userBean = Session.getInstance().getUserFromFile(this);
        initView();
    }

    private void initView() {
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        productId = getIntent().getLongExtra("id", 0);
        if(productId==0){
            return;
        }
        virtualLayoutManager = new VirtualLayoutManager(this);
        LinearLayoutHelper layoutHelper = new LinearLayoutHelper();
        layoutHelper.setMarginTop(ScreenUtil.dip2px(16));
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        marketProductDetailAdapter = new MarketProductDetailAdapter(this);
        delegateAdapter.addAdapter(marketProductDetailAdapter);
        recycleView.setLayoutManager(virtualLayoutManager);
        recycleView.setAdapter(delegateAdapter);
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
        recycleView.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                if (holder instanceof MarketProductTopInfoHolder) {
                    MarketProductTopInfoHolder showItemHolder = (MarketProductTopInfoHolder) holder;
                    NiceVideoPlayer niceVideoPlayer = showItemHolder.niceVideoPlayer;
                    if (niceVideoPlayer.getVisibility() == View.VISIBLE && niceVideoPlayer == NiceVideoPlayerManager.instance().getCurrentNiceVideoPlayer()) {
                        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
                    }
                }
            }
        });
        loadProductInfo();
    }

    private void refresh() {
       loadProductInfo();
    }

    private void loadProductInfo() {
        addDisposable(ApiRetrofit.getInstance().getApiService().getProduct(productId), new BaseObserver<BaseModel<ProductBean>>() {
            @Override
            public void onSuccess(BaseModel<ProductBean> productBean) {
                ptr.refreshComplete();
                loadBrandInfo(productBean.getData().getShopId());
                mProductBean = productBean.getData();
                //nameTv.setText(mProductBean.getName());
                if(mProductBean.getAllot()==1){
                    btnBuyFree.setVisibility(View.VISIBLE);
                }
                if(mProductBean.getBuyout()==1){
                    btnBuyNow.setVisibility(View.VISIBLE);
                }
                marketProductDetailAdapter.setProductInfo(productBean.getData());
                marketProductDetailAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(int errorCode, String msg) {
                ptr.refreshComplete();
                super.onError(errorCode, msg);
            }
        });
    }

    //拉自己的店铺呢信息和供应商(shopId)的店铺信息
    private void loadBrandInfo(long shopId) {
        long brandId=0;
        if(shopId==0){
            if(userBean!=null){
                brandId=userBean.getShopId();
            }
        }else{
            brandId=shopId;
        }
        if (brandId==0){
            return;
        }
        addDisposable(ApiRetrofit.getInstance().getApiService().getBrandInfo(brandId), new BaseObserver<BaseModel<ShopBean>>() {
            @Override
            public void onSuccess(BaseModel<ShopBean> shopBean) {
                if (shopId==0){
                    if (shopBean.getData().getDeposit()< mProductBean.getSupplyPrice()){
                        showInfoPop();
                    }else {
                        showStandardPop(btnBuyFree,1);
                    }
                }else {
                    mShopBean = shopBean.getData();
                    if(shopId>0){
                        marketProductDetailAdapter.setShopInfo(mShopBean);
                    }
                }
            }
        });
    }

    private void showInfoPop(){
        InfoPop infoPop=new InfoPop(this);
        infoPop.setTitle("可用保证金不够哦");
        infoPop.setInfo("缴纳一定金额的保证金\n可免费从选货市场铺货到店");
        infoPop.setSure("查看更多保证金用处");
        infoPop.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoPop.dismiss();
                Intent intent = new Intent(MarketProductDetialActivity.this, WebActivity.class);
                intent.putExtra("url","https://s.fune.store/doc/deposit.html");
                startActivity(intent);
            }
        });
        infoPop.show(btnBuyFree);
    }

    @OnClick({R.id.iv_share,R.id.iv_back, R.id.iv_cart, R.id.btn_reload, R.id.tv_add_cart, R.id.btn_buy_free, R.id.btn_buy_now})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_share:
                ProductSharePop pop=new ProductSharePop(this);
                pop.setListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView textView= (TextView) v;
                        if (textView.getText().toString().equals("分享")){
                            share(view,mProductBean);
                        }else {
                            String[] urls=mProductBean.getPic().split(",");
                            List<String> list=new ArrayList<>();
                            int length=urls.length;
                            for (int j=0;j<length;j++){
                                list.add(StringUtil.getD2cPicUrl(urls[j]));
                            }
                            down(list);
                        }
                    }
                });
                pop.show(view);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_cart:
                //购物车
                if(Util.loginChecked(this,999)){
                    startActivity(new Intent(this,CartActivity.class));
                }
                break;
            case R.id.btn_reload:
                refresh();
                break;
            case R.id.tv_add_cart:
                //加入清单
                showStandardPop(view,0);
                break;
            case R.id.btn_buy_free:
                //免费拿货
                loadBrandInfo(0);
                break;
            case R.id.btn_buy_now:
                //立即采购
                showStandardPop(view,2);
                break;
        }
    }

    private void share(View view,ProductBean bean){
        if (Util.wxInstalled()) {
            SharePop pop = new SharePop(this);
            pop.setTitle(bean.getName());
            pop.setDescription("发现了一个好东西，赶紧看看吧！");
            pop.setImage(StringUtil.getD2cPicUrl(bean.getFirstPic(), 100, 100), false);
            pop.setWebUrl(Constants.SHARE_URL + "product/" + bean.getId());
            pop.show(view);
        } else {
            Util.showToast(this, "请先安装微信");
        }
    }

    private void down(List<String> list){
        createHandle();
        if (downing) {
            Util.showToast(this, "正在下载中");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                count=list.size();
                index=0;
                for (int i=0;i<count;i++){
                    downFile(StringUtil.getD2cPicUrl(list.get(i)));
                }
            }
        }).start();
    }

    private void downFile(String bigImageUrl) {
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);
        if (!sdCardExist) {
            mHandle.sendEmptyMessage(1);
            return;
        }
        int index = bigImageUrl.lastIndexOf("/");
        if (index > 0) {
            String name = bigImageUrl.substring(index + 1);
            if (name.indexOf(".") <= 0) {
                name = name + ".png";
            }
            URL url = null;
            try {
                url = new URL(bigImageUrl);
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConn.getInputStream();
                downing = true;
                int state = FileUtil.save2File(this, Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/ShopKeeper", name, inputStream);
                if (state == -1) {
                    mHandle.sendEmptyMessage(2);
                } else if (state == 1) {
                    mHandle.sendEmptyMessage(3);
                } else {
                    mHandle.sendEmptyMessage(4);
                }
                this.index++;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mHandle.sendEmptyMessage(5);
        }
    }

    private void createHandle() {
        mHandle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int what = msg.what;
                if (index<count)return;
                switch (what) {
                    case 1:
                        Util.showToast(MarketProductDetialActivity.this, "没有内存卡");
                        break;
                    case 2:
                        Util.showToast(MarketProductDetialActivity.this, "保存失败");
                        downing = false;
                        break;
                    case 3:
                        Util.showToast(MarketProductDetialActivity.this, "已经保存");
                        downing = false;
                        break;
                    case 4:
                        Util.showToast(MarketProductDetialActivity.this, "保存成功,请到相册shopkeeper目录查看");
                        downing = false;
                        break;
                    case 5:
                        Util.showToast(MarketProductDetialActivity.this, "读取失败");
                        break;
                    case 6:
                        Util.showToast(MarketProductDetialActivity.this, "正在下载中");
                        break;
                }
            }
        };
    }

    /**
     *
     * @param type 0 加入清单 1 免费拿货 2 立即采购
     */
    private void showStandardPop(View view,int type){
        if (selectStandardPop==null){
            selectStandardPop=new SelectStandardPop(this);
            selectStandardPop.setData(mProductBean);
        }
        selectStandardPop.setType(type);
        selectStandardPop.show(view);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Action action){
        if (action.type== Constants.ActionType.CART_OFFLINE_ADD){
            int type= (int) action.get("type");
            long skuId= (long) action.get("skuId");
            int num= (int) action.get("num");
            switch (type){
                case 0://加入清单
                    addCart();
                    break;
                case 1://免费拿货下单
                    toAllotSettle(skuId,num);
                    break;
                case 2://立即采购下单
                    toPurchaseSettle(skuId,num);
                    break;
            }
        }
    }

    private void addCart(){
        long skuId=selectStandardPop.getSkuId();
        int num=selectStandardPop.getCurrentNum();
        addDisposable(ApiRetrofit.getInstance().getApiService().cartInsert(skuId,num), new BaseObserver() {
            @Override
            public void onSuccess(Object o) {
                Util.showToast(MarketProductDetialActivity.this,"加入清单成功");
            }
        });
    }

    private void toAllotSettle(long skuId,int num){
        Intent intent=new Intent(this,OrderAllotSettleActivity.class);
        intent.putExtra("skuId",skuId);
        intent.putExtra("num",num);
        startActivity(intent);
    }

    private void toPurchaseSettle(long skuId,int num){
        Intent intent=new Intent(this,OrderPurchaseSettleActivity.class);
        intent.putExtra("skuId",skuId);
        intent.putExtra("num",num);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
