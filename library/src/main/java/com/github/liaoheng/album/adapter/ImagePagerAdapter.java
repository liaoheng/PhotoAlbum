package com.github.liaoheng.album.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.github.liaoheng.album.model.Album;

import java.util.List;

/**
 * @author liaoheng
 * @version 2015-04-18 01:47
 */
public class ImagePagerAdapter extends FragmentStatePagerAdapter {

    private List<Album> mAlbums;
    private ImagePagerListener mListener;

    public interface ImagePagerListener {
        Fragment getFragment(int position, Album album);
    }

    public ImagePagerAdapter(FragmentManager fm, List<Album> albums, ImagePagerListener listener) {
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
