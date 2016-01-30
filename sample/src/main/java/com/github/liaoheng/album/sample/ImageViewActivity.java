package com.github.liaoheng.album.sample;

import me.zhanghai.android.systemuihelper.SystemUiHelper;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.github.liaoheng.album.adapter.ImagePagerAdapter;
import com.github.liaoheng.album.model.Album;
import com.github.liaoheng.album.ui.ImagePagerDelegate;

/**
 * 图片翻页显示，可滑动关闭
 * @author liaoheng
 * @version 2015-06-16 16:11
 */
public class ImageViewActivity extends AppCompatActivity {
    private ImagePagerDelegate pagerDelegate;
    private SystemUiHelper     mSystemUiHelper;

    public static void start(Context context, Album album) {
        start(context, album, 0);
    }

    public static void start(Context context, Album album, int pagerPosition) {
        Intent intent = new Intent(context, ImageViewActivity.class);
        intent.putExtras(ImagePagerDelegate.getBundle(album, pagerPosition));
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSystemUiHelper = new SystemUiHelper(this, SystemUiHelper.LEVEL_IMMERSIVE,
            SystemUiHelper.FLAG_IMMERSIVE_STICKY);
        mSystemUiHelper.hide();

        getPagerDelegate().onCreate(savedInstanceState, this);
        getPagerDelegate().initData(getSupportFragmentManager(),
            new ImagePagerAdapter.ImagePagerListener() {
                @Override
                public Fragment getFragment(int position, Album album) {
                    return ImageViewFragment.newInstance(album);
                }
            });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (getPagerDelegate().onOptionsItemSelected(item)) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getPagerDelegate().onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public ImagePagerDelegate getPagerDelegate() {
        if (pagerDelegate == null) {
            pagerDelegate = new ImagePagerDelegate();
        }
        return pagerDelegate;
    }
}
