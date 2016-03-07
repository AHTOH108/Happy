package com.iandp.happy.model.api;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.iandp.happy.model.dataBase.DBHelper;
import com.iandp.happy.model.object.CategoryProduct;
import com.iandp.happy.utils.Constants;

import java.util.ArrayList;

/**
 * Created on 19.01.2016.
 */
public class DBApi {


    private DBHelper dbHelper;

    private static volatile DBApi sDBApi;

    private DBApi(Context appContext) {
        this.dbHelper = new DBHelper(appContext);
    }

    public static DBApi get(Context c) {
        try {
            if (sDBApi == null) {
                synchronized (DBApi.class) {
                    if (sDBApi == null) {
                        sDBApi = new DBApi(c.getApplicationContext());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sDBApi = new DBApi(c.getApplicationContext());
        }
        return sDBApi;
    }

    public boolean addNewCategoryPoduct(CategoryProduct categoryProduct) {
        ContentValues cv = new ContentValues();
        String name = categoryProduct.getName();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        cv.put(Constants.NAME_COLUMN_NAME, name);
        db.insert(Constants.NAME_DB_CATEGORY_PRODUCT, null, cv);
        return true;
    }

    public ArrayList<CategoryProduct> getAllCategoryPoduct() {
        ArrayList<CategoryProduct> categoryProducts = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query(Constants.NAME_DB_CATEGORY_PRODUCT, null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex(Constants.NAME_COLUMN_NAME);
            do {
                // получаем значения по номерам столбцов и пишем все в лог
                CategoryProduct categoryProduct= new CategoryProduct(c.getInt(idColIndex), c.getString(nameColIndex));

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        }
        c.close();
        return categoryProducts;
    }
}
