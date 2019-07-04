package com.d2cmall.shopkeeper.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.model.CategoryBean;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/6/19.
 * 邮箱:hrb940258169@163.com
 */
public class SearchCategoryActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.content_ll)
    LinearLayout contentLl;

    private List<CategoryBean> list = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_category;
    }

    @Override
    public void doBusiness() {
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        loadCategory();
    }

    private void loadCategory() {
        addDisposable(ApiRetrofit.getInstance().getApiService().getClassifyList(Util.buildListParam(1, 20)), new BaseObserver<BaseModel<List<CategoryBean>>>() {
            @Override
            public void onSuccess(BaseModel<List<CategoryBean>> o) {
                if (o.getData() != null && o.getData().size() > 0) {
                    list.addAll(o.getData());
                    addView();
                }
            }

            @Override
            public void onError(int errorCode, String msg) {
                super.onError(errorCode, msg);
            }
        });
    }

    private void addView() {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.layout_search_category_item, contentLl, false);
            addItemView(list.get(i), view);
            contentLl.addView(view);
        }
    }

    private void addItemView(CategoryBean bean, View view) {
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.categoryNameTv.setText(bean.getName());
        viewHolder.categoryNameTv.setTag(bean.getId());
        viewHolder.categoryNameTv.setOnClickListener(this::onClick);
        int size = bean.getChildren().size();
        if (size > 0) {
            String[] name = new String[size];
            for (int i = 0; i < size; i++) {
                name[i] = bean.getChildren().get(i).getName();
            }
            List<Map<String, String>> data = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                Map<String, String> map = new HashMap<>();
                //如果只需要显示图片，可以不用这一行，需要同时将from和to中的相关内容删去
                map.put("ItemText", name[i]);
                data.add(map);
            }
            viewHolder.gridView.setAdapter(new GridViewAdapter(bean.getChildren(), this));
        }
    }

    @OnClick(R.id.back_iv)
    public void onViewClicked() {
        finish();
    }

    public class GridViewAdapter extends BaseAdapter {

        private List<CategoryBean> data;//数据

        private Context context;//上下文

        GridViewAdapter(List<CategoryBean> data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_text, parent, false);
            TextView textView = view.findViewById(R.id.text);
            textView.setTag(data.get(position).getId());
            textView.setText(data.get(position).getName());
            textView.setOnClickListener(SearchCategoryActivity.this::onClick);
            return view;
        }
    }

    @Override
    public void onClick(View v) {
        long id = (long) v.getTag();
        if (id > 0) {
            Intent intent = new Intent(this,SearchResultActivity.class);
            intent.putExtra("id", id);
            if (v instanceof TextView){
                intent.putExtra("title",((TextView)v).getText().toString());
            }
            intent.putExtra("search",false);
            startActivity(intent);
            finish();
        }
    }

    static class ViewHolder {
        @BindView(R.id.category_name_tv)
        TextView categoryNameTv;
        @BindView(R.id.gridView)
        GridView gridView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
