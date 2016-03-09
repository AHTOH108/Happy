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

    private static final int DATABASE_VERSION = 1;
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
            int idCategory = (int) addCategoryProduct(product.getCategoryProduct());
            ContentValues cv = new ContentValues();
            SQLiteDatabase db = this.getWritableDatabase();

            cv.put(KEY_ID_CATEGORY, idCategory);
            cv.put(KEY_BRAND, product.getBrand());
            cv.put(KEY_DESCRIPTION, product.getDescription());
            cv.put(KEY_RATING, product.getRating());
            long i = db.insert(TABLE_PRODUCT, null, cv);
            this.close();
            return i;
        }else{
            //TODO: Add UPDATE !!!!
            return -1;
        }
    }

        //TODO: ADD PAGE_LIMIT !!
    public ArrayList<Product> getAllProduct() {
        ArrayList<Product> listProduct = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCT, null, null, null, null, null, null);

// определяем номера столбцов по имени в выборке
        int idColIndex = cursor.getColumnIndex(KEY_ID);
        int brandColIndex = cursor.getColumnIndex(KEY_BRAND);
        int descriptionColIndex = cursor.getColumnIndex(KEY_DESCRIPTION);
        int ratingColIndex = cursor.getColumnIndex(KEY_RATING);
        if (cursor.moveToFirst()) {
            do {
                Product item = new Product();


                item.setId(cursor.getInt(idColIndex));
                // item.setCategoryProduct(getCategoryProduct(cursor.getInt(1)));
                item.setBrand(cursor.getString(brandColIndex));
                item.setDescription(cursor.getString(descriptionColIndex));
                item.setRating(cursor.getInt(ratingColIndex));
                listProduct.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.close();
        return listProduct;
    }

    public long addCategoryProduct(CategoryProduct categoryProduct) {
        if (categoryProduct.getId() < 0) {
            ContentValues cv = new ContentValues();
            String name = categoryProduct.getName();
            SQLiteDatabase db = this.getWritableDatabase();

            cv.put(KEY_NAME, name);
            long i = db.insert(TABLE_CATEGORY_PRODUCT, null, cv);
            this.close();
            return i;
        }else{
            //TODO: Add UPDATE !!!!
            return -1;
        }
    }

    public CategoryProduct getCategoryProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CATEGORY_PRODUCT, new String[]{KEY_ID, KEY_NAME},
                KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        CategoryProduct categoryProduct = new CategoryProduct();
        if (cursor != null) {
            cursor.moveToFirst();
            String str = cursor.getString(0);
            categoryProduct.setId(Integer.parseInt(str));
            categoryProduct.setName(cursor.getString(1));
            cursor.close();
        }

        this.close();
        return categoryProduct;
    }

    public ArrayList<CategoryProduct> getAllCategoryProduct() {
        ArrayList<CategoryProduct> listCategoryProduct = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY_PRODUCT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CategoryProduct item = new CategoryProduct();
                item.setId(cursor.getInt(0));
                item.setName(cursor.getString(1));
                listCategoryProduct.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.close();
        return listCategoryProduct;
    }

    public long addCost(Cost cost, int idProduct) {
        if (cost.getId() < 0) {
            int idShop = cost.getShop().getId();
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
        }else{
            //TODO: Add UPDATE!!!!
            return -1;
        }
    }

    public ArrayList<Cost> getListCost(int idProduct) {
        ArrayList<Cost> listCost = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_COST,
                new String[]{KEY_ID, KEY_ID_PRODUCT, KEY_ID_SHOP, KEY_PRICE, KEY_PRICE_MAX, KEY_DATE, KEY_VOLUME, KEY_UNITS},
                KEY_ID_PRODUCT + "=?",
                new String[]{String.valueOf(idProduct)}, null, null, null, null);

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
                    item.setId(cursor.getInt(idColIndex));
                    item.setPrice(cursor.getDouble(priceColIndex));
                    item.setPriceMax(cursor.getDouble(priceMaxColIndex));
                    item.setDate(cursor.getInt(dateColIndex));
                    item.setVolume(cursor.getInt(volumeColIndex));
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

    public long addImage(Image image, int idProduct) {
        if (image.getId() < 0) {
            String path = image.getPath();
            ContentValues cv = new ContentValues();
            SQLiteDatabase db = this.getWritableDatabase();

            cv.put(KEY_ID_PRODUCT, idProduct);
            cv.put(KEY_PATH, path);
            long i = db.insert(TABLE_IMAGE, null, cv);
            this.close();
            return i;
        }else{
            //TODO: Add UPDATE Image !!!!
            return -1;
        }
    }

    public ArrayList<Image> getListImage(int idProduct) {
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
                    item.setId(cursor.getInt(idColIndex));
                    item.setPath(cursor.getString(pathColIndex));
                    listImage.add(item);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        this.close();
        return listImage;
    }

    public long addShop(Shop shop) {
        if (shop.getId() < 0) {
            String name = shop.getName();
            double latitude = shop.getLatitude();
            double longitude = shop.getLongitude();
            String address = shop.getAddress();
            int idLogo = shop.getImage().getId();
            if (idLogo < 0)
                idLogo = (int) addLogoShop(shop.getImage());

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
        }else{
            updateShop(shop);
            return shop.getId();
        }
    }

    public boolean updateShop(Shop shop) {
        if (shop.getId() < 0) return false;

        int id = shop.getId();
        String name = shop.getName();
        int idLogo = shop.getImage().getId();
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

    public Shop getShop(int id) {
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
            shop.setId(cursor.getInt(idColIndex));
            shop.setName(cursor.getString(nameColIndex));
            int idLogo = cursor.getInt(idLogoColIndex);
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
        String selectQuery = "SELECT  * FROM " + TABLE_SHOP;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            int idColIndex = cursor.getColumnIndex(KEY_ID);
            int nameColIndex = cursor.getColumnIndex(KEY_NAME);
            int idLogoColIndex = cursor.getColumnIndex(KEY_ID_LOGO);
            int latitudeColIndex = cursor.getColumnIndex(KEY_LATITUDE);
            int longitudeColIndex = cursor.getColumnIndex(KEY_LONGITUDE);
            int addressColIndex = cursor.getColumnIndex(KEY_ADDRESS);

            if (cursor.moveToFirst()) {
                do {
                    Shop shop = new Shop();
                    cursor.moveToFirst();
                    shop.setId(cursor.getInt(idColIndex));
                    shop.setName(cursor.getString(nameColIndex));
                    int idLogo = cursor.getInt(idLogoColIndex);
                    shop.setLatitude(cursor.getDouble(latitudeColIndex));
                    shop.setLongitude(cursor.getDouble(longitudeColIndex));
                    shop.setAddress(cursor.getString(addressColIndex));

                    shop.setImage(getLogoShop(idLogo));
                    listShop.add(shop);
                    //TODO: тут не работает! не переключает на следующую запись!
                } while (cursor.moveToNext() && listShop.size() < 100);
            }
            cursor.close();
        }
        return listShop;
    }

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

        int id = image.getId();
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

    public Image getLogoShop(int id) {
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
            image.setId(cursor.getInt(idColIndex));
            image.setPath(cursor.getString(pathColIndex));
            cursor.close();
        }
        return image;
    }

}
