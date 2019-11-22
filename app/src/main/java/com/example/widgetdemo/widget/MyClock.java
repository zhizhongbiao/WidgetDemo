package com.example.widgetdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.widgetdemo.LogUtils;

/**
 * @ProjectName: WidgetDemo
 * @Package: com.example.widgetdemo.widget
 * @ClassName: MyCl
 * @Description:
 * @Author: TaxiDriverSantos
 * @CreateDate: 2019/11/19   13:49
 * @UpdateUser: TaxiDriverSantos
 * @UpdateDate: 2019/11/19   13:49
 * @UpdateRemark:
 * @Version: 1.0
 */
public class MyClock extends ConstraintLayout {

    private int min;
    private float radiusOut;
    private Paint outPaint;
    private int width;
    private int height;
    private float deltaAngle = -90;
    private float radiusIn;
    private int rounds;
    private Paint inPaint;
    private Paint pointerPaint;
    private float radiusPointer;

    public MyClock(Context context) {
        this(context, null);
    }

    public MyClock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        setWillNotDraw(false);
    }

    private void initPaint() {
        outPaint = new Paint();
        outPaint.setAntiAlias(true);
        outPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        outPaint.setColor(Color.parseColor("#999999"));
        outPaint.setDither(true);

        inPaint = new Paint();
        inPaint.setAntiAlias(true);
        inPaint.setColor(Color.WHITE);
        inPaint.setDither(true);

        pointerPaint = new Paint();
        pointerPaint.setAntiAlias(true);
        pointerPaint.setColor(Color.WHITE);
        pointerPaint.setDither(true);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        width = getWidth();
        height = getHeight();
        LogUtils.e("   width=" + width + "   height= " + height);
        min = Math.min(width, height) / 2;
        radiusOut = 16f / 20f * min;
        radiusIn = 12.5f / 20f * min;
        radiusPointer = 14.25f / 20f * min;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        drawPlate(canvas);

        drawPointer(canvas);

        LogUtils.e(" onDraw  ");
    }

    private void drawPointer(Canvas canvas) {
        double radian = deltaAngle * Math.PI / 180f;

        canvas.save();
        canvas.translate(width / 2f, height / 2f);

        float factor0 = 1f;
        float factor1 = 5 / 6f;
        float factor2 = 1 / 2f;

        double x0 = radiusPointer * Math.cos(radian) * factor0;
        double y0 = radiusPointer * Math.sin(radian) * factor0;

        double x1 = radiusPointer * Math.cos(radian) * factor1;
        double y1 = radiusPointer * Math.sin(radian) * factor1;

        double x2 = radiusPointer * Math.cos(radian) * factor2;
        double y2 = radiusPointer * Math.sin(radian) * factor2;


        pointerPaint.setAntiAlias(true);
        pointerPaint.setStrokeCap(Paint.Cap.ROUND);
        pointerPaint.setColor(Color.RED);
        pointerPaint.setStrokeWidth(0.9f / 25f * min);
        canvas.drawLine(0, 0, (float) x0, (float) y0, pointerPaint);

        pointerPaint.setColor(Color.BLACK);
        pointerPaint.setStrokeWidth(1.25f / 25f * min);
        canvas.drawLine(0, 0, (float) x1, (float) y1, pointerPaint);

        pointerPaint.setStrokeWidth(1.6f / 25f * min);
        canvas.drawLine(0, 0, (float) x2, (float) y2, pointerPaint);

        canvas.restore();
    }


    private void drawPlate(Canvas canvas) {
        canvas.save();
        drawOutCircle(canvas);
        drawScale(canvas);
        drawInCircle(canvas);
        canvas.restore();
        LogUtils.e(" drawPlate  radiusOut=" + radiusOut + "   outPaint= " + outPaint);
    }

    private void drawOutCircle(Canvas canvas) {
        outPaint.setStrokeWidth(1f / 25f * min);
        canvas.translate(width / 2f, height / 2f);
        canvas.drawCircle(0, 0, radiusOut, outPaint);
    }

    private void drawScale(Canvas canvas) {
        outPaint.setStyle(Paint.Style.STROKE);
        outPaint.setColor(Color.parseColor("#EBF3F9"));

        for (int i = 0; i < 8; i++) {

            String text = Math.round(60 / 8f * i) + "";
            Rect rect = new Rect();
            outPaint.getTextBounds(text, 0, text.length(), rect);
            int textW = rect.width();
            int textH = rect.height();

            double r = (i % 2 == 0) ? (radiusOut + (Math.sqrt(Math.pow(textW, 2) + Math.pow(textH, 2)) / 2f)) : radiusOut;

            double radian = -90 * Math.PI / 180f + Math.PI / 4f * i;

            double sX = r * Math.cos(radian) * 17.5f / 20;
            double sY = r * Math.sin(radian) * 17.5f / 20;

            double eX = r * Math.cos(radian) * 19.5f / 20;
            double eY = r * Math.sin(radian) * 19.5f / 20;


            if (i % 2 == 0) {
                outPaint.setTextSize(radiusOut * 1.8f / 20);
                outPaint.setStrokeWidth(1f / 200f * min);


                sX = sX - (textW / 2f);
                sY = sY + (textH / 2f);

                canvas.drawText(text, (float) sX, (float) sY, outPaint);
            } else {
                outPaint.setStrokeWidth(1f / 50f * min);
                canvas.drawLine((float) sX, (float) sY, (float) eX, (float) eY, outPaint);
            }


        }
    }

    private void drawInCircle(Canvas canvas) {
        inPaint.setStrokeWidth(1f / 25f * min);
        inPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle(0, 0, radiusIn, inPaint);
    }


    public void setMinute(int minute) {
        deltaAngle = minute / 60f * 360 - 90;
        invalidate();
    }
}
