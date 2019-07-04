package com.d2cmall.shopkeeper.ui.view;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.model.ShopBean;
import com.d2cmall.shopkeeper.share.WxHandle;
import com.d2cmall.shopkeeper.utils.Base64;
import com.d2cmall.shopkeeper.utils.BitmapUtils;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/6/20.
 * 邮箱:hrb940258169@163.com
 */
public class HomeSharePop implements TransparentPop.InvokeListener {

    @BindView(R.id.close_iv)
    ImageView closeIv;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.scode_iv)
    ImageView scodeIv;
    @BindView(R.id.brand_name_tv)
    TextView brandNameTv;
    private View view;
    private TransparentPop mPop;
    private Context context;
    private String url;
    private WxHandle wxHandle;
    private String title;
    private String description;
    private byte[] imageData;

    public HomeSharePop(Context context, ShopBean shopBean) {
        this.context=context;
        this.url=Constants.SHARE_URL+shopBean.getId();
        title=shopBean.getName();
        description="发现了一家好店，快来看看吧！";
        view = LayoutInflater.from(context).inflate(R.layout.layout_home_share, new LinearLayout(context), false);
        ButterKnife.bind(this, view);
        scodeIv.setImageBitmap(BitmapUtils.createQRImage(Constants.SHARE_URL+shopBean.getId(), ScreenUtil.dip2px(142), ScreenUtil.dip2px(142)));
        ImageLoader.displayImage(Glide.with(context),shopBean.getBanner(),image,0,R.mipmap.bg_dianzhao);
        brandNameTv.setText(shopBean.getName());
        mPop = new TransparentPop(context, this);
        Animation inAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_in_up);
        Animation outAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_out_down);
        mPop.setInAnimation(inAnimation);
        mPop.setOutAnimation(outAnimation);
        wxHandle=new WxHandle(context);
        getImage(StringUtil.getD2cPicUrl(shopBean.getLogo()));
    }

    public void show(View view) {
        mPop.show(view, true);
    }

    private void dismiss() {
        mPop.dismiss(true);
    }

    @Override
    public void invokeView(LinearLayout v) {
        v.setGravity(Gravity.BOTTOM);
        v.addView(view);
    }

    @Override
    public void releaseView(LinearLayout v) {
        v.removeAllViews();
        view = null;
        imageData=null;
        if (wxHandle!=null){
            wxHandle.detach();
        }
    }

    @OnClick({R.id.close_iv, R.id.wx_tv, R.id.friend_tv, R.id.pic_tv, R.id.copy_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.close_iv:
                dismiss();
                break;
            case R.id.wx_tv:
                if (wxHandle.isAppInstalled()){
                    wxHandle.setTitle(title);
                    wxHandle.setDes(description);
                    wxHandle.setWebUrl(url);
                    if (imageData == null || imageData.length == 0) {
                        imageData = BitmapUtils.getBitmapData(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
                    }
                    wxHandle.sendShare(context, imageData, SendMessageToWX.Req.WXSceneSession);
                }else {
                    Util.showToast(context,"请按照微信");
                }
                break;
            case R.id.friend_tv:
                if (wxHandle.isAppInstalled()){
                    wxHandle.setTitle(title);
                    wxHandle.setDes(description);
                    wxHandle.setWebUrl(url);
                    if (imageData == null || imageData.length == 0) {
                        imageData = BitmapUtils.getBitmapData(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
                    }
                    wxHandle.sendShare(context, imageData, SendMessageToWX.Req.WXSceneTimeline);
                }else {
                    Util.showToast(context,"请按照微信");
                }
                break;
            case R.id.pic_tv:
                saveBitmap(url);
                break;
            case R.id.copy_tv:
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText("url", url));
                Util.showToast(context, "复制成功");
                dismiss();
                break;
        }
    }

    private void getImage(String url){
        Glide.with(context).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                imageData = BitmapUtils.getBitmapData(resource);
                resource.recycle();
            }
        });
    }

    private void saveBitmap(String bigImageUrl) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        bmp=Bitmap.createBitmap(bmp,ScreenUtil.dip2px(30),ScreenUtil.dip2px(72),ScreenUtil.getDisplayWidth()-ScreenUtil.dip2px(60),ScreenUtil.dip2px(254));
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
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                FileOutputStream os = new FileOutputStream(file);
                if (type == 1) {
                    bmp.compress(Bitmap.CompressFormat.PNG, 75, stream);
                } else {
                    bmp.compress(Bitmap.CompressFormat.JPEG, 75, stream);
                }
                os.write(stream.toByteArray());
                stream.flush();
                stream.close();
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

}
