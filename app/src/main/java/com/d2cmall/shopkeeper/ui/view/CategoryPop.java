package com.d2cmall.shopkeeper.ui.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.common.City;
import com.d2cmall.shopkeeper.common.District;
import com.d2cmall.shopkeeper.common.Province;
import com.d2cmall.shopkeeper.model.CategoryBean;
import com.d2cmall.shopkeeper.ui.view.loopview.LoopView;
import com.d2cmall.shopkeeper.ui.view.loopview.OnItemSelectedListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryPop implements TransparentPop.InvokeListener {

    @BindView(R.id.pop_cancel)
    TextView popCancel;
    @BindView(R.id.pop_sure)
    TextView popSure;
    @BindView(R.id.id_province)
    LoopView idProvince;
    @BindView(R.id.id_city)
    LoopView idCity;
    @BindView(R.id.id_district)
    LoopView idDistrict;
    private TransparentPop mPop;
    private Context mContext;
    private View bottomView;
    private List<CategoryBean> provinces;
    private int mProvinceIndex=-1;
    private int mCityIndex=-1;
    private int mDistrictIndex=-1;
    private long currentId;
    private boolean isCategory;

    public CategoryPop(Context context, List<CategoryBean> data,boolean isCategory) {
        this.mContext = context;
        this.provinces=data;
        this.isCategory=isCategory;
        init();
        initData();
    }

    private void init() {
        bottomView = LayoutInflater.from(mContext).inflate(R.layout.layout_select_category, new LinearLayout(mContext), false);
        ButterKnife.bind(this, bottomView);
        mPop = new TransparentPop(mContext, this);
        Animation inAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_up);
        Animation outAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_out_down);
        mPop.setInAnimation(inAnimation);
        mPop.setOutAnimation(outAnimation);
        mPop.dismissFromOut();
        initListener();
    }

    private void initData(){
        int size=provinces.size();
        if (size==0)return;
        currentId=provinces.get(0).getId();
        ArrayList<String> provinceList = new ArrayList<>();
        for (int i = 0; i <size; i++) {
            CategoryBean Province = provinces.get(i);
            provinceList.add(Province.getName());
            if (i == 0) {
                int citySize = Province.getChildren().size();
                if (citySize > 0) {
                    ArrayList<String> cityList = new ArrayList<>();
                    for (int j = 0; j < citySize; j++) {
                        CategoryBean City = Province.getChildren().get(j);
                        cityList.add(City.getName());
                        if (j == 0) {
                            int districtSize = City.getChildren().size();
                            if (districtSize > 0) {
                                ArrayList<String> districtList = new ArrayList<>();
                                for (int k = 0; k < districtSize; k++) {
                                    CategoryBean District = City.getChildren().get(k);
                                    districtList.add(District.getName());
                                }
                                mDistrictIndex=0;
                                currentId=provinces.get(0).getChildren().get(0).getChildren().get(0).getId();
                                idDistrict.setItems(districtList);
                            }else {
                                if (!isCategory){
                                    ArrayList<String> districtList = new ArrayList<>();
                                    districtList.add(" ");
                                    idDistrict.setItems(districtList);
                                }
                            }
                        }
                    }
                    mCityIndex=0;
                    currentId=provinces.get(i).getChildren().get(0).getId();
                    idCity.setItems(cityList);
                }else {
                    ArrayList<String> cityList = new ArrayList<>();
                    cityList.add(" ");
                    idCity.setItems(cityList);
                }
            }
        }
        mProvinceIndex=0;
        idProvince.setItems(provinceList);
        bottomView.requestLayout();
    }

    private void initListener() {
        idProvince.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mProvinceIndex = index;
                if (mProvinceIndex < 0) {
                    mProvinceIndex = 0;
                }
                currentId=provinces.get(mProvinceIndex).getId();
                updateCities();
            }
        });

        idCity.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mCityIndex = index;
                if (mCityIndex < 0) {
                    mCityIndex = 0;
                }
                if (provinces.get(mProvinceIndex).getChildren().size()>0){
                    currentId=provinces.get(mProvinceIndex).getChildren().get(mCityIndex).getId();
                }
                if (!isCategory){
                    updateAreas();
                }
            }
        });

        idDistrict.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mDistrictIndex = index;
                if (mDistrictIndex < 0) {
                    mDistrictIndex = 0;
                }
                if (provinces.get(mProvinceIndex).getChildren().size()>0&&provinces.get(mProvinceIndex).getChildren().get(mCityIndex).getChildren().size()>0){
                    currentId=provinces.get(mProvinceIndex).getChildren().get(mCityIndex).getChildren().get(mDistrictIndex).getId();
                }
            }
        });

        popCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        popSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                String provinceName = provinces.get(mProvinceIndex).getName();
                String cityName="";
                if (provinces.get(mProvinceIndex).getChildren().size()>0){
                    cityName = provinces.get(mProvinceIndex).getChildren().get(mCityIndex).getName();
                }
                String districtName="";
                if (mCityIndex>=0&&provinces.get(mProvinceIndex).getChildren().size()>0&&provinces.get(mProvinceIndex).getChildren().get(mCityIndex).getChildren().size()>0){
                    districtName = provinces.get(mProvinceIndex).getChildren().get(mCityIndex).getChildren().get(mDistrictIndex).getName();
                }
                if (callBack != null) {
                    callBack.callback(currentId,provinceName, cityName,districtName);
                }
            }
        });
    }


    public void refreshDataByProvinceCity(String province, String city) {//根据定位的省市，刷新数据
        if (provinces == null || provinces.size() == 0) {
            return;
        }
        int provinceSize = provinces.size();
        for (int i = 0; i < provinceSize; i++) {
            CategoryBean provinceBean = provinces.get(i);
            if (provinceBean.getName().equals(province)) {
                mProvinceIndex = i;
                int citySize = provinceBean.getChildren().size();
                ArrayList<String> cityList = new ArrayList<>();
                for (int j = 0; j < citySize; j++) {
                    CategoryBean cityBean = provinceBean.getChildren().get(j);
                    cityList.add(cityBean.getName());
                    if (cityBean.getName().equals(city)) {
                        mCityIndex = j;
                        updateAreas();
                    }
                }
                idCity.setItems(cityList);
                idCity.setCurrentPosition(mCityIndex);
                idProvince.setCurrentPosition(mProvinceIndex);
                break;
            }
        }
    }

    public void show(View parent) {
        mPop.show(parent, true);
    }

    public void dismiss() {
        if (mPop != null) {
            mPop.dismiss(true);
        }
    }

    @Override
    public void invokeView(LinearLayout v) {
        v.setGravity(Gravity.BOTTOM);
        if (!isCategory){
            idDistrict.setVisibility(View.GONE);
        }
        v.addView(bottomView);
    }

    @Override
    public void releaseView(LinearLayout v) {

    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        if (provinces.get(mProvinceIndex).getChildren().size()==0)return;
        CategoryBean cityBean = provinces.get(mProvinceIndex).getChildren().get(mCityIndex);
        int districtSize = cityBean.getChildren().size();
        if (districtSize > 0) {
            ArrayList<String> districtList = new ArrayList<>();
            for (int i = 0; i < districtSize; i++) {
                districtList.add(cityBean.getChildren().get(i).getName());
            }
            idDistrict.setItems(districtList);
            int selectItem = idDistrict.getSelectedItem();
            if (selectItem + 1 >= districtList.size()) {
                selectItem = 0;
            }
            idDistrict.setCurrentPosition(selectItem);
            mDistrictIndex = selectItem;
            currentId=cityBean.getChildren().get(mDistrictIndex).getId();
        }else {
            mDistrictIndex=-1;
            ArrayList<String> districtList = new ArrayList<>();
            districtList.add(" ");
            idDistrict.setItems(districtList);
        }
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        CategoryBean provinceBean = provinces.get(mProvinceIndex);
        int citySize = provinceBean.getChildren().size();
        if (citySize > 0) {
            ArrayList<String> cityList = new ArrayList<>();
            for (int i = 0; i < citySize; i++) {
                cityList.add(provinceBean.getChildren().get(i).getName());
            }
            idCity.setItems(cityList);
            int selectItem = idCity.getSelectedItem();
            if (selectItem + 1 >= cityList.size()) {
                selectItem = 0;
            }
            idCity.setCurrentPosition(selectItem);
            mCityIndex = selectItem;
            currentId=provinceBean.getChildren().get(mCityIndex).getId();
            if (!isCategory){
                updateAreas();
            }
        }else {
            mCityIndex=-1;
            ArrayList<String> cityList = new ArrayList<>();
            cityList.add(" ");
            idCity.setItems(cityList);
        }
    }

    public interface CallBack {
        void callback(long id,String... selectName);
    }

    private CallBack callBack;

    public void setCallBack(CallBack callback) {
        this.callBack = callback;
    }
}
