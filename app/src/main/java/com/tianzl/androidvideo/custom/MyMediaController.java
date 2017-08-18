package com.tianzl.androidvideo.custom;

import android.content.Context;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.MediaController;

/**
 * Created by tianzl on 2017/8/8.
 */

public class MyMediaController extends MediaController implements
        MediaPlayer.OnVideoSizeChangedListener,MediaPlayer.OnPreparedListener{



    public MyMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyMediaController(Context context, boolean useFastForward) {
        super(context, useFastForward);
    }

    public MyMediaController(Context context) {
        super(context);
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

    }

    @Override
    public void setAnchorView(View view) {
        super.setAnchorView(view);
    }
}
