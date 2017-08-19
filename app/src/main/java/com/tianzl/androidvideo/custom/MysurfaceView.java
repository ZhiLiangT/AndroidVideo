package com.tianzl.androidvideo.custom;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.tianzl.androidvideo.surfaceview.SurfaceActivity;
import com.tianzl.androidvideo.utils.CommTools;

import java.io.IOException;

import static java.lang.Math.min;

/**
 * Created by tianzl on 2017/8/17.
 */

public class MysurfaceView extends SurfaceView implements
        MediaPlayer.OnErrorListener
        ,MediaPlayer.OnCompletionListener
        ,MediaPlayer.OnVideoSizeChangedListener
        ,SurfaceHolder.Callback{
    private static final String TAG=MysurfaceView.class.getSimpleName();

    private MediaPlayer mediaPlayer;
    private SurfaceHolder holder;
    /**视频播放的Url*/
    private String url;
    /**播放状态*/
    private boolean isPlay;
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
    private Context context;
    public MysurfaceView(Context context) {
        super(context);
        init(context);
    }
    public MysurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MysurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public interface  OnVideoPlayingListener{
        void onVideoSizeChanged(int vWidth, int vHeight);
        void onPlaying(int duration, int percent);
        void onStart();
        void onPlayOver();
        void onVideoSize(int videoSize);
    }
    private OnVideoPlayingListener listener;
    public void setOnVideoPlayingListener(OnVideoPlayingListener listener){
        this.listener=listener;
    }

    /**设置监听*/
    private void initEvent() {
        /**注册当surfaceView创建、改变和销毁时应该执行的方法*/
        holder.addCallback(this);
        /**播放出错时的监听*/
        mediaPlayer.setOnErrorListener(this);
        /**播放结束时的监听*/
        mediaPlayer.setOnCompletionListener(this);
        /**视频尺寸的监听*/
        mediaPlayer.setOnVideoSizeChangedListener(this);
    }
    /**初始化*/
    private void init(Context context) {
        this.context=context;
        mediaPlayer=new MediaPlayer();
        holder=this.getHolder();
        /**
         *  这里必须设置为SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS哦，意思
         *  是创建一个push的'surface'，主要的特点就是不进行缓冲
         */
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        systemWidth = dm.widthPixels;
        systemHeight=dm.heightPixels;
        initEvent();
    }
    /**设置全屏播放*/
    public void setFullScreen(){
        hideNavigationBar();
        ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        scaleChangeSize(systemHeight,systemWidth);
    }
    /**恢复半屏播放*/
    public void setHalfScreen(){
        showNavigationBar();
        ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        scaleChangeSize(surWidth,surHeight);
    }
    /**显示状态栏*/
    private void showNavigationBar(){
        View decorView =((Activity)context). getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
    /**隐藏状态栏*/
    public void hideNavigationBar() {
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN; // hide status bar
        ((Activity)context).getWindow().getDecorView().setSystemUiVisibility(uiFlags);
    }

    /**设置视频播放路径*/
    public void setUrl(String url){
        this.url=url;
    }
    /**暂停播放和继续播放*/
    public void pause() {
        if (mediaPlayer!=null){
            if (mediaPlayer.isPlaying()&&isPlay==true){
                mediaPlayer.pause();
            }else {
                mediaPlayer.start();
            }
        }
    }
    /**停止播放*/
    public void stop(){
        mediaPlayer.stop();
    }
    /**指定位置播放*/
    public void seekTo(int progress){
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            /**设置当前播放的位置*/
            mediaPlayer.seekTo(progress);
        }
    }
    /**销毁 回收资源*/
    public void finishVideo(){
        mediaPlayer.stop();
        mediaPlayer.release();
    }
    /**等比例缩放视频*/
    public void scaleChangeSize(float width,float height){
        float xsca=width/videoWidth;
        float ysca=height/videoHeight;
        float r=min(xsca,ysca);
        float w=videoWidth*r;
        float h=videoHeight*r;
        ViewGroup.LayoutParams params= getLayoutParams();
        params.width= (int) w;
        params.height= (int) h;
        setLayoutParams(params);
    }
    /**开始播放*/
    public void play(){
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
            /**异步装载*/
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(final MediaPlayer mediaPlayer) {
                    /**等比例缩放视频尺寸*/
                    videoWidth=mediaPlayer.getVideoWidth();
                    videoHeight=mediaPlayer.getVideoHeight();
                    surWidth=getWidth();
                    surHeight=getHeight();
                    scaleChangeSize(surWidth,surHeight);
                    mediaPlayer.start();
                    listener.onVideoSize(mediaPlayer.getDuration());
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
                                    sleep(500);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    isPlay=true;
                    listener.onStart();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (listener != null ) {
                    listener.onPlaying(mediaPlayer.getDuration(), (Integer) msg.obj);
                    sendEmptyMessageDelayed(0, 1000);
                }
            }
        }
    };



    /**播放结束的监听*/
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        isPlay=false;
        listener.onPlayOver();
    }
    /**播放错误的监听*/
    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        isPlay=false;
        return false;
    }
    /**视频尺寸的监听*/
    @Override
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
        int videoW=mediaPlayer.getVideoWidth();
        int videoH=mediaPlayer.getVideoHeight();
        if (listener!=null){
            listener.onVideoSizeChanged(videoW,videoH);
        }
    }
    /**SurfaceHolder被创建*/
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mediaPlayer.setDisplay(holder);
    }
    /**SurfaceHolder被改变*/
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {}
    /**SurfaceHolder被销毁*/
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {}
}
