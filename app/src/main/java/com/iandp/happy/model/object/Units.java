package com.iandp.happy.model.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.iandp.happy.utils.Constants;

import java.util.ArrayList;

public class Units implements Parcelable {

    private int id;
    private String name;
    private String shortName;

    public Units() {
        id = -1;
        name = "";
        shortName = "";
    }

    public Units(int id, String name, String shortName) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
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

    private void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    private void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public ArrayList<Units> getListUnits() {
        ArrayList<Units> listUnits = new ArrayList<>();
        listUnits.add(new Units(Constants.UNITS_ID_LITER, Constants.UNITS_LITER, Constants.UNITS_LITER_SHORT));
        listUnits.add(new Units(Constants.UNITS_ID_GRAM, Constants.UNITS_GRAM, Constants.UNITS_GRAM_SHORT));
        listUnits.add(new Units(Constants.UNITS_ID_VALUE, Constants.UNITS_VALUE, Constants.UNITS_VALUE_SHORT));
        return listUnits;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.getId());
        dest.writeString(this.getName());
        dest.writeString(this.getShortName());
    }

    public static final Parcelable.Creator<Units> CREATOR = new Parcelable.Creator<Units>() {

        @Override
        public Units createFromParcel(Parcel source) {
            return new Units(source);
        }

        @Override
        public Units[] newArray(int size) {
            return new Units[0];
        }
    };

    private Units(Parcel source) {
        this.setId(source.readInt());
        this.setName(source.readString());
        this.setShortName(source.readString());
    }
}
