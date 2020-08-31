package com.mackwu.audioapp;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.mackwu.audioplayer.AudioPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private AudioPlayer audioPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        audioPlayer = new AudioPlayer.Builder()
                .dataSource("http://cdn-preview-4.deezer.com/stream/c-4d848758913b16ec737a148d514e3914-2.mp3")
                .build();
    }

    @OnClick(R.id.btn_start)
    public void onBtnStartClicked() {
        audioPlayer.start();
    }

    @OnClick(R.id.btn_pause)
    public void onBtnPauseClicked() {
        audioPlayer.pause();
    }

    @OnClick(R.id.btn_stop)
    public void onBtnStopClicked() {

    }
}
