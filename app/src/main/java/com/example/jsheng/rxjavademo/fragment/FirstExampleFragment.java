package com.example.jsheng.rxjavademo.fragment;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.example.jsheng.rxjavademo.App;
import com.example.jsheng.rxjavademo.R;
import com.example.jsheng.rxjavademo.adapter.ApplicationAdapter;
import com.example.jsheng.rxjavademo.bean.AppInfo;
import com.example.jsheng.rxjavademo.bean.AppInfoRich;
import com.example.jsheng.rxjavademo.list.ApplicationsList;
import com.example.jsheng.rxjavademo.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by jun.sheng on 2016/12/15.
 */

public class FirstExampleFragment extends Fragment {

    @BindView(R.id.fragment_first_example_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.fragment_first_example_swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private ApplicationAdapter mAdatper;

    private File mFilesDir;

    public FirstExampleFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first_example, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mAdatper = new ApplicationAdapter(new ArrayList<AppInfo>(), R.layout.applications_list_item);
        mRecyclerView.setAdapter(mAdatper);

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setProgressViewOffset(false, 0,
                getResources().getDimensionPixelSize(R.dimen.swipe_refresh_progress_offset));

        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setRefreshing(true);
        mRecyclerView.setVisibility(View.GONE);

        getFileDir()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        mFilesDir = file;
                        refreshTheList();
                    }
                });
    }

    private Observable<File> getFileDir() {
        return Observable.create(new Observable.OnSubscribe<File>() {
            @Override
            public void call(Subscriber<? super File> subscriber) {
                subscriber.onNext(App.instance.getFilesDir());
                subscriber.onCompleted();
            }
        });
    }

    private void refreshTheList() {
        getApps()
                .toSortedList()
                .subscribe(new Observer<List<AppInfo>>() {
                    @Override
                    public void onCompleted() {
                        ToastUtils.showShortToast(getActivity(),
                                "Here is the list!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShortToast(getActivity(),
                                "Something went wrong!");
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(List<AppInfo> appInfos) {
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mAdatper.addApplications(appInfos);
                        mSwipeRefreshLayout.setRefreshing(false);
                        storeList(appInfos);
                    }
                });
    }

    private void storeList(final List<AppInfo> appInfos) {
        ApplicationsList.getInstance().setList(appInfos);

        Schedulers.io().createWorker().schedule(new Action0() {
            @Override
            public void call() {
                Type type = new TypeToken<List<AppInfo>>() {}.getType();
                String jsonApps = new Gson().toJson(appInfos, type);
                FileUtils.writeFileFromString("AppInfo_List", jsonApps, false);
            }
        });
    }

    private Observable<AppInfo> getApps() {
        return Observable.create(new Observable.OnSubscribe<AppInfo>() {
            @Override
            public void call(Subscriber<? super AppInfo> subscriber) {
                List<AppInfoRich> apps = new ArrayList<>();

                final Intent mainIntent = new Intent(Intent.ACTION_MAIN);
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

                List<ResolveInfo> infos = getActivity().getPackageManager()
                        .queryIntentActivities(mainIntent, 0);
                for (ResolveInfo info : infos) {
                    apps.add(new AppInfoRich(getActivity(), info));
                }

                for (AppInfoRich app : apps) {
                    Bitmap icon = Utils.drawableToBitmap(app.getIcon());
                    String name = app.getName();
                    String iconPath = mFilesDir + "/" + name;
                    Utils.storeBitmap(App.instance, icon, iconPath);

                    if (subscriber.isUnsubscribed()) {
                        return;
                    }

                    subscriber.onNext(new AppInfo(name, iconPath, app.getLastUpdateTime()));
                }

                if (!subscriber.isUnsubscribed()) {
                    subscriber.onCompleted();
                }
            }
        });
    }
}
