package com.tianzl.androidvideo.audio;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tianzl on 2017/8/8.
 */

public class Player implements MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener,MediaPlayer.OnPreparedListener{

    public MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private Timer timer=new Timer();
    public Player(SeekBar seekBar){
        this.seekBar=seekBar;
        try {
            mediaPlayer=new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
        }catch (Exception e){

        }
        timer.schedule(timerTask,0,1000);

    }
    /**定时器更新进度条*/
    TimerTask timerTask=new TimerTask() {
        @Override
        public void run() {
            if (mediaPlayer==null){
                return;
            }
            if (mediaPlayer.isPlaying()&&seekBar.isPressed()==false){
                handleProgress.sendEmptyMessage(0);
            }
        }
    };
    Handler handleProgress = new Handler() {
        public void handleMessage(Message msg) {

            int position = mediaPlayer.getCurrentPosition();
            int duration = mediaPlayer.getDuration();

            if (duration > 0) {
                long pos = seekBar.getMax() * position / duration;
                seekBar.setProgress((int) pos);
            }
        };
    };
    public void play(){
        mediaPlayer.start();
    }
    public void playUrl(String url){
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void pause(){
        mediaPlayer.pause();
    }
    public void stop(){
        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        seekBar.setSecondaryProgress(i);
        int currentProgress=seekBar.getMax()*mediaPlayer
                .getCurrentPosition()/mediaPlayer.getDuration();
        Log.e(currentProgress+"% play", i + "% buffer");
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }
}
