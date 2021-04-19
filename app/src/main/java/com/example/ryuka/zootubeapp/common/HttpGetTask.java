package com.example.ryuka.zootubeapp.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.sip.SipAudioCall;
import android.os.AsyncTask;

import com.example.ryuka.zootubeapp.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

//GETロジック
public class HttpGetTask extends AsyncTask<String, Void, String> {
    private SharedPreferences pref;
    private Context context;
    private SipAudioCall.Listener listener;
    public HttpGetTask(Context context ){
        this.context = context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    /**
     * doInBackground(非同期処理）が終わったタイミングで処理される
     * メインスレッドに値を返却する
     */
    @Override
    protected void onPostExecute(String result){
        //メインスレッドで実行される処理
        super.onPostExecute(result);
    }
    /**
     * 非同期処理のメインロジック
     * HTTP GETリクエストを送る
     */
    @Override
    public String doInBackground(String... id) {
        String json_data = "";
        try {
            //証明書の認証スキップ有無フラグ(TRUE:スキップしない、FALSE：スキップ）
            boolean ssl_flg = true;
            //TRUE:HTTPS接続、FALSE:HTTP接続（デバッグ用で用意、Android7以降は基本的にHTTPS接続)
            boolean https_flg = true;
            if(https_flg){
                String url2 = id[0];
                System.out.println(url2.toString());
                //作成した文字列をURLに変換
                final URL url = new URL(url2);
                HttpsURLConnection urlconn;
                //通常
                urlconn = (HttpsURLConnection) url.openConnection();
                //接続種別の設定（"GET","POST","PUT"など)
                urlconn.setRequestMethod("GET");
                //接続
                urlconn.connect();
                // HTTPレスポンスコード
                //200番台：成功
                //300番台：リダイレクト（サーバーURLが変更されたなど）
                //400番台：クライアントエラー（接続端末にアクセス件が無い、URL誤りなど）
                //500番台：サーバーエラー（ファイアウォールで拒否されている、想定していないリクエストなど）
                final int status = urlconn.getResponseCode();
                //ステータスが200(成功)の場合のみ返却された値の解析を行う
                if (status == HttpsURLConnection.HTTP_OK) {
                    // テキストを取得
                    String line = null;
                    //データの読み取り
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlconn.getInputStream(), "utf8"));
                    StringBuilder sb = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();
                    urlconn.disconnect();
                    System.out.println(sb.toString());
                    json_data = sb.toString();
                }
                else{
                    return context.getString(R.string.error_code3).replace("{0}", String.valueOf(status));
                }
            }
        } catch(ConnectException e){
            //タイムアウトが発生したときの処理
            return context.getString(R.string.error_code2);
        }catch (Exception e) {
            //そのほかの処理
            e.printStackTrace();
            return context.getString(R.string.error_code4);
        }
        return json_data;
    }

}