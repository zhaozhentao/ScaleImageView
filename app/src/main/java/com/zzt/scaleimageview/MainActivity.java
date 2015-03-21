package com.zzt.scaleimageview;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class MainActivity extends ActionBarActivity {

    ScaleImageView scaleImageView;
    ImageView imageView;
    ImageView imageView1;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*
        scaleImageView = (ScaleImageView)findViewById(R.id.scaleimageiew);
        imageView = (ImageView)findViewById(R.id.img);
        imageView1 = (ImageView)findViewById(R.id.img1);

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scaleImageView.open(imageView1);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scaleImageView.open(imageView);
            }
        });
*/

        setContentView(R.layout.activity);
        final ImageView imageView2 = (ImageView)findViewById(R.id.img);
        final MyImageView imageView3 = (MyImageView)findViewById(R.id.img1);
        final ImageView imageView4 = (ImageView)findViewById(R.id.img2);
        imageView3.setWindowHeight(getWindowManager().getDefaultDisplay().getHeight());

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView3.open(imageView2);
            }
        });
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView3.open(imageView4);
            }
        });

    }

}
