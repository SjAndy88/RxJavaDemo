package com.example.jsheng.rxjavademo.navigation;

import android.graphics.drawable.Drawable;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Created by shengjun on 2016/12/18.
 */
@Accessors(prefix = "m")
public class NavigationItem {

    @Getter
    String mText;

    @Getter
    Drawable mDrawable;

    public NavigationItem(String text, Drawable drawable) {
        mText = text;
        mDrawable = drawable;
    }
}
