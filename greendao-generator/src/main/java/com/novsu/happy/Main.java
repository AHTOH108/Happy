package com.novsu.happy;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class Main {

    private static final int VERSION = 1;
    private static final String PACKAGE_DESTINATION = "com.iandp.happy.database";
    private static final String DESTINATION_FOLDER = "app/src/main/java";

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(VERSION, PACKAGE_DESTINATION);

        addTables(schema);

        new DaoGenerator().generateAll(schema, DESTINATION_FOLDER);

    }

    private static void addTables(Schema schema) {
        Entity product = addProduct(schema);
        Entity image = addImage(schema);
        Entity categoryProduct = addCategoryProduct(schema);
        Entity cost = addCost(schema);
        Entity shop = addShop(schema);

        Property categoryIdForProduct = product.addLongProperty("categoryId").notNull().getProperty();
        Property shopIdForCost = cost.addLongProperty("shopId").notNull().getProperty();
        Property productIdForImage = image.addLongProperty("productId").notNull().getProperty();
        Property productIdForCost = cost.addLongProperty("productId").notNull().getProperty();
        Property imageIdForShop = shop.addLongProperty("imageId").notNull().getProperty();
        //Property shopIdForImage = image.addLongProperty("shopId").notNull().getProperty();

        product.addToOne(categoryProduct, categoryIdForProduct, "categoryProduct");
        cost.addToOne(shop, shopIdForCost, "shop");
        shop.addToOne(image, imageIdForShop, "image");

        ToMany productToImage = product.addToMany(image, productIdForImage);
        ToMany productToCost = product.addToMany(cost, productIdForCost);
        //ToMany shopToImage = shop.addToMany(image, shopIdForImage);

        productToImage.setName("images");
        productToCost.setName("costs");
        //shopToImage.setName("images");
    }

    /**
     * Create product Properties
     *
     * @return DBProduct entity
     */
    private static Entity addProduct(Schema schema) {
        Entity entity = schema.addEntity("DBProduct");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.addStringProperty("brand");
        entity.addStringProperty("description");
        entity.addByteProperty("rating");
        return entity;
    }

    /**
     * Create image Properties
     *
     * @return DBImage entity
     */
    private static Entity addImage(Schema schema) {
        Entity entity = schema.addEntity("DBImage");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.addStringProperty("path");
        return entity;
    }

    /**
     * Create categoryProduct Properties
     *
     * @return DBCategoryProduct entity
     */
    private static Entity addCategoryProduct(Schema schema) {
        Entity entity = schema.addEntity("DBCategoryProduct");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.addStringProperty("name");
        return entity;
    }

    /**
     * Create cost Properties
     *
     * @return DBCost entity
     */
    private static Entity addCost(Schema schema) {
        Entity entity = schema.addEntity("DBCost");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.addDoubleProperty("price");
        entity.addDoubleProperty("priceMax");
        entity.addLongProperty("date");
        entity.addDoubleProperty("amount");
        entity.addByteProperty("typeAmount");
        return entity;
    }

    /**
     * Create shop Properties
     *
     * @return DBShop entity
     */
    private static Entity addShop(Schema schema) {
        Entity entity = schema.addEntity("DBShop");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.addStringProperty("name");
        entity.addDoubleProperty("latitude");
        entity.addDoubleProperty("longitude");
        entity.addStringProperty("address");
        return entity;
    }
}
