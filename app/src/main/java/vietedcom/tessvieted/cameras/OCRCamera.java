package vietedcom.tessvieted.cameras;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.List;

import vietedcom.tessvieted.AppLog;

/**
 * Created by Smile on 6/22/2016.
 */
public class OCRCamera implements SurfaceHolder.Callback {
    private CameraView mCameraView;
    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;
    private int mWidthScreen;
    private int mHeightScreen;
    private OnOCRListener mOnOCRListener;
    private Handler mHandler;
    private TakeBitmap mTakeBitmap;
    private Activity mActivity;

    public void setOnOCRListener(OnOCRListener listener) {
        mOnOCRListener = listener;
    }


    public OCRCamera(@NonNull CameraView view, Activity activity) {
        mActivity = activity;
        mCameraView = view;
        mSurfaceHolder = mCameraView.getHolder();
        mSurfaceHolder.addCallback(this);
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mWidthScreen = metrics.widthPixels;
        mHeightScreen = metrics.heightPixels;
        mHandler = new Handler();
    }

    public boolean isDeviceSupportCamera(@NonNull Context context) {
        return context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    public void openCamera() {
        previewCamera();
    }


    public void closeCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        if (mTakeBitmap != null) {
            mTakeBitmap.cancel(true);
            mTakeBitmap = null;
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        if (mCamera == null) return;

        mCamera.stopPreview();
        Camera.Parameters parameters = mCamera.getParameters();
        setCameraDisplayOrientation(mActivity, getCameraIdBack(), mCamera);
        mCamera.setParameters(parameters);
        previewCamera();
    }


    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mCamera == null) return;

            Camera.Parameters parameters = mCamera.getParameters();
            if (parameters == null) return;

            List<Camera.Area> areas = new ArrayList<Camera.Area>();
            Camera.Area area = new Camera.Area(mCameraView.getRect(), 1);
            areas.add(area);
            parameters.setFocusAreas(areas);
            mCamera.setParameters(parameters);
        }
    };

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    public void previewCamera() {
        try {
            mCamera = Camera.open();
            Camera.Parameters parameters = mCamera.getParameters();
            List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
            int w = 10000;
            int h = 10000;
            for (Camera.Size size : sizes) {
                if (size.width < w) {
                    w = size.width;
                    h = size.height;
                }
            }
            parameters.setPictureFormat(PixelFormat.JPEG);
            parameters.setPictureSize(w, h);
            parameters.setPreviewSize(w, h);
            // parameters.setSceneMode(Camera.Parameters.FLASH_MODE_AUTO);
            //  parameters.setColorEffect(Camera.Parameters.EFFECT_SEPIA);
            mCamera.setParameters(parameters);
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();

        } catch (Exception e) {
            AppLog.e(e.toString());
          //   closeCamera();
        }
    }

    public void takeBitmap() {
        if (mCamera == null)
            return;
        try {
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean b, Camera camera) {
                    mCamera.takePicture(null, null, mPicture);
                }
            });

        } catch (Exception e) {
            AppLog.e(e.toString());
        }
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            if (mTakeBitmap != null) {
                mTakeBitmap.cancel(true);
            }
            mTakeBitmap = new TakeBitmap(mOnOCRListener);
            mTakeBitmap.execute(bytes);
        }
    };

    private final class TakeBitmap extends AsyncTask<byte[], Void, Bitmap> {
        private final Matrix mMatrix;
        private OnOCRListener mOnOCRListener;

        private TakeBitmap(OnOCRListener listener) {
            mMatrix = new Matrix();
            mMatrix.postRotate(90);
            mOnOCRListener = listener;
        }

        @Override
        protected Bitmap doInBackground(byte[]... bytes) {
            byte[] byteBitmap = bytes[0];
            long start = System.currentTimeMillis();
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteBitmap, 0, byteBitmap.length);
            if (bitmap == null) {
                return null;
            }
            AppLog.e("TIME DECODE BITMAP: " + (System.currentTimeMillis() - start));
            long start1 = System.currentTimeMillis();
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mMatrix, true);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(rotatedBitmap, mWidthScreen, mHeightScreen, false);
            AppLog.e("ROTATE PICTURE: " + (System.currentTimeMillis() - start1));
            AppLog.e("TIME TAKE PICTURE: " + (System.currentTimeMillis() - start));
            return scaledBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (mOnOCRListener != null) {
                mOnOCRListener.takeBitmap(bitmap);
            }
        }
    }

    private void setCameraDisplayOrientation(Activity activity,
                                             int cameraId,
                                             Camera camera) {
        if (activity == null || camera == null || cameraId < 0)
            return;

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    private int getCameraIdBack() {
        int cameraId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

}
