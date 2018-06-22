package com.github.liaoheng.album.sample;

import android.net.Uri;
import android.os.Parcel;

import com.github.liaoheng.album.model.AlbumMedia;

/**
 * @author liaoheng
 * @version 2018-06-15 11:41
 */
public class Media extends AlbumMedia {

    public Media(String name, Uri url) {
        super(name, url);
    }

    protected Media(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public static final Creator<Media> CREATOR = new Creator<Media>() {
        @Override
        public Media createFromParcel(Parcel in) {
            return new Media(in);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };
}
