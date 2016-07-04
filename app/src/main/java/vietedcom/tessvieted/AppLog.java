package vietedcom.tessvieted;

import android.util.Log;

/**
 * Created by freesky1102 on 9/27/15.
 */
public class AppLog {

    private static final boolean isEnabled = true;

    private static final String TAG = "Xpeak";

    public static void d(String message) {
        if (message == null) return;
        if (isEnabled)
            Log.d(TAG, message);
    }

    public static void e(String message) {
        if (message == null) return;
        if (isEnabled)
            Log.e(TAG, message);
    }

    public static void i(String message) {
        if (message == null) return;
        if (isEnabled)
            Log.i(TAG, message);
    }

    public static void w(String message) {
        if (message == null) return;
        if (isEnabled)
            Log.w(TAG, message);
    }
}
