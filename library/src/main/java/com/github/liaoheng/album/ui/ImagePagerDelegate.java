package com.github.liaoheng.album.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.liaoheng.album.R;
import com.github.liaoheng.album.adapter.ImagePagerAdapter;
import com.github.liaoheng.album.model.Album;
import com.github.liaoheng.album.view.HackyViewPager;

/**
 * 图片翻页
 * @author liaoheng
 * @version 2015-12-23 17:16
 */
public class ImagePagerDelegate {
    /**
     * 第几页
     */
    public static final String PAGER_POSITION = "pagerPosition";
    public static final String ALBUM          = "album";
    private HackyViewPager     mHackyViewPager;
    private TextView           mIndicator;
    private Album              mAlbum;
    private int                mPagerPosition;
    private String             mStringIndicator;

    private ImagePagerDelegateListener mImagePagerDelegateListener;

    public void setImagePagerDelegateListener(ImagePagerDelegateListener imagePagerDelegateListener) {
        this.mImagePagerDelegateListener = imagePagerDelegateListener;
    }

    public interface ImagePagerDelegateListener {
        void indicator(int index);
    }

    public static Bundle getBundle(Album album) {
        return getBundle(album, 0);
    }

    public static Bundle getBundle(Album album, int pagerPosition) {
        final Bundle args = new Bundle();
        args.putSerializable(ImagePagerDelegate.ALBUM, album);
        args.putInt(ImagePagerDelegate.PAGER_POSITION, pagerPosition);
        return args;
    }

    public void getIntExtra(Bundle savedInstanceState, Intent intent) {
        if (savedInstanceState != null) {
            mPagerPosition = savedInstanceState.getInt(PAGER_POSITION);
        } else {
            mPagerPosition = intent.getIntExtra(PAGER_POSITION, 0);
        }
        mAlbum = (Album) intent.getSerializableExtra(ALBUM);
    }

    public void onCreate(Bundle savedInstanceState, AppCompatActivity activity) {
        activity.setContentView(R.layout.photo_album_pager);
        getIntExtra(savedInstanceState, activity.getIntent());
        initActionBar(activity);
        initView(activity);
    }

    public void immersive(Window window) {
        //全屏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int newUiOptions = window.getDecorView().getSystemUiVisibility();
        // Navigation bar hiding:  Backwards compatible to ICS.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        // Status bar hiding: Backwards compatible to Jellybean
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
        // Immersive mode: Backward compatible to KitKat.
        // Note that this flag doesn't do anything by itself, it only augments the behavior
        // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
        // all three flags are being toggled together.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE;
        }
        window.getDecorView().setSystemUiVisibility(newUiOptions);
    }

    private String getIndicatorText(int index) {
        if (mHackyViewPager == null || mHackyViewPager.getAdapter() == null) {
            return String.format(mStringIndicator, 0, 0);
        }
        return String.format(mStringIndicator, index, mHackyViewPager.getAdapter().getCount());
    }

    /**
     *  更新下标
     * @param index 下标位置从1开始
     */
    public void setIndicatorText(int index) {
        if (mImagePagerDelegateListener != null) {
            mImagePagerDelegateListener.indicator(index);
        }
        if (mIndicator == null) {
            return;
        }
        mIndicator.setText(getIndicatorText(index));
    }

    public void initData(FragmentManager fragmentManager,
                         ImagePagerAdapter.ImagePagerListener listener) {
        initData(new ImagePagerAdapter(fragmentManager, mAlbum, listener));
    }

    public void initData(PagerAdapter adapter) {
        if (mAlbum == null || mAlbum.getItems() == null || mAlbum.getItems().isEmpty()) {
            setIndicatorText(0);
            return;
        }
        mHackyViewPager.setAdapter(adapter);
        setIndicatorText(1);

        mHackyViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setIndicatorText(position + 1);
            }
        });
        mHackyViewPager.setCurrentItem(mPagerPosition);
    }

    public void initView(AppCompatActivity activity) {
        initView(activity.getWindow().getDecorView());
    }

    public void initView(View view) {
        mStringIndicator = view.getContext().getString(R.string.photo_album_viewpager_indicator);
        mHackyViewPager = (HackyViewPager) view.findViewById(R.id.photo_album_pager_view);
        mIndicator = (TextView) view.findViewById(R.id.photo_album_pager_indicator);
        if (mHackyViewPager == null) {
            throw new IllegalArgumentException("ViewPager is  null");
        }
    }

    public void initActionBar(AppCompatActivity activity) {
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.photo_album_pager_toolbar);
        if (toolbar == null) {
            return;
        }
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return item.getItemId() == android.R.id.home;
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(PAGER_POSITION, mHackyViewPager.getCurrentItem());
    }

    public HackyViewPager getHackyViewPager() {
        return mHackyViewPager;
    }

    public Album getAlbum() {
        return mAlbum;
    }

    public void setAlbum(Album mAlbum) {
        this.mAlbum = mAlbum;
    }
}
