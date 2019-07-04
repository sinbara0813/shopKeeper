package com.d2cmall.shopkeeper.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.holder.MarketProductHolder;
import com.d2cmall.shopkeeper.holder.MarketProductHolder1;
import com.d2cmall.shopkeeper.model.ProductBean;
import com.d2cmall.shopkeeper.ui.activity.BrandDetailActivity;
import com.d2cmall.shopkeeper.ui.activity.MarketProductDetialActivity;
import com.d2cmall.shopkeeper.ui.view.SharePop;
import com.d2cmall.shopkeeper.utils.DateUtil;
import com.d2cmall.shopkeeper.utils.FileUtil;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者:Created by sinbara on 2019/6/20.
 * 邮箱:hrb940258169@163.com
 */
public class MarketProductAdapter1 extends DelegateAdapter.Adapter<MarketProductHolder1> {

    private Context context;
    private List<ProductBean> data;
    private boolean downing;
    private Handler mHandle;
    private int count;
    private int index;

    public MarketProductAdapter1(Context context, List<ProductBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        LinearLayoutHelper helper=new LinearLayoutHelper();
        return helper;
    }

    @NonNull
    @Override
    public MarketProductHolder1 onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_market_product_item2, viewGroup, false);
        return new MarketProductHolder1(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarketProductHolder1 holder, int i) {
        ProductBean bean=data.get(i);
        if (StringUtil.isEmpty(bean.getName())){
            holder.itemView.setVisibility(View.GONE);
            return;
        }
        ImageLoader.displayImage((Activity) context,bean.getShoplogo(),holder.iv);
        holder.nameTv.setText(bean.getShopName());
        if (!StringUtil.isEmpty(bean.getCreateDate())){
            holder.dateTv.setText(DateUtil.getTimeFromNow(bean.getCreateDate()));
        }
        holder.infoTv.setText(bean.getName());
        List<String> list=new ArrayList<>();
        if (!StringUtil.isEmpty(bean.getPic())){
            String[] urls=bean.getPic().split(",");
            int length=urls.length;
            for (int j=0;j<length;j++){
                list.add(StringUtil.getD2cPicUrl(urls[j]));
            }
            holder.nineRl.setVisibility(View.VISIBLE);
            holder.nineRl.setUrlList(list);
        }else {
            holder.nineRl.setVisibility(View.GONE);
        }
        holder.productPrice.setText("¥"+Util.getNumberFormat(bean.getSupplyPrice()));
        holder.profitTv.setText("利润 ¥"+Util.getNumberFormat(bean.getProfit()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MarketProductDetialActivity.class);
                intent.putExtra("id",bean.getId());
                context.startActivity(intent);
            }
        });
        holder.joinTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BrandDetailActivity.class);
                intent.putExtra("shopId",bean.getShopId());
                context.startActivity(intent);
            }
        });
        holder.nameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BrandDetailActivity.class);
                intent.putExtra("shopId",bean.getShopId());
                context.startActivity(intent);
            }
        });
        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BrandDetailActivity.class);
                intent.putExtra("shopId",bean.getShopId());
                context.startActivity(intent);
            }
        });
        holder.downTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!list.isEmpty()){
                    down(list);
                }
            }
        });
        holder.shareTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(v,bean);
            }
        });
        setProductType(holder,bean);
    }

    private void down(List<String> list){
        createHandle();
        if (downing) {
            Util.showToast(context, "正在下载中");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                count=list.size();
                index=0;
                for (int i=0;i<count;i++){
                    downFile(StringUtil.getD2cPicUrl(list.get(i)));
                }
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
                int state = FileUtil.save2File(context, Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/ShopKeeper", name, inputStream);
                if (state == -1) {
                    mHandle.sendEmptyMessage(2);
                } else if (state == 1) {
                    mHandle.sendEmptyMessage(3);
                } else {
                    mHandle.sendEmptyMessage(4);
                }
                this.index++;
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
                if (index<count)return;
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
                        Util.showToast(context, "保存成功,请到相册shopkeeper目录查看");
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

    private void share(View view,ProductBean bean){
        if (Util.wxInstalled()) {
            SharePop pop = new SharePop(context);
            pop.setTitle(bean.getName());
            pop.setDescription("发现了一个好东西，赶紧看看吧！");
            pop.setImage(StringUtil.getD2cPicUrl(bean.getFirstPic(), 100, 100), false);
            pop.setWebUrl(Constants.SHARE_URL + "product/" + bean.getId());
            pop.show(view);
        } else {
            Util.showToast(context, "请先安装微信");
        }
    }

    private TextView getTagView(String text){
        TextView textView=new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
        textView.setBackgroundColor(Color.parseColor("#f3f3f3"));
        textView.setTextColor(ContextCompat.getColor(context,R.color.color_black4));
        textView.setText(text);
        return textView;
    }

    private void setProductType(MarketProductHolder1 holder,ProductBean recordsBean){
        holder.tagLl.removeAllViews();
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ScreenUtil.dip2px(52),ScreenUtil.dip2px(19));
        lp.setMargins(0,0,ScreenUtil.dip2px(6),0);
        if (recordsBean.getAllot()==1){
            holder.tagLl.addView(getTagView("免费拿货"),lp);
        }
        if (recordsBean.getBuyout()==1){
            holder.tagLl.addView(getTagView("支持买断"),lp);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }
}
