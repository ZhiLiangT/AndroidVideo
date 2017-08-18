package com.tianzl.androidvideo.test;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.tianzl.androidvideo.R;
import com.tianzl.androidvideo.utils.CommTools;

public class ObBitmapActivity extends AppCompatActivity {
    private Button btOb;
    private ImageView ivIcon;
    private String url="https://media.w3.org/2010/05/sintel/trailer.mp4";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ob_bitmap);
        btOb= (Button) findViewById(R.id.bt_obimg);
        ivIcon= (ImageView) findViewById(R.id.test_img);
        btOb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap=CommTools.createVideoThumbnail(url,400,300);
                ivIcon.setImageBitmap(bitmap);
            }
        });
    }
}
