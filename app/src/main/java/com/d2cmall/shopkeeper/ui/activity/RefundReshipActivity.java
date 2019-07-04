package com.d2cmall.shopkeeper.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.adapter.ObjectBindAdapter;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.common.JsonPic;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.model.UserBean;
import com.d2cmall.shopkeeper.ui.view.SingleSelectPop;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;
import com.upyun.library.common.Params;
import com.upyun.library.common.UploadManager;
import com.upyun.library.listener.SignatureListener;
import com.upyun.library.listener.UpCompleteListener;
import com.upyun.library.utils.UpYunUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LWJ on 2019/4/29.
 * Description : RefundReshipActivity
 * B端向供货商申请退货退款
 */

public class RefundReshipActivity extends BaseActivity implements SingleSelectPop.CallBack, AdapterView.OnItemClickListener, ObjectBindAdapter.ViewBinder<JsonPic> {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.img_product)
    ImageView imgProduct;
    @BindView(R.id.tv_product_title)
    TextView tvProductTitle;
    @BindView(R.id.tv_spec)
    TextView tvSpec;
    @BindView(R.id.tv_product_num)
    TextView tvProductNum;
    @BindView(R.id.tv_product_status)
    TextView tvProductStatus;
    @BindView(R.id.tv_reason_title)
    TextView tvReasonTitle;
    @BindView(R.id.tv_reason)
    TextView tvReason;
    @BindView(R.id.tv_status)
    ImageView tvStatus;
    @BindView(R.id.ll_reason)
    LinearLayout llReason;
    @BindView(R.id.tv_return_title)
    TextView tvReturnTitle;
    @BindView(R.id.et_apply_num)
    EditText etApplyNum;
    @BindView(R.id.ll_return_num)
    LinearLayout llReturnNum;
    @BindView(R.id.line_layout)
    View lineLayout;
    @BindView(R.id.tv_amount_title)
    TextView tvAmountTitle;
    @BindView(R.id.et_apply_amount)
    EditText etApplyAmount;
    @BindView(R.id.tv_remark_title)
    TextView tvRemarkTitle;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.gridView)
    GridView gridView;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.btn_sure)
    Button btnSure;
    private String reason = "";//原因
    private ArrayList<String> imgUpyunPaths;
    private int maxSelected = 3;

    private ArrayList<JsonPic> photos;
    private ArrayList<JsonPic> jsonPics;
    private ObjectBindAdapter<JsonPic> adapter;
    private JsonPic emptyPic;
    private int imageSize;
    private int type;//type=0 退货退款 =1退款
    private int upLoadIndex;

    @Override
    public int getLayoutId() {
        return R.layout.activity_refund_reship;
    }

    @Override
    public void doBusiness() {
        type = getIntent().getIntExtra("type", 0);
        initOrderInfo();
        initView();
    }

    private void initOrderInfo() {
        tvName.setText("退货退款申请");
        ImageLoader.displayImage(this, "", imgProduct, R.mipmap.ic_logo_empty5);
        tvProductTitle.setText("商品名称");
        tvSpec.setText("规格颜色");
        tvProductNum.setText("x1");
        if(type==0){
            llReturnNum.setVisibility(View.VISIBLE);
            lineLayout.setVisibility(View.VISIBLE);
            etApplyNum.setHint("最多可退3件");
        }

        etApplyAmount.setHint("最多可退599.0");
    }

    private void initView() {

        photos = new ArrayList<>();
        jsonPics = new ArrayList<>();
        emptyPic = new JsonPic();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        imageSize = Math.round(70 * dm.density);
        int gridViewWidth = Math.round(10 * 2 * dm.density + 70 * 3 * dm.density);
        gridView.getLayoutParams().width = gridViewWidth;
        photos.add(emptyPic);
        adapter = new ObjectBindAdapter<>(this, photos, R.layout.list_item_post_image, this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        imgUpyunPaths = new ArrayList<>();
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               checkBtnStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        if(type==0){
            etApplyNum.addTextChangedListener(textWatcher);
        }
        etApplyAmount.addTextChangedListener(textWatcher);
        tvReason.addTextChangedListener(textWatcher);
    }

    private void checkBtnStatus() {
        if(type==0){ //type=0 退货退款 =1退款
            if(StringUtil.isEmpty(etApplyNum.getText().toString().trim()) || Integer.valueOf(etApplyNum.getText().toString().trim())<=0){
                btnSure.setBackgroundColor(Color.parseColor("#dddddd"));
                btnSure.setEnabled(false);
                return;
            }
        }
        if(StringUtil.isEmpty(etApplyAmount.getText().toString().trim()) || Double.valueOf(etApplyAmount.getText().toString().trim())<=0){
            btnSure.setBackgroundColor(Color.parseColor("#dddddd"));
            btnSure.setEnabled(false);
            return;
        }
        if(StringUtil.isEmpty(tvReason.getText().toString().trim())){
            btnSure.setBackgroundColor(Color.parseColor("#dddddd"));
            btnSure.setEnabled(false);
            return;
        }
        btnSure.setBackgroundColor(getResources().getColor(R.color.normal_blue));
        btnSure.setEnabled(true);
    }


    @OnClick({R.id.iv_back, R.id.ll_reason,R.id.btn_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_reason:
                selectReasonPop();
                break;
            case R.id.btn_sure:
                if (!jsonPics.isEmpty()) {
                    upLoadIndex = 0;
                    uploadFile();
                }else{
                    applyRefundReship();
                }
                break;
        }
    }

    private void applyRefundReship() {
        btnSure.setEnabled(false);

        if(type==0){//退款退货

        }else{//退款

        }
    }

    private void uploadFile() {
        UserBean user = Session.getInstance().getUserFromFile(this);
        JsonPic jsonPic = jsonPics.get(upLoadIndex);
        File file = new File(jsonPic.getMediaPath());
        final Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(Params.BUCKET, Constants.UPYUN_SPACE);
        paramsMap.put(Params.SAVE_KEY, Util.getUPYunSavePath(user.getId(), Constants.TYPE_CUSTOMER));
        paramsMap.put(Params.RETURN_URL, "httpbin.org/post");
        UpCompleteListener completeListener = new UpCompleteListener() {
            @Override
            public void onComplete(boolean isSuccess, String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    imgUpyunPaths.add(jsonObject.optString("url"));
                } catch (JSONException e) {
                }
                upLoadIndex++;
                if (upLoadIndex < jsonPics.size()) {
                    uploadFile();
                } else {
                    applyRefundReship();
                }
            }
        };
        SignatureListener signatureListener = new SignatureListener() {
            @Override
            public String getSignature(String raw) {
                return UpYunUtils.md5(raw + Constants.UPYUN_KEY);
            }
        };
        UploadManager.getInstance().blockUpload(file, paramsMap, signatureListener, completeListener, null);
    }


    public void handlerPermission() {
        if (photos.contains(emptyPic)) {
            photos.remove(emptyPic);
        }
        PackageManager pkgManager = getPackageManager();
        boolean cameraPermission =
                pkgManager.checkPermission(Manifest.permission.CAMERA, getPackageName()) == PackageManager.PERMISSION_GRANTED;
        if (Build.VERSION.SDK_INT >= 23 && !cameraPermission) {
            requestPermission();
        } else {
            choosePic();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                Constants.RequestCode.REQUEST_PERMISSION);
    }

    private void choosePic() {
        Matisse.from(RefundReshipActivity.this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                .theme(R.style.Matisse_Dracula)
                .countable(true)
                .capture(true)
                .captureStrategy(
                        new CaptureStrategy(true, "com.d2cmall.shopkeeper.fileprovider"))
                .maxSelectable(maxSelected)
                .gridExpectedSize(ScreenUtil.dip2px(120))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(456);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Constants.RequestCode.REQUEST_PERMISSION) {
            if ((grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                choosePic();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void selectReasonPop() {
        String[] titles = new String[]{"商品需要维修", "收到商品破损", "商品错发或漏发", "收到商品与描述不符", "商品质量问题"};
        SingleSelectPop singleSelectPop = new SingleSelectPop(this, titles);
        singleSelectPop.show(getWindow().getDecorView(), llReason);
        singleSelectPop.setCallBack(this);
    }

    @Override
    public void callback(View trigger, int index, String value) {
        tvReason.setText(value);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JsonPic jsonPic = (JsonPic) parent.getAdapter().getItem(position);
        if (jsonPic != null) {
            if (StringUtil.isEmpty(jsonPic.getMediaPath())) {
                handlerPermission();
            }
        }
    }


    private class ViewHolder {
        View addView;
        View deleteView;
        ImageView imageView;
    }


    @Override
    public void setViewValue(View view, JsonPic jsonPic, int position) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.addView = view.findViewById(R.id.add_btn);
            holder.deleteView = view.findViewById(R.id.delete);
            holder.imageView = (ImageView) view.findViewById(R.id.image);
            view.getLayoutParams().width = imageSize;
            view.getLayoutParams().height = imageSize;
            view.setTag(holder);
        }
        if (StringUtil.isEmpty(jsonPic.getMediaPath())) {
            holder.imageView.setVisibility(View.GONE);
            holder.deleteView.setVisibility(View.GONE);
            holder.addView.setVisibility(View.VISIBLE);
        } else {
            holder.addView.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
            holder.deleteView.setVisibility(View.VISIBLE);
            holder.deleteView.setOnClickListener(new OnPhotoDeleteClickListener(jsonPic));
            Glide.with(this)
                    .load(Uri.fromFile(new File(jsonPic.getMediaPath())))
                    .error(R.mipmap.ic_default_pic)
                    .into(holder.imageView);
        }
    }

    private class OnPhotoDeleteClickListener implements View.OnClickListener {
        private JsonPic jsonPic;

        private OnPhotoDeleteClickListener(JsonPic jsonPic) {
            this.jsonPic = jsonPic;
        }

        @Override
        public void onClick(View v) {
            photos.remove(jsonPic);
            jsonPics.remove(jsonPic);
            maxSelected = 3 - jsonPics.size();
            if (!photos.contains(emptyPic)) {
                photos.add(emptyPic);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 456) {
            if (resultCode == RESULT_OK) {
                for (String string : Matisse.obtainPathResult(data)) {
                    JsonPic jsonPic = new JsonPic();
                    jsonPic.setMediaPath(string);
                    photos.add(jsonPic);
                    jsonPics.add(jsonPic);
                }
                if (!photos.contains(emptyPic) && jsonPics.size() < 3) {
                    photos.add(emptyPic);
                }
                if (jsonPics.size() >= 3 && photos.contains(emptyPic)) {
                    photos.remove(emptyPic);
                }
                maxSelected = 3 - jsonPics.size();
                adapter.notifyDataSetChanged();
            } else {
                if (!photos.contains(emptyPic) && jsonPics.size() < 3) {
                    photos.add(emptyPic);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
