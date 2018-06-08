package com.github.liaoheng.album.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * 相册
 *
 * @author liaoheng
 * @version 2015-04-18 01:42
 */
public class Album implements Parcelable {
    public Album() {
    }

    public Album(String name, Uri url) {
        this.name = name;
        this.url = url;
    }

    protected Album(Parcel in) {
        this.name = in.readString();
        this.url = in.readParcelable(Uri.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeParcelable(this.url, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
    }

    public static final Parcelable.Creator<Album> CREATOR = new Parcelable.Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    private String name;
    private Uri url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getUrl() {
        return url;
    }

    public void setUrl(Uri url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Album{" +
                "name='" + name + '\'' +
                ", url=" + url +
                '}';
    }
}
