package vietedcom.tessvieted;

import android.app.Application;

import java.io.IOException;

import vietedcom.tessvieted.bitmaps.OCRUtil;

/**
 * Created by Smile on 6/22/2016.
 */
public class App extends Application {
    private static App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        try {
            OCRUtil.moveLibrariesTessToFileDir(this);
        } catch (IOException e) {
            AppLog.e(" OCRUtil.moveLibrariesTessToFileDir " + e.toString());
        }
    }

    public static App get() {
        return sInstance;
    }
}
