package com.iandp.happy.model.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created on 21.09.2015.
 */
public class Cost implements Parcelable {

    private int id;
    private long date;
    private double price;
    private double volume;
    private Units units;
    private double priceFromUnit;
    private Shop shop;


    public Cost() {
        this.id = -1;
        this.date = 0;
        this.price = 0;
        this.volume = 0;
        this.units = new Units();
        this.priceFromUnit = 0;
        this.shop = new Shop();
    }

    public Cost(Units units) {
        this.id = -1;
        this.date = 0;
        this.price = 0;
        this.volume = 0;
        this.units = units;
        this.priceFromUnit = 0;
        this.shop = new Shop();
    }

    public Cost(long date, double price, double volume, Units units, Shop shop) {
        this.id = -1;
        this.date = date;
        this.price = price;
        setVolume(volume);
        this.units = units;
        this.shop = shop;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (getVolume() > 0)
            setPriceFromUnit(price/volume);
        else
            setPriceFromUnit(0);
        this.price = price;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        if (volume > 0)
            setPriceFromUnit(price/volume);
        else
            setPriceFromUnit(0);
        this.volume = volume;
    }

    public Units getUnits() {
        return units;
    }

    public void setUnits(Units units) {
        this.units = units;
    }

    public double getPriceFromUnit() {
        return priceFromUnit;
    }

    private void setPriceFromUnit(double priceFromUnit) {
        this.priceFromUnit = priceFromUnit;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(this.getId());
        dest.writeLong(this.getDate());
        dest.writeDouble(this.getPrice());
        dest.writeDouble(this.getVolume());
        dest.writeParcelable(this.getUnits(), Parcelable.CONTENTS_FILE_DESCRIPTOR);
        dest.writeDouble(this.getPriceFromUnit());
        dest.writeParcelable(this.getShop(), Parcelable.CONTENTS_FILE_DESCRIPTOR);
    }

    public static final Parcelable.Creator<Cost> CREATOR = new Parcelable.Creator<Cost>() {

        @Override
        public Cost createFromParcel(Parcel source) {
            return new Cost(source);
        }

        @Override
        public Cost[] newArray(int size) {
            return new Cost[size];
        }
    };

    private Cost(Parcel source) {
        this.setId(source.readInt());
        this.setDate(source.readLong());
        this.setPrice(source.readDouble());
        this.setVolume(source.readDouble());
        this.setUnits((Units) source.readParcelable(Units.class.getClassLoader()));
        this.setPriceFromUnit(source.readDouble());
        this.setShop((Shop) source.readParcelable(Shop.class.getClassLoader()));
    }
}
