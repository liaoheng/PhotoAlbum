package com.github.liaoheng.album.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.github.liaoheng.album.model.IMedia;

import java.util.List;

/**
 * @author liaoheng
 * @version 2015-04-18 01:47
 */
public class ImagePagerAdapter<T extends IMedia> extends FragmentStatePagerAdapter {

    private List<T> mAlbums;
    private ImagePagerListener<T> mListener;

    public interface ImagePagerListener<M extends IMedia> {
        Fragment getFragment(int position, M album);
    }

    public ImagePagerAdapter(FragmentManager fm, List<T> albums, ImagePagerListener<T> listener) {
        super(fm);
        mAlbums = albums;
        mListener = listener;
    }

    @Override
    public Fragment getItem(int position) {
        return mListener.getFragment(position, mAlbums.get(position));
    }

    @Override
    public int getCount() {
        return mAlbums == null ? 0 : mAlbums.size();
    }
}
