package com.zzt.scaleimageview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

/**
 * Created by zzt on 2015/3/20.
 */
public class ScaleImageView extends ImageView {

    private static final int DEFAULT_ANIM_DURATION = 400;

    private final int VERTICAL = 1;
    private final int HORIZON = 2;
    private int scaleDirection = HORIZON;

    private View parentView;
    private int parentWidth;
    private int parentHeight;

    private ValueAnimator animator;

    private int left = 0;
    private int top = 0;
    private int right;
    private int bottom;

    private int beginTop;
    private int finalTop;
    private int beginLeft;
    private int finalLeft;

    private int[] mLocation = new int[2];
    private int[] srcLocation = new int[2];
    private int[] parentLocation = new int[2];

    private int mWindowHeight;

    private boolean mIsOpen;

    private boolean mFirstAnimPlay = false;

    public ScaleImageView(Context context) {
        this(context, null);
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
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
                if(scaleDirection == VERTICAL){
                    bottom = (int)(value * (parentHeight - drawableOriginHeight) + drawableOriginHeight);
                    right = bottom * drawableOriginWidth / drawableOriginHeight;
                }else{
                    right = drawableOriginWidth + (int)((parentWidth - drawableOriginWidth)*value);
                    bottom = right * drawableOriginHeight / drawableOriginWidth;
                }
                top = beginTop + (int)(value*(finalTop - beginTop));
                left = beginLeft + (int)(value*(finalLeft - beginLeft));
                parentView.setBackgroundColor((int) (200 * value) << 24);
                ScaleImageView.super.setFrame(left, top, right + left, bottom + top);
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
                    parentView.setVisibility(INVISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
    }

    private int drawableOriginWidth;
    private int drawableOriginHeight;


    public void open(ImageView srcImageView){

        if(animator.isRunning()){
            return ;
        }

        if(parentView == null){
            parentView = (View)getParent();
            parentWidth = parentView.getWidth();
            parentHeight = parentView.getHeight();
            parentView.getLocationInWindow(parentLocation);
        }

        Drawable drawable = srcImageView.getDrawable();
        right = drawableOriginWidth = drawable.getIntrinsicWidth();
        bottom = drawableOriginHeight = drawable.getIntrinsicHeight();

        setImageDrawable(drawable);

        if(parentView.getWidth()/(float)drawableOriginWidth > parentView.getHeight()/(float)drawableOriginHeight)
            scaleDirection = VERTICAL;//以垂直方向参考
        else
            scaleDirection = HORIZON;

        srcImageView.getLocationInWindow(srcLocation);
        beginLeft = srcLocation[0] - parentLocation[0];
        beginTop = srcLocation[1] - parentLocation[1];

        if(scaleDirection == VERTICAL){
            finalTop = 0;
            finalLeft = (parentWidth - parentHeight*drawableOriginWidth/drawableOriginHeight)/2;
        }else{
            finalTop = mWindowHeight/2 - mLocation[1] - parentWidth/drawableOriginWidth*drawableOriginHeight/2;
            finalLeft = 0;
        }
        mIsOpen = true;
        mFirstAnimPlay = true;
        parentView.setVisibility(VISIBLE);
        animator.setFloatValues(0, 1);
        animator.start();
    }

    public void close(){
        if(animator.isRunning())
            return ;
        mIsOpen = false;
        animator.setFloatValues(1, 0);
        animator.start();
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        return super.setFrame(left, top, right+left, bottom+top);
    }

    public void setWindowHeight(int windowHeight){
        mWindowHeight = windowHeight;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if(false == mFirstAnimPlay)
            getLocationInWindow(mLocation);
        super.onLayout(changed, left, top, right, bottom);
    }
}
