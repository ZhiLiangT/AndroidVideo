package com.tianzl.androidvideo.test;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tianzl.androidvideo.R;
import com.tianzl.androidvideo.custom.MysurfaceView;
import com.tianzl.androidvideo.entity.VideoInfo;
import com.tianzl.androidvideo.surfaceview.SurfaceActivity;
import com.tianzl.androidvideo.utils.CommTools;

import static java.lang.Math.min;

public class TestSurfaceActivity extends AppCompatActivity implements View.OnClickListener{
    private Intent intent;
    private VideoInfo videoInfo;
    private ImageView imgPlay;
    private ImageView imgBack;
    private SeekBar seekBar;
    private TextView tvTotalTime;
    private TextView tvPlayTime;
    private ImageView ivAll;
    private MysurfaceView mysurfaceView;
    private boolean isFull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_surface);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        mysurfaceView.setOnVideoPlayingListener(new MysurfaceView.OnVideoPlayingListener() {
            @Override
            public void onVideoSizeChanged(int vWidth, int vHeight) {

            }

            @Override
            public void onPlaying(int duration, int percent) {
                Log.i("surface","播放进度"+"总时长"+duration+" 当前播放进度"+percent);
                seekBar.setMax(duration);
                seekBar.setProgress(percent);
                tvPlayTime.setText(CommTools.LongToHms(percent));
            }

            @Override
            public void onStart() {
                Toast.makeText(TestSurfaceActivity.this,"开始播放",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPlayOver() {
                finish();
            }
            /**播放总时长*/
            @Override
            public void onVideoSize(int videoSize) {
                tvTotalTime.setText(CommTools.LongToHms(videoSize));
                seekBar.setMax(videoSize);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                /**当进度条停止修改的时候触发*/
                /**取得当前进度条的刻度*/
                int progress = seekBar.getProgress();
                /**设置当前播放的位置*/
                mysurfaceView.seekTo(progress);
                tvPlayTime.setText(""+CommTools.LongToHms(progress));


            }
        });
    }

    private void initData() {
        intent=getIntent();
        videoInfo=intent.getParcelableExtra("VIDEO_INFO");
        mysurfaceView.setUrl(videoInfo.getFilePath());
    }

    private void initView() {
        imgBack= (ImageView) findViewById(R.id.test_sur_iv_back);
        imgBack.setOnClickListener(this);
        imgPlay= (ImageView) findViewById(R.id.test_sur_iv_play);
        imgPlay.setOnClickListener(this);
        ivAll= (ImageView) findViewById(R.id.test_sur_iv_full);
        ivAll.setOnClickListener(this);
        seekBar= (SeekBar) findViewById(R.id.test_sur_seekbar);
        tvTotalTime= (TextView) findViewById(R.id.test_sur_tv_total_time);
        tvPlayTime= (TextView) findViewById(R.id.test_sur_tv_start_time);
        mysurfaceView= (MysurfaceView) findViewById(R.id.test_sur_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mysurfaceView.setUrl(videoInfo.getFilePath());
        mysurfaceView.play();
    }


    @Override
    public void finish() {
        super.finish();
        mysurfaceView.finishVideo();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.test_sur_iv_back:
                finish();
                break;
            case R.id.test_sur_iv_play:
                mysurfaceView.pause();
                break;
            case R.id.test_sur_iv_full:
                isFull();
                break;

        }
    }
    public void isFull(){
        if (isFull){
            mysurfaceView.setHalfScreen();
            isFull=false;
        }else {
            mysurfaceView.setFullScreen();
            isFull=true;
        }
    }
}
