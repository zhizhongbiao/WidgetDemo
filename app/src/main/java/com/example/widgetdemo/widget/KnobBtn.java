package com.example.widgetdemo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.Nullable;

import com.example.widgetdemo.LogUtils;
import com.example.widgetdemo.R;

/**
 * @ProjectName: CC
 * @Package: com.aylson.new_wardrobe
 * @ClassName: KnobBtn
 * @Description:
 * @Author: TaxiDriverSantos
 * @CreateDate: 2019/11/9   14:37
 * @UpdateUser: TaxiDriverSantos
 * @UpdateDate: 2019/11/9   14:37
 * @UpdateRemark:
 * @Version: 1.0
 */
public class KnobBtn extends View {

    private int width;
    private int height;
    private Paint paint;
    private int radius;
    private RectF rectF;
    private VelocityTracker velocityTracker;
    private int maximumFlingVelocity;
    private int scaledTouchSlop;

    private float lastAngle;
    private float targetAngle = 1;
    private float deltaAngle;
    private int cRadius;
    private Paint textPaint;
    private Paint btnPaint;
    private PaintFlagsDrawFilter paintFlagsDrawFilter;
    private Paint tagPaint;
    private boolean isNeedShowText;
    private Paint cTextPaint;


    public KnobBtn(Context context) {
        this(context, null);
    }

    public KnobBtn(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KnobBtn(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        this.maximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        scaledTouchSlop = viewConfiguration.getScaledTouchSlop();
        LogUtils.d("maximumFlingVelocity =" + this.maximumFlingVelocity);
    }

    private void initPaint() {
        paint = new Paint();
        paint.setColor(Color.CYAN);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        tagPaint = new Paint();
        tagPaint.setColor(Color.CYAN);
        tagPaint.setAntiAlias(true);
        tagPaint.setStyle(Paint.Style.STROKE);
        tagPaint.setStrokeWidth(10);

        textPaint = new Paint();
        textPaint.setColor(Color.CYAN);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(34);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setStyle(Paint.Style.FILL);


        cTextPaint = new Paint();
        cTextPaint.setColor(Color.CYAN);
        cTextPaint.setAntiAlias(true);
        cTextPaint.setTextSize(64);
        cTextPaint.setTextAlign(Paint.Align.CENTER);
        cTextPaint.setStyle(Paint.Style.FILL);

        btnPaint = new Paint();
        btnPaint.setAntiAlias(true);
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();


        int min = Math.min(Math.abs(width), Math.abs(height));
        radius = min / 2;
        cRadius = radius * 12 / 16;
        rectF = new RectF(-cRadius, -cRadius, cRadius, cRadius);
        paint.setStrokeWidth(5);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isNeedShowText = true;
                float downX = event.getX();
                float downY = event.getY();
                lastAngle = calcAngle2(downX, downY);
                break;
            case MotionEvent.ACTION_MOVE:

                float currentX = event.getX();
                float currentY = event.getY();
                float currentAngle = calcAngle2(currentX, currentY);


                deltaAngle = currentAngle - lastAngle;
                LogUtils.w("  deltaAngle= " + deltaAngle);
                if (currentAngle > 340 || currentAngle < 40) {
                    deltaAngle = 0;
                }
                targetAngle = targetAngle + deltaAngle;


                LogUtils.e("    targetAngle  =  " + targetAngle
                        + "     currentAngle =  " + currentAngle
                        + "     lastAngle    =  " + lastAngle
                        + "     deltaAngle   =  " + deltaAngle);


                invalidate();
                lastAngle = currentAngle;


                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isNeedShowText = false;
                invalidate();
                break;
        }
        return true;
    }

    private float calcAngle2(float targetX, float targetY) {

        float x = (width / 2) - targetX;
        float y = targetY - height / 2;
        double radian;

        LogUtils.w("calcAngle  x =  " + x + "    y =" + y);

        if (y != 0) {
            float tan = Math.abs(x / y);
            if (y > 0) {
                if (x >= 0) {
                    radian = Math.atan(tan);
                } else {
                    radian = 2 * Math.PI - Math.atan(tan);
                }
            } else {
                if (x >= 0) {
                    radian = Math.PI - Math.atan(tan);
                } else {
                    radian = Math.PI + Math.atan(tan);
                }
            }
        } else {
            if (x > 0) {
                radian = Math.PI / 2;
            } else {
                radian = 1.5 * Math.PI;
            }
        }
        float temp = (float) ((radian * 180) / Math.PI);


        return temp;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (radius <= 0) {
            return;
        }

        drawBtn(canvas);
        drawArc(canvas);
        drawTag(canvas);

        if (isNeedShowText) {
            drawText(canvas);
        }
        drawCenterText(canvas);
    }

    private void drawCenterText(Canvas canvas) {
        canvas.save();
        canvas.translate(width / 2, height / 2);
        float textWidth = textPaint.measureText(targetAngle + "");
        float ascent = textPaint.ascent();
        float descent = textPaint.descent();
        float textHeight = descent - ascent;
        LogUtils.e("ascent = " + ascent + "     descent =" + descent + "     textHeight=" + textHeight);

        canvas.drawText(Math.floor(targetAngle) + "°", 0, 0+textHeight/2, cTextPaint);
        canvas.restore();
    }

    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.b400);

    private void drawBtn(Canvas canvas) {
        int bHeight = bitmap.getHeight();
        int bWidth = bitmap.getWidth();
        float ratio = cRadius * 1.0f / bWidth * 2f * 0.9f;
        LogUtils.e("ratio  = " + ratio + "    bHeight=" + bHeight + "    cRadius=" + cRadius);
        Matrix matrix = new Matrix();
        matrix.postTranslate((width - bWidth) / 2, (height - bHeight) / 2);
        matrix.postScale(ratio, ratio, width / 2, height / 2);
        canvas.setDrawFilter(paintFlagsDrawFilter);
        canvas.drawBitmap(bitmap, matrix, btnPaint);

    }

    private void drawText(Canvas canvas) {
        canvas.save();
        canvas.translate(width / 2, height / 2);

        int r = radius * 14 / 16;

        float x = -(float) (r * Math.sin(45 * Math.PI / 180));
        float y = (float) (r * Math.cos(45 * Math.PI / 180));


//        float textWidth = textPaint.measureText(targetAngle + "");
//        float ascent = textPaint.ascent();
//        float descent = textPaint.descent();
//        float textHeight = descent - ascent;
//        LogUtils.e("ascent = " + ascent + "     descent =" + descent + "     textHeight=" + textHeight);

        canvas.rotate(targetAngle);
        canvas.rotate(-135, x, y);

        canvas.drawText(Math.floor(targetAngle) + "°", x, y, textPaint);
        canvas.restore();
    }

    private void drawTag(Canvas canvas) {
        canvas.save();
        canvas.translate(width / 2, height / 2);
        int r1 = cRadius * 12 / 16;
        int r2 = cRadius * 14 / 16;

        float x1 = (float) (-r1 * Math.sin(45 * Math.PI / 180));
        float y1 = (float) (r1 * Math.cos(45 * Math.PI / 180));

        float x2 = (float) (-r2 * Math.sin(45 * Math.PI / 180));
        float y2 = (float) (r2 * Math.cos(45 * Math.PI / 180));


        canvas.rotate(targetAngle);
        canvas.drawLine(x1, y1, x2, y2, tagPaint);
        canvas.restore();
    }

    private void drawArc(Canvas canvas) {

        if (targetAngle > 270) {
            targetAngle = 270;
        } else if (targetAngle < 1) {
            targetAngle = 1;
        }

        canvas.save();
        canvas.translate(width / 2, height / 2);
        canvas.drawArc(rectF, 135, targetAngle, false, paint);
        canvas.restore();
    }


}
