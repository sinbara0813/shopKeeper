package com.d2cmall.shopkeeper.ui.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.utils.Base64;
import com.d2cmall.shopkeeper.utils.BitmapUtils;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/3/18.
 * 邮箱:hrb940258169@163.com
 */

public class ScanCodePop implements TransparentPop.InvokeListener {

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.info)
    TextView info;
    private View view;
    private TransparentPop mPop;
    private String url;

    public ScanCodePop(Context context, String url, boolean isImage) {
        this.url = url;
        view = LayoutInflater.from(context).inflate(R.layout.layout_scan_code, new LinearLayout(context), false);
        ButterKnife.bind(this, view);
        if (isImage) {
            Glide.with(context).load(StringUtil.getD2cPicUrl(url)).asBitmap().into(image);
        } else {
            image.setImageBitmap(BitmapUtils.createQRImage(url, ScreenUtil.dip2px(142), ScreenUtil.dip2px(142)));
        }
        info.setText("添加店铺微信，不定期派发福利"+"\n"+"长按保存图片");
        if (isImage&&StringUtil.isEmpty(url)){
            info.setText("商家尚未上传微信二维码");
        }

        mPop = new TransparentPop(context, this);
        image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                saveBitmap(url);
                return false;
            }
        });
    }

    private void saveBitmap(String bigImageUrl) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        if (bmp != null) {
            String fileName = Base64.encode(bigImageUrl.getBytes());
            String foldStr = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/ShopKeeper";
            File folder = new File(foldStr);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            int index = bigImageUrl.lastIndexOf(".");
            int type = 1;
            if (index > 0 && index + 2 < bigImageUrl.length()) {
                String s = bigImageUrl.substring(index + 1, index + 2);
                if (s.equals("j")) {
                    type = 2;
                }
            }
            File file = null;
            if (type == 1) {
                file = new File(folder, fileName + ".png");
            } else {
                file = new File(folder, fileName + ".jpg");
            }
            if (file.exists()) {
                Util.showToast(view.getContext(), "已保存!");
                return;
            }
            try {
                FileOutputStream os = new FileOutputStream(file);
                if (type == 1) {
                    bmp.compress(Bitmap.CompressFormat.PNG, 75, os);
                } else {
                    bmp.compress(Bitmap.CompressFormat.JPEG, 75, os);
                }
                os.flush();
                os.close();
            } catch (Exception e) {
            } finally {

            }
            bmp.recycle();
            Util.showToast(view.getContext(), "保存成功!");
            if (file != null) {
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                view.getContext().sendBroadcast(intent);
            }
        }
    }

    public void show(View view) {
        mPop.show(view, false);
    }

    public void dismiss() {
        mPop.dismiss(false);
    }

    @Override
    public void invokeView(LinearLayout v) {
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ScreenUtil.getDisplayWidth() - ScreenUtil.dip2px(40), -2);
        v.setGravity(Gravity.CENTER);
        v.addView(view, ll);
    }

    @Override
    public void releaseView(LinearLayout v) {
        v.removeAllViews();
        view = null;
    }

}
