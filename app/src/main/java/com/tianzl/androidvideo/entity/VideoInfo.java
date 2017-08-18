package com.tianzl.androidvideo.entity;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tianzl on 2017/8/6.
 */

public class VideoInfo implements Parcelable {
    private String filePath;
    private String mimeType;
    private Bitmap b;
    private String title;
    private String time;
    private String size;
    private int img;


    @Override
    public String toString() {
        return "VideoInfo{" +
                "filePath='" + filePath + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", b=" + b +
                ", title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", size='" + size + '\'' +
                '}';
    }

    public VideoInfo() {
    }

    public VideoInfo(String filePath, String title, int img) {
        this.filePath = filePath;
        this.title = title;
        this.img = img;
    }

    public VideoInfo(String filePath, String title, String time) {
        this.filePath = filePath;
        this.title = title;
        this.time = time;
    }

    public VideoInfo(String filePath, String mimeType, Bitmap b, String title, String time, String size) {
        this.filePath = filePath;
        this.mimeType = mimeType;
        this.b = b;
        this.title = title;
        this.time = time;
        this.size = size;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Bitmap getB() {
        return b;
    }

    public void setB(Bitmap b) {
        this.b = b;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.filePath);
        dest.writeString(this.mimeType);
        dest.writeParcelable(this.b, flags);
        dest.writeString(this.title);
        dest.writeString(this.time);
        dest.writeString(this.size);
        dest.writeInt(this.img);
    }

    protected VideoInfo(Parcel in) {
        this.filePath = in.readString();
        this.mimeType = in.readString();
        this.b = in.readParcelable(Bitmap.class.getClassLoader());
        this.title = in.readString();
        this.time = in.readString();
        this.size = in.readString();
        this.img = in.readInt();
    }

    public static final Parcelable.Creator<VideoInfo> CREATOR = new Parcelable.Creator<VideoInfo>() {
        @Override
        public VideoInfo createFromParcel(Parcel source) {
            return new VideoInfo(source);
        }

        @Override
        public VideoInfo[] newArray(int size) {
            return new VideoInfo[size];
        }
    };
}
