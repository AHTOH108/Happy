package com.iandp.happy.database;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;

import com.iandp.happy.database.DBProduct;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DBPRODUCT".
*/
public class DBProductDao extends AbstractDao<DBProduct, Long> {

    public static final String TABLENAME = "DBPRODUCT";

    /**
     * Properties of entity DBProduct.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Brand = new Property(1, String.class, "brand", false, "BRAND");
        public final static Property Description = new Property(2, String.class, "description", false, "DESCRIPTION");
        public final static Property Rating = new Property(3, Byte.class, "rating", false, "RATING");
        public final static Property CategoryId = new Property(4, long.class, "categoryId", false, "CATEGORY_ID");
    };

    private DaoSession daoSession;


    public DBProductDao(DaoConfig config) {
        super(config);
    }
    
    public DBProductDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DBPRODUCT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"BRAND\" TEXT," + // 1: brand
                "\"DESCRIPTION\" TEXT," + // 2: description
                "\"RATING\" INTEGER," + // 3: rating
                "\"CATEGORY_ID\" INTEGER NOT NULL );"); // 4: categoryId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DBPRODUCT\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, DBProduct entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String brand = entity.getBrand();
        if (brand != null) {
            stmt.bindString(2, brand);
        }
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(3, description);
        }
 
        Byte rating = entity.getRating();
        if (rating != null) {
            stmt.bindLong(4, rating);
        }
        stmt.bindLong(5, entity.getCategoryId());
    }

    @Override
    protected void attachEntity(DBProduct entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public DBProduct readEntity(Cursor cursor, int offset) {
        DBProduct entity = new DBProduct( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // brand
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // description
            cursor.isNull(offset + 3) ? null : (byte) cursor.getShort(offset + 3), // rating
            cursor.getLong(offset + 4) // categoryId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, DBProduct entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setBrand(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDescription(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setRating(cursor.isNull(offset + 3) ? null : (byte) cursor.getShort(offset + 3));
        entity.setCategoryId(cursor.getLong(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(DBProduct entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(DBProduct entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getDBCategoryProductDao().getAllColumns());
            builder.append(" FROM DBPRODUCT T");
            builder.append(" LEFT JOIN DBCATEGORY_PRODUCT T0 ON T.\"CATEGORY_ID\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected DBProduct loadCurrentDeep(Cursor cursor, boolean lock) {
        DBProduct entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        DBCategoryProduct categoryProduct = loadCurrentOther(daoSession.getDBCategoryProductDao(), cursor, offset);
         if(categoryProduct != null) {
            entity.setCategoryProduct(categoryProduct);
        }

        return entity;    
    }

    public DBProduct loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<DBProduct> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<DBProduct> list = new ArrayList<DBProduct>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<DBProduct> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<DBProduct> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
