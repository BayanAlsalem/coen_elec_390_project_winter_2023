package com.example.coen_elec_390_project_winter_2023.Dashboard;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.core.content.ContextCompat;

import com.example.coen_elec_390_project_winter_2023.R;

public class SemiCircleProgressBar extends View {

    private int max = 5000;
    private int progress = 0;
    private Paint paint;
    private Paint textPaint;
    private RectF rectF;
    private int targetProgress;
    private int startColor;
    private int endColor;
    private Handler progressHandler;


    public SemiCircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        rectF = new RectF();

        startColor = ContextCompat.getColor(context, R.color.progress_start_color);
        endColor = ContextCompat.getColor(context, R.color.progress_end_color);

        paint.setAntiAlias(true);
        paint.setStrokeWidth(50f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.BUTT);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(ContextCompat.getColor(context, R.color.text_color));
        textPaint.setTextSize(60f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Typeface.MONOSPACE);

        progressHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (progress < targetProgress) {
                    progress++;
                } else if (progress > targetProgress) {
                    progress--;
                }
                invalidate();
                if (progress != targetProgress) {
                    progressHandler.sendEmptyMessageDelayed(0, 5);
                }
                return true;
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int minDimension = Math.min(width, height);

        paint.setShader(null);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.progress_background_color));
        canvas.drawArc(rectF, 135, 270, false, paint);

        LinearGradient gradient = new LinearGradient(0, 0, minDimension, minDimension, startColor, endColor, Shader.TileMode.CLAMP);
        paint.setShader(gradient);

        float sweepAngle = 270f * progress / max;
        canvas.drawArc(rectF, 135, sweepAngle, false, paint);

        String progressText = String.valueOf(progress);
        float x = getWidth() / 2f;
        float y = (getHeight() / 2f) - ((textPaint.descent() + textPaint.ascent()) / 2f);
        canvas.drawText(progressText, x, y, textPaint);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectF.set(0 + paint.getStrokeWidth() / 2, 0 + paint.getStrokeWidth() / 2, w - paint.getStrokeWidth() / 2, h - paint.getStrokeWidth() / 2);
    }

    public void setMax(int max) {
        this.max = max;
        invalidate();
    }

    public void setProgress(float progress) {
        if (progress <= max) {
            animateProgress(this.progress, (int) progress);
        } else {
            animateProgress(this.progress, max);
        }
    }

    /*public void setProgress(int progress) {
        if (progress <= max) {
            targetProgress = progress;
        } else {
            targetProgress = max;
        }
        progressHandler.sendEmptyMessage(0);
    }*/

    private void animateProgress(int from, int to) {
        ValueAnimator animator = ValueAnimator.ofInt(from, to);
        animator.setDuration(1000); // Duration of the animation, in milliseconds
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                SemiCircleProgressBar.this.progress = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }




}