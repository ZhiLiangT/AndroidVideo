package com.tianzl.androidvideo.textureview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tianzl.androidvideo.R;
import com.tianzl.androidvideo.custom.MyTextureView;
import com.tianzl.androidvideo.entity.VideoInfo;
import com.tianzl.androidvideo.utils.CommTools;

public class TextureViewActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView ivPlay;
    private ImageView ivBack;
    private ImageView ivDown;
    private ImageView ivShare;
    private ImageView ivFull;
    private SeekBar seekBar;
    private TextView tvTotalTime;
    private TextView tvPlayTime;
    private MyTextureView myTextureView;
    private TextView tvSort;

    private Intent intent;
    private VideoInfo videoInfo;
    private String url;
    private String sort;
    private int type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texture_view);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        //进度条的监听事件
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int pre=seekBar.getProgress();
                myTextureView.setPlayerPosition(pre);
            }
        });
        //MyTextureView的监听事件
        myTextureView.setOnVideoPlayingListener(new MyTextureView.OnVideoPlayingListener() {
            @Override
            public void onVideoSizeChanged(int vWidth, int vHeight) {

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onPlaying(int duration, int percent) {
                seekBar.setMax(duration);
                seekBar.setProgress(percent);
                tvPlayTime.setText(CommTools.LongToHms(percent));
            }

            @Override
            public void onPause() {

            }

            @Override
            public void onRestart() {

            }

            @Override
            public void onPlayingFinish() {

            }

            @Override
            public void onTextureDestory() {

            }

            @Override
            public void onPrepare() {
                myTextureView.setUrl(url);
                myTextureView.play();
            }
        });
    }

    private void initData() {
        intent=getIntent();
        videoInfo=intent.getParcelableExtra("VIDEO_INFO");
        sort=intent.getStringExtra("VIDEO_SORT");
        type=intent.getIntExtra("VIDEO_TYPE",-1);
        url=videoInfo.getFilePath();
        if (sort!=null){
            tvSort.setText(sort);
        }
        if (type!=-1){
            if (type==0){
                tvTotalTime.setText(videoInfo.getTime());
            }
        }

        myTextureView.setVideoMode(MyTextureView.CENTER_MODE);

    }

    private void initView() {
        myTextureView= (MyTextureView) findViewById(R.id.mytexture);
        ivPlay= (ImageView) findViewById(R.id.texture_iv_play);
        ivPlay.setOnClickListener(this);
        ivBack= (ImageView) findViewById(R.id.texture_iv_back);
        ivBack.setOnClickListener(this);
        ivDown= (ImageView) findViewById(R.id.texture_iv_download);
        ivDown.setOnClickListener(this);
        ivShare= (ImageView) findViewById(R.id.texture_iv_share);
        ivShare.setOnClickListener(this);
        ivFull= (ImageView) findViewById(R.id.texture_iv_full);
        ivFull.setOnClickListener(this);
        seekBar= (SeekBar) findViewById(R.id.texture_seekbar);
        tvTotalTime= (TextView) findViewById(R.id.texture_tv_total_time);
        tvPlayTime= (TextView) findViewById(R.id.texture_tv_start_time);
        tvSort= (TextView) findViewById(R.id.texture_tv_sort);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void finish() {
        super.finish();
        myTextureView.release();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.texture_iv_play:
                myTextureView.pause();
                break;
            case R.id.texture_iv_full:

                break;
            case R.id.texture_iv_back:
                finish();
                break;
        }
    }
}
