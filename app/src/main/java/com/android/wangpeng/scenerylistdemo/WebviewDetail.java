package com.android.wangpeng.scenerylistdemo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebviewDetail extends AppCompatActivity {

    WebView webView_detail;
    android.support.v7.app.ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_webview_detail);
        actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final Activity activity = this;

        webView_detail = (WebView) findViewById(R.id.webviewDetail);

        webView_detail.getSettings().setJavaScriptEnabled(true);
        webView_detail.getSettings().setUseWideViewPort(true);

        webView_detail.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                activity.setProgress(progress * 1000);
            }
        });
        webView_detail.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = getIntent();
        final String url_detail = intent.getStringExtra("detail_url");
        webView_detail.loadUrl(url_detail);

        webView_detail.setWebViewClient(new WebViewClient()
        {
            public boolean shouldOverrideUrlLoading(WebView view,String url)
            {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView_detail.canGoBack()) {
            webView_detail.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        *//*switch (item.getItemId()){
            case android.R.id.home:
                Intent intent =WebviewDetail.this.getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }*//*
        return true;

    }*/

}
