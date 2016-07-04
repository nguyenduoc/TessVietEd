package vietedcom.tessvieted.cameras;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by Smile on 6/22/2016.
 */
public class CameraView extends SurfaceView {
    private Paint mRectPaint, mHintPaint;

    public CameraView(Context context) {
        super(context);
        init(context);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CameraView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(@NonNull Context context) {
        mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPaint.setColor(Color.WHITE);
        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setStrokeWidth(3f);

        mHintPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHintPaint.setColor(Color.BLACK);
        mHintPaint.setAlpha(200);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRect(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void drawRect(@NonNull Canvas canvas) {
        int l = getWidth() / 2 - 150;
        int t = getHeight() / 2 - 75;
        int r = getWidth() / 2 + 150;
        int b = getHeight() / 2 + 75;
        RectF rect = new RectF(l, t, r, b);
        canvas.drawRect(0, 0, l, getHeight(), mHintPaint);
        canvas.drawRect(l, 0, r, t, mHintPaint);
        canvas.drawRect(r, 0, getWidth(), getHeight(), mHintPaint);
        canvas.drawRect(l, b, r, getHeight(), mHintPaint);
        canvas.drawRect(rect, mRectPaint);
    }

    public Bitmap getFullBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public RectF getRectF() {
        int l = getWidth() / 2 - 150;
        int t = getHeight() / 2 - 75;
        int r = getWidth() / 2 + 150;
        int b = getHeight() / 2 + 75;
        return new RectF(l, t, r, b);
    }

    public Rect getRect() {
        int l = getWidth() / 2 - 150;
        int t = getHeight() / 2 - 75;
        int r = getWidth() / 2 + 150;
        int b = getHeight() / 2 + 75;
        return new Rect(l, t, r, b);
    }

    public Bitmap getRectBitmap(Bitmap bitmap) {
        if (bitmap == null) return null;
        int l = getWidth() / 2 - 150;
        int t = getHeight() / 2 - 75;
        bitmap = Bitmap.createScaledBitmap(bitmap, getWidth(), getHeight(), false);
        return Bitmap.createBitmap(bitmap, l, t, 300, 150);
    }
}
