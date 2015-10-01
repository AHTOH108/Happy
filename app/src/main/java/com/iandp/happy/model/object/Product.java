package com.iandp.happy.model.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class Product implements Parcelable {

    private int id;
    private String name;
    private String description;
    private Units unit;
    private ArrayList<OfferShop> offerShop;
    private String image;

    public Product() {
        this.id = -1;
        this.name = "";
        this.description = "";
        this.unit = new Units();
        this.offerShop = new ArrayList<>();
        this.image = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Units getUnit() {
        return unit;
    }

    public void setUnit(Units unit) {
        this.unit = unit;
    }

    public ArrayList<OfferShop> getOfferShop() {
        return offerShop;
    }

    public void setOfferShop(ArrayList<OfferShop> offerShop) {
        this.offerShop = offerShop;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.getId());
        dest.writeString(this.getName());
        dest.writeString(this.getDescription());
        dest.writeParcelable(this.getUnit(), Parcelable.CONTENTS_FILE_DESCRIPTOR);
        dest.writeTypedList(this.getOfferShop());
        dest.writeString(this.getImage());
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {

        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    private Product(Parcel source) {
        this.setId(source.readInt());
        this.setName(source.readString());
        this.setDescription(source.readString());
        this.setUnit((Units) source.readParcelable(Units.class.getClassLoader()));
        ArrayList<OfferShop> listOffer = new ArrayList<>();
        source.readTypedList(listOffer, OfferShop.CREATOR);
        this.setOfferShop(listOffer);
        this.setImage(source.readString());
    }
}
