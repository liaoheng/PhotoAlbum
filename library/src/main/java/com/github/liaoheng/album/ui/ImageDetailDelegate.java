package com.github.liaoheng.album.ui;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.liaoheng.album.R;
import com.github.liaoheng.album.model.Album;

/**
 * 图片查看
 * @author liaoheng
 * @version 2015-12-23 17:16
 */
public class ImageDetailDelegate {

    private Album             mAlbum;
    private PhotoView         mPhotoView;
    private View              mRetry;
    private ProgressBar       mProgressBar;
    private PhotoViewAttacher mAttacher;
    private ImageListener     mImageListener;

    public static Bundle getBundle(Album album) {
        final Bundle args = new Bundle();
        args.putSerializable(ImagePagerDelegate.ALBUM, album);
        return args;
    }

    public void onCreate(Bundle savedInstanceState, Fragment fragment) {
        fragment.setHasOptionsMenu(true);
    }

    public View onCreateView(View view, Bundle savedInstanceState, Fragment fragment,
                             PhotoViewAttacher.OnPhotoTapListener listener) {
        mAlbum = fragment.getArguments() != null
            ? (Album) fragment.getArguments().getSerializable(ImagePagerDelegate.ALBUM) : null;
        mPhotoView = (PhotoView) view.findViewById(R.id.photo_album_detail_image);
        if (mPhotoView == null) {
            throw new IllegalArgumentException("PhotoView is null");
        }
        mAttacher = new PhotoViewAttacher(mPhotoView);

        mAttacher.setOnPhotoTapListener(listener);

        mProgressBar = (ProgressBar) view.findViewById(R.id.photo_album_detail_loading);
        mRetry = view.findViewById(R.id.photo_album_detail_retry);
        if (mRetry != null) {
            mRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadImage();
                }
            });
        }
        loadImage();
        return view;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState, Fragment fragment,
                             PhotoViewAttacher.OnPhotoTapListener listener) {
        View v = inflater.inflate(R.layout.photo_album_detail, container, false);
        return onCreateView(v, savedInstanceState, fragment, listener);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.photo_album_menu, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item, Activity activity) {
        if (item.getItemId() == R.id.photo_album_download_image) {
            if (mImageListener != null) {
                if (mAlbum == null || TextUtils.isEmpty(mAlbum.getUrl())) {
                    Toast.makeText(activity,
                        activity.getString(R.string.photo_album_not_download_image),
                        Toast.LENGTH_LONG).show();
                    return true;
                }
                mImageListener.downloadStart(mAlbum.getUrl());
            }
        }
        return false;
    }

    public interface ImageListener {
        void load(String imageUrl, ImageView imageView);

        void destroy(String imageUrl, ImageView imageView);

        void downloadStart(String imageUrl);
    }

    public void setImageListener(ImageListener imageListener) {
        this.mImageListener = imageListener;
    }

    public void started() {
        if (mRetry != null) {
            mRetry.setVisibility(View.GONE);
        }
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void error() {
        if (mRetry != null) {
            mRetry.setVisibility(View.VISIBLE);
        }
    }

    public void complete() {
        mAttacher.update();
    }

    public void finished() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void loadImage() {
        if (mAlbum == null) {
            return;
        }
        if (mImageListener != null) {
            mImageListener.load(mAlbum.getUrl(), mPhotoView);
        }
    }

    public void onDestroyView() {
        mAttacher.cleanup();
        if (mAlbum == null) {
            return;
        }
        if (mImageListener != null) {
            mImageListener.destroy(mAlbum.getUrl(), mPhotoView);
        }
    }
}
