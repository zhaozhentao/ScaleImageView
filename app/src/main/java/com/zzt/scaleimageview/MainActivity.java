package com.zzt.scaleimageview;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        final ImageView imageView1 = (ImageView)findViewById(R.id.img);
        final ImageView imageView2 = (ImageView)findViewById(R.id.img2);
        final ScaleImageView scaleImageView = (ScaleImageView)findViewById(R.id.img1);

        scaleImageView.setWindowHeight(getWindowManager().getDefaultDisplay().getHeight());

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scaleImageView.open(imageView1);
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scaleImageView.open(imageView2);
            }
        });

    }

}
