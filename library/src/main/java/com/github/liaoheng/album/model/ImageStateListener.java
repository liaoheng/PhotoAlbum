package com.github.liaoheng.album.model;

/**
 * @author liaoheng
 * @version 2018-06-07 14:16
 */
public interface ImageStateListener {
    void start();

    void error(Throwable e);

    void complete();

    void finished();

    void destroy();

    void loadImage();
}
