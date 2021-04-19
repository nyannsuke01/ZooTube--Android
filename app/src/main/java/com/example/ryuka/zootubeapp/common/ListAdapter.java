package com.example.ryuka.zootubeapp.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ryuka.zootubeapp.R;
import com.example.ryuka.zootubeapp.util.YoutubeData;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * 独自のカスタムリストを定義するクラス
 */
public class ListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<YoutubeData> jsonlist;
    private ListItemButtonClickListener mListener;
    private static final int resource = R.layout.list_items;
    /**
     * ViewHolder
     * リストを生成する度に新しく作り直していると
     * そのたびに描画処理が発生するため、Holderとして保持しておく
     */
    static class ViewHolder {
        TextView tv_title,tv_content;
        ImageView img_item;
    }
    /**
     * 初回処理
     * 引数を受け取りクラス変数に格納する
     * ここで受け取ったデータが後にリスト表示内容に利用される
     */
    public ListAdapter(Context context, List<YoutubeData> json, ListItemButtonClickListener listener){
        //context
        inflater = LayoutInflater.from(context);
        jsonlist = json;
        mListener = listener;
    }
    /**
     * 表示内容設定処理
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        //convertViewがNULL　＝　初回の場合は各レイアウトオブジェクトの初期化
        if (convertView == null) {
            //リストの行レイアウトを定義
            convertView = inflater.inflate(resource, null);
            //Holderの初期化
            holder = new ViewHolder();
            //各レイアウトオブジェクトをHolderに紐づける
            holder.img_item = convertView.findViewById(R.id.ib_thumbnail);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.tv_content = convertView.findViewById(R.id.tv_content);
            //設定
            convertView.setTag(holder);
        } else {
            //既に一度生成しているため、それを呼び出す
            holder = (ViewHolder) convertView.getTag();
        }
        //タイトル項目の値を設定
        holder.tv_title.setText(jsonlist.get(position).getTitle());
        //コメント項目の値を設定
        holder.tv_content.setText(jsonlist.get(position).getContent());
        try {
            //サムネイル画像（URL形式）の取得＆セット
            ImageGetTask task = new ImageGetTask(holder.img_item);
            task.execute(jsonlist.get(position).getThumbnail());
            //サムネイル項目のクリック時動作を設定
            holder.img_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //FragmentOne側のクリックリスナーを呼び出す（position=押された行、v=img_item）
                    mListener.onItemButtonClick(position, v);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }
    /**
     * リストの行数を管理
     * ※特に利用しないが念のため
     */
    @Override
    public int getCount() {
        return jsonlist.size();
    }
    /**
     * リストの表示内容を返却
     * ※特に利用しないが念のため
     */
    @Override
    public Object getItem(int position) {
        return position;
    }
    /**
     * リストのIDを返却
     * ※特に利用しないが念のため
     */
    @Override
    public long getItemId(int position) {
        return position;
    }
    /**
     * URL形式のサムネイル画像を画面に表示できるように取得・変換する処理
     */
    public static class ImageGetTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView image;

        public ImageGetTask(ImageView _image) {
            image = _image;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap image;
            try {
                //URL取得
                URL imageUrl = new URL(params[0]);
                //読み込み用ストリームを生成
                InputStream imageIs;
                //指定されたURLの画像を読み込み
                imageIs = imageUrl.openStream();
                //読み込んだ画像をBitmap形式に変換することで画面表示できる形式へ
                image = BitmapFactory.decodeStream(imageIs);
                return image;
            } catch (MalformedURLException e) {
                return null;
            } catch (IOException e) {
                return null;
            }
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            // 取得した画像をImageViewに設定します。
            image.setImageBitmap(result);
        }
    }
    /**
     * リスト項目クリック時処理をFragmentOneから引き継ぐ
     */
    public interface ListItemButtonClickListener {
        public void onItemButtonClick(int position, View view);
    }
}