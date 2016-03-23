package com.iandp.happy.utils;


import com.iandp.happy.model.object.Units;

/**
 * Date creation: 23.03.2016.
 */
public class StringUtils {


    public static String getPriceRub(double price) {
        return String.format("%1.2f", price) + " руб.";
        //return String.format("%1.2f", price).replace(",", ".") + "руб.";
    }

    public static String getForUnits(double volume, Units units) {
        return "за " + String.format("%1.2f", volume).replace(",", ".") + " "+ units.getShortName();
    }

}
