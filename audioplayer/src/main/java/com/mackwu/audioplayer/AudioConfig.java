package com.mackwu.audioplayer;

import android.media.MediaPlayer;

/**
 * ===================================================
 * Created by MackWu on 2020/8/26 15:46
 * <a href="mailto:wumengjiao828@163.com">Contact me</a>
 * <a href="https://github.com/mackwu828">Follow me</a>
 * ===================================================
 */
public class AudioConfig {

    private MediaPlayer mediaPlayer;
    private String dataSource;

    public AudioConfig(Builder builder) {
        this.mediaPlayer = builder.mediaPlayer;
        this.dataSource = builder.dataSource;
    }

    public static class Builder{
        private MediaPlayer mediaPlayer;
        private String dataSource;

        public Builder() {
        }

        public Builder mediaPlayer(MediaPlayer mediaPlayer) {
            this.mediaPlayer = mediaPlayer;
            return this;
        }

        public Builder dataSource(String dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public AudioConfig build() {
            return new AudioConfig(this);
        }
    }
}
