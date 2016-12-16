package com.example.jsheng.rxjavademo.bean;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by jun.sheng on 2016/12/15.
 */

@Data
@Accessors(prefix = "m")
public class AppInfo implements Comparable<Object> {

    long mLastUpdateTime;
    String mName;
    String mIcon;

    public AppInfo(String name, String icon, long lastUpdateTime) {
        mName = name;
        mIcon = icon;
        mLastUpdateTime = lastUpdateTime;
    }

    @Override
    public int compareTo(Object o) {
        AppInfo appInfo = (AppInfo) o;
        return getName().compareTo(appInfo.getName());
    }
}
