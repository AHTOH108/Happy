package com.iandp.happy.model.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created on 20.11.2015.
 */
public class Image implements Parcelable {

    private long id;
    private String path;

    public Image() {
        this.id = -1;
        this.path = "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.getId());
        dest.writeString(this.getPath());
    }

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {

        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[0];
        }
    };

    private Image(Parcel source) {
        this.setId(source.readLong());
        this.setPath(source.readString());
    }
}
