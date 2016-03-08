package com.iandp.happy.model.api;

import android.content.Context;

import com.iandp.happy.model.DBDataModule;
import com.iandp.happy.model.object.CategoryProduct;
import com.iandp.happy.model.object.Product;

import java.util.ArrayList;

/**
 * Created on 07.03.2016.
 */
public class ProductApi extends Api {

    public ProductApi(Context context) {
        super(context);
    }

    public Product getProduct(int id){
        return new Product();
    }

    public ArrayList <Product> getListProduct(int lastId, int count){
        ArrayList<Product> listProduct = new ArrayList<>();
        for (int i = lastId; i < lastId + count; i ++){
            listProduct.add(new Product());
        }
        return listProduct;
    }

    public ArrayList<Product> getListProduct(){
        return DBDataModule.get(mContext).getDbHelper().getAllProduct();
    }

    public long addProduct(Product product){
        return DBDataModule.get(mContext).getDbHelper().addNewProduct(product);
    }
}
