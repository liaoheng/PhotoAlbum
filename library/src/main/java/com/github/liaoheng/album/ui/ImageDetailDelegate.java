package com.github.liaoheng.album.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.github.liaoheng.album.R;
import com.github.liaoheng.album.model.Album;
import com.github.liaoheng.album.model.ImageStateListener;
import com.github.liaoheng.album.view.PhotoView;

/**
 * 图片查看
 *
 * @author liaoheng
 * @version 2015-12-23 17:16
 */
public class ImageDetailDelegate implements ImageStateListener {

    private Album mAlbum;
    private ImageView mPhotoView;
    private ProgressBar mProgressBar;
    private PhotoViewAttacher mPhotoViewAttacher;
    private ImageListener mImageListener;

    public static Bundle getBundle(Album album) {
        final Bundle args = new Bundle();
        args.putParcelable(ImagePagerDelegate.ALBUM, album);
        return args;
    }

    public void onCreate(View view, Bundle args) {
        if (args == null) {
            throw new IllegalArgumentException("args is null");
        }
        mAlbum = args.getParcelable(ImagePagerDelegate.ALBUM);
        if (mAlbum == null) {
            throw new IllegalArgumentException("Album is null");
        }
        mPhotoView = view.findViewById(R.id.photo_album_detail_image);
        if (mPhotoView == null) {
            throw new IllegalArgumentException("ImageView is null");
        }
        if (isPhotoView()) {
            mPhotoViewAttacher = ((PhotoView) mPhotoView).getAttacher();
        } else {
            mPhotoViewAttacher = new PhotoViewAttacher(mPhotoView);
            mPhotoView.setScaleType(ImageView.ScaleType.MATRIX);
        }

        mProgressBar = (ProgressBar) view.findViewById(R.id.photo_album_detail_loading);
    }

    private boolean isPhotoView() {
        return mPhotoView instanceof com.github.chrisbanes.photoview.PhotoView;
    }

    public interface ImageListener {
        void load(Album album, ImageView imageView);

        void error(Album album, ImageView imageView,Throwable e);

        void destroy(Album album, ImageView imageView);
    }

    public void setImageListener(ImageListener imageListener) {
        this.mImageListener = imageListener;
    }

    @Override
    public void start() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void error(Throwable e) {
        if (mImageListener != null) {
            mImageListener.error(mAlbum, mPhotoView,e);
        }
    }

    @Override
    public void complete() {
        if (!isPhotoView() && mPhotoViewAttacher != null) {
            mPhotoViewAttacher.update();
        }
    }

    @Override
    public void finished() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void destroy() {
        if (mImageListener != null) {
            mImageListener.destroy(mAlbum, mPhotoView);
        }
    }

    @Override
    public void loadImage() {
        if (mImageListener != null) {
            mImageListener.load(mAlbum, mPhotoView);
        }
    }

    public PhotoViewAttacher getPhotoViewAttacher() {
        return mPhotoViewAttacher;
    }

    public Album getAlbum() {
        return mAlbum;
    }
}
