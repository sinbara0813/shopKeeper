package com.d2cmall.shopkeeper.ui.activity;

import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.adapter.TabPagerAdapter;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.ui.fragment.CustomerActionRecordFragment;
import com.d2cmall.shopkeeper.ui.fragment.OrderListFragment;
import com.d2cmall.shopkeeper.ui.view.ExpandViewPager;
import com.d2cmall.shopkeeper.ui.view.RoundedImageView;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LWJ on 2019/2/21.
 * Description : CustomerDetailActivity
 * 客户详情
 */

public class CustomerDetailActivity extends BaseActivity {
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.iv_level)
    ImageView ivLevel;
    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumber;
    @BindView(R.id.head_fl)
    RelativeLayout headFl;
    @BindView(R.id.title_fl)
    FrameLayout titleFl;
    @BindView(R.id.iv_user_head_pic)
    RoundedImageView ivUserHeadPic;
    @BindView(R.id.tabLayout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.view_pager)
    ExpandViewPager viewPager;
    private ArrayList fragments;
    private ArrayList titles;
    private TabPagerAdapter tabPagerAdapter;

    private long id;

    @Override
    public int getLayoutId() {
        return R.layout.activity_custom_detail;
    }

    @Override
    public void doBusiness() {
        id = getIntent().getLongExtra("id", 0);
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
       /* ShadowDrawable.setShadowDrawable(rlTitle, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), 0, 0, 0, ScreenUtil.dip2px(16), 0, 0);*/
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add("浏览记录");
        titles.add("订单记录");
        fragments.add(CustomerActionRecordFragment.newInstance(id));//行为记录
        fragments.add(OrderListFragment.newInstance(id));//订单记录
        tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(tabPagerAdapter);
        tabLayout.setViewPager(viewPager);
        tabPagerAdapter.notifyDataSetChanged();
        ivLevel.setVisibility(View.GONE);
        String nickName = getIntent().getStringExtra("nickName");
        String phone = getIntent().getStringExtra("phone");
        String avatar = getIntent().getStringExtra("avatar");
        ImageLoader.displayRoundImage(this, avatar, ivUserHeadPic, R.mipmap.icon_default_avatar);
        tvPhoneNumber.setText(getString(R.string.lable_phone_number, phone));
        if (StringUtil.isEmpty(nickName)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("昵称");
            String s = String.valueOf(id);
            String substring = s.substring(s.length() - 4, s.length());
            stringBuilder.append(substring);
            tvUserName.setText(stringBuilder.toString());
        } else {
            tvUserName.setText(nickName);
        }
    }

    @OnClick(R.id.iv_back)
    public void click(View view) {
        finish();
    }
}
