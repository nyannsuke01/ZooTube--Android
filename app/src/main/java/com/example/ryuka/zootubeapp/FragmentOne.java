package com.example.ryuka.zootubeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;

import com.example.ryuka.zootubeapp.common.HttpGetTask;
import com.example.ryuka.zootubeapp.common.ListAdapter;
import com.example.ryuka.zootubeapp.util.YoutubeData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/**
 * 各タブ毎のページ内処理をここに定義
 * 全タブ共通で利用
 */
public class FragmentOne extends Fragment {
    //画面UI
    private ListView lv_list;
    //List周り
    private BaseAdapter adapter;
    //共通
    private View view;
    private List<YoutubeData> youtubeData = new ArrayList<>();
    public String  page_key;
    /**
     * 呼び出し時処理
     */
    public static FragmentOne newInstance(String key) {
        //フラグメントの初期化
        FragmentOne frag = new FragmentOne();
        //生成と描画のタイミングが異なるため
        //Bundleに使いたい引数を保持しておく
        Bundle b = new Bundle();
        //使いたい引数（key）の保存
        //猫や犬などタブ毎の検索キーワードが入ってくる
        b.putString("key",key);
        //Bundleにセットする
        frag.setArguments(b);
        return frag;
    }
    /**
     * 起動時処理
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //特に何も処理しない
        super.onCreate(savedInstanceState);
    }
    /**
     * View生成時処理
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //このフラグメントで表示するレイアウトファイルの指定
        view = inflater.inflate(R.layout.tab_one, null);
        //Bundleに保存したキーの受け取り（key＝猫、犬、兎等）
        page_key = getArguments().getString("key");
        //リストビューの初期化
        lv_list = view.findViewById(R.id.lv_list);
        //クォータ節約のため、一度取得したデータがある場合は再度検索しない
        if(youtubeData==null || youtubeData.size()==0){
            //youtubeDataが0件の場合＝未検索のため検索処理を実行
            http_request();
        }else{
            //youtubeDataに1件以上存在する場合
            //検索済みのため、以前のデータでリスト表示を行う
            setList();
        }
        return view;
    }
    /**
     * リストに項目をセット
     */
    public void setList() {
        // ListViewに表示するデータ
        final ArrayList<String> items = new ArrayList<>();
        // ListViewをセット
        adapter = new ListAdapter(this.getContext(), youtubeData, new ListAdapter.ListItemButtonClickListener() {
            //リストのアイテムがクリックされた際の処理を定義
            public void onItemButtonClick(int position, View view) {
                switch (view.getId()){
                    case R.id.ib_thumbnail://サムネイルタップ時処理
                        //WebView画面の呼び出し
                        Intent i = new Intent(getContext(),ShareActivity.class);
                        //WebView画面へ文字列を送る
                        i.putExtra("VIDEO_ID",youtubeData.get(position).getVideoID());
                        i.putExtra("CHANNEL_ID",youtubeData.get(position).getChannelID());
                        i.putExtra("VIDEO_CONTENT",youtubeData.get(position).getContent());
                        i.putExtra("VIDEO_THUMBNAIL",youtubeData.get(position).getThumbnail());
                        //WebView画面の起動
                        startActivity(i);
                        break;
                }

            }
        });
        //設定をリストに反映
        lv_list.setAdapter(adapter);
    }
    /**
     * API問い合わせ
     */
    public void http_request(){
        try {
            //URL文字列作成
            String urls = getString(R.string.base_url) + getString(R.string.api_key);
            urls= urls.replace("target_key",page_key);
            //HttpGetTaskクラスの呼び出し
            new HttpGetTask(getContext()){
                @Override
                protected void onPostExecute(String response) {
                    try {
                        //念のためクリアしておく
                        youtubeData.clear();
                        //JSONオブジェクトの生成
                        JSONObject json = new JSONObject(response);
                        //レスポンスの解析
                        for (int i = 0; i < json.getJSONArray("items").length(); i++) {
                            //ひとまず結果が格納されているitemsオブジェクトを展開
                            JSONObject jsonObject1 = json.getJSONArray("items").getJSONObject(i);
                            //VIDEOIDの取得
                            String video_id = jsonObject1.getJSONObject("id").getString("videoId");
                            //動画タイトルの取得
                            String  title = jsonObject1.getJSONObject("snippet").getString("title");
                            //動画コメントの取得
                            String  content = jsonObject1.getJSONObject("snippet").getString("description");
                            //サムネイルの取得
                            String  thumbnails = jsonObject1.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("medium").getString("url");
                            //サムネイルの取得
                            String  channel = jsonObject1.getJSONObject("snippet").getString("channelId");
                            //youtubeDataリストに追加（ここに追加されたデータがリストに表示）
                            youtubeData.add(new YoutubeData(thumbnails,title,content,video_id,channel));
                            //デバッグ用に値をコンソール出力
                            System.out.println(title);
                            System.out.println(content);
                            System.out.println(thumbnails.toString());
                        }
                        //リスト表示処理呼び出し
                        setList();
                    } catch (Exception e) {
                        /// 解析エラー時の処理
                        e.printStackTrace();
                        Toast.makeText(getContext(),response,Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute(urls);//HTTPGetTaskの引数としてkey(犬や猫などの検索ワード）を渡す
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}