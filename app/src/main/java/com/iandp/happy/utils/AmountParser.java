package com.iandp.happy.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 03.11.2016.
 */

public class AmountParser {

    public static final byte ID_AMOUNT_THING = 1;
    public static final byte ID_AMOUNT_KG = 2;
    public static final byte ID_AMOUNT_LITER = 3;

    public static final String AMOUNT_THING = "штук";
    public static final String AMOUNT_THING_SHORT = "шт.";
    public static final String AMOUNT_KG = "Киллограмм";
    public static final String AMOUNT_KG_SHORT = "Кг.";
    public static final String AMOUNT_LITER = "литр";
    public static final String AMOUNT_LITER_SHORT = "л.";

    private ArrayList<Byte> listType = new ArrayList<>();

    public static byte getNextType(byte type){
        switch (type){
            case ID_AMOUNT_THING:
                type = ID_AMOUNT_KG;
                break;
            case ID_AMOUNT_KG:
                type = ID_AMOUNT_LITER;
                break;
            case ID_AMOUNT_LITER:
            default:
                type = ID_AMOUNT_THING;
                break;
        }
        return type;
    }

    public static String getShortNameType(byte type){
        String name = "";
        switch (type){
            case ID_AMOUNT_THING:
                name = AMOUNT_THING_SHORT;
                break;
            case ID_AMOUNT_KG:
                name = AMOUNT_KG_SHORT;
                break;
            case ID_AMOUNT_LITER:
                name = AMOUNT_LITER_SHORT;
                break;
        }
        return name;
    }

}
