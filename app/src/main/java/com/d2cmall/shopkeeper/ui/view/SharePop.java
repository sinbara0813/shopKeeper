package com.d2cmall.shopkeeper.ui.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.d2cmall.shopkeeper.common.Short;
import com.d2cmall.shopkeeper.share.WxHandle;
import com.d2cmall.shopkeeper.utils.BitmapUtils;
import com.d2cmall.shopkeeper.utils.HttpUtils;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.wxapi.WXEntryActivity;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Name: D2CMALL
 * Anthor: hrb
 * Date: 2017/9/8 16:05
 * Copyright (c) 2016 d2cmall. All rights reserved.
 */
public class SharePop implements TransparentPop.InvokeListener,View.OnClickListener{

    @BindView(R.id.line)
    View line;
    @BindView(R.id.ll_download)
    TextView llDownload;
    @BindView(R.id.ll_delete)
    TextView llDelete;
    @BindView(R.id.delete_ll)
    LinearLayout deleteLl;
    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.share_content)
    LinearLayout shareContent;

    private TransparentPop mPop;
    private Context context;
    private View shareLayout;
    private ArrayList<String> des;
    private ArrayList<Integer> images;
    private String title = "让你欲罢不能的D2C全球好设计";
    private String description = "D2C全球好设计- 汇集全球好设计,寻找您专属的原创新品";
    private String webUrl = Constants.detailWebUrl;
    private String shareUrl;
    private String imageUrl = "http://img.d2c.cn/app/a/16/05/10/fa55b70135c181482ae5c6d39c3277b1";
    private boolean isWebView = false;
    private String func;
    private String channel = "pasteboard";
    public static final int IMAGE_SIZE = 32768;
    public static final int WEIBO_IMAGE_SIZE = 1000 * 1000;
    private boolean isPic; //是否是图片分享
    private boolean isDetail; //是否是商品详情页的大图
    private boolean isSelf;
    private byte[] imageData;
    private byte[] bigImageData;
    private float scale = 1;
    private String bigImageUrl;
    private String productUrl; //商品的地址
    private Bitmap bigBitmap;
    private long showId = -1;
    private boolean isFocus;
    private boolean isPromotionLink;
    private boolean isShareMini;
    private boolean productShare;
    private String bgImageUrl;
    private boolean isRun;
    private boolean isBuyer;
    private String miniPath, miniWebUrl;
    private byte[] miniPicData;
    private boolean appIdIsBuyer;//标示分享到小程序的appId是不是买手小程序的appId
    private WxHandle wxHandle;

    public SharePop(Context context) {
        this(context, true, false);
    }

    public SharePop(Context context, boolean isFocus) {
        this(context, isFocus, false);
    }

    public SharePop(Context context, boolean isFocus, boolean isSelf) {
        this(context, isFocus, isSelf, false);
    }

    public SharePop(Context context, boolean isFocus, boolean isSelf, boolean isBuyer) {
        this.context = context;
        this.isFocus = isFocus;
        this.isSelf = isSelf;
        this.isBuyer = isBuyer;
        init();
    }

    public void setMiniProjectPath(String path) {
        this.miniPath = path;
    }

    public void setMiniWebUrl(String webUrl) {
        this.miniWebUrl = Constants.SHARE_URL + webUrl;
    }

    public void setMiniProjectDec(String dec) {
        this.description = dec;
    }

    public void setMiniPicData(byte[] data) {
        this.miniPicData = data;
    }


    public void setShowId(long id) {
        this.showId = id;
    }

    private void init() {
        shareLayout = LayoutInflater.from(context).inflate(R.layout.layout_share_pop, new LinearLayout(context), false);
        ButterKnife.bind(this, shareLayout);
        mPop = new TransparentPop(context, this);
        if (!isFocus) {
            mPop.setFocusable(false);
        }
        Animation inAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_in_up);
        Animation outAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_out_down);
        mPop.setInAnimation(inAnimation);
        mPop.setOutAnimation(outAnimation);
        wxHandle=new WxHandle(context);
        initData(wxHandle.isAppInstalled());
        int count = 0;
        if (isBuyer) {
            count++;
            llDownload.setVisibility(View.VISIBLE);
        } else {
            llDownload.setVisibility(View.GONE);
        }
        if (isSelf) {
            if (!isBuyer) {
                llDelete.setPadding(0, 0, 0, 0);
            }
            llDelete.setVisibility(View.VISIBLE);
            count++;
        } else {
            llDelete.setVisibility(View.GONE);
        }
        if (count > 0) {
            line.setVisibility(View.VISIBLE);
            deleteLl.setVisibility(View.VISIBLE);
        } else {
            line.setVisibility(View.GONE);
            deleteLl.setVisibility(View.GONE);
        }
        llDownload.setOnClickListener(this);
        llDelete.setOnClickListener(this);
        cancel.setOnClickListener(this);
        shareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }

    private void openWx(File f) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm",
                "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");

        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            imageUris.add(Uri.fromFile(f));
        } else {
            Uri uri = null;
            try {
                uri = Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), f.getAbsolutePath(), f.getName(), null));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            imageUris.add(uri);
        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        intent.putExtra("Kdescription", description);
        context.startActivity(intent);
    }

    public void initData(boolean isInstallWx) {
        des = new ArrayList<>();
        images = new ArrayList<>();
        if (isInstallWx) {
            des.add(context.getString(R.string.label_share_wxzone));
            des.add(context.getString(R.string.label_share_wxfriend));
            images.add(R.mipmap.icon_share_pyq);
            images.add(R.mipmap.icon_share_wx);
        }
/*        des.add(context.getString(R.string.label_share_qq));
        images.add(R.mipmap.icon_share_qq);
        des.add(context.getString(R.string.label_share_qqzone));
        images.add(R.mipmap.icon_share_qqkj);
        des.add(context.getString(R.string.label_share_weibo));
        images.add(R.mipmap.icon_share_wb);
        if (!isPic) {
            des.add(context.getString(R.string.label_share_link));
            images.add(R.mipmap.icon_share_fzlj);
        }*/
        addContent();
    }

    /**
     * 添加分享渠道
     */
    private void addContent(){
        shareContent.removeAllViews();
        if (des.size()==1){
            View view=getItemView(shareContent,des.get(0),images.get(0));
            shareContent.addView(view);
        }else if (des.size()==2){
            LinearLayout linearLayout=new LinearLayout(context);
            linearLayout.setPadding(ScreenUtil.dip2px(60),0,ScreenUtil.dip2px(60),0);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams childLl=new LinearLayout.LayoutParams(-2,-2);
            childLl.setMargins(0,0,ScreenUtil.dip2px(100),0);
            for (int i=0;i<2;i++){
                View view=getItemView(linearLayout,des.get(i),images.get(i));
                if (i==0){
                    linearLayout.addView(view,childLl);
                }else {
                    linearLayout.addView(view);
                }
            }
            shareContent.addView(linearLayout);
        } else {
            int size=des.size();
            int rowSize=size%3==0?size/3:size/3+1;
            for (int i=0;i<rowSize;i++){
                LinearLayout linearLayout=new LinearLayout(context);
                linearLayout.setPadding(ScreenUtil.dip2px(30),0,ScreenUtil.dip2px(30),0);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams childLl;
                for (int j=0;j<3;j++){
                    if (3*i+j>=size){
                        break;
                    }
                    View view=getItemView(linearLayout,des.get(3*i+j),images.get(3*i+j));
                    childLl=new LinearLayout.LayoutParams(-2,-2);
                    if (j==2){
                        childLl.setMargins(0,0,0,0);
                    }else {
                        childLl.setMargins(0,0,ScreenUtil.dip2px(50),0);
                    }
                    linearLayout.addView(view,childLl);
                }
                shareContent.addView(linearLayout);
                if (i!=rowSize-1){
                    View line=new View(context);
                    line.setBackgroundColor(context.getResources().getColor(R.color.line));
                    LinearLayout.LayoutParams lineLl=new LinearLayout.LayoutParams(-1, ScreenUtil.dip2px(0.5f));
                    shareContent.addView(line,lineLl);
                }
            }
        }
    }

    private View getItemView(LinearLayout parent,String text,int sourceId){
        View view=LayoutInflater.from(context).inflate(R.layout.list_item_share_dialog,parent,false);
        ImageView imageView=view.findViewById(R.id.image);
        TextView textView=view.findViewById(R.id.text);
        imageView.setImageResource(sourceId);
        textView.setText(text);
        view.setId(sourceId);
        view.setOnClickListener(this);
        return view;
    }

    public void setBgImageUrl(String bgImageUrl) {
        this.bgImageUrl = bgImageUrl;
    }

    @Override
    public void invokeView(LinearLayout v) {
        v.setGravity(Gravity.BOTTOM);
        v.addView(shareLayout);
    }

    public void share2Wx() {
        channel = "moments";
        WXEntryActivity.webUrl = webUrl;
        WXEntryActivity.channel = channel;
        dismiss();
        if (isPic) {
        } else {
            wxHandle.setTitle(title);
            wxHandle.setDes(description);
            wxHandle.setWebUrl(StringUtil.isEmpty(shareUrl) ? webUrl : shareUrl);
            if (imageData == null || imageData.length == 0) {
                imageData = BitmapUtils.getBitmapData(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
            }
            wxHandle.sendShare(context, imageData, SendMessageToWX.Req.WXSceneSession);
        }
    }

    public void share2WxFriend() {
        channel = "Weixin";
        WXEntryActivity.webUrl = webUrl;
        WXEntryActivity.channel = channel;
        dismiss();
        if (isPic) {
        } else {
            wxHandle.setTitle(title);
            wxHandle.setDes(description);
            wxHandle.setWebUrl(StringUtil.isEmpty(shareUrl) ? webUrl : shareUrl);
            if (imageData == null || imageData.length == 0) {
                imageData = BitmapUtils.getBitmapData(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
            }
            wxHandle.sendShare(context, imageData, SendMessageToWX.Req.WXSceneTimeline);
        }
    }


    public void show(View view) {
        mPop.show(view, true);
    }

    private void dismiss() {
        mPop.dismiss(true);
    }

    @Override
    public void releaseView(LinearLayout v) {
        imageData = null;
        bigImageData = null;
        miniPicData = null;
        if (bigBitmap != null) {
            bigBitmap.recycle();
        }
        ((ViewGroup) shareLayout).removeAllViews();
        shareLayout = null;
        if (wxHandle!=null){
            wxHandle.detach();
        }
    }

    public void setWebView(boolean webView) {
        isWebView = webView;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public void setProductUrl(String url) {
        this.productUrl = url;
    }

    public void setPic(boolean pic) {
        isPic = pic;
    }

    public void setDetail(boolean detail) {
        isDetail = detail;
    }

    public void setBigBitmap(Bitmap bigBitmap) {
        this.bigBitmap = bigBitmap.copy(Bitmap.Config.ARGB_8888,true);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
        if (!StringUtil.isEmpty(webUrl)) {
            long shopId = 0;
            if (Session.getInstance().getUserFromFile(context) != null) {
                shopId = Session.getInstance().getUserFromFile(context).getId();
            }
            if (webUrl.contains("?")){
                shareUrl=webUrl+"&shopId="+shopId;
            }else {
                shareUrl=webUrl+"?shopId="+shopId;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String jsonStr = HttpUtils.request("http://apis.baidu.com/3023/shorturl/shorten", "url_long=" + shareUrl);
                    if (jsonStr != null && jsonStr.contains("urls")) {
                        Gson gson = new Gson();
                        Short shortBean = gson.fromJson(jsonStr, Short.class);
                        shareUrl = shortBean.getUrls().get(0).getUrl_short();
                    }
                }
            }).start();
        }
    }

    public void setImage(String url, final boolean isBig) {
        if (isBig) {
            bigImageUrl = url;
            bigImageUrl = StringUtil.getD2cPicUrl(bigImageUrl);
        }
        Glide.with(context).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (isBig) {
                    bigImageData = BitmapUtils.getBitmapData(resource);
                    if (imageData==null)return;
                    while (bigImageData.length > 1597152) {
                        scale -= 0.2;
                        Bitmap scaleBitmap = BitmapUtils.getScaleBitmap(resource, scale, scale);
                        bigImageData = BitmapUtils.getBitmapData(scaleBitmap);
                        scaleBitmap.recycle();
                    }
                } else {
                    imageData = BitmapUtils.getBitmapData(resource);
                    if (imageData==null)return;
                    while (imageData.length > IMAGE_SIZE) {
                        scale -= 0.1;
                        Bitmap scaleBitmap = BitmapUtils.getScaleBitmap(resource, scale, scale);
                        imageData = BitmapUtils.getBitmapData(scaleBitmap);
                        scaleBitmap.recycle();
                    }
                }
            }
        });
    }

    public void setMiniImage(String url, boolean isLocal, int resId) {
        if (isLocal) { //本地图片
            if (miniPicData == null) {
                Bitmap mini = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), resId), 250, 250, true);
                miniPicData = BitmapUtils.getBitmapData(mini);
                if (miniPicData == null) {
                    mini.recycle();
                }
            }
        } else {
            Glide.with(context).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    if (miniPicData == null) {
                        miniPicData = BitmapUtils.getBitmapData(resource);
                        resource.recycle();
                    }
                }
            });
        }
    }

    public void setBigImageUrl(String bigImageUrl) {
        this.bigImageUrl = bigImageUrl;
    }

    public boolean isPromotionLink() {
        return isPromotionLink;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.mipmap.icon_share_qq:
                break;
            case R.mipmap.icon_share_pyq:
                if (isShareMini) {//分享海报图到朋友圈
                } else {
                    share2WxFriend();
                }
                break;
            case R.mipmap.icon_share_wx:
                if (isShareMini || productShare) {
                } else {
                    share2Wx();
                }
                break;
            case R.mipmap.icon_share_wb:
                break;
            case R.mipmap.icon_share_qqkj:
                break;
            case R.mipmap.icon_share_fzlj:
                break;
            case R.mipmap.icon_share_hb:
                break;
            case R.id.cancel:
                dismiss();
                break;
            case R.id.ll_download:
                break;
            case R.id.ll_delete:
                break;
        }
    }

    public void setAppIdIsBuyer(boolean appIdIsBuyer) {
        this.appIdIsBuyer = appIdIsBuyer;
    }

    public void setShareMini(boolean shareMini) {
        isShareMini = shareMini;
    }

    public void setProductShare(boolean productShare) {
        this.productShare = productShare;
    }
}
