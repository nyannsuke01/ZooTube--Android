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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ShareCompat;

import com.example.ryuka.zootubeapp.R;
import com.example.ryuka.zootubeapp.common.HttpGetTask;
import com.example.ryuka.zootubeapp.common.ListAdapter;
import com.example.ryuka.zootubeapp.util.YoutubeData;

import org.json.JSONObject;

public class ShareActivity extends AppCompatActivity {
    private ImageButton ib_thumbnail,ib_play;
    private ImageView ib_icon;
    private Button ib_share;
    private EditText et_text;
    private TextView tv_channel_name,tv_channel_info,tv_content;
    private String video_id,channel_id,video_content,video_thumbnail;
    private String channelURL;
    /**
     * 起動時処理
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        //FragmentOneからの引数受け取り
        Intent intent = getIntent();
        //VideoID受け取り
        video_id = intent.getStringExtra("VIDEO_ID");
        //ChannelID受け取り
        channel_id = intent.getStringExtra("CHANNEL_ID");
        //Video詳細受け取り
        video_content = intent.getStringExtra("VIDEO_CONTENT");
        //Videoサムネイル受け取り
        video_thumbnail = intent.getStringExtra("VIDEO_THUMBNAIL");
        //ChannelIDが取得できた場合、Channel詳細を取得
        if(channel_id.length()>0){
            //ChannelID取得用URL生成
            channelURL = getString(R.string.channel_url)+getString(R.string.api_key);
            //ChannelIDのセット
            channelURL = channelURL.replace("target_id",channel_id);
        }
        //コメント欄
        et_text = findViewById(R.id.et_text);
        //チャンネル名表示欄
        tv_channel_name = findViewById(R.id.tv_channel_name);
        //チャンネル詳細表示欄
        tv_channel_info = findViewById(R.id.tv_channel_info);
        //ビデオ詳細表示欄
        tv_content = findViewById(R.id.tv_content);
        //チャンネルアイコン設定欄
        ib_icon = findViewById(R.id.ib_icon);
        //サムネイル
        ib_thumbnail = findViewById(R.id.ib_thumbnail);
        ib_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //サムネイルタップ時処理
                //WebView画面の呼び出し
                Intent i = new Intent(ShareActivity.this,WebViewActivity.class);
                //WebView画面へ文字列を送る（VIDEOID）
                i.putExtra("VIDEO_ID",video_id);
                //WebView画面の起動
                startActivity(i);
            }
        });
        ib_play = findViewById(R.id.ib_play);
        ib_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //サムネイルタップ時処理
                //WebView画面の呼び出し
                Intent i = new Intent(ShareActivity.this,WebViewActivity.class);
                //WebView画面へ文字列を送る（VIDEOID）
                i.putExtra("VIDEO_ID",video_id);
                //WebView画面の起動
                startActivity(i);
            }
        });
        ib_share = findViewById(R.id.ib_share);
        ib_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //シェア機能の呼び出し
                ShareVideoURL(getString(R.string.youtube_url)+video_id);
            }
        });
        //アクションバーの設定
        setActionBar();
        //チャンネル情報取得処理
        http_request();
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
                Intent intent = new Intent(ShareActivity.this, MainActivity.class);
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
     * シェアボタン押下時機能
     */
    public void ShareVideoURL(String share_url){
        //メッセージのタイトル
        String articleTitle = et_text.getText().toString();
        //送るURL
        String sharedText = share_url;
        // builderの生成　
        ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(this);
        // アプリ一覧が表示されるDialogのタイトルの設定
        builder.setChooserTitle("共有");
        // シェアするタイトル
        builder.setSubject(articleTitle);
        // シェアするテキスト
        builder.setText(sharedText);
        // シェアするタイプ（他にもいっぱいあるよ）
        builder.setType("text/plain");
        // Shareアプリ一覧のDialogの表示
        builder.startChooser();
    }
    /**
     * API問い合わせ
     */
    public void http_request() {
        try {
            //HttpGetTaskクラスの呼び出し
            new HttpGetTask(this) {
                @Override
                protected void onPostExecute(String response) {
                    try {
                        //JSONオブジェクトの生成
                        JSONObject json = new JSONObject(response);
                        //レスポンスの解析
                        for (int i = 0; i < json.getJSONArray("items").length(); i++) {
                            //ひとまず結果が格納されているitemsオブジェクトを展開
                            JSONObject jsonObject1 = json.getJSONArray("items").getJSONObject(i);
                            //チャンネルタイトルの取得
                            String title = jsonObject1.getJSONObject("snippet").getString("title");
                            //チャンネル登録者数の取得
                            String content = "非公開";
                            try{
                                //チャンネル登録者数は非公開の場合もあるので
                                //エラーが出ても良いようにtrycatchで囲んでおく
                                content = jsonObject1.getJSONObject("statistics").getString("subscriberCount");
                            }catch (Exception e){
                            }
                            //サムネイルの取得
                            String thumbnails = jsonObject1.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("medium").getString("url");
                            //デバッグ用に値をコンソール出力
                            System.out.println(title);
                            System.out.println(content);
                            System.out.println(thumbnails.toString());
                            //ページ設定処理
                            setPage(title,content,thumbnails);
                        }
                    } catch (Exception e) {
                        /// 解析エラー時の処理
                        e.printStackTrace();
                        Toast.makeText(ShareActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute(channelURL);//HTTPGetTaskの引数としてkey(犬や猫などの検索ワード）を渡す
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setPage(String channelname,String channelinfo,String channelthmbnails){
        tv_content.setText(video_content);
        tv_channel_name.setText(channelname);
        tv_channel_info.setText(getString(R.string.channelinfo) + channelinfo);
        //サムネイル画像（URL形式）の取得＆セット
        ListAdapter.ImageGetTask task1 = new ListAdapter.ImageGetTask(ib_icon);
        task1.execute(channelthmbnails);
        //サムネイル画像（URL形式）の取得＆セット
        ListAdapter.ImageGetTask task2 = new ListAdapter.ImageGetTask(ib_thumbnail);
        task2.execute(video_thumbnail);
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