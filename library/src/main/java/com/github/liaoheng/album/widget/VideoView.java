package com.github.liaoheng.album.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;

import java.util.LinkedList;
import java.util.List;

/**
 * @author liaoheng
 * @version 2018-07-11 15:34
 */
public class VideoView extends android.widget.VideoView {
    private VideoListener mVideoListener;
    private List<MediaPlayer.OnCompletionListener> mCompletionListeners = new LinkedList<>();

    public VideoView(Context context) {
        super(context);
        init();
    }

    public VideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        super.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                for (MediaPlayer.OnCompletionListener completionListener : mCompletionListeners) {
                    completionListener.onCompletion(mp);
                }
            }
        });
        addOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mVideoListener != null) {
                    state = -1;
                    mVideoListener.stop();
                }
            }
        });
    }

    @Deprecated
    @Override
    public void setOnCompletionListener(MediaPlayer.OnCompletionListener l) {
        //super.setOnCompletionListener(l);
    }

    public void addOnCompletionListener(MediaPlayer.OnCompletionListener l) {
        mCompletionListeners.add(l);
    }

    public interface VideoListener {
        void start();

        void pause();

        void resume();

        void stop();
    }

    public void setVideoListener(VideoListener mVideoListener) {
        this.mVideoListener = mVideoListener;
    }

    private int state;

    public int getState() {
        return state;
    }

    @Override
    public void setVideoURI(Uri uri) {
        super.setVideoURI(uri);
        state = 0;
    }

    @Override
    public void start() {
        super.start();
        state = 1;
        if (mVideoListener != null) {
            mVideoListener.start();
        }
    }

    @Override
    public void resume() {
        state = 0;
        super.resume();
        if (mVideoListener != null) {
            mVideoListener.resume();
        }
    }

    @Override
    public void pause() {
        state = 2;
        super.pause();
        if (mVideoListener != null) {
            mVideoListener.pause();
        }
    }

    @Override
    public void stopPlayback() {
        state = -1;
        super.stopPlayback();
        if (mVideoListener != null) {
            mVideoListener.stop();
        }
    }
}
