package com.d2cmall.shopkeeper.ui.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.ui.view.loopview.LoopView;
import com.d2cmall.shopkeeper.ui.view.loopview.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:Created by sinbara on 2019/6/21.
 * 邮箱:hrb940258169@163.com
 */
public class StatAddressPop implements TransparentPop.InvokeListener {

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
    @BindView(R.id.start_tv)
    TextView startTv;
    @BindView(R.id.end_tv)
    TextView endTv;
    private TransparentPop mPop;
    private Context mContext;
    private View bottomView;
    private TextView currentSelectTv;
    public int currentYear;
    public int currentMonth;
    public int currentDay;
    ArrayList<String> months=new ArrayList<>();
    ArrayList<String> days=new ArrayList<>();
    ArrayList<String> years=new ArrayList<>();
    String[] allDays;
    public int lastDayIndex;

    public StatAddressPop(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        bottomView = LayoutInflater.from(mContext).inflate(R.layout.layout_stat_date_pop, new LinearLayout(mContext), false);
        ButterKnife.bind(this, bottomView);
        mPop = new TransparentPop(mContext, this);
        Animation inAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_up);
        Animation outAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_out_down);
        mPop.setInAnimation(inAnimation);
        mPop.setOutAnimation(outAnimation);
        mPop.dismissFromOut();
        initListener();
        initData();
    }

    private void initData(){
        months = new ArrayList<>();
        days = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH) + 1;
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        allDays = mContext.getResources().getStringArray(R.array.days);
        for (int i=0;i<10;i++){
            years.add((currentYear-9+i)+"年");
        }
        for (int i=0;i<12;i++){
            months.add((1+i)+"月");
        }
        getDay(currentMonth);
        idProvince.setItems(years);
        idProvince.setCurrentPosition(9);
        idCity.setItems(months);
        idCity.setCurrentPosition(currentMonth-1);
        idDistrict.setItems(days);
    }

    private void getDay(int month) {
        int count = 0;
        if (month == 2) {
            if ((currentYear % 4 == 0 && currentYear % 100 != 0) || (currentYear % 400 == 0)) {
                count = 29;
            } else {
                count = 28;
            }
        } else {
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    count = 31;
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    count = 30;
                    break;
            }
        }
        days.clear();
        for (int i = 0; i < count; i++) {
            days.add(allDays[i] + "日");
        }
    }

    private void initListener() {
        idProvince.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                currentYear = Integer.valueOf(years.get(index).substring(0, years.get(index).length() - 1));
                getDay(currentMonth);
                idDistrict.setItems(days);
                if (lastDayIndex + 1 >= days.size()) {
                    lastDayIndex = days.size() - 1;
                }
                if (lastDayIndex != 0) {
                    currentDay = Integer.valueOf(days.get(lastDayIndex).substring(0, days.get(lastDayIndex).length() - 1));
                }
                idDistrict.setCurrentPosition(lastDayIndex);
                if (currentSelectTv!=null){
                    currentSelectTv.setText(currentYear+"-"+currentMonth+"-"+currentDay);
                }
            }
        });

        idCity.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                currentMonth = Integer.valueOf(months.get(index).substring(0, months.get(index).length() - 1));
                getDay(currentMonth);
                idDistrict.setItems(days);
                if (lastDayIndex + 1 >= days.size()) {
                    lastDayIndex = days.size() - 1;
                }
                if (lastDayIndex != 0) {
                    currentDay = Integer.valueOf(days.get(lastDayIndex).substring(0, days.get(lastDayIndex).length() - 1));
                }
                idDistrict.setCurrentPosition(lastDayIndex);
                if (currentSelectTv!=null){
                    currentSelectTv.setText(currentYear+"-"+currentMonth+"-"+currentDay);
                }
            }
        });

        idDistrict.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                lastDayIndex = index;
                currentDay = Integer.valueOf(days.get(index).substring(0, days.get(index).length() - 1));
                if (currentSelectTv!=null){
                    currentSelectTv.setText(currentYear+"-"+currentMonth+"-"+currentDay);
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
                String start=startTv.getText().toString().trim();
                String end=endTv.getText().toString().trim();
                if (callBack != null&&start.contains("-")&&end.contains("-")) {
                    callBack.callback(start,end);
                }
            }
        });

        startTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSelectTv=startTv;
                startTv.setTextColor(ContextCompat.getColor(mContext,R.color.normal_blue));
                endTv.setTextColor(ContextCompat.getColor(mContext,R.color.color_black4));
            }
        });

        endTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSelectTv=endTv;
                endTv.setTextColor(ContextCompat.getColor(mContext,R.color.normal_blue));
                startTv.setTextColor(ContextCompat.getColor(mContext,R.color.color_black4));
            }
        });
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
        v.addView(bottomView);
    }

    @Override
    public void releaseView(LinearLayout v) {

    }

    public interface CallBack {
        void callback(String startTime,String endTime);
    }

    private CallBack callBack;

    public void setCallBack(CallBack callback) {
        this.callBack = callback;
    }
}
