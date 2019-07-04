package com.d2cmall.shopkeeper.ui.activity;

import android.app.slice.Slice;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.ui.view.SharePop;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.ui.view.web.MyWebView;
import com.d2cmall.shopkeeper.ui.view.web.WebHandler;
import com.d2cmall.shopkeeper.utils.App;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.r0adkll.slidr.Slidr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LWJ on 2019/3/18.
 * Description : WebActivity
 * 网页
 */

public class WebActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;
    @BindView(R.id.img_finish)
    ImageView imgFinish;
    private String url;
    private BridgeWebView webView;
    private WebHandler webHandler;
    private String title;
    private boolean isShareShow;
    private String productImage;
    private long productId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public void doBusiness() {
        init();
    }

    private void init() {
        Slidr.attach(this);
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        webView = new MyWebView(this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        frameLayout.addView(webView);
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        productId = getIntent().getLongExtra("productId", 0);
        productImage = getIntent().getStringExtra("productImage");
        isShareShow = getIntent().getBooleanExtra("isShareShow", false);
        if (isShareShow) {
            tvRight.setVisibility(View.VISIBLE);
            tvRight.setText("分享");
        }
        if (!StringUtil.isEmpty(title)) {
            tvName.setText(title);
        }
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        String ua = webView.getSettings().getUserAgentString();
        /*webView.getSettings().setUserAgentString(ua + String.format(Constants.USER_AGENT_URL,
                "Android-"+Util.getPageVersionName(this), Util.getNetType(this)));*/
        webView.getSettings().setUserAgentString(ua + "shopkeeper/Android/" + App.getPageVersionName(this));//d2cmall/Android/3.3.0
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setWebViewClient(new MyWebClient(webView));
        webHandler = new WebHandler(this);

        webView.registerHandler("d2cinit", webHandler);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        loadUrl();
    }

    private class MyWebClient extends BridgeWebViewClient {

        public MyWebClient(BridgeWebView webView) {
            super(webView);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Matcher mat21 = Pattern.compile("tel:(\\d+-?\\d+)").matcher(url);
            if (mat21.find()) {
                Intent call = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + mat21.group(0));
                call.setData(data);
                startActivity(call);
                return true;
            }
            if ( url.startsWith("weixin://")
                    ||  url.startsWith("alipays://")
                    ||  url.startsWith("mqqapi://")  )
            {
                //打开本地App进行支付
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Matcher matcher = Pattern.compile("shop.d2c.cn/(\\d+)").matcher(url);
            Matcher matcher1 = Pattern.compile("fune.store/(\\d+)").matcher(url);
            if (matcher.find()||matcher1.find()){
                imgFinish.setVisibility(View.VISIBLE);
            }else {
                imgFinish.setVisibility(webView.canGoBack()?View.VISIBLE:View.GONE);
            }
        }
    }

    private void loadUrl() {
        webView.loadUrl(url);
    }


    @OnClick({R.id.iv_back, R.id.tv_right,R.id.img_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (webView.canGoBack()){
                    webView.goBack();
                }else {
                    finish();
                }
                break;
            case R.id.tv_right:
                share();
                break;
            case R.id.img_finish:
                finish();
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void share() {
        if (productId == 0) return;
        if (Util.wxInstalled()) {
            SharePop pop = new SharePop(this);
            pop.setTitle(title);
            pop.setDescription("发现了一个好东西，赶紧看看吧！");
            pop.setImage(StringUtil.getD2cPicUrl(productImage, 100, 100), false);
            pop.setWebUrl(Constants.SHARE_URL + "product/" + productId);
            pop.show(tvRight);
        } else {
            Util.showToast(this, "请先安装微信");
        }
    }
}
