package com.d2cmall.shopkeeper.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.example.bannerlibrary.DefineBaseIndicatorBanner;


/**
 * Name: d2c
 * Anthor: hrb
 * Date: 2017/7/27 16:38
 * Copyright (c) 2016 d2cmall. All rights reserved.
 */
public class Banner extends DefineBaseIndicatorBanner<String,Banner.ViewHolder, Banner> {

    private RequestManager requestManager;
    private ItemClick itemClick;

    public Banner(Context context) {
        this(context, null);
    }

    public Banner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public Banner.ViewHolder onCreateItemViewHolder(ViewGroup parent) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.layout_banner_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindItemView(Banner.ViewHolder holder, int position) {
        if (requestManager!=null){
            ImageLoader.displayImage(requestManager,mDatas.get(position),holder.iv);
        }else {
            ImageLoader.displayImage((Activity) mContext,mDatas.get(position),holder.iv);
        }
        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClick!=null){
                    itemClick.onItemClick(position);
                }
            }
        });
    }

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public interface ItemClick {
        void onItemClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv;

        public ViewHolder(View itemView) {
            super(itemView);
            iv=itemView.findViewById(R.id.iv);
        }
    }

    public void setRequestManager(RequestManager requestManager) {
        this.requestManager = requestManager;
    }
}
