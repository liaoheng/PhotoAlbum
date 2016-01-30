package com.github.liaoheng.album.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.github.liaoheng.album.adapter.ImagePagerAdapter;
import com.github.liaoheng.album.model.Album;

/**
 * 图片翻页实现
 *
 * @author liaoheng
 * @version 2015-04-18 01:46
 */
public class ImagePagerActivity extends AppCompatActivity {

    private final String       TAG = ImagePagerActivity.class.getSimpleName();
    private ImagePagerDelegate imagePager;

    public static void start(Context context, Album album) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putExtras(ImagePagerDelegate.getBundle(album));
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getImagePager().immersive(getWindow());
        getImagePager().onCreate(savedInstanceState, this);
        getImagePager().initData(getSupportFragmentManager(), new ImagePagerAdapter.ImagePagerListener() {
            @Override
            public Fragment getFragment(int position, Album album) {
                return ImageDetailFragment.newInstance(album);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (getImagePager().onOptionsItemSelected(item)) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getImagePager().onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public ImagePagerDelegate getImagePager() {
        if (imagePager == null) {
            imagePager = new ImagePagerDelegate();
        }
        return imagePager;
    }
}
