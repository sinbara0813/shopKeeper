package com.d2cmall.shopkeeper.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.common.JsonPic;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.holder.SimpleImageHolder;
import com.d2cmall.shopkeeper.ui.activity.AddProductActivity;
import com.d2cmall.shopkeeper.ui.view.UploadImageView;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * 作者:Created by sinbara on 2019/3/29.
 * 邮箱:hrb940258169@163.com
 */

public class SampleImageAdapter extends RecyclerView.Adapter<SimpleImageHolder> {

    private Context context;
    private RequestManager manager;
    private ArrayList<JsonPic> pics;
    private Listener listener;
    private long userId;

    public SampleImageAdapter(Context context,RequestManager manager, ArrayList<JsonPic> pics,Listener listener){
        this.context=context;
        this.manager=manager;
        this.pics=pics;
        this.listener=listener;
        userId= Session.getInstance().getUser().getId();
    }

    @Override
    public SimpleImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_upload_img,parent,false);
        return new SimpleImageHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleImageHolder holder, int position) {
        JsonPic jsonPic=pics.get(position);
        UploadImageView image = holder.iv;
        ImageButton delete = holder.delete;
        ImageView addBtn = holder.addIv;
        holder.itemView.setTag(jsonPic.getMediaPath());
        if (StringUtil.isEmpty(jsonPic.getMediaPath())) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null){
                        listener.emptyClick(v);
                    }
                }
            });
            image.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            addBtn.setVisibility(View.VISIBLE);
        } else {
            addBtn.setVisibility(View.GONE);
            image.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null){
                        listener.itemDelete(jsonPic);
                    }
                }
            });
            image.setJsonPic(jsonPic);
            if (!StringUtil.isEmpty(jsonPic.getUploadPath())){
                image.setProgress(100);
                ImageLoader.displayImage(manager,jsonPic.getUploadPath(),image);
            }else {
                Glide.with(context)
                        .load(Uri.fromFile(new File(StringUtil.getD2cPicUrl(jsonPic.getMediaPath()))))
                        .error(R.mipmap.ic_default_pic)
                        .into(image);
                image.userId = userId;
                if (listener!=null&&!listener.hasUpload(jsonPic.getMediaPath())) {
                    image.startUpload();
                } else {
                    image.setProgress(100);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return pics==null?0:pics.size();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener{
        public void emptyClick(View v);
        public void itemDelete(JsonPic jsonPic);
        public boolean hasUpload(String url);
    }
}
