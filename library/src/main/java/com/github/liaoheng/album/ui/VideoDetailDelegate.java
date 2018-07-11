package com.github.liaoheng.album.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.github.liaoheng.album.R;
import com.github.liaoheng.album.model.IMedia;

/**
 * @author liaoheng
 * @version 2018-07-10 14:46
 */
public class VideoDetailDelegate<T extends IMedia> {
    private T mAlbum;
    private VideoView mVideoView;
    private ImageView mCoverView;
    private ImageDetailDelegate.ImageListener<T> mImageListener;

    public static <T extends IMedia> Bundle getBundle(T album) {
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
        mVideoView = view.findViewById(R.id.photo_album_detail_video);
        if (mVideoView == null) {
            throw new IllegalArgumentException("VideoView is null");
        }
        mCoverView = view.findViewById(R.id.photo_album_detail_image);
        mImageListener.load(mAlbum, mCoverView);
    }

    public void setImageListener(ImageDetailDelegate.ImageListener<T> imageListener) {
        this.mImageListener = imageListener;
    }

    public boolean isPlaying() {
        return mVideoView.isPlaying();
    }

    public void play(Uri url) {
        mVideoView.setVideoURI(url);
        start();
    }

    public void play() {
        play(mAlbum.getUrl());
    }

    public void start() {
        mVideoView.start();
    }

    public void pause() {
        mVideoView.pause();
    }

    public void stop() {
        mVideoView.stopPlayback();
    }

    public void resume() {
        mVideoView.resume();
    }

    public VideoView getVideoView() {
        return mVideoView;
    }

    public ImageView getCoverView() {
        return mCoverView;
    }

    public T getAlbum() {
        return mAlbum;
    }
}
