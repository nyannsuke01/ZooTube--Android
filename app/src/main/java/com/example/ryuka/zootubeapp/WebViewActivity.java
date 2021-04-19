package com.example.ryuka.zootubeapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.ryuka.zootubeapp.common.TabPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private String video_id;
    /**
     * 起動時処理
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        //FragmentOneからの引数受け取り
        Intent intent = getIntent();
        video_id = intent.getStringExtra("VIDEO_ID");
        //アクションバーの設定処理
        setActionBar();
        //WebViewの設定処理
        initWebview();
        //指定ページへ遷移
        String url = getString(R.string.youtube_url)+video_id;
        webView.loadUrl(url);
    }
    /**
     * アクションバーの設定処理
     * 指定したレイアウトのアクションバーを反映させる
     */
    private void setActionBar(){
        ActionBar actionBar = getSupportActionBar();
        // 通常表示されるタイトルを非表示にする。
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        // 独自のビューを表示するように設定。
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.action_bar, null);
        cView.findViewById(R.id.ib_top).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent のインスタンスを取得する（最初の画面）
                Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
                // ActivitySecond と ActivityThird を消す
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // ActivityFirst を再利用する（onCreate() は呼ばれない）
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                // 遷移先の画面を呼び出す
                startActivity(intent);
            }
        });
        // 独自のビューを指定
        actionBar.setCustomView(cView);
    }
    /**
     * WebViewの設定
     */
    public void initWebview(){
        //WebView初期設定
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebChromeClient(new WebChromeClient());
        //スクロールバーの表示有無（true:表示、false:非表示）
        webView.setScrollContainer(true);
        // JavaScriptを有効化(JavaScript インジェクションに注意)
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        //スクロールバーの形式（SCROLLBARS_INSIDE_OVERLAY＝ページ枠内にバー表示）
        webView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
        //WebViewのカスタマイズ（WebViewClientにて定義）
        webView.setWebViewClient(new WebViewClient() {
            /*
             *ページ遷移時処理
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request){
                //ここの記載がないと、ページ遷移時に空白表示となってしまう
                final String new_url = String.valueOf(request.getUrl());
                //ページの表示
                webView.loadUrl(new_url);
                return true;
            }
            /*
             *初回ページ開始時処理
             * ＵＲＬ表示欄にＵＲＬを表示
             */
            @Override
            public void onPageStarted (WebView view, String url, Bitmap favicon) {
            }
            /*
             *ページ読み込み終了時処理
             */
            @Override
            public void onPageFinished(WebView view, String url){
                //ページ表示後処理
            }
            /*
             *WebView内のエラーキャッチ
             *エラーコード-11(SSLエラー)の場合は独自の再起動処理を実装
             *他のエラーについては実装無し
             */
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                //エラーコード取得
                int error_code = error.getErrorCode();
            }
        });
    }
    /**
     * 画面終了時処理
     */
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    /**
     * 画面復帰時処理
     */
    @Override
    public void onResume() {
        //ダークテーマだと文字色等の調整が大変なのでOFFにする
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onResume();
    }
}