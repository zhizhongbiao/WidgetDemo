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
public class MyRegulator extends View {

    private Paint paint;
    private int width;
    private int height;
    private final int pieceCount = 5;
    private float horizontalLineWidth;
    private float sX;
    private float eY;
    private float scale;
    private float strokeWidthV;
    private float strokeWidthH;
    private float eX;
    private float sY;
    private float[] pts;
    private int factor;
    private String content;
    private boolean isNeedShowText;
    private float lastY;
    private float lastX;

    public MyRegulator(Context context) {
        this(context, null);
    }

    public MyRegulator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRegulator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        LogUtils.e("   width=" + width + "   height= " + height);

        scale = height * 15f / 18 / pieceCount;
        strokeWidthV = width / 30f;
        strokeWidthH = width / 60f;

        LogUtils.e(" scale = " + scale + "  strokeWidthV=" + strokeWidthV + "  strokeWidthH =" + strokeWidthH);

        sX = (width / 2f);
        horizontalLineWidth = width / 15f;
        eX = sX - horizontalLineWidth;
        sY = height * 1.5f / 18;
        eY = height * 16.5f / 18;

        pts = new float[24];
        for (int i = 0; i < pts.length; i++) {
            if (i % 2 == 0) {
                pts[i] = i / 2 % 2 == 0 ? sX + (strokeWidthV / 2f) : eX;
            } else {
                if (i % 4 == 3) {
                    pts[i] = pts[i - 2];
                } else {
                    pts[i] = sY + scale * (i / 4);
                }
            }
            LogUtils.e(" pts[" + i + "] = " + pts[i]);
        }


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
        canvas.drawLine(sX, eY - factor * scale, sX, eY, paint);
        paint.setStrokeWidth(strokeWidthH);
        int length = pts.length;
        float[] copys = Arrays.copyOfRange(pts, length-(factor+1)*4, length);
        canvas.drawLines(copys, paint);
        canvas.restore();
    }

    private void drawRuler(Canvas canvas) {
        canvas.save();
        paint.setColor(Color.parseColor("#797979"));
        paint.setStrokeWidth(strokeWidthV);
        canvas.drawLine(sX, sY, sX, eY, paint);
        paint.setStrokeWidth(strokeWidthH);
        canvas.drawLines(pts, paint);
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
        content = getContentText(factor);
        Rect rect = new Rect();
        paint.getTextBounds(content, 0, content.length(), rect);
        int textWidth = rect.width();
        int textHeight = rect.height();

        float x = width * 3f / 4f - textWidth / 2f;
        float y = (eY - factor * scale) - height / 28f;
        canvas.drawText(content, x, y, paint);
        canvas.restore();
    }

    private String getContentText(int factor) {
        String[] strs = {"零级", "一级", "二级", "三级", "四级", "五级", "六级"};
        return strs[factor];
    }

    private void drawCircle(Canvas canvas) {
        canvas.save();
        LogUtils.e(" drawCircle  radius=" + horizontalLineWidth + "   paint= " + paint);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        ensureFactorValid();
        canvas.drawCircle(sX, eY - factor * scale, horizontalLineWidth, paint);
        canvas.restore();
    }

    private void ensureFactorValid() {
        if (factor < 0) {
            factor = 0;
        }

        if (factor > pieceCount) {
            factor = pieceCount;
        }
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

                float currentX = event.getX();
                float currentY = event.getY();
                LogUtils.w("  currentY= " + currentY + "    currentX=" + currentX);

                factor = calculateFacotr(currentY);
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

    private int calculateFacotr(float currentY) {

        int f = 0;

        if (isInTheRange(currentY, (int) (eY - scale * 0.5f), (int) eY)) {
            f = 0;
        }
        if (isInTheRange(currentY, (int) (eY - scale * 1.5f), (int) (eY - scale * 0.5f))) {
            f = 1;
        }

        if (isInTheRange(currentY, (int) (eY - scale * 2.5f), (int) (eY - scale * 1.5f))) {
            f = 2;
        }

        if (isInTheRange(currentY, (int) (eY - scale * 3.5f), (int) (eY - scale * 2.5f))) {
            f = 3;
        }

        if (isInTheRange(currentY, (int) (eY - scale * 4.5f), (int) (eY - scale * 3.5f))) {
            f = 4;
        }

        if (isInTheRange(currentY, (int) (eY - scale * 5f), (int) (eY - scale * 4.5f))) {
            f = 5;
        }
        LogUtils.e("calculateFacotr    f= " + f + "   currentY=" + currentY);

        return f;
    }

    public static boolean isInTheRange(float current, float min, float max) {
        return Math.max(min, current) == Math.min(current, max);
    }
}
