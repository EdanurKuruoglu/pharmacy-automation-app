package com.example.proje_1eczaneotomasyonuygulamas;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ScreenUtil {
    public static float[] normalizeCoordinates(Context context, float x, float y) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

        float normalizedX = x / metrics.widthPixels;
        float normalizedY = y / metrics.heightPixels;

        return new float[]{normalizedX, normalizedY};
    }

    public static float[] denormalizeCoordinates(Context context, float x, float y) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

        float denormalizedX = x * metrics.widthPixels;
        float denormalizedY = y * metrics.heightPixels;

        return new float[]{denormalizedX, denormalizedY};
    }
}
