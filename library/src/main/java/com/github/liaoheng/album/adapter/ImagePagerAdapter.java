package com.github.liaoheng.album.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.github.liaoheng.album.model.Album;

/**
 * @author liaoheng
 * @version 2015-04-18 01:47
 */
public class ImagePagerAdapter extends FragmentStatePagerAdapter {

    private Album              mAlbum;
    private ImagePagerListener listener;

    public interface ImagePagerListener {
        Fragment getFragment(int position, Album album);
    }

    public ImagePagerAdapter(FragmentManager fm, Album album, ImagePagerListener listener) {
        super(fm);
        this.mAlbum = album;
        this.listener = listener;
    }

    @Override
    public Fragment getItem(int position) {
        return listener.getFragment(position, mAlbum.getItems().get(position));
    }

    @Override
    public int getCount() {
        return mAlbum.getItems() == null ? 0 : mAlbum.getItems().size();
    }
}
