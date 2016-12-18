package com.example.jsheng.rxjavademo.navigation;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jsheng.rxjavademo.R;

import java.util.List;

/**
 * Created by shengjun on 2016/12/18.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.ViewHolder> {

    private List<NavigationItem> mDataList;

    private NavigationDrawerCallbacks mNavigationDrawerCallbacks;

    private int mSelectedPosition;

    private int mTouchedPosition = -1;
    private View.OnTouchListener onTouchListener;
    private View.OnClickListener onClickListener;

    public NavigationDrawerAdapter(List<NavigationItem> dataList) {
        mDataList = dataList;
    }

    public void setNavigationDrawerCallbacks(NavigationDrawerCallbacks navigationDrawerCallbacks) {
        this.mNavigationDrawerCallbacks = navigationDrawerCallbacks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation_drawer_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textView.setText(mDataList.get(position).getText());
        holder.textView.setCompoundDrawablesWithIntrinsicBounds(mDataList.get(position).getDrawable(), null, null, null);

        holder.itemView.setTag(R.id.commonKey, position);
        if (onTouchListener == null) {
            onTouchListener = new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int pos = (int) v.getTag(R.id.commonKey);
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            touchPosition(pos);
                            return false;
                        case MotionEvent.ACTION_CANCEL:
                            touchPosition(-1);
                            return false;
                        case MotionEvent.ACTION_MOVE:
                            return false;
                        case MotionEvent.ACTION_UP:
                            touchPosition(-1);
                            return false;
                    }
                    return true;
                }
            };
        }
        holder.itemView.setOnTouchListener(onTouchListener);
        if (onClickListener == null) {
            onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag(R.id.commonKey);
                    if (mNavigationDrawerCallbacks != null) {
                        mNavigationDrawerCallbacks.onNavigationDrawerItemSelected(pos);
                    }
                }
            };
        }
        holder.itemView.setOnClickListener(onClickListener);

        if (mSelectedPosition == position || mTouchedPosition == position) {
            holder.itemView.setBackgroundColor(
                    holder.itemView.getContext().getResources().getColor(R.color.selected_gray));
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void touchPosition(int position) {
        int lastPosition = mTouchedPosition;
        mTouchedPosition = position;
        if (lastPosition >= 0) {
            notifyItemChanged(lastPosition);
        }
        if (position >= 0) {
            notifyItemChanged(position);
        }
    }

    public void selectPosition(int position) {
        int lastPosition = mSelectedPosition;
        mSelectedPosition = position;
        notifyItemChanged(lastPosition);
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return mDataList != null ? mDataList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_name);
        }
    }
}
