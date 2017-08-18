package com.tianzl.androidvideo.audio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.tianzl.androidvideo.R;

public class AudioPlayerActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btPlay;
    private Button btPause;
    private Button btStop;
    private SeekBar seekBar;
    private Player player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        initView();
    }

    private void initView() {
        btPlay= (Button) findViewById(R.id.audio_bt_play);
        btPlay.setOnClickListener(this);
        btPause= (Button) findViewById(R.id.audio_bt_pause);
        btPause.setOnClickListener(this);
        btStop= (Button) findViewById(R.id.audio_bt_stop);
        btStop.setOnClickListener(this);
        seekBar= (SeekBar) findViewById(R.id.audio_seekbar);
        seekBar.setOnSeekBarChangeListener(new seekBarChangeListener());
        player=new Player(seekBar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.audio_bt_play:
                String url="http://219.138.125.22/myweb/mp3/CMP3/JH19.MP3";
                player.playUrl(url);
                break;
            case R.id.audio_bt_pause:
                player.pause();
                break;
            case R.id.audio_bt_stop:
                player.stop();
                break;
        }
    }
    class seekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        int i;
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            this.i = i * player.mediaPlayer.getDuration()
                    / seekBar.getMax();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            player.mediaPlayer.seekTo(i);
        }
    }
}
