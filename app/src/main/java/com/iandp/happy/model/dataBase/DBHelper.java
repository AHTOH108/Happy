package com.iandp.happy.model.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.iandp.happy.model.object.CategoryProduct;
import com.iandp.happy.model.object.Cost;
import com.iandp.happy.model.object.Image;
import com.iandp.happy.model.object.Product;
import com.iandp.happy.model.object.Shop;
import com.iandp.happy.model.object.Units;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {

    //private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "dataBaseHappy";

    private static final String TABLE_PRODUCT = "product";
    private static final String TABLE_CATEGORY_PRODUCT = "categoryProduct";
    private static final String TABLE_COST = "cost";
    private static final String TABLE_IMAGE = "image";
    private static final String TABLE_SHOP = "shop";
    private static final String TABLE_LOGO_SHOP = "logoShop";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ID_CATEGORY = "idCategory";
    private static final String KEY_BRAND = "brand";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_RATING = "rating";
    private static final String KEY_PATH = "path";
    private static final String KEY_ID_PRODUCT = "idProduct";
    private static final String KEY_ID_SHOP = "idShop";
    private static final String KEY_PRICE = "price";
    private static final String KEY_PRICE_MAX = "priceMax";
    private static final String KEY_DATE = "date";
    private static final String KEY_VOLUME = "volume";
    private static final String KEY_UNITS = "units";
    private static final String KEY_ID_LOGO = "idLogo";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_ADDRESS = "address";

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO: CHECK TYPE - LONG vs INTEGER !!!!
        db.execSQL("create table " + TABLE_PRODUCT + " ("
                + KEY_ID + " integer primary key autoincrement,"
                + KEY_ID_CATEGORY + " integer,"
                + KEY_BRAND + " text,"
                + KEY_DESCRIPTION + " text,"
                + KEY_RATING + " integer"
                + ");");

        db.execSQL("create table " + TABLE_CATEGORY_PRODUCT + " ("
                + KEY_ID + " integer primary key autoincrement,"
                + KEY_NAME + " text"
                + ");");

        db.execSQL("create table " + TABLE_COST + " ("
                + KEY_ID + " integer primary key autoincrement,"
                + KEY_ID_PRODUCT + " integer,"
                + KEY_ID_SHOP + " integer,"
                + KEY_PRICE + " real,"
                + KEY_PRICE_MAX + " real,"
                + KEY_DATE + " integer,"
                + KEY_VOLUME + " real,"
                + KEY_UNITS + " integer"
                + ");");

        db.execSQL("create table " + TABLE_IMAGE + " ("
                + KEY_ID + " integer primary key autoincrement,"
                + KEY_PATH + " text,"
                + KEY_ID_PRODUCT + " integer"
                + ");");

        db.execSQL("create table " + TABLE_SHOP + " ("
                + KEY_ID + " integer primary key autoincrement,"
                + KEY_NAME + " text,"
                + KEY_ID_LOGO + " integer,"
                + KEY_LATITUDE + " real,"
                + KEY_LONGITUDE + " real,"
                + KEY_ADDRESS + " text"
                + ");");

        db.execSQL("create table " + TABLE_LOGO_SHOP + " ("
                + KEY_ID + " integer primary key autoincrement,"
                + KEY_PATH + " text"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long addNewProduct(Product product) {
        if (product.getId() < 0) {
            long idCategory = product.getCategoryProduct().getId();
            ContentValues cv = new ContentValues();
            SQLiteDatabase db = this.getWritableDatabase();

            cv.put(KEY_ID_CATEGORY, idCategory);
            cv.put(KEY_BRAND, product.getBrand());
            cv.put(KEY_DESCRIPTION, product.getDescription());
            cv.put(KEY_RATING, product.getRating());
            long i = db.insert(TABLE_PRODUCT, null, cv);
            this.close();
            updateListCost(product.getCostList(), i);
            return i;
        } else {
            updateProduct(product);
            return -1;
        }
    }

    public boolean updateProduct(Product product) {
        if (product.getId() < 0) return false;

        long id = product.getId();
        long idCategory = product.getCategoryProduct().getId();
        String brand = product.getBrand();
        String description = product.getDescription();
        int rating = product.getRating();

        ContentValues cv = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        cv.put(KEY_ID_CATEGORY, idCategory);
        cv.put(KEY_BRAND, brand);
        cv.put(KEY_DESCRIPTION, description);
        cv.put(KEY_RATING, rating);
        int updCount = db.update(TABLE_PRODUCT, cv, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        this.close();
        updateListCost(product.getCostList(), product.getId());
        return updCount > 0;
    }

    //TODO: ADD PAGE_LIMIT !!
    public ArrayList<Product> getAllProduct() {
        ArrayList<Product> listProduct = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCT, null, null, null, null, null, KEY_ID + " DESC");

        // определяем номера столбцов по имени в выборке
        int idColIndex = cursor.getColumnIndex(KEY_ID);
        int idCategory = cursor.getColumnIndex(KEY_ID_CATEGORY);
        int brandColIndex = cursor.getColumnIndex(KEY_BRAND);
        int descriptionColIndex = cursor.getColumnIndex(KEY_DESCRIPTION);
        int ratingColIndex = cursor.getColumnIndex(KEY_RATING);
        if (cursor.moveToFirst()) {
            do {
                Product item = new Product();

                item.setId(cursor.getLong(idColIndex));
                item.setCategoryProduct(getCategoryProduct(cursor.getLong(idCategory)));
                item.setBrand(cursor.getString(brandColIndex));
                item.setDescription(cursor.getString(descriptionColIndex));
                item.setRating(cursor.getInt(ratingColIndex));
                item.setCostList(getListCost(item.getId()));
                listProduct.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.close();
        return listProduct;
    }

    public Product getProduct(long id) {
        if (id < 0) return new Product();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PRODUCT, new String[]{KEY_ID, KEY_ID_CATEGORY, KEY_BRAND, KEY_DESCRIPTION, KEY_RATING},
                KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        Product product = new Product();
        if (cursor != null) {
            int idColIndex = cursor.getColumnIndex(KEY_ID);
            int idCategoryColIndex = cursor.getColumnIndex(KEY_ID_CATEGORY);
            int brandColIndex = cursor.getColumnIndex(KEY_BRAND);
            int descriptionColIndex = cursor.getColumnIndex(KEY_DESCRIPTION);
            int ratingColIndex = cursor.getColumnIndex(KEY_RATING);

            cursor.moveToFirst();
            product.setId(cursor.getLong(idColIndex));
            product.setCategoryProduct(getCategoryProduct(cursor.getLong(idCategoryColIndex)));
            product.setBrand(cursor.getString(brandColIndex));
            product.setDescription(cursor.getString(descriptionColIndex));
            product.setRating(cursor.getInt(ratingColIndex));
            cursor.close();
            product.setCostList(getListCost(product.getId()));
        }

        this.close();
        return product;
    }

    public int removeProduct(long idProduct) {
        SQLiteDatabase db = this.getWritableDatabase();
        int delCount = db.delete(TABLE_PRODUCT, KEY_ID + " = " + idProduct, null);
        db.close();
        return delCount;
    }

    /**
     * ************ CategoryProduct **********
     */

    public long addCategoryProduct(CategoryProduct categoryProduct) {
        if (categoryProduct.getId() < 0) {
            ContentValues cv = new ContentValues();
            String name = categoryProduct.getName();
            SQLiteDatabase db = this.getWritableDatabase();

            cv.put(KEY_NAME, name);
            long i = db.insert(TABLE_CATEGORY_PRODUCT, null, cv);
            this.close();
            return i;
        } else {
            updateCategoryProduct(categoryProduct);
            return -1;
        }
    }

    public boolean updateCategoryProduct(CategoryProduct categoryProduct) {
        if (categoryProduct.getId() < 0) return false;

        long id = categoryProduct.getId();
        String name = categoryProduct.getName();

        ContentValues cv = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        cv.put(KEY_ID, id);
        cv.put(KEY_NAME, name);
        int updCount = db.update(TABLE_CATEGORY_PRODUCT, cv, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        this.close();
        return updCount > 0;
    }

    public CategoryProduct getCategoryProduct(long id) {
        if (id < 0) return new CategoryProduct();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CATEGORY_PRODUCT, new String[]{KEY_ID, KEY_NAME},
                KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        CategoryProduct categoryProduct = new CategoryProduct();
        if (cursor != null) {
            cursor.moveToFirst();

            int idColIndex = cursor.getColumnIndex(KEY_ID);
            int nameColIndex = cursor.getColumnIndex(KEY_NAME);

            String str = cursor.getString(idColIndex);
            //categoryProduct.setId(cursor.getLong(idColIndex));
            categoryProduct.setId(Long.valueOf(str));
            categoryProduct.setName(cursor.getString(nameColIndex));
            cursor.close();
        }

        this.close();
        return categoryProduct;
    }

    public ArrayList<CategoryProduct> getAllCategoryProduct() {
        ArrayList<CategoryProduct> listCategoryProduct = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORY_PRODUCT, null, null, null, null, null, KEY_NAME);
        if (cursor.moveToFirst()) {
            int idColIndex = cursor.getColumnIndex(KEY_ID);
            int nameColIndex = cursor.getColumnIndex(KEY_NAME);

            do {
                CategoryProduct item = new CategoryProduct();
                item.setId(cursor.getLong(idColIndex));
                item.setName(cursor.getString(nameColIndex));
                listCategoryProduct.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.close();
        return listCategoryProduct;
    }

    public int removeCategoryProduct(long idCategory) {
        SQLiteDatabase db = this.getWritableDatabase();
        int delCount = db.delete(TABLE_CATEGORY_PRODUCT, KEY_ID + " = " + idCategory, null);
        db.close();
        return delCount;
    }

    /**
     * ************ Cost **********
     */

    public long addCost(Cost cost, long idProduct) {
        if (cost.getId() < 0) {
            long idShop = cost.getShop().getId();
            double price = cost.getPrice();
            double priceMax = cost.getPriceMax();
            long date = cost.getDate();
            double volume = cost.getVolume();
            int units = cost.getUnits().getId();
            ContentValues cv = new ContentValues();
            SQLiteDatabase db = this.getWritableDatabase();

            cv.put(KEY_ID_PRODUCT, idProduct);
            cv.put(KEY_ID_SHOP, idShop);
            cv.put(KEY_PRICE, price);
            cv.put(KEY_PRICE_MAX, priceMax);
            cv.put(KEY_DATE, date);
            cv.put(KEY_VOLUME, volume);
            cv.put(KEY_UNITS, units);
            long i = db.insert(TABLE_COST, null, cv);
            this.close();
            return i;
        } else {
            updateCost(cost, idProduct);
            return -1;
        }
    }

    public void updateListCost(ArrayList<Cost> listCost, long idProduct) {
        if (listCost != null) {
            SQLiteDatabase db = this.getWritableDatabase();
            for (int i = 0; i < listCost.size(); i++) {
                Cost cost = listCost.get(i);
                ContentValues cv = new ContentValues();
                cv.put(KEY_ID_SHOP, cost.getShop().getId());
                cv.put(KEY_ID_PRODUCT, idProduct);
                cv.put(KEY_PRICE, cost.getPrice());
                cv.put(KEY_PRICE_MAX, cost.getPriceMax());
                cv.put(KEY_DATE, cost.getDate());
                cv.put(KEY_VOLUME, cost.getVolume());
                cv.put(KEY_UNITS, cost.getUnits().getId());
                db.update(TABLE_COST, cv, KEY_ID + " = ?",
                        new String[]{String.valueOf(cost.getId())});
            }
            this.close();
        }
    }

    public boolean updateCost(Cost cost, long idProduct) {
        if (cost.getId() < 0) return false;

        long id = cost.getId();
        long idShop = cost.getShop().getId();
        double price = cost.getPrice();
        double priceMax = cost.getPriceMax();
        long date = cost.getDate();
        double volume = cost.getVolume();
        int units = cost.getUnits().getId();

        ContentValues cv = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        cv.put(KEY_ID_SHOP, idShop);
        cv.put(KEY_ID_PRODUCT, idProduct);
        cv.put(KEY_PRICE, price);
        cv.put(KEY_PRICE_MAX, priceMax);
        cv.put(KEY_DATE, date);
        cv.put(KEY_VOLUME, volume);
        cv.put(KEY_UNITS, units);
        int updCount = db.update(TABLE_COST, cv, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        this.close();
        return updCount > 0;
    }

    public ArrayList<Cost> getListCost(long idProduct) {
        ArrayList<Cost> listCost = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COST, null, KEY_ID_PRODUCT + "=?",
                new String[]{String.valueOf(idProduct)},
                null, null, KEY_DATE + " DESC");

        if (cursor != null) {
            int idColIndex = cursor.getColumnIndex(KEY_ID);
            int idShopColIndex = cursor.getColumnIndex(KEY_ID_SHOP);
            int priceColIndex = cursor.getColumnIndex(KEY_PRICE);
            int priceMaxColIndex = cursor.getColumnIndex(KEY_PRICE_MAX);
            int dateColIndex = cursor.getColumnIndex(KEY_DATE);
            int volumeColIndex = cursor.getColumnIndex(KEY_VOLUME);
            int unitsColIndex = cursor.getColumnIndex(KEY_UNITS);

            if (cursor.moveToFirst()) {
                do {
                    Cost item = new Cost();
                    item.setId(cursor.getLong(idColIndex));
                    item.setPrice(cursor.getDouble(priceColIndex));
                    item.setPriceMax(cursor.getDouble(priceMaxColIndex));
                    item.setDate(cursor.getLong(dateColIndex));
                    item.setVolume(cursor.getDouble(volumeColIndex));
                    item.setUnits(new Units(cursor.getInt(unitsColIndex)));
                    item.setShop(getShop(cursor.getInt(idShopColIndex)));

                    listCost.add(item);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        this.close();
        return listCost;
    }

    public int removeCost(long idCost) {
        SQLiteDatabase db = this.getWritableDatabase();
        int delCount = db.delete(TABLE_CATEGORY_PRODUCT, KEY_ID + " = " + idCost, null);
        db.close();
        return delCount;
    }

    /**
     * ************ Image **********
     */

    public long addImage(Image image, long idProduct) {
        if (image.getId() < 0) {
            String path = image.getPath();
            ContentValues cv = new ContentValues();
            SQLiteDatabase db = this.getWritableDatabase();

            cv.put(KEY_ID_PRODUCT, idProduct);
            cv.put(KEY_PATH, path);
            long i = db.insert(TABLE_IMAGE, null, cv);
            this.close();
            return i;
        } else {
            //TODO: Add UPDATE Image !!!!
            return -1;
        }
    }

    public ArrayList<Image> getListImage(long idProduct) {
        ArrayList<Image> listImage = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_LOGO_SHOP,
                new String[]{KEY_ID, KEY_ID_PRODUCT, KEY_PATH},
                KEY_ID_PRODUCT + "=?",
                new String[]{String.valueOf(idProduct)}, null, null, null, null);

        if (cursor != null) {
            int idColIndex = cursor.getColumnIndex(KEY_ID);
            int pathColIndex = cursor.getColumnIndex(KEY_PATH);

            if (cursor.moveToFirst()) {
                do {
                    Image item = new Image();
                    item.setId(cursor.getLong(idColIndex));
                    item.setPath(cursor.getString(pathColIndex));
                    listImage.add(item);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        this.close();
        return listImage;
    }

    /**
     * ************ Shop **********
     */

    public long addShop(Shop shop) {
        if (shop.getId() < 0) {
            String name = shop.getName();
            double latitude = shop.getLatitude();
            double longitude = shop.getLongitude();
            String address = shop.getAddress();
            long idLogo = shop.getImage().getId();
            if (idLogo < 0)
                idLogo = addLogoShop(shop.getImage());

            ContentValues cv = new ContentValues();
            SQLiteDatabase db = this.getWritableDatabase();

            cv.put(KEY_NAME, name);
            cv.put(KEY_ID_LOGO, idLogo);
            cv.put(KEY_LATITUDE, latitude);
            cv.put(KEY_LONGITUDE, longitude);
            cv.put(KEY_ADDRESS, address);

            long i = db.insert(TABLE_SHOP, null, cv);
            this.close();
            return i;
        } else {
            updateShop(shop);
            return shop.getId();
        }
    }

    public boolean updateShop(Shop shop) {
        if (shop.getId() < 0) return false;

        long id = shop.getId();
        String name = shop.getName();
        long idLogo = shop.getImage().getId();
        double latitude = shop.getLatitude();
        double longitude = shop.getLongitude();
        String address = shop.getAddress();

        ContentValues cv = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        cv.put(KEY_ID, id);
        cv.put(KEY_NAME, name);
        cv.put(KEY_ID_LOGO, idLogo);
        cv.put(KEY_LATITUDE, latitude);
        cv.put(KEY_LONGITUDE, longitude);
        cv.put(KEY_ADDRESS, address);
        // обновляем по id
        int updCount = db.update(TABLE_SHOP, cv, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        this.close();
        return updCount > 0;
    }

    public Shop getShop(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SHOP,
                new String[]{KEY_ID, KEY_NAME, KEY_ID_LOGO, KEY_LATITUDE, KEY_LONGITUDE, KEY_ADDRESS},
                KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        Shop shop = new Shop();
        if (cursor != null) {
            int idColIndex = cursor.getColumnIndex(KEY_ID);
            int nameColIndex = cursor.getColumnIndex(KEY_NAME);
            int idLogoColIndex = cursor.getColumnIndex(KEY_ID_LOGO);
            int latitudeColIndex = cursor.getColumnIndex(KEY_LATITUDE);
            int longitudeColIndex = cursor.getColumnIndex(KEY_LONGITUDE);
            int addressColIndex = cursor.getColumnIndex(KEY_ADDRESS);

            cursor.moveToFirst();
            shop.setId(cursor.getLong(idColIndex));
            shop.setName(cursor.getString(nameColIndex));
            long idLogo = cursor.getLong(idLogoColIndex);
            shop.setLatitude(cursor.getDouble(latitudeColIndex));
            shop.setLongitude(cursor.getDouble(longitudeColIndex));
            shop.setAddress(cursor.getString(addressColIndex));
            cursor.close();
            shop.setImage(getLogoShop(idLogo));
        }
        return shop;
    }

    public ArrayList<Shop> getAllShop() {
        ArrayList<Shop> listShop = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        //String selectQuery = "SELECT  * FROM " + TABLE_SHOP;
        Cursor cursor = db.query(TABLE_SHOP, null, null, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int idColIndex = cursor.getColumnIndex(KEY_ID);
                int nameColIndex = cursor.getColumnIndex(KEY_NAME);
                int idLogoColIndex = cursor.getColumnIndex(KEY_ID_LOGO);
                int latitudeColIndex = cursor.getColumnIndex(KEY_LATITUDE);
                int longitudeColIndex = cursor.getColumnIndex(KEY_LONGITUDE);
                int addressColIndex = cursor.getColumnIndex(KEY_ADDRESS);
                do {
                    Shop shop = new Shop();
                    shop.setId(cursor.getLong(idColIndex));
                    shop.setName(cursor.getString(nameColIndex));
                    long idLogo = cursor.getLong(idLogoColIndex);
                    shop.setLatitude(cursor.getDouble(latitudeColIndex));
                    shop.setLongitude(cursor.getDouble(longitudeColIndex));
                    shop.setAddress(cursor.getString(addressColIndex));

                    shop.setImage(getLogoShop(idLogo));
                    listShop.add(shop);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return listShop;
    }

    public int removeShop(long idShop) {
        SQLiteDatabase db = this.getWritableDatabase();
        int delCount = db.delete(TABLE_SHOP, KEY_ID + " = " + idShop, null);
        db.close();
        return delCount;
    }

    /**
     * ************ LogoShop **********
     */

    public long addLogoShop(Image image) {
        String path = image.getPath();
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        cv.put(KEY_PATH, path);
        long i = db.insert(TABLE_LOGO_SHOP, null, cv);
        this.close();
        return i;
    }

    public boolean updateLogoShop(Image image) {
        if (image.getId() < 0) return false;

        long id = image.getId();
        String path = image.getPath();

        ContentValues cv = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        cv.put(KEY_ID, id);
        cv.put(KEY_PATH, path);
        // обновляем по id
        int updCount = db.update(TABLE_LOGO_SHOP, cv, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        this.close();
        return updCount > 0;
    }

    public Image getLogoShop(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LOGO_SHOP,
                new String[]{KEY_ID, KEY_PATH},
                KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        Image image = new Image();
        if (cursor != null) {
            int idColIndex = cursor.getColumnIndex(KEY_ID);
            int pathColIndex = cursor.getColumnIndex(KEY_PATH);
            cursor.moveToFirst();
            image.setId(cursor.getLong(idColIndex));
            image.setPath(cursor.getString(pathColIndex));
            cursor.close();
        }
        return image;
    }

}
