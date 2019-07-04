package com.d2cmall.shopkeeper.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.ArrayMap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.adapter.SampleImageAdapter;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.base.mvp.BaseView;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.common.JsonPic;
import com.d2cmall.shopkeeper.decoration.SpaceGridItemDecoration;
import com.d2cmall.shopkeeper.model.CategoryBean;
import com.d2cmall.shopkeeper.model.ProductBean;
import com.d2cmall.shopkeeper.ui.view.CategoryPop;
import com.d2cmall.shopkeeper.ui.view.OverScrollView;
import com.d2cmall.shopkeeper.ui.view.ProductSkuView;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/2/20.
 * 邮箱:hrb940258169@163.com
 * 添加商品
 */

public class AddProductActivity extends BaseActivity implements BaseView,SampleImageAdapter.Listener{

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_et)
    EditText titleEt;
    @BindView(R.id.sort_tv)
    TextView sortTv;
    @BindView(R.id.sort_et)
    TextView sortEt;
    @BindView(R.id.select_sort_iv)
    ImageView selectSortIv;
    @BindView(R.id.tv_spec)
    TextView categoryTv;
    @BindView(R.id.category_et)
    TextView categoryEt;
    @BindView(R.id.select_category_iv)
    ImageView selectCategoryIv;
    @BindView(R.id.content_ll)
    LinearLayout contentLl;
    @BindView(R.id.add_sku_tv)
    TextView addSkuTv;
    @BindView(R.id.add_warehouse)
    TextView addWarehouse;
    @BindView(R.id.put_away)
    TextView putAway;
    @BindView(R.id.scrollView)
    OverScrollView scrollView;
    @BindView(R.id.pic_ll)
    RecyclerView picLl;
    @BindView(R.id.sort_rl)
    RelativeLayout sortRl;
    @BindView(R.id.spec_rl)
    RelativeLayout specRl;
    @BindView(R.id.describe_et)
    EditText describeEt;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_desc_num)
    TextView tvDescNum;
    @BindView(R.id.tv_name_num)
    TextView tvNameNum;

    private ArrayList<JsonPic> jsonPics;
    private SampleImageAdapter sampleImageAdapter;
    private JsonPic emptyPic;
    private HashMap<String, String> imgUpyunPaths;
    private InputMethodManager im;
    private int maxSelected = 9;
    private int uploadedNum;
    private long categoryId;
    private long classifyId;
    private ArrayList<ProductSkuView> skuViews = new ArrayList<>();
    private ProductBean productBean;
    private int exitPicNum;
    private int stock;

    @Override
    public void doBusiness() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        productBean= (ProductBean) getIntent().getSerializableExtra("product");
        if (productBean!=null){
            tvName.setText("编辑商品");
        }else {
            tvName.setText("添加商品");
        }
        initView();
    }

    private void initView() {
        ShadowDrawable.setShadowDrawable(rlTitle, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), 0, 0, 0, ScreenUtil.dip2px(16), 0, 0);
        ShadowDrawable.setShadowDrawable(addWarehouse, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), ScreenUtil.dip2px(8), 0, 0);
        ShadowDrawable.setShadowDrawable(putAway, Color.parseColor("#4050D2"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), ScreenUtil.dip2px(8), 0, 0);
        describeEt.setMovementMethod(ScrollingMovementMethod.getInstance());
        jsonPics = new ArrayList<>();
        imgUpyunPaths = new HashMap<>();
        emptyPic = new JsonPic();

        titleEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvNameNum.setText(getString(R.string.label_char_num,String.valueOf(s.length()),"180"));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        describeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvDescNum.setText(getString(R.string.label_char_num,String.valueOf(s.length()),"1000"));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        describeEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    //处理事件
                }
                return false;
            }
        });


        if (productBean!=null){
            initProduct();
        }else {
            jsonPics.add(emptyPic);
            addSkuLayout(true);
        }
        picLl.setLayoutManager(new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false));
        picLl.addItemDecoration(new SpaceGridItemDecoration(ScreenUtil.dip2px(8)));
        sampleImageAdapter = new SampleImageAdapter(this, Glide.with(this),jsonPics,this);
        picLl.setAdapter(sampleImageAdapter);
        mItemTouchHelper.attachToRecyclerView(picLl);
        describeEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                describeEt.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    private void initProduct(){
        if (productBean==null)return;
        ArrayList<JsonPic> selectedPhotos = new ArrayList<JsonPic>();
        String[] pics=productBean.getPic().split(",");
        exitPicNum=pics.length;
        for (int i=0;i<exitPicNum;i++) {
            if (StringUtil.isEmpty(pics[i]))continue;
            JsonPic pic = new JsonPic();
            pic.setMediaPath(pics[i]);
            pic.setUploadPath(pics[i]);
            selectedPhotos.add(pic);
        }
        if (!jsonPics.isEmpty()) {
            jsonPics.remove(emptyPic);
        }
        jsonPics.addAll(selectedPhotos);
        maxSelected = 9 - jsonPics.size();
        if (!jsonPics.isEmpty() && jsonPics.size() < 9) {
            jsonPics.add(emptyPic);
        }
        titleEt.setText(productBean.getName());
        if(productBean.getClassify()!=null){
            sortEt.setText(getCategoryName(productBean.getClassify()));
            setShopId(productBean.getClassify());//后台数据严谨点就可以删掉
        }
        classifyId=productBean.getClassifyId();
        if(productBean.getCategory()!=null){
            categoryEt.setText(getCategoryName(productBean.getCategory()));
        }
        categoryId=productBean.getCategoryId();
        if(productBean.getSkuList()!=null){
            List<ProductBean.SkuListBean> skuListBeans=productBean.getSkuList();
            int size=skuListBeans.size();
            for (int i=0;i<size;i++){
                if (i==0){
                    addSkuLayout(true);
                }else {
                    addSkuLayout(false);
                }
                skuViews.get(i).setId(skuListBeans.get(i).getId());
                skuViews.get(i).setStandard(skuListBeans.get(i).getStandard());
                skuViews.get(i).setPrice(skuListBeans.get(i).getSellPrice());
                skuViews.get(i).setStore(skuListBeans.get(i).getStock());
                //设置进价和利润
                if(!StringUtil.isEmpty(productBean.getSourceId())){
                    skuViews.get(i).setSupplyPrice(skuListBeans.get(i).getSupplyPrice());
                }
                if(!StringUtil.isEmpty(productBean.getSourceId())){
                    skuViews.get(i).setNoEditStore();
                }
            }
        }
        if(!StringUtil.isEmpty(productBean.getDescription())){
            describeEt.setText(productBean.getDescription().replace("</br>","\n"));
        }
    }

    private String getCategoryName(CategoryBean categoryBean){
        StringBuilder result=new StringBuilder();
        result.append(categoryBean.getName());
        if (categoryBean.getChildren()!=null&&categoryBean.getChildren().size()>0){
            result.append(" ").append(categoryBean.getChildren().get(0).getName());
        }
        return result.toString();
    }

    private void setShopId(CategoryBean categoryBean){
        if (categoryBean!=null){
            categoryBean.setShopId(Session.getInstance().getUser().getShopId());
            int size=categoryBean.getChildren().size();
            for (int i=1;i<size;i++){
                setShopId(categoryBean.getChildren().get(i));
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.add_warehouse, R.id.put_away, R.id.add_sku_tv, R.id.sort_rl, R.id.spec_rl})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.add_sku_tv:
                if (skuViews!=null&&skuViews.size()>29)return;
                addSkuLayout(false);
                break;
            case R.id.add_warehouse://上架出售
                addProduct(false);
                break;
            case R.id.put_away://加入仓库
                addProduct(true);
                break;
            case R.id.sort_rl://分类
                sortRl.setEnabled(false);
                showCategoryPop(false);
                break;
            case R.id.spec_rl://品类
                specRl.setEnabled(false);
                showCategoryPop(true);
                break;
        }
    }

    private ArrayMap<String, String> buildParam(int p, int ps) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("p", String.valueOf(p));
        map.put("ps", String.valueOf(ps));
        return map;
    }

    private void showCategoryPop(boolean isCategory) {
        if (getCurrentFocus() != null) {
            im.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
        if (isCategory) {
            addDisposable(ApiRetrofit.getInstance().getApiService().getCategoryList(buildParam(1, 20)), new BaseObserver<BaseModel<List<CategoryBean>>>() {
                @Override
                public void onSuccess(BaseModel<List<CategoryBean>> o) {
                    specRl.setEnabled(true);
                    if (o.getData().size() == 0) {
                        Util.showToast(AddProductActivity.this, "暂无品类数据", false);
                        Intent intent=new Intent(AddProductActivity.this,CategoryManagerActivity.class);
                        startActivity(intent);
                        return;
                    }
                    CategoryPop pop = new CategoryPop(AddProductActivity.this, o.getData(), false);
                    pop.setCallBack(new CategoryPop.CallBack() {
                        @Override
                        public void callback(long id, String... selectName) {
                            categoryId = id;
                            categoryEt.setText(getValue(selectName));
                        }
                    });
                    pop.show(specRl);
                }

                @Override
                public void onError(int errorCode, String msg) {
                    specRl.setEnabled(true);
                    super.onError(errorCode, msg);
                }
            });
        } else {
            ArrayMap<String,String> map=buildParam(1,20);
            map.put("type","0");
            addDisposable(ApiRetrofit.getInstance().getApiService().getClassifyList(map), new BaseObserver<BaseModel<List<CategoryBean>>>() {
                @Override
                public void onSuccess(BaseModel<List<CategoryBean>> o) {
                    sortRl.setEnabled(true);
                    if (o.getData().size() == 0) {
                        Util.showToast(AddProductActivity.this, "暂无分类数据", false);
                        return;
                    }
                    CategoryPop pop = new CategoryPop(AddProductActivity.this, o.getData(), false);
                    pop.setCallBack(new CategoryPop.CallBack() {
                        @Override
                        public void callback(long id, String... selectName) {
                            classifyId = id;
                            sortEt.setText(getValue(selectName));
                        }
                    });
                    pop.show(specRl);
                }

                @Override
                public void onError(int errorCode, String msg) {
                    sortRl.setEnabled(true);
                    super.onError(errorCode, msg);
                }
            });
        }
    }

    private String getValue(String... strings) {
        int size = strings.length;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            if (!StringUtil.isEmpty(strings[i])) {
                if (i != 0) {
                    builder.append(" ");
                }
                builder.append(strings[i]);
            }
        }
        return builder.toString();
    }

    /**
     * 添加商品
     */
    private void addProduct(boolean isPutaway) {
        int size=jsonPics.contains(emptyPic)?jsonPics.size()-1:jsonPics.size();
        if (uploadedNum==0&&exitPicNum==0||size==0){
            Toast.makeText(this, "请上传至少一张图片！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (uploadedNum < (jsonPics.contains(emptyPic) ? jsonPics.size()-exitPicNum - 1 : jsonPics.size()-exitPicNum)) {
            Toast.makeText(this, "图片还在上传中！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isEmpty(titleEt.getText().toString())) {
            Util.showToast(this,"请填写标题！");
            return;
        }
        if (categoryId == 0 || classifyId == 0) {
            Util.showToast(this,"请选择商品分类和品类！");
            return;
        }
        ProductBean bean = productBean;
        if (bean==null){
            bean=new ProductBean();
            bean.setCrowd(0);//拼团
        }
        if (!getSkuList(bean)) return;
        addWarehouse.setEnabled(false);
        putAway.setEnabled(false);
        bean.setShopId(Session.getInstance().getUser().getShopId());
        bean.setName(titleEt.getText().toString());
        String realStr=describeEt.getText().toString().replaceAll("\n","</br>");
        bean.setDescription(realStr);
        bean.setCategoryId(categoryId);
        bean.setClassifyId(classifyId);
        bean.setPrice(bean.getSkuList().get(0).getSellPrice());
        bean.setStock(stock);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                builder.append(",");
            }
            builder.append(jsonPics.get(i).getUploadPath());
        }
        bean.setPic(builder.toString());
        if (isPutaway) {
            bean.setStatus(1);
        } else {
            bean.setStatus(0);
        }
        if (productBean!=null){
            addDisposable(ApiRetrofit.getInstance().getApiService().productUpdate(bean), new BaseObserver(this) {
                @Override
                public void onSuccess(Object o) {
                    Util.showToast(AddProductActivity.this,"商品更新成功！");
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }else {
            addDisposable(ApiRetrofit.getInstance().getApiService().productInsert(bean), new BaseObserver(this) {
                @Override
                public void onSuccess(Object o) {
                    Util.showToast(AddProductActivity.this,"添加商品成功！");
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }
    }

    private boolean getSkuList(ProductBean productBean) {
        stock=0;
        ArrayList<ProductBean.SkuListBean> list = new ArrayList<>();
        int size = skuViews.size();
        for (int i = 0; i < size; i++) {
            if (skuViews.get(i).isDeleted){
                continue;
            }
            ProductBean.SkuListBean sku = skuViews.get(i).getSku();
            if (sku == null) {
                return false;
            }
            stock+=sku.getStock();
            list.add(sku);
        }
        productBean.setSkuList(list);
        return true;
    }

    private void addSkuLayout(boolean isFirst) {
        ProductSkuView skuView = new ProductSkuView(this);
        if (!isFirst) {
            skuView.setUnDelete(true);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
            lp.setMargins(0, ScreenUtil.dip2px(16), 0, 0);
            contentLl.addView(skuView, lp);
        } else {
            contentLl.addView(skuView);
        }
        skuViews.add(skuView);
    }

    @Override
    public void onBackPressed() {
        if (titleEt.getText().toString().length() > 0 || jsonPics.size() > 1) {
            if (getCurrentFocus() != null) {
                im.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
            new AlertDialog.Builder(this)
                    .setMessage("确定退出吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            overridePendingTransition(0, R.anim.slide_out_right);
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        } else {
            finish();
            overridePendingTransition(0, R.anim.slide_out_right);
        }
    }

    public void showPop() {
        PackageManager pkgManager = getPackageManager();
        boolean cameraPermission =
                pkgManager.checkPermission(Manifest.permission.CAMERA, getPackageName()) == PackageManager.PERMISSION_GRANTED;
        boolean storagePermission =
                pkgManager.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getPackageName()) == PackageManager.PERMISSION_GRANTED;
        if (Build.VERSION.SDK_INT >= 23 && (!cameraPermission || !storagePermission)) {
            requestPermission();
        } else {
            choosePic();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                Constants.RequestCode.REQUEST_PERMISSION);
    }

    @Override
    public void requestStart() {

    }

    @Override
    public void requestFinish() {
        putAway.setEnabled(true);
        addWarehouse.setEnabled(true);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void emptyClick(View v) {
        showPop();
    }

    @Override
    public void itemDelete(JsonPic jsonPic) {
        if (jsonPics.contains(jsonPic)) {
            jsonPics.remove(jsonPic);
            if (imgUpyunPaths.containsKey(jsonPic.getMediaPath())) {
                uploadedNum--;
            }
        }
        maxSelected = 10 - jsonPics.size();
        if (!jsonPics.contains(emptyPic)) {
            maxSelected--;
            jsonPics.add(emptyPic);
        }
        if (sampleImageAdapter!=null){
            sampleImageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean hasUpload(String url) {
        if (imgUpyunPaths.containsKey(url)){
            uploadedNum++;
            return true;
        }else {
            return false;
        }
    }

    private void choosePic() {
        Matisse.from(AddProductActivity.this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG, MimeType.AVI, MimeType.MP4, MimeType.MKV))
                .theme(R.style.Matisse_Dracula)
                .countable(true)
                .capture(true)
                .captureStrategy(
                        new CaptureStrategy(true, "com.d2cmall.shopkeeper.fileprovider"))
                .maxSelectablePerMediaType(maxSelected, 1)
                .gridExpectedSize(ScreenUtil.dip2px(120))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(456);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Constants.RequestCode.REQUEST_PERMISSION) {
            if ((grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED&& grantResults[2] == PackageManager.PERMISSION_GRANTED)) {
                choosePic();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case 456:
                    ArrayList<JsonPic> selectedPhotos = new ArrayList<JsonPic>();
                    for (String path : Matisse.obtainPathResult(data)) {
                        JsonPic pic = new JsonPic();
                        pic.setMediaPath(path);
                        selectedPhotos.add(pic);
                    }
                    if (!jsonPics.isEmpty()) {
                        jsonPics.remove(emptyPic);
                    }
                    jsonPics.addAll(selectedPhotos);
                    maxSelected = 9 - jsonPics.size();
                    if (!jsonPics.isEmpty() && jsonPics.size() < 9) {
                        jsonPics.add(emptyPic);
                    }
                    if (sampleImageAdapter!=null){
                        sampleImageAdapter.notifyDataSetChanged();
                    }
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(JsonPic pic) {
        if (!imgUpyunPaths.containsKey(pic.getMediaPath())) {
            imgUpyunPaths.put(pic.getMediaPath(), pic.getUploadPath());
            uploadedNum++;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_product;
    }

    @Override
    protected void onDestroy() {
        if (sampleImageAdapter!=null){
            sampleImageAdapter.setListener(null);
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

            // 获取触摸响应的方向   包含两个 1.拖动dragFlags 2.侧滑删除swipeFlags
            // 代表只能是向左侧滑删除，当前可以是这样ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT
            int swipeFlags = ItemTouchHelper.ACTION_STATE_IDLE;
            int dragFlags;
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            } else {
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            }
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        /**
         * 拖动的时候不断的回调方法
         */
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //获取到原来的位置
            int fromPosition = viewHolder.getAdapterPosition();
            //获取到拖到的位置
            int targetPosition = target.getAdapterPosition();
            if (jsonPics.contains(emptyPic)&&(fromPosition==jsonPics.size()-1||targetPosition==jsonPics.size()-1)){
                return true;
            }
            if (fromPosition < targetPosition) {
                for (int i = fromPosition; i < targetPosition; i++) {
                    Collections.swap(jsonPics, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > targetPosition; i--) {
                    Collections.swap(jsonPics, i, i - 1);
                }
            }
            sampleImageAdapter.notifyItemMoved(fromPosition, targetPosition);
            return true;
        }

        /**
         * 侧滑删除后会回调的方法
         */
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }
    });
}
