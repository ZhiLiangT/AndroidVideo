package com.tianzl.androidvideo.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tianzl on 2017/8/6.
 */

public class Video implements Parcelable {
    private String url;
    private String name;
    private int img;

    public Video(String url, String name, int img) {
        this.url = url;
        this.name = name;
        this.img = img;
    }

    @Override
    public String toString() {
        return "Video{" +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", img=" + img +
                '}';
    }

    public Video() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.name);
        dest.writeInt(this.img);
    }

    protected Video(Parcel in) {
        this.url = in.readString();
        this.name = in.readString();
        this.img = in.readInt();
    }

    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}
