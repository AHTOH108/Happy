package com.iandp.happy.model;

import android.content.Context;

import com.iandp.happy.model.dataBase.DBHelper;

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

    public DBHelper getDbHelper() {
        return dbHelper;
    }
}
