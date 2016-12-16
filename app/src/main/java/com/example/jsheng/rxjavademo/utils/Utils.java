package com.example.jsheng.rxjavademo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.FileOutputStream;
import java.io.IOException;

import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by jun.sheng on 2016/12/15.
 */
public class Utils {

    public static Bitmap drawableToBitmap(Drawable drawble) {
        if (drawble instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawble).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawble.getIntrinsicWidth(), drawble.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawble.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawble.draw(canvas);
        return bitmap;
    }

    public static void storeBitmap(final Context context, final Bitmap bitmap, final String filename) {
        Schedulers.io().createWorker().schedule(new Action0() {
            @Override
            public void call() {
                blockingStoreBitmap(context, bitmap, filename);
            }
        });
    }

    private static void blockingStoreBitmap(Context context, Bitmap bitmap, String filename) {
        FileOutputStream fOut = null;
        try {
            fOut = context.openFileOutput(filename, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fOut != null) {
                    fOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
