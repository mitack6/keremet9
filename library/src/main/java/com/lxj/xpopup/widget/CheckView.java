package com.lxj.xpopupdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.lxj.xpopup.util.Utils;

/**
 * Description:
 * Create by dance, at 2018/12/21
 */
public class CheckView extends View {
    Paint paint;

    public CheckView(Context context) {
        this(context, null);
    }

    public CheckView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(Utils.dp2px(context, 2));
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path = new Path();
        // first part
        path.moveTo(getMeasuredWidth()/4, getMeasuredHeight()/2);
        path.lineTo(getMeasuredWidth()/2 , getMeasuredHeight()*3/4);
        path.lineTo(getMeasuredWidth(), getMeasuredHeight()/4);
        canvas.drawPath(path, paint);
    }
}
