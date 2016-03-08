package com.iandp.happy.model;

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
public class DBDataModule {

    private DBHelper dbHelper;

    private static volatile DBDataModule sDBDataModule;

    private DBDataModule(Context appContext) {
        this.dbHelper = new DBHelper(appContext);
    }

    public static DBDataModule get(Context c) {
        try {
            if (sDBDataModule == null) {
                synchronized (DBDataModule.class) {
                    if (sDBDataModule == null) {
                        sDBDataModule = new DBDataModule(c.getApplicationContext());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sDBDataModule = new DBDataModule(c.getApplicationContext());
        }
        return sDBDataModule;
    }

    public DBHelper getDbHelper(){
        return dbHelper;
    }
}
