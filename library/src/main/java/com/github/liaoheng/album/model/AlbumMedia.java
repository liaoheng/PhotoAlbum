package com.github.liaoheng.album.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author liaoheng
 * @version 2018-06-21 17:04
 */
public class AlbumMedia implements Parcelable,IMedia {
    private int type;
    private String name;
    private Uri url;
    private String desc;

    public AlbumMedia(IMedia media) {
        setType(media.getType());
        setName(media.getName());
        setUrl(media.getUrl());
        setDesc(media.getDesc());
    }

    public AlbumMedia(String name, Uri url) {
        this.name = name;
        this.url = url;
    }

    public AlbumMedia(String name, Uri url, String desc) {
        this.name = name;
        this.url = url;
        this.desc = desc;
    }

    public AlbumMedia(int type, String name, Uri url, String desc) {
        this.type = type;
        this.name = name;
        this.url = url;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Media{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", url=" + url +
                ", desc='" + desc + '\'' +
                '}';
    }

    protected AlbumMedia(Parcel in) {
        type = in.readInt();
        name = in.readString();
        url = in.readParcelable(Uri.class.getClassLoader());
        desc = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeString(name);
        dest.writeParcelable(url, flags);
        dest.writeString(desc);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AlbumMedia> CREATOR = new Creator<AlbumMedia>() {
        @Override
        public AlbumMedia createFromParcel(Parcel in) {
            return new AlbumMedia(in);
        }

        @Override
        public AlbumMedia[] newArray(int size) {
            return new AlbumMedia[size];
        }
    };
}
