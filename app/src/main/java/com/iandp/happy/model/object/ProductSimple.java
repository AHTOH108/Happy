package com.iandp.happy.model.object;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created on 03.11.2016.
 */

public class ProductSimple implements Parcelable {

    private long id;
    private String name;
    private double price;
    private double amount;
    private byte typeAmount;

    public ProductSimple() {
        this.id = -1;
        this.name = "";
        this.price = -1;
        this.amount = -1;
        this.typeAmount = -1;
    }

    public ProductSimple(long id, String name, double price, double amount, byte typeAmount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.typeAmount = typeAmount;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public byte getTypeAmount() {
        return typeAmount;
    }

    public void setTypeAmount(byte typeAmount) {
        this.typeAmount = typeAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.getId());
        dest.writeString(this.getName());
        dest.writeDouble(this.getPrice());
        dest.writeDouble(this.getAmount());
        dest.writeByte(this.getTypeAmount());
    }

    public static final Parcelable.Creator<ProductSimple> CREATOR = new Parcelable.Creator<ProductSimple>() {

        @Override
        public ProductSimple createFromParcel(Parcel source) {
            return new ProductSimple(source);
        }

        @Override
        public ProductSimple[] newArray(int size) {
            return new ProductSimple[size];
        }
    };

    private ProductSimple(Parcel source) {
        this.setId(source.readLong());
        this.setName(source.readString());
        this.setPrice(source.readDouble());
        this.setAmount(source.readDouble());
        this.setTypeAmount(source.readByte());
    }
}
