package com.example.widgetdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

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
public class MyCl extends View {

    private int min;
    private float radius;
    private Paint paint;
    private int width;
    private int height;
    private float deltaAngle = 30;
    private float radius2;
    private int rounds;
    private double x;
    private double y;

    public MyCl(Context context) {
        this(context, null);
    }

    public MyCl(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
//        setWillNotDraw(false);
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        width = getWidth();
        height = getHeight();

        LogUtils.e("   width="+width+"   height= "+height);

        min = Math.min(width, height)/2;

        radius = 1f / 16f * min;
        radius2 = 1.5f / 16f * min;
        paint.setStrokeWidth(5f / 16f * min);

//        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawGrayCircle(canvas);
        drawWhiteArc(canvas);
        drawStrokeCircle(canvas);
        drawString(canvas);

        LogUtils.e(" onDraw  ");
    }

    private void drawString(Canvas canvas) {

    }



    private void drawStrokeCircle(Canvas canvas) {
        rounds = (int) (deltaAngle / 360);
        deltaAngle = deltaAngle % 360;

        canvas.save();
        float halfW = width / 2f;
        float halfH = height / 2f;
        double deltaPI = deltaAngle * Math.PI / 180f;

        if (deltaAngle <= 90) {
            x = halfW + Math.sin(deltaPI) * radius;
            y = halfH - Math.cos(deltaPI) * radius;
        }

        if (deltaAngle > 90 && deltaAngle <= 180) {
            x = halfW + Math.sin(Math.PI - deltaPI) * radius;
            y = halfH + Math.cos(Math.PI - deltaPI) * radius;
        }

        if (deltaAngle <= 270 && deltaAngle > 180) {
            x = halfW - Math.sin(deltaPI - Math.PI) * radius;
            y = halfH + Math.cos(deltaPI - Math.PI) * radius;
        }


        if (deltaAngle >= 270) {
            x = halfW - Math.sin(2 * Math.PI - deltaPI) * radius;
            y = halfH - Math.cos(2 * Math.PI - deltaPI) * radius;
        }


        paint.setColor(Color.parseColor("#E6E6E6"));
        canvas.drawCircle((int) x, (int) y, radius2, paint);
        canvas.restore();
    }

    private void drawWhiteArc(Canvas canvas) {
        canvas.save();
        canvas.translate(width / 2f, height / 2f);
        paint.setColor(Color.parseColor("#E6E6E6"));
        RectF rectF = new RectF(-radius, -radius, radius, radius);
        canvas.drawArc(rectF, -90, deltaAngle, false, paint);
        canvas.restore();
    }

    private void drawGrayCircle(Canvas canvas) {
        canvas.save();
        canvas.translate(width / 2f, height / 2f);
        paint.setColor(Color.parseColor("#999999"));

        LogUtils.e(" drawGrayCircle  radius="+radius+"   paint= "+paint);
//        paint.setColor(Color.RED);
        canvas.drawCircle(0, 0, radius, paint);
        canvas.restore();
    }
}
