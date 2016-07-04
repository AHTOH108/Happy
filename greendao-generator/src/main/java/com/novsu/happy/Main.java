package com.novsu.happy;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class Main {

    private static final int VERSION = 1;
    private static final String PACKAGE_DESTINATION = "com.iandp.happy.database";
    private static final String DESTINATION_FOLDER = "app/src/main/java";

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(VERSION, PACKAGE_DESTINATION);

        Entity shop = schema.addEntity("Shop");
        shop.addIdProperty();
        shop.addStringProperty("name");
        shop.addDoubleProperty("latitude");
        shop.addDoubleProperty("longitude");
        shop.addStringProperty("address");

        Entity image = schema.addEntity("Image");
        image.addIdProperty();
        image.addStringProperty("path");

        Property imageId = shop.addLongProperty("imageId").getProperty();
        shop.addToOne(image, imageId);

        new DaoGenerator().generateAll(schema, DESTINATION_FOLDER);
    }
}
