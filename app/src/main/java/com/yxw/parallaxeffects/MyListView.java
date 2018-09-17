package com.yxw.parallaxeffects;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

public class MyListView extends ListView {
    private int mOriginalHeight;//显示高度
    private int drawableHeight;//图片drawable的原始高度
    private ImageView mImage;

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setParallaxImage(ImageView mImage) {
        this.mImage = mImage;
        mOriginalHeight = mImage.getHeight();//getHeight()得到显示的高度
        drawableHeight = mImage.getDrawable().getIntrinsicHeight();//ImageView得到drawable图片，然后得到图片drawable的原始高度
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        // 手指拉动 并且 是下拉
        if (isTouchEvent && deltaY < 0) {
            // 把拉动的瞬时变化量的绝对值交给Header, 就可以实现放大效果
            if (mImage.getHeight() <= drawableHeight) {
                int newHeight = (int) (mImage.getHeight() + Math.abs(deltaY / 3.0f));
                // 高度不超出图片最大高度时,才让其生效
                mImage.getLayoutParams().height = newHeight;
                mImage.requestLayout();
            }
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                // 执行回弹动画, 方式一: 属性动画\值动画
                // 从当前高度mImage.getHeight(), 执行动画到原始高度mOriginalHeight
                final int startHeight = mImage.getHeight();
                final int endHeight = mOriginalHeight;
                valueAnimator(startHeight, endHeight);

                // 执行回弹动画, 方式二: 自定义Animation
//                ResetAnimation animation = new ResetAnimation(mImage, startHeight, endHeight);
//                startAnimation(animation);
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void valueAnimator(final int startHeight, final int endHeight) {
        ValueAnimator mValueAnim = ValueAnimator.ofInt(startHeight,endHeight);
        mValueAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator mAnim) {
                //float fraction = mAnim.getAnimatedFraction();
                //Integer newHeight = (int) (startHeight + fraction * (endHeight - startHeight));
                int newHeight = (int) mAnim.getAnimatedValue();
                mImage.getLayoutParams().height = newHeight;
                mImage.requestLayout();
            }
        });

        mValueAnim.setInterpolator(new OvershootInterpolator());
        mValueAnim.setDuration(500);
        mValueAnim.start();
    }
}
