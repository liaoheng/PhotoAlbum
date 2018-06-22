package com.github.liaoheng.album.sample;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.github.liaoheng.album.adapter.ImagePagerAdapter;
import com.github.liaoheng.album.ui.ImagePagerDelegate;

import java.util.ArrayList;

import me.zhanghai.android.systemuihelper.SystemUiHelper;

/**
 * @author liaoheng
 * @version 2015-06-16 16:11
 */
public class ImageViewActivity extends AppCompatActivity {
    private ImagePagerDelegate<Media> pagerDelegate;
    private SystemUiHelper mSystemUiHelper;

    public static void start(Context context, ArrayList<Media> albums) {
        start(context, albums, 0);
    }

    public static void start(Context context, ArrayList<Media> albums, int pagerPosition) {
        Intent intent = new Intent(context, ImageViewActivity.class);
        intent.putExtras(ImagePagerDelegate.getBundle(albums, pagerPosition));
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_album_pager);

        mSystemUiHelper = new SystemUiHelper(this, SystemUiHelper.LEVEL_IMMERSIVE,
                SystemUiHelper.FLAG_IMMERSIVE_STICKY);
        mSystemUiHelper.hide();

        Toolbar toolbar = (Toolbar) findViewById(R.id.photo_album_pager_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getPagerDelegate().onCreate(savedInstanceState, this);
        getPagerDelegate().setPagerAdapter(getSupportFragmentManager(),
                new ImagePagerAdapter.ImagePagerListener<Media>() {
                    @Override
                    public Fragment getFragment(int position, Media album) {
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

    public ImagePagerDelegate<Media> getPagerDelegate() {
        if (pagerDelegate == null) {
            pagerDelegate = new ImagePagerDelegate<>();
        }
        return pagerDelegate;
    }
}
