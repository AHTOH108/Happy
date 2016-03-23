package com.iandp.happy.utils;


import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Date creation: 23.03.2016.
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }


}
