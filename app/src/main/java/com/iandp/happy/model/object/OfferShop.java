package com.iandp.happy.model.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Anton on 21.09.2015.
 */
public class OfferShop implements Parcelable {

    private int id;
    private Shop shop;
    private double quantity;
    private double priceProduct;
    private double priceFromUnit;

    public OfferShop() {
        this.id = -1;
        this.shop = new Shop();
        this.quantity = -1;
        this.priceProduct = -1;
        this.priceFromUnit = -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(double priceProduct) {
        this.priceProduct = priceProduct;
    }

    public double getPriceFromUnit() {
        return priceFromUnit;
    }

    public void setPriceFromUnit(double priceFromUnit) {
        this.priceFromUnit = priceFromUnit;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.getId());
        dest.writeParcelable(this.getShop(), Parcelable.CONTENTS_FILE_DESCRIPTOR);
        dest.writeDouble(this.getQuantity());
        dest.writeDouble(this.getPriceProduct());
        dest.writeDouble(this.getPriceFromUnit());
    }

    public static final Parcelable.Creator<OfferShop> CREATOR = new Parcelable.Creator<OfferShop>() {

        @Override
        public OfferShop createFromParcel(Parcel source) {
            return new OfferShop(source);
        }

        @Override
        public OfferShop[] newArray(int size) {
            return new OfferShop[size];
        }
    };

    private OfferShop(Parcel source) {
        this.setId(source.readInt());
        this.setShop((Shop) source.readParcelable(Units.class.getClassLoader()));
        this.setQuantity(source.readDouble());
        this.setPriceProduct(source.readDouble());
        this.setPriceFromUnit(source.readDouble());
    }
}
