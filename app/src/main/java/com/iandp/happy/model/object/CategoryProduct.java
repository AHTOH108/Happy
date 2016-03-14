package com.iandp.happy.model.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created on 20.11.2015.
 */
public class CategoryProduct implements Parcelable {

    private long id;
    private String name;

    public CategoryProduct() {
        this.id = -1;
        this.name = "";
    }

    public CategoryProduct(String name) {
        this.id = -1;
        this.name = name;
    }

    public CategoryProduct(int id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.getId());
        dest.writeString(this.getName());
    }

    public static final Parcelable.Creator<CategoryProduct> CREATOR = new Parcelable.Creator<CategoryProduct>() {

        @Override
        public CategoryProduct createFromParcel(Parcel source) {
            return new CategoryProduct(source);
        }

        @Override
        public CategoryProduct[] newArray(int size) {
            return new CategoryProduct[0];
        }
    };

    private CategoryProduct(Parcel source) {
        this.setId(source.readLong());
        this.setName(source.readString());
    }
}
