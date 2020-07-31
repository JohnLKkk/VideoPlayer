package com.example.testdemo.testModel.testView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.testdemo.R;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * Created by Void on 2020/7/23 17:25
 */
public class BitmapView extends View {
    private Paint cPaint = new Paint();
    private Paint rPaint = new Paint();

    public BitmapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        cPaint.setColor(ContextCompat.getColor(context, R.color.colorAccent));
        rPaint.setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
    }

    public BitmapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0,0,70,70,rPaint);
        canvas.drawCircle(50, 50, 10, cPaint);
    }
}
