package com.wyt.videolibrary.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by taihong on 2019/7/25
 */
public class VideoUrlBean implements Parcelable {
    public String name;
    public String url;


    public VideoUrlBean(String name, String url) {
        this.name = name;
        this.url = url;
    }

    protected VideoUrlBean(Parcel in) {
        name = in.readString();
        url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VideoUrlBean> CREATOR = new Creator<VideoUrlBean>() {
        @Override
        public VideoUrlBean createFromParcel(Parcel in) {
            return new VideoUrlBean(in.readString(),in.readString());
        }

        @Override
        public VideoUrlBean[] newArray(int size) {
            return new VideoUrlBean[size];
        }
    };
}
