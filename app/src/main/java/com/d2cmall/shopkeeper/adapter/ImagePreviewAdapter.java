package com.d2cmall.shopkeeper.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.ui.activity.ImagePreviewActivity;
import com.d2cmall.shopkeeper.ui.view.ninegrid.ImageInfo;
import com.d2cmall.shopkeeper.ui.view.photoview.OnPhotoTapListener;
import com.d2cmall.shopkeeper.ui.view.photoview.PhotoView;
import com.d2cmall.shopkeeper.ui.view.photoview.PhotoViewAttacher;
import com.d2cmall.shopkeeper.utils.FileUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ImagePreviewAdapter extends PagerAdapter implements OnPhotoTapListener {

    private List<ImageInfo> imageInfo;
    private Context context;
    private View currentView;
    private Handler mHandle;
    private boolean downing;

    public ImagePreviewAdapter(Context context, @NonNull List<ImageInfo> imageInfo) {
        super();
        this.imageInfo = imageInfo;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageInfo.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        currentView= (View) object;
    }

    public View getPrimaryItem() {
        return currentView;
    }

    public ImageView getPrimaryImageView() {
        return (ImageView) currentView.findViewById(R.id.pv);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_photoview, container, false);
        final ProgressBar pb = (ProgressBar) view.findViewById(R.id.pb);
        pb.setVisibility(View.VISIBLE);
        final PhotoView imageView = (PhotoView) view.findViewById(R.id.pv);
        final ImageInfo info = this.imageInfo.get(position);
        imageView.setOnPhotoTapListener(this);
        Glide.with(context).load(info.bigImageUrl).asBitmap().placeholder(R.mipmap.ic_logo_empty5).error(R.mipmap.ic_logo_empty5).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                pb.setVisibility(View.GONE);
                imageView.setImageBitmap(resource);
            }
        });
        container.addView(view);
        return view;
    }

    private void downLoadPic(final String url) {
        createHandle();
        if (downing) {
            Util.showToast(context, "正在下载中");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                downFile(url);
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
                int state = FileUtil.save2File(context, Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/D2C", name, inputStream);
                if (state == -1) {
                    mHandle.sendEmptyMessage(2);
                } else if (state == 1) {
                    mHandle.sendEmptyMessage(3);
                } else {
                    mHandle.sendEmptyMessage(4);
                }
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
                switch (what) {
                    case 1:
                        Util.showToast(context, "没有内存卡");
                        break;
                    case 2:
                        Util.showToast(context, "保存失败");
                        downing = false;
                        break;
                    case 3:
                        Util.showToast(context, "已经保存");
                        downing = false;
                        break;
                    case 4:
                        Util.showToast(context, "保存成功");
                        downing = false;
                        break;
                    case 5:
                        Util.showToast(context, "读取失败");
                        break;
                    case 6:
                        Util.showToast(context, "正在下载中");
                        break;
                }
            }
        };
    }

    private PhotoView imageView;
    private ProgressBar pb;

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void onPhotoTap(ImageView view, float x, float y) {
        ((ImagePreviewActivity) context).finishActivityAnim();
    }
}
