package com.github.liaoheng.album.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.liaoheng.album.R;
import com.github.liaoheng.album.adapter.ImagePagerAdapter;
import com.github.liaoheng.album.model.Album;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片翻页
 *
 * @author liaoheng
 * @version 2015-12-23 17:16
 */
public class ImagePagerDelegate {
    public static final String PAGER_POSITION = "pagerPosition";
    public static final String ALBUM = "album";
    private ViewPager mViewPager;
    private TextView mIndicator;
    private List<Album> mAlbums;
    private int mPagerPosition;
    private String mStringIndicator;

    private ImagePagerDelegateListener mImagePagerDelegateListener;

    public void setImagePagerDelegateListener(ImagePagerDelegateListener imagePagerDelegateListener) {
        this.mImagePagerDelegateListener = imagePagerDelegateListener;
    }

    public interface ImagePagerDelegateListener {
        void indicator(int index);
    }

    public static Bundle getBundle(ArrayList<Album> albums) {
        return getBundle(albums, 0);
    }

    public static Bundle getBundle(ArrayList<Album> albums, int pagerPosition) {
        final Bundle args = new Bundle();
        args.putParcelableArrayList(ImagePagerDelegate.ALBUM, albums);
        args.putInt(ImagePagerDelegate.PAGER_POSITION, pagerPosition);
        return args;
    }

    public void getIntExtra(Bundle savedInstanceState, Bundle args) {
        if (savedInstanceState != null) {
            mPagerPosition = savedInstanceState.getInt(PAGER_POSITION);
        } else {
            mPagerPosition = args.getInt(PAGER_POSITION, 0);
        }
        mAlbums = args.getParcelableArrayList(ALBUM);
        if (mAlbums == null) {
            throw new IllegalArgumentException("Albums is null");
        }
    }

    public void onCreate(Bundle savedInstanceState, Activity activity) {
        getIntExtra(savedInstanceState, activity.getIntent().getExtras());
        initView(activity.getWindow().getDecorView());
    }

    private String getIndicatorText(int index) {
        if (mViewPager == null || mViewPager.getAdapter() == null) {
            return String.format(mStringIndicator, 0, 0);
        }
        return String.format(mStringIndicator, index, mViewPager.getAdapter().getCount());
    }

    /**
     * 更新下标
     *
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

    public void setPagerAdapter(FragmentManager fragmentManager,
            ImagePagerAdapter.ImagePagerListener listener) {
        setPagerAdapter(new ImagePagerAdapter(fragmentManager, mAlbums, listener));
    }

    public void setPagerAdapter(PagerAdapter adapter) {
        if (mAlbums == null || mAlbums.isEmpty()) {
            setIndicatorText(0);
            return;
        }
        mViewPager.setAdapter(adapter);
        setIndicatorText(1);

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setIndicatorText(position + 1);
            }
        });
        mViewPager.setCurrentItem(mPagerPosition);
    }

    public void initView(View view) {
        mStringIndicator = view.getContext().getString(R.string.photo_album_viewpager_indicator);
        mViewPager = (ViewPager) view.findViewById(R.id.photo_album_pager_view);
        mIndicator = (TextView) view.findViewById(R.id.photo_album_pager_indicator);
        if (mViewPager == null) {
            throw new IllegalArgumentException("ViewPager is  null");
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return item.getItemId() == android.R.id.home;
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(PAGER_POSITION, mViewPager.getCurrentItem());
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }
}
