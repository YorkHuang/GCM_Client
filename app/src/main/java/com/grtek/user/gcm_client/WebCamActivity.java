package com.grtek.user.gcm_client;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;


/**
 * Created by user on 2016/8/11.
 */
public class WebCamActivity extends AppCompatActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //消除標題列
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //消除狀態列
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_webcam);

        Intent intent = getIntent();
        String webCamUrl = intent.getExtras().getString("Web_Cam_URL");

        mWebView = (WebView) findViewById(R.id.webView_webcam);
        mWebView.loadUrl(webCamUrl);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
    }

}
