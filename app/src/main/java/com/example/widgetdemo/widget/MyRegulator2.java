package com.example.widgetdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.widgetdemo.LogUtils;

import java.util.Arrays;

/**
 * @ProjectName: WidgetDemo
 * @Package: com.example.widgetdemo.widget
 * @ClassName: MyRegulator
 * @Description:
 * @Author: TaxiDriverSantos
 * @CreateDate: 2019/11/19   13:49
 * @UpdateUser: TaxiDriverSantos
 * @UpdateDate: 2019/11/19   13:49
 * @UpdateRemark:
 * @Version: 1.0
 */
public class MyRegulator2 extends View {

    private Paint paint;
    private int width;
    private int height;
    private float horizontalLineWidth;
    private float sX;
    private float eY;

    private float strokeWidthV;
    private float strokeWidthH;
    private float eX;
    private float sY;

    private String content;
    private boolean isNeedShowText;
    private float lastY;
    private float lastX;

    private float deltaY = 0;
    private float currentX;
    private float currentY;

    public MyRegulator2(Context context) {
        this(context, null);
    }

    public MyRegulator2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRegulator2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        LogUtils.e("   width=" + width + "   height= " + height);

        strokeWidthV = width / 30f;
        strokeWidthH = width / 60f;

        LogUtils.e("  strokeWidthV=" + strokeWidthV + "  strokeWidthH =" + strokeWidthH);

        sX = (width / 2f);
        horizontalLineWidth = width / 15f;
        eX = sX - horizontalLineWidth;
        sY = height * 1.5f / 18;
        eY = height * 16.5f / 18;

        currentY=eY;

    }

    @Override
    protected void onDraw(Canvas canvas) {

        drawRuler(canvas);
        drawString(canvas);
        drawLightRuler(canvas);
        drawCircle(canvas);
        LogUtils.e(" onDraw  ");
    }

    private void drawLightRuler(Canvas canvas) {
        canvas.save();
        paint.setColor(Color.parseColor("#FFFFFF"));
        paint.setStrokeWidth(strokeWidthV);
        ensureCurrentYvalid();
        canvas.drawLine(sX, currentY, sX, eY, paint);
        canvas.restore();
    }

    private void drawRuler(Canvas canvas) {
        canvas.save();
        paint.setColor(Color.parseColor("#797979"));
        paint.setStrokeWidth(strokeWidthV);
        canvas.drawLine(sX, sY, sX, eY, paint);
        canvas.restore();
    }

    private void drawString(Canvas canvas) {

        if (!isNeedShowText) {
            return;
        }

        canvas.save();
        LogUtils.e(" drawCircle  radius=" + horizontalLineWidth + "   paint= " + paint);
        paint.setStrokeWidth(strokeWidthH / 50f);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);

        paint.setTextSize(strokeWidthV * 4);
        content = getContentText(deltaY / (eY - sY));
        Rect rect = new Rect();
        paint.getTextBounds(content, 0, content.length(), rect);
        int textWidth = rect.width();
        int textHeight = rect.height();

        float x = width * 3f / 4f - textWidth / 2f;

        ensureCurrentYvalid();

        float y = currentY - height / 28f;
        canvas.drawText(content, x, y, paint);
        canvas.restore();
    }

    private void ensureCurrentYvalid() {
        if (currentY > eY) {
            currentY = eY;
        }
        if (currentY < sY) {
            currentY = sY;
        }
    }

    private void drawCircle(Canvas canvas) {
        canvas.save();
        LogUtils.e(" drawCircle  radius=" + horizontalLineWidth + "   paint= " + paint);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        ensureCurrentYvalid();
        canvas.drawCircle(sX, currentY, horizontalLineWidth, paint);
        canvas.restore();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isNeedShowText = true;
                lastX = event.getX();
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                currentX = event.getX();
                currentY = event.getY();
                LogUtils.w("  currentY= " + currentY + "    currentX=" + currentX);


                deltaY = deltaY + (currentY - lastY);
                invalidate();
                lastX = currentX;
                lastY = currentY;

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isNeedShowText = false;
                invalidate();
                break;
        }
        return true;
    }


    private String getContentText(float factor) {

        if (factor > 1) {
            factor = 1;
        }

        if (factor < 0) {
            factor = 0;
        }

        return Math.round((1 - factor) * 100) + "%";
    }


}
