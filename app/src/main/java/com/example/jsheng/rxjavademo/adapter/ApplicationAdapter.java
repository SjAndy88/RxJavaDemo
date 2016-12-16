package com.example.jsheng.rxjavademo.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jsheng.rxjavademo.R;
import com.example.jsheng.rxjavademo.bean.AppInfo;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by jun.sheng on 2016/12/16.
 */
public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ViewHolder> {

    private List<AppInfo> mApplications;

    private int mRowLayout;

    public ApplicationAdapter(List<AppInfo> applications, int rowLayout) {
        mApplications = applications;
        mRowLayout = rowLayout;
    }

    public void addApplications(List<AppInfo> applications) {
        mApplications.clear();
        mApplications.addAll(applications);
        notifyDataSetChanged();
    }

    public void addApplication(int position, AppInfo appInfo) {
        if (position < 0) {
            position = 0;
        }
        mApplications.add(position, appInfo);
        notifyItemInserted(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mRowLayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final AppInfo appInfo = mApplications.get(position);
        holder.name.setText(appInfo.getName());
        getBitmap(appInfo.getIcon())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        holder.image.setImageBitmap(bitmap);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return mApplications == null ? 0 : mApplications.size();
    }

    private Observable<Bitmap> getBitmap(final String icon) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                subscriber.onNext(BitmapFactory.decodeFile(icon));
                subscriber.onCompleted();
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;

        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
