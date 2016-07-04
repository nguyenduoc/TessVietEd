package vietedcom.tessvieted.cameras;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import vietedcom.tessvieted.AppLog;
import vietedcom.tessvieted.R;
import vietedcom.tessvieted.bitmaps.BitmapToText;
import vietedcom.tessvieted.bitmaps.OnDetectBitmapListener;

/**
 * Created by Smile on 6/22/2016.
 */
public class CameraFragment extends Fragment {
    private CameraView mCameraView;
    private OCRCamera mOcrCamera;
    private TextView mBitmap;
    private BitmapToText mBitmapToText = new BitmapToText();
    int count = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCameraView = (CameraView) view.findViewById(R.id.camera_view);
        mBitmap = (TextView) view.findViewById(R.id.bitmap);
        view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOcrCamera.takeBitmap();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //  getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mOcrCamera = new OCRCamera(mCameraView, getActivity());
        mBitmapToText.setOnDetectBitmapListener(new OnDetectBitmapListener() {
            @Override
            public void detect(String text) {
                ++count;
                mBitmap.setText(text + "\n\r" + count);
               mOcrCamera.takeBitmap();
            }
        });
        mOcrCamera.setOnOCRListener(new OnOCRListener() {
            @Override
            public void takeBitmap(Bitmap bitmap) {
                long start = System.currentTimeMillis();
                bitmap = mCameraView.getRectBitmap(bitmap);
                AppLog.e("TIME GET BITMAP " + (System.currentTimeMillis() - start));
                long start1 = System.currentTimeMillis();
                mBitmapToText.recognized(bitmap);
                AppLog.e("TOTAL TIME DETECT " + (System.currentTimeMillis() - start1));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mOcrCamera.openCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mOcrCamera.closeCamera();
    }
}
