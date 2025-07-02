package com.example.proje_1eczaneotomasyonuygulamas;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.List;

public class CustomImageView extends View {
    private Bitmap bitmap;
    private Matrix matrix;
    private Matrix inverseMatrix;
    private Paint paint;
    private List<float[]> coordinates;
    private OnCoordinateTouchListener onCoordinateTouchListener;

    public CustomImageView(Context context) {
        super(context);
        init(null);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {
        if (set != null) {
            TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.CustomImageView);
            int imageResId = ta.getResourceId(R.styleable.CustomImageView_srcCompat, 0);
            if (imageResId != 0) {
                bitmap = BitmapFactory.decodeResource(getResources(), imageResId);
            }
            ta.recycle();
        }
        matrix = new Matrix();
        inverseMatrix = new Matrix();
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_dark));
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true); // Daha yumuşak çemberler için
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap != null) {
            int viewWidth = getWidth();
            int viewHeight = getHeight();
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();

            float scale = Math.min((float) viewWidth / bitmapWidth, (float) viewHeight / bitmapHeight);
            float dx = (viewWidth - bitmapWidth * scale) * 0.5f;
            float dy = (viewHeight - bitmapHeight * scale) * 0.5f;

            matrix.setScale(scale, scale);
            matrix.postTranslate(dx, dy);

            canvas.drawBitmap(bitmap, matrix, null);

            // İnverse matrisi güncelle
            matrix.invert(inverseMatrix); // Bu satırın matrix.postTranslate()'den sonra olduğundan emin olun

            if (coordinates != null) {
                for (float[] coords : coordinates) {
                    float[] scaledCoords = new float[2];
                    matrix.mapPoints(scaledCoords, coords);
                    canvas.drawCircle(scaledCoords[0], scaledCoords[1], 10, paint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float[] touchPoint = new float[]{event.getX(), event.getY()};
            inverseMatrix.mapPoints(touchPoint);

            if (onCoordinateTouchListener != null) {
                onCoordinateTouchListener.onCoordinateTouch(touchPoint[0], touchPoint[1]);
            }
            return true;
        }
        return false;
    }

    public void setCoordinates(List<float[]> coordinates) {
        this.coordinates = coordinates;
        invalidate();
    }

    public void setImageResource(int resId) {
        bitmap = BitmapFactory.decodeResource(getResources(), resId);
        invalidate();
    }

    public void setOnCoordinateTouchListener(OnCoordinateTouchListener listener) {
        this.onCoordinateTouchListener = listener;
    }

    public interface OnCoordinateTouchListener {
        void onCoordinateTouch(float x, float y);
    }
}
