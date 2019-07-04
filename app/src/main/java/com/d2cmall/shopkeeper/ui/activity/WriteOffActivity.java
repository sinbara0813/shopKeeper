package com.d2cmall.shopkeeper.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.result.ResultParser;

import java.util.Collection;

import butterknife.BindView;
import butterknife.OnClick;
import google.zxing.client.AmbientLightManager;
import google.zxing.client.BeepManager;
import google.zxing.client.InactivityTimer;
import google.zxing.client.IntentSource;
import google.zxing.client.camera.CameraManager;
import google.zxing.client.decode.CaptureActivityHandler;
import google.zxing.client.view.ViewfinderView;

/**
 * Created by LWJ on 2019/3/4.
 * Description : WriteOffActivity
 * 核销
 */

public class WriteOffActivity extends BaseActivity implements SurfaceHolder.Callback {
    @BindView(R.id.viewfinder_view)
    ViewfinderView viewfinderView;
    @BindView(R.id.preview_view)
    SurfaceView previewView;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_write_off_record)
    TextView tvWriteOffRecord;
    @BindView(R.id.et_code)
    TextView etCode;

    private boolean hasSurface;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private AmbientLightManager ambientLightManager;
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private Result lastResult;
    private Collection<BarcodeFormat> decodeFormats;
    private String characterSet;
    private Result savedResultToShow;
    private IntentSource source;
    private MultiFormatReader multiFormatReader;
    private boolean safeToTakePicture;
    private SurfaceHolder surfaceHolder;
    private boolean isFromOrder=false;

    @Override
    public int getLayoutId() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        return R.layout.activity_write_off;
    }

    @Override
    public void doBusiness() {
        init();
        initListener();
    }
    private void init() {
        Sofia.with(this).statusBarLightFont().statusBarBackground(Color.parseColor("#80000000"));
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        surfaceHolder = previewView.getHolder();
        ambientLightManager = new AmbientLightManager(this);
        isFromOrder = getIntent().getBooleanExtra("isFromOrder",false);
    }

    private void initListener() {
        etCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if(!StringUtil.isEmpty(etCode.getText().toString().trim()) ){
                        if(!isDigit(etCode.getText().toString().trim())){
                            Util.showToast(WriteOffActivity.this,"请输入正确的优惠券ID");
                            return true;
                        }
                        showTip(etCode.getText().toString().trim());
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public boolean isDigit(String strNum) {
        return strNum.matches("[0-9]{1,}");
    }

    private void showTip(String result) {
        Long couponId = Long.valueOf(result);
        AlertDialog.Builder builder = new AlertDialog.Builder(WriteOffActivity.this);
                builder.setMessage("确定核销该优惠券?");
                builder.setCancelable(true);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        writeOffCoupon(couponId);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }



    private void writeOffCoupon(Long couponId) {
        addDisposable(ApiRetrofit.getInstance().getApiService().cancleCoupon(couponId), new BaseObserver() {
            @Override
            public void onSuccess(Object o) {
                Util.showToast(WriteOffActivity.this,"已核销");
                finish();
            }
        });
    }







    @Override
    protected void onResume() {
        super.onResume();
        showCamera();
    }

    private void showCamera(){
        cameraManager = new CameraManager(getApplication());
        viewfinderView.setCameraManager(cameraManager);
        handler = null;
        lastResult = null;
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view); // 预览
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            surfaceHolder.addCallback(this);
        }
        beepManager.updatePrefs();
        ambientLightManager.start(cameraManager);
        inactivityTimer.onResume();
        source = IntentSource.NONE;
        decodeFormats = null;
        characterSet = null;
    }

    @Override
    protected void onPause() {
        if (cameraManager!=null){
            if (handler != null) {
                handler.quitSynchronously();
                handler = null;
            }
            inactivityTimer.onPause();
            ambientLightManager.stop();
            beepManager.close();
            cameraManager.closeDriver();
            if (!hasSurface) {
                SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
                SurfaceHolder surfaceHolder = surfaceView.getHolder();
                surfaceHolder.removeCallback(this);
            }
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        viewfinderView.recycleLineDrawable();
        super.onDestroy();
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, Constants.RequestCode.REQUEST_PERMISSION);
        }else {
            openCamera(holder);
        }
    }

    private void openCamera(SurfaceHolder holder){
        if (!hasSurface) {
            hasSurface = true;
            try {
                initCamera(holder);
            } catch (Exception e) {
                Util.showToast(WriteOffActivity.this, R.string.msg_camera_permission_error);
                finish();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public void handleDecode(Result rawResult, Bitmap bitmap) {
        inactivityTimer.onActivity();
        lastResult = rawResult;
        beepManager.playBeepSoundAndVibrate();
        final String result = ResultParser.parseResult(rawResult).toString();
        if(!StringUtil.isEmpty(result)){
            if(isFromOrder){
                Intent intent = new Intent();
                intent.putExtra("deliveryCode",result);
                setResult(RESULT_OK,intent);
                finish();
            }else{
                if(!isDigit(result)){
                    Util.showToast(WriteOffActivity.this,result);
                    return;
                }
                showTip(result);
            }

            return;
        }
        resetCapture();
    }

    public void resetCapture() {
        if ((source == IntentSource.NONE) && lastResult != null) {
            restartPreviewAfterDelay(0L);
        }
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
        resetStatusView();
    }

    private void resetStatusView() {
        viewfinderView.setVisibility(View.VISIBLE);
        lastResult = null;
    }


    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }


    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            return;
        }
        if (cameraManager.isOpen()) {
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            if (handler == null) {
                handler = new CaptureActivityHandler(this, decodeFormats,
                        null, characterSet, cameraManager);
                safeToTakePicture = true;
            }
            decodeOrStoreSavedBitmap(null, null);
        } catch (Exception ex) {
            Util.showToast(WriteOffActivity.this, R.string.msg_camera_framework_bug);
            finish();
        }
    }

    private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
        if (handler == null) {
            savedResultToShow = result;
        } else {
            if (result != null) {
                savedResultToShow = result;
            }
            if (savedResultToShow != null) {
                Message message = Message.obtain(handler,
                        R.id.decode_succeeded, savedResultToShow);
                handler.sendMessage(message);
            }
            savedResultToShow = null;
        }
    }









    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (!checkGrantResults(grantResults)){
            finish();
        }else {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view); // 预览
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            openCamera(surfaceHolder);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean checkGrantResults(int[] grantResults) {
        boolean result=true;
        int size=grantResults.length;
        for (int i=0;i<size;i++){
            if (grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return result;
    }


    @OnClick({R.id.iv_back, R.id.tv_write_off_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_write_off_record:
                //核销记录
//                startActivity(new Intent(this,WriteOffRecordActivity.class));
                break;
        }
    }
}
