package com.d2cmall.shopkeeper.ui.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.model.CategoryBean;
import com.d2cmall.shopkeeper.ui.activity.ProductListActivity;
import com.d2cmall.shopkeeper.utils.KeyboardUtil;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/2/22.
 * 邮箱:hrb940258169@163.com
 */

public class CategoryItemChildView extends RelativeLayout {

    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.count_tv)
    TextView countTv;
    @BindView(R.id.delete_iv)
    ImageView deleteIv;
    @BindView(R.id.edit_iv)
    ImageView editIv;

    public boolean isEdit;
    private CategoryBean categoryBean;
    private BaseActivity baseActivity;
    private List<Integer> sortList;

    public CategoryItemChildView(Context context) {
        this(context, null);
        if (context instanceof BaseActivity) {
            baseActivity = (BaseActivity) context;
        }
    }

    public CategoryItemChildView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_category_item_child, this, true);
        ButterKnife.bind(this, this);
    }

    public void isEdit(boolean isEdit) {
        this.isEdit = isEdit;
        if (isEdit) {
            deleteIv.setVisibility(VISIBLE);
            editIv.setVisibility(VISIBLE);
            countTv.setVisibility(GONE);
        } else {
            deleteIv.setVisibility(GONE);
            editIv.setVisibility(GONE);
            countTv.setVisibility(VISIBLE);
        }
    }

    @OnClick({R.id.edit_iv, R.id.delete_iv,R.id.count_tv})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.edit_iv:
                showEditDialog();
                break;
            case R.id.delete_iv:
                deleteCategory();
                break;
            case R.id.count_tv:
                Intent intent=new Intent(baseActivity, ProductListActivity.class);
                intent.putExtra("categoryIds",String.valueOf(categoryBean.getId()));
                intent.putExtra("name",nameTv.getText().toString());
                baseActivity.startActivity(intent);
                break;
        }
    }

    private void deleteCategory() {
        if (categoryBean == null) return;
        if (baseActivity == null) return;
        new AlertDialog.Builder(getContext())
                .setMessage("确定删除该品类吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        baseActivity.addDisposable(ApiRetrofit.getInstance().getApiService().categoryDelete(categoryBean.getId()), new BaseObserver() {
                            @Override
                            public void onSuccess(Object o) {
                                if (sortList != null) {
                                    sortList.remove((Integer) categoryBean.getSort());
                                }
                                categoryBean = null;
                                if (getParent() != null) {
                                    ((ViewGroup) getParent()).removeView(CategoryItemChildView.this);
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("取消", null)
                .show();

    }

    private void showEditDialog() {
        LinearLayout linearLayout=new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        final EditText editText = new EditText(getContext());
        linearLayout.addView(editText);
        final EditText sortEditText=new EditText(getContext());
        sortEditText.setHint("排序号");
        sortEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams(-1,-2);
        ll.setMargins(0, ScreenUtil.dip2px(10),0,0);
        linearLayout.addView(sortEditText,ll);
        editText.setText(nameTv.getText().toString());
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(getContext());
        inputDialog.setTitle("请输入名称").setView(linearLayout);
        inputDialog.setPositiveButton("确定", (dialog, which) ->{
            if (StringUtil.isEmpty(editText.getText().toString())||StringUtil.isEmpty(sortEditText.getText().toString())) {
                Util.showToast(baseActivity, "请输入名称和排序");
                return;
            }
            updateCategoryName(editText.getText().toString(),sortEditText.getText().toString());}
        ).show();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                KeyboardUtil.showKeyboard(editText);
            }
        }, 200);
    }

    private void updateCategoryName(String name,String sort) {
        if (categoryBean == null) return;
        if (baseActivity == null) return;
        int oldSort=categoryBean.getSort();
        int s=-1;
        if (!StringUtil.isEmpty(sort)){
            s=Integer.valueOf(sort);
            s=Math.abs(s);
        }
        ViewGroup parentView=(ViewGroup)getParent();
        if (s<1){
            s=1;
        }
        if (s!=-1){
            categoryBean.setSort(s);
        }
        categoryBean.setName(name);
        categoryBean.setShopId(Session.getInstance().getUser().getShopId());
        baseActivity.addDisposable(ApiRetrofit.getInstance().getApiService().categoryUpdate(categoryBean), new BaseObserver<BaseModel<CategoryBean>>() {

            @Override
            public void onSuccess(BaseModel<CategoryBean> o) {
                categoryBean = o.getData();
                nameTv.setText(name);
                if (sortList!=null){
                    sortList.remove((Integer)oldSort);
                    sortList.add(o.getData().getSort());
                }
                parentView.removeView(CategoryItemChildView.this);
                parentView.addView(CategoryItemChildView.this,getSortIndex(o.getData().getSort()));
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

    public void setName(String name) {
        nameTv.setText(name);
    }

    public String getName() {
        return nameTv.getText().toString();
    }

    public void setCategoryBean(CategoryBean categoryBean) {
        this.categoryBean = categoryBean;
    }

    public void setSortList(List<Integer> sortList) {
        this.sortList = sortList;
    }
}
