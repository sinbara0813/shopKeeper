package com.d2cmall.shopkeeper.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.ArrayMap;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.base.mvp.BaseView;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.model.UserBean;
import com.d2cmall.shopkeeper.ui.view.RoundedImageView;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by LWJ on 2019/3/1.
 * Description : EditPersonInfoActivity
 * 编辑个人信息
 */

public class EditPersonInfoActivity extends BaseActivity implements BaseView {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.iv_user_head_pic)
    RoundedImageView ivUserHeadPic;
    @BindView(R.id.ll_head_pic)
    LinearLayout llHeadPic;
    @BindView(R.id.et_nick_name)
    EditText etNickName;
    @BindView(R.id.et_role)
    EditText etRole;
    @BindView(R.id.et_phone)
    TextView etPhone;
    @BindView(R.id.tv_login_out)
    TextView tvLoginOut;
    private UserBean user;
    private String newHeadUrl;

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_person_info;
    }

    @Override
    public void doBusiness() {
        tvName.setText("个人信息");
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        ShadowDrawable.setShadowDrawable(rlTitle, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), 0, 0, 0, ScreenUtil.dip2px(16), 0, 0);
       /* ShadowDrawable.setShadowDrawable(tvLoginOut, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), ScreenUtil.dip2px(20), 0, 0);*/
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("提交");
        tvRight.setTextColor(getResources().getColor(R.color.normal_blue));
        initView();
    }

    private void initView() {
        user = Session.getInstance().getUserFromFile(this);
        if(user !=null){
            ImageLoader.displayRoundImage(this, user.getAvatar(),ivUserHeadPic,R.mipmap.icon_default_avatar);
            if(!StringUtil.isEmpty(user.getRoleName())){
                etRole.setText(user.getRoleName());
            }
            if(!StringUtil.isEmpty(user.getNickname())){
                etNickName.setText(user.getNickname());
            }
            if(!StringUtil.isEmpty(user.getAccount())){
                etPhone.setText(user.getAccount());
            }
        }
    }


    @OnClick({R.id.iv_back, R.id.tv_right, R.id.ll_head_pic, R.id.tv_login_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                //提交
                updateInfo();
                break;
            case R.id.ll_head_pic:
                //头像
                handlePermission();
                break;
            case R.id.tv_login_out:
                //退出登录
                loginOut();
                break;
        }
    }


    public void handlePermission() {
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
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                Constants.RequestCode.REQUEST_PERMISSION);
    }

    private void choosePic() {

        Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                .theme(R.style.Matisse_Dracula)
                .countable(true)
                .capture(true)
                .captureStrategy(
                        new CaptureStrategy(true, "com.d2cmall.shopkeeper.fileprovider"))
                .maxSelectable(1)
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case 456:
                    if (data != null) {
                        List<Uri> uris = Matisse.obtainResult(data);
                        List<String> strings = Matisse.obtainPathResult(data);
                        Intent intent = new Intent(this, CropActivity.class);
                        intent.setData(uris.get(0));
                        intent.putExtra("path", strings.get(0));
                        intent.putExtra("mode",1);
                        startActivityForResult(intent, 1121);
                    }
                    break;
                case 1121:
                    if (data != null) {
                        Uri uri = data.getData();
                        String cropPath = data.getStringExtra("cropPath");
                        Glide.with(this)
                                .load(uri)
                                .error(R.mipmap.icon_addpic)
                                .crossFade()
                                .into(ivUserHeadPic);
                        uploadFile(cropPath);
                    }
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        final Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(Params.BUCKET, Constants.UPYUN_SPACE);
        paramsMap.put(Params.SAVE_KEY, Util.getUPYunSavePath(0, Constants.TYPE_AVATAR));
        paramsMap.put(Params.RETURN_URL, "httpbin.org/post");
        UpCompleteListener completeListener = new UpCompleteListener() {

            @Override
            public void onComplete(boolean isSuccess, String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    newHeadUrl = jsonObject.optString("url");
                } catch (JSONException e) {
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

    private void updateInfo() {
        if(user==null){
            return;
        }
        if(StringUtil.isEmpty(etNickName.getText().toString().trim())){
            Util.showToast(this,"请输入昵称");
            return;
        }
        if(etNickName.getText().toString().trim().length()>9){
            Util.showToast(this,"昵称长度应在9个字符之内");
            return;
        }
        if(StringUtil.isEmpty(etPhone.getText().toString().trim())){
            Util.showToast(this,"请输入手机号");
            return;
        }
        if(StringUtil.isEmpty(etRole.getText().toString().trim())){
            Util.showToast(this,"请输入角色");
            return;
        }
        ArrayMap<String,String> map = new ArrayMap<>();
        map.put("id",String.valueOf(user.getId()));
        map.put("nickname",etNickName.getText().toString().trim());
        if(!StringUtil.isEmpty(newHeadUrl)){
            map.put("avatar",newHeadUrl);
        }
        addDisposable(ApiRetrofit.getInstance().getApiService().updatePersonInfo(map), new BaseObserver<BaseModel<UserBean>>(this) {
            @Override
            public void onSuccess(BaseModel<UserBean> bean) {
                Util.showToast(EditPersonInfoActivity.this,"更新成功");
                Session.getInstance().saveUserToFile(EditPersonInfoActivity.this,bean.getData());
                finish();
            }
        });

    }

    private void loginOut() {
        addDisposable(ApiRetrofit.getInstance().getApiService().loginout(), new BaseObserver() {
            @Override
            public void onSuccess(Object o) {
                Session.getInstance().logout(EditPersonInfoActivity.this);
                Util.showToast(EditPersonInfoActivity.this,"退出登录成功");
                Intent intent = new Intent(EditPersonInfoActivity.this, LoginActivity.class);
                intent.putExtra("isFromeLoginOut",true);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void requestStart() {

    }

    @Override
    public void requestFinish() {

    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
