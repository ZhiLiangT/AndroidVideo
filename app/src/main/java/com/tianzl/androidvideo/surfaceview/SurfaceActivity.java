package com.tianzl.androidvideo.surfaceview;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tianzl.androidvideo.R;
import com.tianzl.androidvideo.entity.VideoInfo;
import com.tianzl.androidvideo.utils.CommTools;

import java.io.IOException;
import java.math.BigDecimal;


import static java.lang.Math.min;

/**
 * 使用SurfaceView播放视频
 */
public class SurfaceActivity extends AppCompatActivity implements
        View.OnClickListener,View.OnTouchListener,MediaPlayer.OnErrorListener
    ,MediaPlayer.OnCompletionListener,MediaPlayer.OnVideoSizeChangedListener
{
    private static final String TAG=SurfaceActivity.class.getSimpleName();
    /**控件*/
    private SurfaceView surfaceView;
    private LinearLayout topLayout;
    private LinearLayout bottomLayout;
    private ImageView ivState;
    private ImageView ivBack;
    private ImageView ivDown;
    private ImageView ivShare;
    private ImageView ivPlay;
    private ImageView ivFull;
    private TextView tvPlayTime;
    private TextView tvTotalTime;
    private TextView tvSort;
    private SeekBar seekBar;
    private RelativeLayout rootLayout;
    private RelativeLayout rvLayout;
    /**相关变量*/
    private Intent intent;
    private VideoInfo videoInfo;
    private String sort;
    private boolean isPlay;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder holder;
    private GestureDetector mGestureDetector;
    private boolean isShowMenu;
    private int type;
    /**横竖屏标识*/
    private boolean screenDirection=true;

    /**视频的宽高*/
    private float videoHeight;
    private float videoWidth;
    /**系统屏幕的宽高*/
    private float systemWidth;
    private float systemHeight;
    /**控件的宽高*/
    private float surWidth;
    private float surHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        /**注册当surfaceView创建、改变和销毁时应该执行的方法*/
        holder.addCallback(new MySurfaceHolderCallback());
        /**播放出错时的监听*/
        mediaPlayer.setOnErrorListener(this);
        /**播放结束时的监听*/
        mediaPlayer.setOnCompletionListener(this);
        /**视频尺寸的监听*/
        mediaPlayer.setOnVideoSizeChangedListener(this);
        /**为进度条添加进度更改事件*/
        seekBar.setOnSeekBarChangeListener(change);
    }

    private void initData() {
        intent=getIntent();
        videoInfo=intent.getParcelableExtra("VIDEO_INFO");
        type=intent.getIntExtra("VIDEO_TYPE",-1);
        sort=intent.getStringExtra("VIDEO_SORT");
        tvSort.setText(sort);
        /**设置总时长*/
        if (type==0){
            tvTotalTime.setText(videoInfo.getTime());
        }
        rootLayout.setOnTouchListener(this);
        mGestureDetector=new GestureDetector(listener);
        mediaPlayer = new MediaPlayer();
        /**获取SurfaceHolder*/
        holder=surfaceView.getHolder();
        /**
         *  这里必须设置为SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS哦，意思
         *  是创建一个push的'surface'，主要的特点就是不进行缓冲
         */
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        systemWidth = dm.widthPixels;
        systemHeight=dm.heightPixels;
    }

    private void initView() {
        rootLayout= (RelativeLayout) findViewById(R.id.surface_root);
        surfaceView= (SurfaceView) findViewById(R.id.surface_view);
        topLayout= (LinearLayout) findViewById(R.id.surface_top_ll);
        bottomLayout= (LinearLayout) findViewById(R.id.surface_bottom_ll);
        ivState= (ImageView) findViewById(R.id.surface_iv_state);
        ivState.setOnClickListener(this);
        ivBack= (ImageView) findViewById(R.id.surface_iv_back);
        ivBack.setOnClickListener(this);
        ivDown= (ImageView) findViewById(R.id.surface_iv_download);
        ivDown.setOnClickListener(this);
        ivShare= (ImageView) findViewById(R.id.surface_iv_share);
        ivShare.setOnClickListener(this);
        ivPlay= (ImageView) findViewById(R.id.surface_iv_play);
        ivPlay.setOnClickListener(this);
        ivFull= (ImageView) findViewById(R.id.surface_iv_full);
        ivFull.setOnClickListener(this);
        tvPlayTime= (TextView) findViewById(R.id.surface_tv_start_time);
        tvTotalTime= (TextView) findViewById(R.id.surface_tv_total_time);
        tvSort= (TextView) findViewById(R.id.surface_tv_sort);
        seekBar= (SeekBar) findViewById(R.id.surface_seekbar);
        rvLayout= (RelativeLayout) findViewById(R.id.surface_rl_sv);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.surface_iv_state:
                pause();
                break;
            case R.id.surface_iv_back:
                finish();
                break;
            case R.id.surface_iv_download:
                Toast.makeText(this,"本地视频无需下载",Toast.LENGTH_SHORT).show();
                break;
            case R.id.surface_iv_share:
                Toast.makeText(this,"暂无分享功能",Toast.LENGTH_SHORT).show();
                break;
            case R.id.surface_iv_play:
                pause();
                break;
            case R.id.surface_iv_full:
                screenCut();
                break;
        }
    }
    /**手动切换横竖屏*/
    public void  screenCut(){
        if (screenDirection){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            screenDirection=false;
        }else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            screenDirection=true;
        }
    }


    /**监听横竖屏切换*/
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
            showNavigationBar();
            double xsca=surWidth/videoWidth;
            double ysca=surHeight/videoHeight;
            double r=min(xsca,ysca);
            double width=videoWidth*r;
            double height=videoHeight*r;
            ViewGroup.LayoutParams params= surfaceView.getLayoutParams();
            params.width= (int) width;
            params.height= (int) height;
            surfaceView.setLayoutParams(params);
            Toast.makeText(SurfaceActivity.this, "现在是竖屏", Toast.LENGTH_SHORT).show();
        }
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            hideNavigationBar();
//            rootLayout.setSystemUiVisibility(View.INVISIBLE);
            /**等比例缩放视频尺寸*/
            double xsca=systemHeight/videoWidth;
            double ysca=systemWidth/videoHeight;
            double r=min(xsca,ysca);
            double width=videoWidth*r;
            double height=videoHeight*r;
            ViewGroup.LayoutParams params= surfaceView.getLayoutParams();
            params.width= (int) width;
            params.height= (int) height;
            surfaceView.setLayoutParams(params);
            Toast.makeText(SurfaceActivity.this, "现在是横屏", Toast.LENGTH_SHORT).show();
        }
    }
    private void showNavigationBar(){
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
    public void hideNavigationBar() {
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN; // hide status bar
        getWindow().getDecorView().setSystemUiVisibility(uiFlags);
    }

    /**定时任务*/
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                int playTime= (int) msg.obj;
                tvPlayTime.setText(""+CommTools.LongToHms(playTime));
            }
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            isShowHideTitle(false);
            handler.postDelayed(this, 3000);
        }
    };

    /**暂停和继续播放*/
    private void pause(){
        Log.i(TAG,"暂停和继续播放");
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            handler.removeCallbacks(runnable);
            ivState.setSelected(false);
            ivPlay.setSelected(false);
            /**暂停时，上下标题栏一直显示*/
            isShowHideTitle(true);

        }else {
            ivState.setSelected(true);
            ivPlay.setSelected(true);
            mediaPlayer.start();
            /**继续播放时，上下标题栏等待3s隐藏*/
            handler.postDelayed(runnable,3000);

        }
    }
    /**控制上下标题栏显示和隐藏*/
    private void isShowHideTitle(boolean is){

        if (is){
            topLayout.setVisibility(View.VISIBLE);
            bottomLayout.setVisibility(View.VISIBLE);
            ivState.setVisibility(View.VISIBLE);
            isShowMenu=true;
        }else {
            topLayout.setVisibility(View.GONE);
            bottomLayout.setVisibility(View.GONE);
            ivState.setVisibility(View.GONE);
            isShowMenu=false;
        }
    }
    /**加载完成后进来直接播放*/
    @Override
    protected void onResume() {
        play();
        super.onResume();
    }
    /**销毁时同时销毁MediaPlayer，释放资源*/
    @Override
    public void finish() {
        stop();
        handler.removeCallbacks(runnable);
        super.finish();
    }
    private SeekBar.OnSeekBarChangeListener change = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            /**当进度条停止修改的时候触发*/
            /**取得当前进度条的刻度*/
            int progress = seekBar.getProgress();
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                /**设置当前播放的位置*/
                mediaPlayer.seekTo(progress);
                tvPlayTime.setText(""+CommTools.LongToHms(progress));
            }
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }
    };



    class MySurfaceHolderCallback implements SurfaceHolder.Callback{

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            Log.i(TAG, "surfaceHolder被创建了");
            mediaPlayer.setDisplay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            Log.i(TAG, "surfaceHolder被改变了");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            Log.i(TAG, "surfaceHolder被销毁了");
        }
    }
    /**播放*/
    private void play(){
        /**设置多媒体流类型*/
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        /**设置用于展示mediaPlayer的容器  */
        try {
            mediaPlayer.setDataSource(videoInfo.getFilePath());
            /**异步装载*/
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(final MediaPlayer mediaPlayer) {
                    /**等比例缩放视频尺寸*/
                    videoWidth=mediaPlayer.getVideoWidth();
                    videoHeight=mediaPlayer.getVideoHeight();
                    surWidth=surfaceView.getWidth();
                    surHeight=surfaceView.getHeight();
                    double xsca=surWidth/videoWidth;
                    double ysca=surHeight/videoHeight;
                    double r=min(xsca,ysca);
                    double width=videoWidth*r;
                    double height=videoHeight*r;
                    ViewGroup.LayoutParams params= surfaceView.getLayoutParams();
                    params.width= (int) width;
                    params.height= (int) height;
                    surfaceView.setLayoutParams(params);

                    /**装载完成，开始播放*/
                    mediaPlayer.start();
                    if (type==1){
                        tvTotalTime.setText(CommTools.LongToHms(mediaPlayer.getDuration()));
                    }

                    /**为进度条设置最大值*/
                    seekBar.setMax(mediaPlayer.getDuration());
                    /**开始播放时，一些变量的初始化*/
                    ivState.setSelected(true);
                    ivPlay.setSelected(true);
                    /**开始线程，更新进度条的刻度*/
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                isPlay = true;
                                while (isPlay) {
                                    int current = mediaPlayer.getCurrentPosition();
                                    Message message=Message.obtain();
                                    message.what=1;
                                    message.obj=current;
                                    handler.sendMessage(message);
                                    seekBar.setProgress(current);
                                    sleep(500);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    handler.removeCallbacks(runnable);
                    handler.postDelayed(runnable, 3000);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**停止播放*/
    private void stop(){
        Log.e(TAG,"停止播放");
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    /**播放出错时的监听*/
    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Toast.makeText(this,"播放出错，请重新播放",Toast.LENGTH_SHORT).show();
        return false;
    }
    /**播放结束时的监听*/
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.e(TAG,"播放结束时的监听");
        isPlay=false;
        Toast.makeText(this,"播放结束",Toast.LENGTH_SHORT).show();
        ivState.setSelected(false);
        ivPlay.setSelected(false);
        isShowHideTitle(true);
    }
    /**视频尺寸的监听*/
    @Override
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height) {

    }
    /**触摸事件*/
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return mGestureDetector.onTouchEvent(motionEvent);
    }
    /**手势监听*/
    GestureDetector.OnGestureListener listener = new GestureDetector.OnGestureListener(){

        @Override
        public boolean onDown(MotionEvent motionEvent) {
//            Log.i(TAG, "onDown");
            if (isShowMenu){
                isShowHideTitle(false);
            }else {
                isShowHideTitle(true);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable,3000);
            }
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {
//            Log.i(TAG, "onShowPress");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
//            Log.i(TAG, "onSingleTapUp");
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
//            Log.i(TAG, "onScroll");
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
//            Log.i(TAG, "onLongPress");
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
//            Log.i(TAG, "onFling");
            return false;
        }
    };

}
