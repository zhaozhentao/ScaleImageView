package com.zzt.scaleimageview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.zzt.interfaces.ScaleListener;

/**
 * Created by zzt on 2015/3/19.
 */
public class ScaleImageView1 extends FrameLayout {

    private ScaleImageViewContent scaleImageViewContent;
    public ScaleImageView1(Context context) {
        this(context, null);
    }

    public ScaleImageView1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleImageView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scaleImageViewContent = new ScaleImageViewContent(context);
        scaleImageViewContent.setScaleListener(new ScaleListener() {
            @Override
            public void onClosedEnd() {
                setVisibility(INVISIBLE);
            }
        });
        addView(scaleImageViewContent);
        setVisibility(INVISIBLE);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

        setLayerType(LAYER_TYPE_HARDWARE, null);
    }

    public void open(ImageView imageView){
        setVisibility(VISIBLE);
        scaleImageViewContent.open(imageView);
    }

    public void close(){
        scaleImageViewContent.close();
    }

    public void setBgAlpha(int alpha) {
        scaleImageViewContent.setBgAlpha(alpha);
    }

}
