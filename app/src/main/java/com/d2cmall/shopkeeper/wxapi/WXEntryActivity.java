package com.d2cmall.shopkeeper.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.common.Constants;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    public static String webUrl;
    public static String channel = "Weixin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_weixin);
        api = WXAPIFactory.createWXAPI(this, Constants.WEIXINAPPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onResp(final BaseResp resp) {
        if (resp instanceof SendAuth.Resp) {
        } else {
            if (resp.errCode == 0) {
                Toast.makeText(this, "发送成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "发送失败", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

}
