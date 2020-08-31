package com.mackwu.audioplayer;

/**
 * ===================================================
 * Created by MackWu on 2020/8/26 15:44
 * <a href="mailto:wumengjiao828@163.com">Contact me</a>
 * <a href="https://github.com/mackwu828">Follow me</a>
 * ===================================================
 */
public enum PlayState {

    /**
     * 初始状态
     */
    IDLE,

    /**
     * 播放中
     */
    PLAYING,

    /**
     * 准备中
     */
    PREPARING,

    /**
     * 准备完成
     */
    PREPARED,

    /**
     * 暂停
     */
    PAUSED,

    /**
     * 停止
     */
    STOPPED,

    /**
     * 完成
     */
    COMPLETED,

    /**
     * 释放
     */
    RELEASED,

    /**
     * 错误
     */
    ERROR,
}
