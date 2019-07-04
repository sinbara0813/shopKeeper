package com.d2cmall.shopkeeper.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.utils.BitmapUtils;
import com.d2cmall.shopkeeper.utils.DialogUtil;
import com.d2cmall.shopkeeper.utils.Util;
import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.LoadCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by rookie on 2017/11/23.
 * 图片裁剪
 */

public class CropActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.CropImageView)
    com.isseiaoki.simplecropview.CropImageView mCropView;
    @BindView(R.id.image_view)
    ImageView imageView;
    @BindView(R.id.crop)
    TextView crop;
    @BindView(R.id.rotate)
    TextView rotate;
    private Uri firstUri;
    private boolean cropEnable = false;
    private String path, savePath;
    private Dialog progressDialog;
    private int mode = -1;
    private int ratioX;
    private int ratioY;

    @Override
    public int getLayoutId() {
        return R.layout.activity_crop_layout;
    }

    @Override
    public void doBusiness() {
        tvName.setText("编辑");
        tvRight.setText("完成");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setTextColor(getResources().getColor(R.color.normal_blue));
        firstUri = getIntent().getData();
        path = getIntent().getStringExtra("path");
        mode = getIntent().getIntExtra("mode", -1);
        ratioX=getIntent().getIntExtra("ratioX",0);
        ratioY=getIntent().getIntExtra("ratioY",0);
        progressDialog = DialogUtil.createLoadingDialog(this);
        mCropView.setInitialFrameScale(0.8f);
        mCropView.setCropMode(CropImageView.CropMode.SQUARE);
        if (mode==1){
            mCropView.setCropMode(CropImageView.CropMode.CIRCLE_SQUARE);
        } else if (mode == 6) {
            mCropView.setCropMode(CropImageView.CropMode.FREE);
        }
        if (ratioY!=0&&ratioX!=0){
            mCropView.setCustomRatio(ratioX,ratioY);
        }
        mCropView.startLoad(firstUri, new LoadCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Util.showToast(CropActivity.this, "图片加载失败");
                finish();
            }
        });
        mCropView.setCropEnabled(false);
    }

    private void save(Bitmap bigBitmap) {
        progressDialog.show();
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);
        if (!sdCardExist) {
            Util.showToast(this, "请确保要内存卡！");
            progressDialog.dismiss();
            tvRight.setEnabled(true);
            return;
        }
        if (bigBitmap == null) {
            progressDialog.dismiss();
            Util.showToast(this, "图片保存失败！");
            finish();
            tvRight.setEnabled(true);
            return;
        }
        String fileName = String.valueOf(System.currentTimeMillis());
        String foldStr = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/D2CHead";
        File folder = new File(foldStr);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file = new File(folder, fileName + ".png");
        if (file.exists()) {
            progressDialog.dismiss();
            Util.showToast(this, "图片保存失败！");
            tvRight.setEnabled(true);
            return;
        }
        if (bigBitmap.getByteCount() > 8485760) {
            bigBitmap = bigBitmap.copy(Bitmap.Config.RGB_565, true);
            float scale = 1;
            while (bigBitmap.getByteCount() > 8485760) {
                scale -= 0.2;
                bigBitmap = BitmapUtils.getScaleBitmap(bigBitmap, scale, scale);
            }
        }
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bigBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            FileOutputStream os = new FileOutputStream(file);
            os.write(stream.toByteArray());
            stream.flush();
            stream.close();
            os.flush();
            os.close();
        } catch (Exception ex) {
            Util.showToast(this, "保存出错！");
            tvRight.setEnabled(true);
            progressDialog.dismiss();
            file = null;
        } finally {
            if (bigBitmap != null) {
                bigBitmap.recycle();
            }
        }
        if (file != null) {
            progressDialog.dismiss();
            Intent intent = new Intent();
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            intent.putExtra("cropPath", file.getAbsolutePath());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void rotateImage() {
        mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
    }

    @OnClick({R.id.iv_back, R.id.tv_right, R.id.crop, R.id.rotate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                Util.showToast(this,"保存中...");
                tvRight.setEnabled(false);
                save(mCropView.getCroppedBitmap());
                break;
            case R.id.crop:
                if (cropEnable) {
                    mCropView.setCropEnabled(false);
                    cropEnable = false;
                } else {
                    mCropView.setCropEnabled(true);
                    cropEnable = true;
                }
                break;
            case R.id.rotate:
                rotateImage();
                break;
        }
    }

}
