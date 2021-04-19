package com.example.ryuka.zootubeapp.common;

import android.content.Context;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.ryuka.zootubeapp.R;
import com.example.ryuka.zootubeapp.FragmentOne;

import java.util.List;
/**
 * タブの遷移をコントロール
 */
public class TabPagerAdapter extends FragmentPagerAdapter {
    private Fragment fragment;
    private Context context;
    /**
     * 初期化処理
     */
    public TabPagerAdapter(FragmentManager fm, Context c) {
        super(fm);
        //context(Androidの実体）をActivityから受け取り、クラス内変数に保管
        //これをしないとFragmentでは文字リソースや画像ファイル等へアクセスできなくなる
        context = c;
    }
    /**
     * 優先オブジェクトの定義
     */
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        fragment = (Fragment) object;
    }
    /**
     * 現在のフラグメントを返却
     * 　※今回は特に利用していないが念のため実装
     */
    public Fragment getFragment() {
        return fragment;
    }
    /**
     * フラグメントの管理数を定義
     * 　※10ページ表示する場合は10、ページの増減があるたびにココを変更する必要あり
     */
    @Override
    public int getCount() {
        return 10;
    }
    /**
     * ページャー
     * フラグメントの切り替えが起こるたびに呼び出され
     * 表示対象のフラグメントによって引数を変えてフラグメントを呼び出す
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FragmentOne.newInstance(context.getString(R.string.fragment_one));
            case 1:
                return FragmentOne.newInstance(context.getString(R.string.fragment_two));
            case 2:
                return FragmentOne.newInstance(context.getString(R.string.fragment_three));
            case 3:
                return FragmentOne.newInstance(context.getString(R.string.fragment_four));
            case 4:
                return FragmentOne.newInstance(context.getString(R.string.fragment_five));
            case 5:
                return FragmentOne.newInstance(context.getString(R.string.fragment_six));
            case 6:
                return FragmentOne.newInstance(context.getString(R.string.fragment_seven));
            case 7:
                return FragmentOne.newInstance(context.getString(R.string.fragment_eight));
            case 8:
                return FragmentOne.newInstance(context.getString(R.string.fragment_nine));
            case 9:
                return FragmentOne.newInstance(context.getString(R.string.fragment_ten));
        }
        return null;
    }
    /**
     * ページタイトル設定
     * タブの表示名を定義する箇所
     */
    @Override
    public CharSequence getPageTitle(int position) {
        String result = "";
        switch (position){
            case 0:
                result = context.getString(R.string.fragment_one);
                break;
            case 1:
                result = context.getString(R.string.fragment_two);
                break;
            case 2:
                result = context.getString(R.string.fragment_three);
                break;
            case 3:
                result = context.getString(R.string.fragment_four);
                break;
            case 4:
                result = context.getString(R.string.fragment_five);
                break;
            case 5:
                result = context.getString(R.string.fragment_six);
                break;
            case 6:
                result = context.getString(R.string.fragment_seven);
                break;
            case 7:
                result = context.getString(R.string.fragment_eight);
                break;
            case 8:
                result = context.getString(R.string.fragment_nine);
                break;
            case 9:
                result = context.getString(R.string.fragment_ten);
                break;
        }
        return result;
    }
    /**
     * フラグメントから受け取った情報を受け取る処理
     * ※今回は利用していないが、フラグメント側で文字入力などをおこなう場合は
     * ここの処理を介してActivity等に返却することが可能
     */
    public interface DialogFragmentResultListener {
        void onDialogFragmentResult(int requestCode, int resultCode, String data);
    }
}