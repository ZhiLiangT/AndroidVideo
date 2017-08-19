package com.tianzl.androidvideo.videoview;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import com.tianzl.androidvideo.R;

import com.tianzl.androidvideo.entity.VideoInfo;

public class VideoViewActivity extends AppCompatActivity implements View.OnClickListener{
    private String TAG=VideoViewActivity.class.getSimpleName();
    private VideoInfo videoInfo;
    private VideoView videoView;
    private LinearLayout bottomLayout;
    private LinearLayout topLayout;
    /**进度条*/
    private SeekBar seekBar;
    /**返回*/
    private ImageView ivBack;
    /**下载*/
    private ImageView ivDown;
    /**分享*/
    private ImageView ivShare;
    /**播放*/
    private ImageView ivPlay;
    /**全屏*/
    private ImageView ivFull;
    /**播放时间和总时长*/
    private TextView tvPlayTime,tvTotalTime;
    /**当前视频的次序*/
    private TextView tvSort;
    /**播放画面中间的图片*/
    private ImageView ivState;

    private Intent intent;
    private String position;
    private int progress;
    private MediaPlayer mp;
    private MyMediaConnller connller;
    //播放状态
    private boolean isPlay=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        intent=getIntent();
        videoInfo=intent.getParcelableExtra("VIDEO_INFO");
        position=intent.getStringExtra("VIDEO_SORT");
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        //缓冲进度的监听
        videoView.setOnPreparedListener(new MyPlayOnPreparedListener());
        //播放完成回调
        videoView.setOnCompletionListener( new MyPlayerOnCompletionListener());
    }
    private void initData() {

        videoView.setMediaController(new MediaController(this));
        videoView.setVideoPath(videoInfo.getFilePath());
        tvSort.setText(position);

    }
    private void initView() {
        videoView= (VideoView) findViewById(R.id.videoview_view);
        topLayout= (LinearLayout) findViewById(R.id.video_top_ll);
        bottomLayout= (LinearLayout) findViewById(R.id.video_bottom_ll);
        seekBar= (SeekBar) findViewById(R.id.video_seekbar);
        ivBack= (ImageView) findViewById(R.id.video_iv_back);
        ivBack.setOnClickListener(this);
        ivDown= (ImageView) findViewById(R.id.video_iv_download);
        ivDown.setOnClickListener(this);
        ivShare= (ImageView) findViewById(R.id.video_iv_share);
        ivShare.setOnClickListener(this);
        ivPlay= (ImageView) findViewById(R.id.video_iv_play);
        ivPlay.setOnClickListener(this);
        ivFull= (ImageView) findViewById(R.id.video_iv_full);
        ivFull.setOnClickListener(this);
        tvPlayTime= (TextView) findViewById(R.id.video_tv_start_time);
        tvTotalTime= (TextView) findViewById(R.id.video_tv_total_time);
        tvSort= (TextView) findViewById(R.id.video_tv_sort);
        ivState= (ImageView) findViewById(R.id.video_iv_state);
        ivState.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            /**全屏显示*/
            case R.id.video_iv_full:

                break;
            /**播放和暂停*/
            case R.id.video_iv_play:
                pause();
                break;
            /**返回*/
            case R.id.video_iv_back:
                finish();
                break;
            /**下载*/
            case R.id.video_iv_download:
                Toast.makeText(this,"本地文件无需下载",Toast.LENGTH_SHORT).show();
                break;
            /**分享*/
            case R.id.video_iv_share:
                break;
            /**播放画面中间的图片*/
            case R.id.video_iv_state:
                break;
        }
    }
    /**定时任务*/
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            isSelectedTitlebar(false);
            handler.postDelayed(this, 3000);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        videoView.start();
        ivState.setSelected(true);
        ivPlay.setSelected(true);
    }

    @Override
    public void finish() {
        videoView.stopPlayback();
        super.finish();
    }

    //显示和隐藏上下标题栏
    public void isSelectedTitlebar(boolean isShow){
        if (isShow){
            //显示
            bottomLayout.setVisibility(View.VISIBLE);
            topLayout.setVisibility(View.VISIBLE);
            ivState.setVisibility(View.VISIBLE);
            isPlay=false;
        }else{
            //隐藏
            bottomLayout.setVisibility(View.GONE);
            topLayout.setVisibility(View.GONE);
            ivState.setVisibility(View.VISIBLE);
            isPlay=true;
        }
    }
    /**播放和暂停*/
    public void pause(){
        //如果正在播放，暂停播放，显示顶部和底部标签
        if (videoView.isPlaying()){
            videoView.pause();
            isPlay=true;
            isSelectedTitlebar(true);
        }else {
            videoView.start();
            isPlay=false;
            isSelectedTitlebar(false);
        }
    }

    private void showNavigationBar(){
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
    public void hideNavigationBar() {
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN; // hide status bar

        getWindow().getDecorView().setSystemUiVisibility(uiFlags);
    }


    class MyMediaConnller extends MediaController {

        public MyMediaConnller(Context context, AttributeSet attrs) {
            super(context, attrs);

        }

    }

    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText( VideoViewActivity.this, "播放完成了", Toast.LENGTH_SHORT).show();
        }
    }
    class MyPlayOnPreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
        }
    }
}
