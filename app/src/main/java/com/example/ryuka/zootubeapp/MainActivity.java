package com.example.ryuka.zootubeapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.ryuka.zootubeapp.common.HttpGetTask;
import com.example.ryuka.zootubeapp.common.TabPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    /**
     * 起動時処理
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //タブの設定
        setViews();
        //アクションバーの設定
        setActionBar();
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
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
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
     * 画面生成処理
     */
    private void setViews() {
        //タブページの設定
        FragmentManager manager = getSupportFragmentManager();
        //Fragmentの表示領域を取得
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        //Pagerを設定することで切り替えを可能に
        TabPagerAdapter adapter = new TabPagerAdapter(manager,this);
        viewPager.setAdapter(adapter);
        //選択用のタブレイアウトも呼び出してviewPagerと連動させておく
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }
    /**
     * 画面終了時処理
     */
    @Override
    public void onPause() {
        super.onPause();
        //UX向上のため、画面を切り替える際はふわっと画面を閉じる
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    /**
     * 画面復帰時処理
     */
    @Override
    public void onResume() {
        super.onResume();
        //ダークテーマだと文字色等の調整が大変なのでOFFにする
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //画面を有効活用するためメニューバーを非表示にする
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE |View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
}