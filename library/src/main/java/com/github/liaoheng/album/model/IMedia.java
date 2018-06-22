package com.github.liaoheng.album.model;

import android.net.Uri;
import android.os.Parcelable;

/**
 * @author liaoheng
 * @version 2018-06-22 10:14
 */
public interface IMedia extends Parcelable {

    int getType();

    String getName();

    Uri getUrl();

    String getDesc();
}
