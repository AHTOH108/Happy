package com.iandp.happy.database;

import com.iandp.happy.database.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "DBSHOP".
 */
public class DBShop {

    private Long id;
    private String name;
    private Double latitude;
    private Double longitude;
    private String address;
    private long imageId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient DBShopDao myDao;

    private DBImage image;
    private Long image__resolvedKey;


    public DBShop() {
    }

    public DBShop(Long id) {
        this.id = id;
    }

    public DBShop(Long id, String name, Double latitude, Double longitude, String address, long imageId) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.imageId = imageId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDBShopDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    /** To-one relationship, resolved on first access. */
    public DBImage getImage() {
        long __key = this.imageId;
        if (image__resolvedKey == null || !image__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DBImageDao targetDao = daoSession.getDBImageDao();
            DBImage imageNew = targetDao.load(__key);
            synchronized (this) {
                image = imageNew;
            	image__resolvedKey = __key;
            }
        }
        return image;
    }

    public void setImage(DBImage image) {
        if (image == null) {
            throw new DaoException("To-one property 'imageId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.image = image;
            imageId = image.getId();
            image__resolvedKey = imageId;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}