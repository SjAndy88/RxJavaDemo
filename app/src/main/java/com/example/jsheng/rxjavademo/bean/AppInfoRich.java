package com.example.jsheng.rxjavademo.bean;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.util.Locale;

import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Created by jun.sheng on 2016/12/15.
 */

@Accessors(prefix = "m")
public class AppInfoRich implements Comparable<Object> {

    @Setter
    String mName = null;

    private Context mContext;
    private ResolveInfo mResolveInfo;
    private ComponentName mComponentName;
    private PackageInfo pi;
    private Drawable icon;

    public AppInfoRich(Context context, ResolveInfo ri) {
        mContext = context;
        mResolveInfo = ri;

        ActivityInfo activityInfo = ri.activityInfo;
        mComponentName = new ComponentName(activityInfo.applicationInfo.packageName,
                activityInfo.name);
    }

    public String getName() {
        if (mName != null) {
            return mName;
        } else {
            try {
                return getNameFromResolveInfo(mResolveInfo);
            } catch (PackageManager.NameNotFoundException e) {
                return getPackageName();
            }
        }
    }

    public String getActivityName() {
        return mResolveInfo.activityInfo.name;
    }

    public String getPackageName() {
        return mResolveInfo.activityInfo.packageName;
    }

    public ComponentName getComponentName() {
        return mComponentName;
    }

    public String getComponentInfo() {
        if (getComponentName() != null) {
            return getComponentName().toString();
        } else {
            return "";
        }
    }

    public ResolveInfo getResolveInfo() {
        return mResolveInfo;
    }

    public PackageInfo getPackageInfo() {
        return  pi;
    }

    public String getVersionName() {
        PackageInfo pi = getPackageInfo();
        if (pi != null) {
            return pi.versionName;
        } else {
            return "";
        }
    }

    public int getVersionCode() {
        PackageInfo pi = getPackageInfo();
        if (pi != null) {
            return pi.versionCode;
        } else {
            return 0;
        }
    }

    public Drawable getIcon() {
        if (icon == null) {
            icon = getResolveInfo().loadIcon(mContext.getPackageManager());
        }
        return icon;
    }

    public long getFirstInstallTime() {
        PackageInfo pi = getPackageInfo();
        if (pi != null && versionOK()) {
            return pi.firstInstallTime;
        } else {
            return 0;
        }
    }

    public long getLastUpdateTime() {
        PackageInfo pi = getPackageInfo();
        if (pi != null && versionOK()) {
            return pi.lastUpdateTime;
        } else {
            return 0;
        }
    }

    private boolean versionOK() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    private String getNameFromResolveInfo(ResolveInfo ri) throws PackageManager.NameNotFoundException {
        String name = ri.resolvePackageName;
        if (ri.activityInfo != null) {
            PackageManager packageManager = mContext.getPackageManager();
            Resources res = packageManager.getResourcesForApplication(ri.activityInfo.applicationInfo);
            Resources engRes = getEnglishResources(res);

            if (ri.activityInfo.labelRes != 0) {
                name = engRes.getString(ri.activityInfo.labelRes);

                if (TextUtils.isEmpty(name)) {
                    name = res.getString(ri.activityInfo.labelRes);
                }
            } else {
                name = ri.activityInfo.applicationInfo.loadLabel(packageManager).toString();
            }

        }
        return name;
    }

    private Resources getEnglishResources(Resources resources) {
        AssetManager assets = resources.getAssets();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration config = new Configuration(resources.getConfiguration());
        config.locale = Locale.US;
        return new Resources(assets, metrics, config);

    }


    @Override
    public int compareTo(Object o) {
        AppInfoRich app = (AppInfoRich) o;
        return getName().compareTo(app.getName());
    }

    @Override
    public String toString() {
        return getName();
    }
}
