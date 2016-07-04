package vietedcom.tessvieted.bitmaps;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.googlecode.tesseract.android.TessBaseAPI;

import vietedcom.tessvieted.AppLog;

/**
 * Created by Smile on 6/22/2016.
 */
public class BitmapToText {
    private TessBaseAPI mTessBaseAPI;

    public BitmapToText() {
    }

    private OnDetectBitmapListener mOnDetectBitmapListener;

    public void setOnDetectBitmapListener(OnDetectBitmapListener listener) {
        this.mOnDetectBitmapListener = listener;
    }

    public void recognized(final Bitmap bitmap) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                if (bitmap == null) return "";
                mTessBaseAPI = new TessBaseAPI();
                mTessBaseAPI.init(OCRUtil.getPathLibraries(), OCRUtil.LANG);
                mTessBaseAPI.setImage(bitmap);
                long start = System.currentTimeMillis();
                String recognizedText = mTessBaseAPI.getUTF8Text();
                AppLog.e("TIME_DETECT_IMAGE: " + (System.currentTimeMillis() - start));
                mTessBaseAPI.end();
                return recognizedText;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (mOnDetectBitmapListener != null) {
                    mOnDetectBitmapListener.detect(s);
                }
            }
        }.execute();
    }
}
