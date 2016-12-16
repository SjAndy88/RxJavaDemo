package com.example.jsheng.rxjavademo;

import android.app.Application;
import android.content.Context;
/**
 * Created by jun.sheng on 2016/12/15.
 */

public class App extends Application {

    public static Context instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
