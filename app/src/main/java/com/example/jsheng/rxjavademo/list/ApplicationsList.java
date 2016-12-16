package com.example.jsheng.rxjavademo.list;

import com.example.jsheng.rxjavademo.bean.AppInfo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Created by jun.sheng on 2016/12/16.
 */

@Accessors(prefix = "m")
public class ApplicationsList {

    private static ApplicationsList sInstance = new ApplicationsList();

    @Getter
    @Setter
    private List<AppInfo> mList;

    private ApplicationsList() {

    }

    public static ApplicationsList getInstance() {
        return sInstance;
    }
}
