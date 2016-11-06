package com.iandp.happy.database.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.iandp.happy.database.DBCategoryProduct;
import com.iandp.happy.database.DBCost;
import com.iandp.happy.database.DBImage;
import com.iandp.happy.database.DBProduct;
import com.iandp.happy.database.DBProductSimple;
import com.iandp.happy.database.DBProductSimpleDao;
import com.iandp.happy.database.DBShop;
import com.iandp.happy.database.DaoMaster;
import com.iandp.happy.database.DaoSession;
import com.iandp.happy.model.object.Product;
import com.iandp.happy.model.object.ProductSimple;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.greenrobot.dao.async.AsyncOperation;
import de.greenrobot.dao.async.AsyncOperationListener;
import de.greenrobot.dao.async.AsyncSession;
import de.greenrobot.dao.query.WhereCondition;

/**
 * Created on 30.10.2016.
 */

public class DatabaseManager implements IDatabaseManager, AsyncOperationListener {

    private static DatabaseManager instance;
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase database;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private AsyncSession asyncSession;
    private List<AsyncOperation> completedOperations;

    /**
     * Constructs a new DatabaseManager with the specified arguments.
     *
     * @param context The Android {@link android.content.Context}.
     */
    public DatabaseManager(final Context context) {
        /*
        The Android Activity reference for access to DatabaseManager.
        */
        //Context context = context;
        mHelper = new DaoMaster.DevOpenHelper(context, "dbHappy", null);
        completedOperations = new CopyOnWriteArrayList<>();
    }

    /**
     * @param context The Android {@link android.content.Context}.
     * @return this.instance
     */
    public static DatabaseManager getInstance(Context context) {

        if (instance == null) {
            instance = new DatabaseManager(context);
        }

        return instance;
    }

    @Override
    public void onAsyncOperationCompleted(AsyncOperation operation) {
        completedOperations.add(operation);
    }

    /**
     * Query for readable DB
     */
    public void openReadableDb() throws SQLiteException {
        database = mHelper.getReadableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
        asyncSession = daoSession.startAsyncSession();
        asyncSession.setListener(this);
    }

    /**
     * Query for writable DB
     */
    public void openWritableDb() throws SQLiteException {
        database = mHelper.getWritableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
        asyncSession = daoSession.startAsyncSession();
        asyncSession.setListener(this);
    }

    @Override
    public void closeDbConnections() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
        if (database != null && database.isOpen()) {
            database.close();
        }
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
        if (instance != null) {
            instance = null;
        }
    }

    @Override
    public void dropDatabase() {
        try {
            openWritableDb();
            DaoMaster.dropAllTables(database, true); // drops all tables
            mHelper.onCreate(database);              // creates the tables
            asyncSession.deleteAll(DBProduct.class);    // clear all elements from a table
            asyncSession.deleteAll(DBCategoryProduct.class);
            asyncSession.deleteAll(DBImage.class);
            asyncSession.deleteAll(DBCost.class);
            asyncSession.deleteAll(DBShop.class);
            asyncSession.deleteAll(DBProductSimple.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ProductSimple insertProduct(ProductSimple product) {
        DBProductSimple dbProduct;
        try {
            if (product != null) {
                dbProduct = TranslatorObjectDB.translateProductSimple(product);

                openWritableDb();
                DBProductSimpleDao productDao = daoSession.getDBProductSimpleDao();
                if (dbProduct.getId() < 0){
                    dbProduct.setId(null);
                    product.setId(productDao.insert(dbProduct));
                }else {
                    DBProductSimple oldDBProduct = productDao.load(dbProduct.getId());
                    if (oldDBProduct == null) {
                        productDao.insert(dbProduct);
                    } else {
                        daoSession.update(dbProduct);
                    }
                }

                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return product;
    }

    @Override
    public void removeProductSimple(long productId) {
        try {
            openReadableDb();
            DBProductSimpleDao productDao = daoSession.getDBProductSimpleDao();
            productDao.deleteByKey(productId);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<ProductSimple> listProductSimple(int lastId, int limit) {
        List<ProductSimple> listProduct = new ArrayList<>();
        try {
            openReadableDb();
            DBProductSimpleDao productDao = daoSession.getDBProductSimpleDao();

            List<DBProductSimple> listDBProduct;
            if (limit > 0)
                listDBProduct = productDao.queryBuilder()
                        .orderDesc(DBProductSimpleDao.Properties.Id) // sort
                        .where(DBProductSimpleDao.Properties.Id.gt(lastId))
                        .limit(limit)
                        .list();
            else
                listDBProduct = productDao.queryBuilder()
                        .orderDesc(DBProductSimpleDao.Properties.Id) // sort
                        .where(DBProductSimpleDao.Properties.Id.gt(lastId))
                        .list();

            for (DBProductSimple dbProduct : listDBProduct) {
                listProduct.add(TranslatorObjectDB.translateDBProductSimple(dbProduct));
            }
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>(listProduct);
    }

    @Override
    public DBProduct insertProduct(Product product) {
        return null;
    }

    @Override
    public void updateProduct(Product product) {

    }

    @Override
    public void removeProduct(Long productId) {

    }

    @Override
    public Product getProductById(Long productId) {
        return null;
    }

    @Override
    public ArrayList<Product> listProduct(int lastId, int limit) {
        return null;
    }

}
