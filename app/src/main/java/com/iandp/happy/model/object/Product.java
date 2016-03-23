package com.iandp.happy.model.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class Product implements Parcelable {

    private long id;
    private CategoryProduct categoryProduct;
    private String brand;
    private String description;
    private int rating;
    private ArrayList<Cost> costList;
    private ArrayList<Image> imageList;

    public Product() {
        this.id = -1;
        this.categoryProduct = new CategoryProduct();
        this.brand = "";
        this.description = "";
        this.rating = 0;
        this.costList = new ArrayList<>();
        this.imageList = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CategoryProduct getCategoryProduct() {
        return categoryProduct;
    }

    public void setCategoryProduct(CategoryProduct categoryProduct) {
        this.categoryProduct = categoryProduct;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public ArrayList<Cost> getCostList() {
        return costList;
    }

    public Cost getFirstCost() {
        if (costList.size() <= 0)
            return new Cost();
        else
            return costList.get(0);
    }

    public void setFirstCost(Cost cost) {
        if (costList.size() <= 0)
            costList.add(cost);
        else
            costList.set(0, cost);
    }

    public void addCost(Cost cost) {
        costList.add(cost);
    }

    public void setCostList(ArrayList<Cost> costList) {
        this.costList = costList;
    }

    public ArrayList<Image> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<Image> imageList) {
        this.imageList = imageList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(this.getId());
        dest.writeParcelable(this.getCategoryProduct(), Parcelable.CONTENTS_FILE_DESCRIPTOR);
        dest.writeString(this.getBrand());
        dest.writeString(this.getDescription());
        dest.writeInt(this.getRating());
        dest.writeTypedList(this.getCostList());
        dest.writeTypedList(this.getImageList());
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
        this.setId(source.readLong());
        this.setCategoryProduct((CategoryProduct) source.readParcelable(CategoryProduct.class.getClassLoader()));
        this.setBrand(source.readString());
        this.setDescription(source.readString());
        this.setRating(source.readInt());
        ArrayList<Cost> listOffer = new ArrayList<>();
        source.readTypedList(listOffer, Cost.CREATOR);
        setCostList(listOffer);
        ArrayList<Image> listImage = new ArrayList<>();
        source.readTypedList(listImage, Image.CREATOR);
        setImageList(listImage);
    }
}
