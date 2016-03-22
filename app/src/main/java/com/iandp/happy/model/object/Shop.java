package com.iandp.happy.model.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created on 21.09.2015.
 */
public class Shop implements Parcelable {
    private long id;
    private String name;
    private Image image;
    private double latitude;
    private double longitude;
    private String address;

    public Shop(long id, String name,Image image, double latitude, double longitude, String address) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public Shop() {
        this.id = -1;
        this.name = "";
        this.image = new Image();
        this.latitude = 0;
        this.longitude = 0;
        this.address = "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.getId());
        dest.writeString(this.getName());
        dest.writeParcelable(this.getImage(), Parcelable.CONTENTS_FILE_DESCRIPTOR);
        dest.writeDouble(this.getLatitude());
        dest.writeDouble(this.getLongitude());
        dest.writeString(this.getAddress());
    }

    public static final Parcelable.Creator<Shop> CREATOR = new Parcelable.Creator<Shop>() {

        @Override
        public Shop createFromParcel(Parcel source) {
            return new Shop(source);
        }

        @Override
        public Shop[] newArray(int size) {
            return new Shop[size];
        }
    };

    private Shop(Parcel source) {
        this.setId(source.readLong());
        this.setName(source.readString());
        this.setImage((Image) source.readParcelable(Image.class.getClassLoader()));
        this.setLatitude(source.readDouble());
        this.setLongitude(source.readDouble());
        this.setAddress(source.readString());
    }
}
