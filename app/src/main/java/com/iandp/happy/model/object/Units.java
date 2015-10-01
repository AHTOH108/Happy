package com.iandp.happy.model.object;

import android.os.Parcel;
import android.os.Parcelable;

public class Units implements Parcelable {

    private static final String LITER = "литр";
    private static final String LITER_SHORT = "л.";
    private static final String GRAM = "грамм";
    private static final String GRAM_SHORT = "г.";

    private int type;

    public Units() {
        type = -1;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeString() {
        switch (type){
            case 1: return LITER;
            case 2: return GRAM;
            default: return "";
        }
    }

    public String getTypeStringShort() {
        switch (type){
            case 1: return LITER_SHORT;
            case 2: return GRAM_SHORT;
            default: return "";
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.getType());
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {

        @Override
        public Product createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[0];
        }
    };

    private Units(Parcel source) {
        this.setType(source.readInt());
    }
}
