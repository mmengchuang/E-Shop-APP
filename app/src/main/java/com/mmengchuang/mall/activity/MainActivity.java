package com.mmengchuang.mall.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.mmengchuang.mall.R;
import com.mmengchuang.mall.utils.JsInterface;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    private TextView mTextView;
    private JsInterface jsInterface;
    private static final String URL = "http://16260390do.51mypc.cn/app/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (WebView) findViewById(R.id.act_main_webview);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_WIFI_STATE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, 1);
        } else {
            jsInterface = new JsInterface(this);
            showWebView();
        }
    }

    /**
     * 展示WebView
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void showWebView() {
        try {
            mWebView = (WebView) findViewById(R.id.act_main_webview);
            mTextView = (TextView) findViewById(R.id.act_main_tv_title);

            mWebView.requestFocus();

            mWebView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int progress) {
                    mTextView.setText("Loading...");
                    MainActivity.this.setProgress(progress);
                    if (progress >= 80) {
                        mTextView.setText("JsAndroid Test");
                    }
                }
            });
//            webView.setWebViewClient
            mWebView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    return false;
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                    return super.shouldOverrideUrlLoading(view, url);
                    //该方法在Build.VERSION_CODES.LOLLIPOP以前有效，从Build.VERSION_CODES.LOLLIPOP起，建议使用shouldOverrideUrlLoading(WebView, WebResourceRequest)} instead
                    //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
                    //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242

//                    if (url.toString().contains("sina.cn")){
//                        view.loadUrl("http://ask.csdn.net/questions/178242");
//                        return true;
//                    }

                    return false;
                }
            });
            mWebView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK
                            && mWebView.canGoBack()) {
                        mWebView.goBack();
                        return true;
                    }
                    return false;
                }
            });
            // WebView的管理设置状态
            WebSettings webSettings = mWebView.getSettings();
            // 设置android下容许执行js的脚本
            webSettings.setJavaScriptEnabled(true);
            // 编码方式
            webSettings.setDefaultTextEncodingName("utf-8");
            /*
             * 使用这个函数将一个对象绑定到Javascript,因此可以从Javascript访问的方法,
             * Android（Java）与js（HTML）交互的接口函数, jsObj 为桥连对象可随意设值
             */
            mWebView.addJavascriptInterface(jsInterface, "jsObj");
            /*
             * Android（Java）访问js（HTML）端代码是通过loadUrl函数实现的，访问格式如：
             * mWebView.loadUrl("javascript: showFromHtml()");
             */
            mWebView.loadUrl(URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
