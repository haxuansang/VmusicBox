package com.mozia.VmusicBox;

/**
 * Created by thiennhan on 8/12/17.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class VerticalSeekBar extends SeekBar {
    protected OnSeekBarChangeListener changeListener;

    public VerticalSeekBar(Context context) {
        super(context);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    protected void onDraw(Canvas c) {
        c.rotate(-90.0f);
        c.translate((float) (-getHeight()), 0.0f);
        super.onDraw(c);
    }

    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        onSizeChanged(getWidth(), getHeight(), 0, 0);
        onSizeChanged(getWidth(), getHeight(), 0, 0);
    }

    private void attemptClaimDrag() {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case 0:
                setPressed(true);
                setPressed(true);
                if (this.changeListener != null) {
                    this.changeListener.onStartTrackingTouch(this);
                }
                attemptClaimDrag();
                break;
            case 1:
                setSelected(false);
                setPressed(false);
                if (this.changeListener != null) {
                    this.changeListener.onStopTrackingTouch(this);
                    break;
                }
                break;
            case 2:
                int progress = getMax() - ((int) ((((float) getMax()) * event.getY()) / ((float) getHeight())));
                setProgress(progress);
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                if (this.changeListener != null) {
                    this.changeListener.onProgressChanged(this, progress, true);
                    break;
                }
                break;
        }
        return true;
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener mListener) {
        this.changeListener = mListener;
    }
}