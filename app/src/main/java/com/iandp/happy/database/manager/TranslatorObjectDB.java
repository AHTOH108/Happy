package com.iandp.happy.database.manager;

import com.iandp.happy.database.DBCategoryProduct;
import com.iandp.happy.database.DBCost;
import com.iandp.happy.database.DBImage;
import com.iandp.happy.database.DBProduct;
import com.iandp.happy.database.DBProductSimple;
import com.iandp.happy.database.DBShop;
import com.iandp.happy.model.object.CategoryProduct;
import com.iandp.happy.model.object.Cost;
import com.iandp.happy.model.object.Image;
import com.iandp.happy.model.object.Product;
import com.iandp.happy.model.object.ProductSimple;
import com.iandp.happy.model.object.Shop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 30.10.2016.
 */

public class TranslatorObjectDB {

    public static ProductSimple translateDBProductSimple(DBProductSimple dbObject) {
        ProductSimple object = new ProductSimple();
        object.setId(dbObject.getId());
        object.setName(dbObject.getName());
        object.setPrice(dbObject.getPrice());
        object.setAmount(dbObject.getAmount());
        object.setTypeAmount(dbObject.getTypeAmount());
        return object;
    }

    public static DBProductSimple translateProductSimple(ProductSimple object) {
        DBProductSimple dbObject = new DBProductSimple();
        dbObject.setId(object.getId());
        dbObject.setName(object.getName());
        dbObject.setPrice(object.getPrice());
        dbObject.setAmount(object.getAmount());
        dbObject.setTypeAmount(object.getTypeAmount());
        return dbObject;
    }

    public static Product translateDBProduct(DBProduct dbObject) {
        Product object = new Product();
        object.setId(dbObject.getId());
        object.setBrand(dbObject.getBrand());
        object.setDescription(dbObject.getDescription());
        object.setRating(dbObject.getRating());
        object.setCategoryProduct(translateDBCategory(dbObject.getCategoryProduct()));
        object.setCostList(translateDBCostList(dbObject.getCosts()));
        object.setImageList(translateDBImageList(dbObject.getImages()));
        return object;
    }

    public static CategoryProduct translateDBCategory(DBCategoryProduct dbObject) {
        CategoryProduct object = new CategoryProduct();
        object.setId(dbObject.getId());
        object.setName(dbObject.getName());
        return object;
    }

    public static Image translateDBImage(DBImage dbObject) {
        Image object = new Image();
        object.setId(dbObject.getId());
        object.setPath(dbObject.getPath());
        return object;
    }

    public static ArrayList<Image> translateDBImageList(List<DBImage> dbObjectList) {
        ArrayList<Image> objectList = new ArrayList<>();
        for (DBImage dbObject : dbObjectList) {
            objectList.add(translateDBImage(dbObject));
        }
        return objectList;
    }

    public static ArrayList<Cost> translateDBCostList(List<DBCost> dbObjectList) {
        ArrayList<Cost> objectList = new ArrayList<>();
        for (DBCost dbObject: dbObjectList) {
            Cost object = new Cost();
            object.setId(dbObject.getId());
            object.setPrice(dbObject.getPrice());
            object.setPriceMax(dbObject.getPriceMax());
            object.setDate(dbObject.getDate());
            //TODO: Rewrite TypeAmount!!!!
            object.setVolume(dbObject.getAmount());
            //object.setTypeAmount(dbObject.getTypeAmount());
            object.setShop(translateDBShop(dbObject.getShop()));

            objectList.add(object);
        }
        return objectList;
    }

    public static Shop translateDBShop(DBShop dbObject) {
        Shop object = new Shop();
        object.setId(dbObject.getId());
        object.setName(dbObject.getName());
        object.setLatitude(dbObject.getLatitude());
        object.setLongitude(dbObject.getLongitude());
        object.setAddress(dbObject.getAddress());
        object.setImage(translateDBImage(dbObject.getImage()));
        return object;
    }



}
