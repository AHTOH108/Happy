package com.iandp.happy.database;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "DBCATEGORY_PRODUCT".
 */
public class DBCategoryProduct {

    private Long id;
    private String name;

    public DBCategoryProduct() {
    }

    public DBCategoryProduct(Long id) {
        this.id = id;
    }

    public DBCategoryProduct(Long id, String name) {
        this.id = id;
        this.name = name;
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

}
