package com.d2cmall.shopkeeper.ui.activity;

import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.ArrayMap;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.model.CategoryBean;
import com.d2cmall.shopkeeper.ui.view.CategoryItemView;
import com.d2cmall.shopkeeper.ui.view.OverScrollView;
import com.d2cmall.shopkeeper.utils.KeyboardUtil;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/2/22.
 * 邮箱:hrb940258169@163.com
 * 分类管理
 */

public class CategoryManagerActivity extends BaseActivity {

    @BindView(R.id.scrollView)
    OverScrollView scrollView;
    @BindView(R.id.edit_iv)
    TextView editIv;
    @BindView(R.id.add_iv)
    ImageView addIv;
    @BindView(R.id.content_ll)
    LinearLayout contentLl;

    private boolean isEdit;
    private List<CategoryItemView> viewList;
    private int p = 1;
    private View emptyView;
    private List<Integer> sortList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_category_manager;
    }

    @Override
    public void doBusiness() {
        viewList = new ArrayList<>();
        sortList=new ArrayList<>();
        addDisposable(ApiRetrofit.getInstance().getApiService().getCategoryList(buildParam(1, 20)), new BaseObserver<BaseModel<List<CategoryBean>>>() {

            @Override
            public void onSuccess(BaseModel<List<CategoryBean>> o) {
                if (o.getData() != null && o.getData().size() > 0) {
                    int size = o.getData().size();
                    for (int i = 0; i < size; i++) {
                        sortList.add(o.getData().get(i).getSort());
                        CategoryBean bean = o.getData().get(i);
                        CategoryItemView categoryItemView=addCategoryLayout(bean.getName(),-1);
                        categoryItemView.setCategoryBean(bean);
                        categoryItemView.setSortList(sortList);
                        int childSize = bean.getChildren() == null ? 0 : bean.getChildren().size();
                        List<Integer> childSortList=new ArrayList<>();
                        for (int j = 0; j < childSize; j++) {
                            CategoryBean bean1 = o.getData().get(i).getChildren().get(j);
                            childSortList.add(bean1.getSort());
                            categoryItemView.addCategoryChildLayout(bean1,-1);
                        }
                        categoryItemView.setChildSortList(childSortList);
                    }
                }else {
                    addEmptyView();
                }
            }
        });
    }

    private void addEmptyView(){
        TextView textView=new TextView(CategoryManagerActivity.this);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
        textView.setText("暂无品类，现在添加一个品类吧");
        textView.setTextColor(Color.parseColor("#cdcdd7"));
        textView.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams(-2,ScreenUtil.getDisplayHeight()-ScreenUtil.dip2px(44)-ScreenUtil.getStatusBarHeight(this));
        ll.gravity=Gravity.CENTER;
        contentLl.addView(textView,ll);
        emptyView=textView;
    }

    private CategoryItemView addCategoryLayout(String name,int sort) {
        if (emptyView!=null){
            contentLl.removeView(emptyView);
            emptyView=null;
        }
        CategoryItemView itemView = new CategoryItemView(this);
        itemView.setName(name);
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(-1, -2);
        if (contentLl.getChildCount() > 0) {
            ll.setMargins(ScreenUtil.dip2px(20), 0, ScreenUtil.dip2px(20), ScreenUtil.dip2px(20));
            int childCount=contentLl.getChildCount();
            if (sort<=childCount&&sort>=0){
                contentLl.addView(itemView,sort,ll);
            }else {
                contentLl.addView(itemView, ll);
            }
        } else {
            ll.setMargins(ScreenUtil.dip2px(20), ScreenUtil.dip2px(20), ScreenUtil.dip2px(20), ScreenUtil.dip2px(20));
            contentLl.addView(itemView, ll);
        }
        viewList.add(itemView);
        return itemView;
    }

    private ArrayMap<String, String> buildParam(int p, int ps) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("p", String.valueOf(p));
        map.put("ps", String.valueOf(ps));
        return map;
    }

    @OnClick({R.id.add_iv, R.id.edit_iv,R.id.iv_back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.add_iv:
                showDialog();
                break;
            case R.id.edit_iv:
                isEdit = !isEdit;
                if (isEdit) {
                    editIv.setText("完成");
                } else {
                    editIv.setText("管理");
                }
                for (CategoryItemView itemView :
                        viewList) {
                    itemView.isEdit(isEdit);
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void showDialog() {
        LinearLayout linearLayout=new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        final EditText editText = new EditText(this);
        linearLayout.addView(editText);
        final EditText sortEditText=new EditText(this);
        sortEditText.setHint("排序号");
        sortEditText.setText(String.valueOf(getMaxSort()));
        sortEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams(-1,-2);
        ll.setMargins(0,ScreenUtil.dip2px(10),0,0);
        linearLayout.addView(sortEditText,ll);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(this);
        inputDialog.setTitle("请输入名称").setView(linearLayout);
        inputDialog.setPositiveButton("确定", (dialog, which) -> {
            if (StringUtil.isEmpty(editText.getText().toString())||StringUtil.isEmpty(sortEditText.getText().toString())) {
                Util.showToast(CategoryManagerActivity.this, "请输入名称和排序");
                return;
            }
            addCategory(editText.getText().toString(),sortEditText.getText().toString());}
        ).show();
        contentLl.postDelayed(new Runnable() {
            @Override
            public void run() {
                KeyboardUtil.showKeyboard(editText);
            }
        }, 200);
    }

    private void addCategory(String name,String sort) {
        CategoryBean bean = new CategoryBean();
        bean.setName(name);
        bean.setLevel(1);
        int s=-1;
        if (!StringUtil.isEmpty(sort)){
            s=Integer.valueOf(sort);
            s=Math.abs(s);
        }
        if (s<1){
            s=1;
        }
        if (s!=-1){
            bean.setSort(s);
        }
        addDisposable(ApiRetrofit.getInstance().getApiService().categoryInsert(bean), new BaseObserver<BaseModel<CategoryBean>>() {
            @Override
            public void onSuccess(BaseModel<CategoryBean> o) {
                if (o.getData() != null) {
                    sortList.add(o.getData().getSort());
                    CategoryItemView itemView=addCategoryLayout(name,getSortIndex(o.getData().getSort()));
                    itemView.setCategoryBean(o.getData());
                    itemView.setSortList(sortList);
                }
            }
        });
    }

    private int getSortIndex(int sort){
        Collections.sort(sortList, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.intValue()>o2.intValue()?-1:o1.intValue()<o2.intValue()?1:0;
            }
        });
        return sortList.indexOf(sort);
    }

    private int getMaxSort(){
        int size=sortList.size();
        int max=1;
        for (int i=0;i<size;i++){
            if (max<sortList.get(i)){
                max=sortList.get(i);
            }
        }
        return max;
    }
}
