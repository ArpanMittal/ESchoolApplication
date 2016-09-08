package com.organization.sjhg.e_school.Utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;

import com.organization.sjhg.e_school.Structure.GlobalConstants;

/**
 * Created by arpan on 7/4/2016.
 */
public class ImageViewHelper extends ImageView {
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    String savedItemClicked;

    private float dx; // postTranslate X distance
    private float dy; // postTranslate Y distance
    private float[] matrixValues = new float[9];
    float matrixX = 0; // X coordinate of matrix inside the ImageView
    float matrixY = 0; // Y coordinate of matrix inside the ImageView
    float width = 0; // width of drawable
    float height = 0; // height of drawable

    public ImageViewHelper(Context context) {
        super(context);
    }

    public ImageViewHelper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewHelper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ImageViewHelper(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);

        Drawable drawable = this.getDrawable();
        Rect rectDrawable = drawable.getBounds();
        float leftOffset = (this.getMeasuredWidth() - rectDrawable.width()) / 2f;
        float topOffset = (this.getMeasuredHeight() - rectDrawable.height()) / 2f;

        this.matrix.set(this.getImageMatrix());
        matrix.postTranslate(leftOffset, topOffset);

        this.setImageMatrix(matrix);


    }





    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        dumpEvent(event);
        getParent().requestDisallowInterceptTouchEvent(true);
        // Handle touch events here...
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d(GlobalConstants.LOG_TAG, "mode=DRAG");
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                Log.d(GlobalConstants.LOG_TAG, "oldDist=" + oldDist);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    Log.d(GlobalConstants.LOG_TAG, "mode=ZOOM");
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                Log.d(GlobalConstants.LOG_TAG, "mode=NONE");
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    // ...
                    matrix.set(savedMatrix);
                    matrix.getValues(matrixValues);
                    matrixX = matrixValues[2];
                    matrixY = matrixValues[5];
                    width = matrixValues[0] * (((ImageView) this).getDrawable()
                            .getIntrinsicWidth());
                    height = matrixValues[4] * (((ImageView) this).getDrawable()
                            .getIntrinsicHeight());

                    dx = event.getX() - start.x;
                    dy = event.getY() - start.y;

                    //if image will go outside left bound
                    if (matrixX + dx + width < 100){
                        dx = 100 -matrixX-width;
                    }
                    //if image will go outside right bound
                    if(matrixX + dx +100 > this.getWidth()){
                        dx = this.getWidth() - matrixX - 100;
                    }
                    //if image will go oustside top bound
                    if (matrixY + dy + height< 100){
                        dy = 100 -matrixY - height;
                    }
                    //if image will go outside bottom bound
                    if(matrixY + dy +100 > this.getHeight()){
                        dy = this.getHeight() - matrixY - 100;
                    }
                    matrix.postTranslate(dx, dy);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float tScale = newDist / oldDist;
                        matrix.postScale(tScale, tScale, mid.x, mid.y);
                    }

                    matrix.getValues(matrixValues);
                    float scaleX = matrixValues[Matrix.MSCALE_X];
                    float scaleY = matrixValues[Matrix.MSCALE_Y];

                    if(scaleX <= 0.7f) {
                        matrix.postScale((0.7f)/scaleX, (0.7f)/scaleY, mid.x, mid.y);
                    } else if(scaleX >= 2.5f) {
                        matrix.postScale((2.5f)/scaleX, (2.5f)/scaleY, mid.x, mid.y);
                    }
                }
                break;
        }
        this.setImageMatrix(matrix);
        return true;
    }
    //return super.onTouchEvent(event);


    private void dumpEvent(MotionEvent event) {
        String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
                "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(
                    action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }
        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }
        sb.append("]");
        Log.d(GlobalConstants.LOG_TAG, sb.toString());
    }

    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
}
