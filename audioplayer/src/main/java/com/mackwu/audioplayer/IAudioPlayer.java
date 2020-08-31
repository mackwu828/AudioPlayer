package com.mackwu.audioplayer;

/**
 * ===================================================
 * Created by MackWu on 2020/8/26 15:41
 * <a href="mailto:wumengjiao828@163.com">Contact me</a>
 * <a href="https://github.com/mackwu828">Follow me</a>
 * ===================================================
 */
public interface IAudioPlayer {

    /**
     * 开始播放
     */
    void start();

    /**
     * 暂停播放
     */
    void pause();

    /**
     * 停止播放
     */
    void stop();

    /**
     * 释放资源
     */
    void release();

}
