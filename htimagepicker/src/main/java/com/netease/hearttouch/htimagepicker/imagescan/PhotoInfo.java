package com.netease.hearttouch.htimagepicker.imagescan;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zyl06 on 6/23/15.
 */
public class PhotoInfo implements Parcelable {
    private static final long serialVersionUID = 1L;

    private int imageId;
    private String filePath;
    private String absolutePath;
    private long size;
    private boolean choose = false;
    private long lastModifyTime;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int id) {
        this.imageId = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imageId);
        dest.writeString(filePath);
        dest.writeString(absolutePath);
        dest.writeLong(size);
        dest.writeInt(choose ? 1 : 0);
        dest.writeLong(lastModifyTime);
    }

    public static final Parcelable.Creator<PhotoInfo> CREATOR = new Creator<PhotoInfo>() {
        @Override
        public PhotoInfo createFromParcel(Parcel source) {
            PhotoInfo photoInfo = new PhotoInfo();
            photoInfo.setImageId(source.readInt());
            photoInfo.setFilePath(source.readString());
            photoInfo.setAbsolutePath(source.readString());
            photoInfo.setSize(source.readLong());
            photoInfo.setChoose(source.readInt() == 1);
            photoInfo.setLastModifyTime(source.readLong());
            return photoInfo;
        }

        @Override
        public PhotoInfo[] newArray(int size) {
            return new PhotoInfo[size];
        }
    };
}
