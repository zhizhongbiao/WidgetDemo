package com.example.widgetdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
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
public class MyCl extends ConstraintLayout {

    private int min;
    private float radius;
    private Paint grayPaint;
    private int width;
    private int height;
    private float deltaAngle = 30;
    private float radius2;
    private int rounds;
    private Paint lightPaint;
    private String content;

    public MyCl(Context context) {
        this(context, null);
    }

    public MyCl(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        setWillNotDraw(false);
    }

    private void initPaint() {
        grayPaint = new Paint();
        grayPaint.setAntiAlias(true);
        grayPaint.setStyle(Paint.Style.STROKE);
        grayPaint.setColor(Color.parseColor("#999999"));

        lightPaint = new Paint();
        lightPaint.setAntiAlias(true);
        lightPaint.setColor(Color.WHITE);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        width = getWidth();
        height = getHeight();

        LogUtils.e("   width=" + width + "   height= " + height);

        min = Math.min(width, height) / 2;

        radius = 15f / 20f * min;
        radius2 = 1f / 19f * radius;
        grayPaint.setStrokeWidth(1f / 25f * min);
        lightPaint.setStrokeWidth(1f / 25f * min);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawGrayCircle(canvas);
        drawLightArc(canvas);
        drawLightCircle(canvas);
        drawString(canvas);

        LogUtils.e(" onDraw  ");
    }

    private void drawString(Canvas canvas) {

        content = content+" M";

        Rect rect = new Rect();
        lightPaint.getTextBounds(content,0, content.length(),rect);
        int textW = rect.width();
        int textH = rect.height();

        lightPaint.setStrokeWidth(1f / 1000f * min);
        lightPaint.setTextSize(radius * 3/ 20f);

        canvas.save();
        int[] ints = calculateCoordinator(radius+(min-radius)/2f);
        canvas.drawText(content, ints[0], ints[1], lightPaint);
        canvas.restore();
    }


    private void drawLightCircle(Canvas canvas) {
        canvas.save();
        int[] ints = calculateCoordinator(radius);
        lightPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle(ints[0], ints[1], radius2, lightPaint);
        canvas.restore();
    }

    private int[] calculateCoordinator(float referRadius) {

        rounds = (int) (deltaAngle / 360);
        deltaAngle = deltaAngle % 360;

        int[] ints = new int[2];

        double x = width / 2f;
        double y = height * 5 / 20f;

        float halfW = width / 2f;
        float halfH = height / 2f;
        double deltaPI = deltaAngle * Math.PI / 180f;

        if (deltaAngle <= 90) {
            x = halfW + Math.sin(deltaPI) * referRadius;
            y = halfH - Math.cos(deltaPI) * referRadius;
        }

        if (deltaAngle > 90 && deltaAngle <= 180) {
            x = halfW + Math.sin(Math.PI - deltaPI) * referRadius;
            y = halfH + Math.cos(Math.PI - deltaPI) * referRadius;
        }

        if (deltaAngle <= 270 && deltaAngle > 180) {
            x = halfW - Math.sin(deltaPI - Math.PI) * referRadius;
            y = halfH + Math.cos(deltaPI - Math.PI) * referRadius;
        }


        if (deltaAngle >= 270) {
            x = halfW - Math.sin(2 * Math.PI - deltaPI) * referRadius;
            y = halfH - Math.cos(2 * Math.PI - deltaPI) * referRadius;
        }

        ints[0] = (int) x;
        ints[1] = (int) y;

        return ints;
    }

    private void drawLightArc(Canvas canvas) {
        canvas.save();
        canvas.translate(width / 2f, height / 2f);
        lightPaint.setStyle(Paint.Style.STROKE);
        RectF rectF = new RectF(-radius, -radius, radius, radius);
        canvas.drawArc(rectF, -90, deltaAngle, false, lightPaint);
        canvas.restore();
    }

    private void drawGrayCircle(Canvas canvas) {
        canvas.save();
        canvas.translate(width / 2f, height / 2f);
        canvas.drawCircle(0, 0, radius, grayPaint);
        canvas.restore();
        LogUtils.e(" drawGrayCircle  radius=" + radius + "   grayPaint= " + grayPaint);
    }


    public void setContent(int minute) {
        this.content = minute+"";
        deltaAngle=minute/60f*360;
        invalidate();
    }


}
