package com.mackwu.audioplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

/**
 * ===================================================
 * Created by MackWu on 2020/8/26 15:41
 * <a href="mailto:wumengjiao828@163.com">Contact me</a>
 * <a href="https://github.com/mackwu828">Follow me</a>
 * ===================================================
 */
public class AudioPlayer implements IAudioPlayer, MediaPlayer.OnPreparedListener {

    private static String TAG = AudioPlayer.class.getSimpleName();
    private MediaPlayer mediaPlayer;
    private String dataSource;
    private PlayState playState = PlayState.IDLE;

    public AudioPlayer(Builder builder) {
        this.mediaPlayer = builder.mediaPlayer;
        this.dataSource = builder.dataSource;
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
    }

    public static class Builder {
        private MediaPlayer mediaPlayer;
        private String dataSource;

        public Builder() {
            this.mediaPlayer = new MediaPlayer();
        }

        public Builder mediaPlayer(MediaPlayer mediaPlayer) {
            this.mediaPlayer = mediaPlayer;
            return this;
        }

        public Builder dataSource(String dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public AudioPlayer build() {
            return new AudioPlayer(this);
        }
    }

    @Override
    public void start() {
        Log.d(TAG, "start...  playState: " + playState);
        if (mediaPlayer == null) return;
        try {
            if (playState == PlayState.IDLE) {
                mediaPlayer.setDataSource(dataSource);
                mediaPlayer.prepareAsync();
                playState = PlayState.PREPARING;
            }
            // 如果状态是已暂停，重新播放
            if (playState == PlayState.PAUSED) {
                mediaPlayer.start();
                playState = PlayState.PLAYING;
            }
            // 如果状态是已停止，重新准备
            if (playState == PlayState.STOPPED) {
                mediaPlayer.prepareAsync();
                playState = PlayState.PREPARING;
            }
            // 如果状态是已完成，重新播放
            if (playState == PlayState.COMPLETED) {
                mediaPlayer.start();
                playState = PlayState.PLAYING;
            }
            // 如果状态是错误，重置并设置资源，准备播放
            if (playState == PlayState.ERROR) {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(dataSource);
                mediaPlayer.prepareAsync();
            }
        } catch (IOException e) {
            e.printStackTrace();
            playState = PlayState.ERROR;
        }
    }

    @Override
    public void pause() {
        Log.d(TAG, "pause...  playState: " + playState);
        if (mediaPlayer == null) return;
        if (playState == PlayState.PLAYING) {
            mediaPlayer.pause();
            playState = PlayState.PAUSED;
        }
    }

    @Override
    public void stop() {
        Log.d(TAG, "stop...  playState: " + playState);
        if (mediaPlayer == null) return;
        if (playState == PlayState.PREPARED || playState == PlayState.PLAYING || playState == PlayState.PAUSED) {
            mediaPlayer.stop();
            playState = PlayState.STOPPED;
        }
    }

    @Override
    public void release() {
        Log.d(TAG, "release...");
        mediaPlayer.release();
        mediaPlayer = null;
        playState = PlayState.RELEASED;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onPrepared...");
        try {
            playState = PlayState.PREPARED;
            mediaPlayer.start();
            playState = PlayState.PLAYING;
        } catch (Exception e) {
            e.printStackTrace();
            playState = PlayState.ERROR;
        }
    }

}
