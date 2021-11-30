package com.donews.alive.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * @author by SnowDragon
 * Date on 2020/11/26
 * Description:
 */
public class LockView extends LinearLayout {

    public static final int SCROLL_HEIGHT_OVER = 500;

    private float mLastY = 0;
    private float mCurY = 0;
    private Scroller mScroller;
    private int viewHeight;

    public LockView(Context context) {
        this(context, null);
    }

    public LockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mScroller = new Scroller(context);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float deltaY = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float y = this.getY();
                mCurY = event.getY();
                deltaY = mCurY - mLastY;
                if (deltaY < 0 || getScrollY() > 0) {
                    this.scrollTo(0, (int) (-1 * deltaY));
                }
                break;
            case MotionEvent.ACTION_UP:
                int scrollY = getScrollY();
                if (scrollY > SCROLL_HEIGHT_OVER) {
                    smoothScrollTo(0, viewHeight - scrollY);
                    callScreenScrollListener(scrollY);

                } else {
                    smoothScrollTo(0, -scrollY);
                }
                invalidate();
                break;
            default:
        }


        return true;
    }

    private void smoothScrollTo(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        smoothScrollBy(dx, dy);
    }

    private void smoothScrollBy(int dx, int dy) {
        mScroller.startScroll(0, this.getScrollY(), dx, dy);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        //如果当前正在滚动
        if (mScroller.computeScrollOffset()) {
            //滚动到(x,y)处
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    private ScreenScrollViewListener scrollViewListener;

    private void callScreenScrollListener(int scrollY) {
        if (scrollViewListener != null) {
            scrollViewListener.scrollY(scrollY);
        }
    }

    public void setScreenScrollViewListener(ScreenScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    public interface ScreenScrollViewListener {
        /**
         * @param scrollY Y轴滚动
         */
        void scrollY(int scrollY);
    }

}
