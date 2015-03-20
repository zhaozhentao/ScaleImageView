package com.zzt.scaleimageview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zzt.interfaces.ScaleListener;

/**
 * Created by zzt on 2015/3/18.
 */
public class ScaleImageViewContent extends ImageView{

    private static final int DEFAULT_ANIM_DURATION = 300;

    private View parent;
    private ValueAnimator animator;

    /**
     * 需要被放大的图片的宽高
     * */
    private int mOriginalWidth;
    private int mOriginalHeight;

    /**
     * 图片最终要被放大的宽高
     * */
    private int mDrawableFinalWidth;
    private int mDrawableFinalHeight;
    private int mDrawableFinalMarginTop;

    private int mMarginTop;
    private int mMarginLeft;

    private int mParentWidth;
    private int mParentHeight;

    private FrameLayout.LayoutParams mLayoutParams;

    private int[] location;
    private int[] parentLocation;
    private int[] mLocation = new int[2];

    private boolean mIsOpen = false;

    private ImageView mOriginImageView;

    private int mBgAlpha = 255;

    private ScaleListener mScaleListener;

    public ScaleImageViewContent(Context context) {
        this(context, null);
    }

    public ScaleImageViewContent(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleImageViewContent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

        animator = ValueAnimator.ofFloat(0, 0);
        animator.setDuration(DEFAULT_ANIM_DURATION);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                parent.setBackgroundColor((int)(mBgAlpha * value)<< 24);
                mLayoutParams.topMargin = (int) ( mMarginTop + ( mDrawableFinalMarginTop - mMarginTop) * value);
                mLayoutParams.leftMargin = (int) (mMarginLeft * (1.0f - value));
                mLayoutParams.width = (int) (mOriginalWidth + (mParentWidth - mOriginalWidth) * value);
                mLayoutParams.height = (int) (mOriginalHeight + (mDrawableFinalHeight - mOriginalHeight) * value);
                ScaleImageViewContent.this.setLayoutParams(mLayoutParams);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(mIsOpen){

                }else{
                    mScaleListener.onClosedEnd();
                    ScaleImageViewContent.this.setImageDrawable(null);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void open(ImageView imageView){
        if(animator.isRunning()){
            return ;
        }

        mOriginImageView = imageView;

        if(parent==null)
            parent = (View)getParent();

        location = getViewWindowLocation(imageView);
        parentLocation = getViewWindowLocation(parent);
        location[1] = location[1] - parentLocation[1];

        mLayoutParams = (FrameLayout.LayoutParams)getLayoutParams();
        mMarginTop = location[1] - parent.getPaddingTop();
        mMarginLeft = location[0];
        //放大前的图片的宽高
        mOriginalWidth = imageView.getWidth();
        mOriginalHeight = imageView.getHeight();

        mParentWidth = parent.getWidth();
        mParentHeight = parent.getHeight() - parent.getPaddingTop();
        mLayoutParams.topMargin = mMarginTop;
        mLayoutParams.leftMargin = mMarginLeft;
        mLayoutParams.width = mOriginalWidth;
        mLayoutParams.height = mOriginalHeight;
        setLayoutParams(mLayoutParams);

        setImageDrawable(imageView.getDrawable());

        final int drawableWidth = getDrawable().getIntrinsicWidth();
        final int drawableHeight = getDrawable().getIntrinsicHeight();
        //图片放大后和parent的宽度一样
        mDrawableFinalWidth = parent.getWidth();
        mDrawableFinalHeight = Math.min(mParentHeight,
                Math.round((float) mDrawableFinalWidth / (float) drawableWidth * (float) drawableHeight));
        mDrawableFinalMarginTop = Math.max(0,
                (mParentHeight - mDrawableFinalHeight)/2 );

        mIsOpen = true;
        animator.setFloatValues(0, 1.0f);
        animator.start();
    }

    public void close(){
        if(animator.isRunning())
            return;

        mIsOpen = false;
        animator.setFloatValues(1.0f, 0);
        animator.start();
    }

    private int[] getViewWindowLocation(View view){
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return location;
    }

    public boolean getOpenStatus(){
        return mIsOpen;
    }

    public void setBgAlpha(int alpha){
        if(alpha<0 || alpha>255)
            throw new IllegalArgumentException("alpha should be [0..255]");
        mBgAlpha = alpha;
    }

    public void setScaleListener(ScaleListener scaleListener){
        mScaleListener = scaleListener;
    }

}
