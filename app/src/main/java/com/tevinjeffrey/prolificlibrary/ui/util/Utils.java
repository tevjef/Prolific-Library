package com.tevinjeffrey.prolificlibrary.ui.util;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

public class Utils {
    @NonNull
    public static int getWindowHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels - getStatusBarHeight(activity);
    }

    public static int getStatusBarHeight(Activity activity) {
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
