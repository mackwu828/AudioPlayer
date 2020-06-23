//package com.zeasn.audioplayer_media
//
//import android.content.Context
//import android.media.AudioFocusRequest
//import android.media.AudioManager
//import android.media.MediaPlayer
//import android.os.Build
//import android.os.Handler
//import android.os.Looper
//import android.os.PowerManager
//import com.mackwu.audioplayer.bean.AudioPlayState
//import com.mackwu.audioplayer.util.logD
//
//
///**
// * ===================================================
// * Created by MackWu on 2019/12/27 18:03
// *
// * <a href="mailto:wumengjiao828@163.com">Contact me</a>
// * <a href="https://github.com/mackwu828">Follow me</a>
// * ===================================================
// */
//class CMediaPlayer(private val context: Context) : BasePlayer(context),
//        MediaPlayer.OnBufferingUpdateListener,
//        MediaPlayer.OnCompletionListener,
//        MediaPlayer.OnPreparedListener,
//        MediaPlayer.OnErrorListener,
//        AudioManager.OnAudioFocusChangeListener,
//        MediaPlayer.OnSeekCompleteListener {
//
//    private var mediaPlayer: MediaPlayer? = null
//    private var state = AudioPlayState.IDLE
//    private val handler = Handler(Looper.getMainLooper())
//
//    override val sourceChild: String = "audio.mp3"
//
//    override val currentPosition: Int
//        get() = mediaPlayer?.currentPosition ?: 0
//
//    override val duration: Int
//        get() = mediaPlayer?.duration ?: 0
//
//    override fun prepare() {
//        logD(TAG, "prepare... state: $state")
//        mediaPlayer?.prepareAsync()
//        state = AudioPlayState.PREPARING
//        onPreparing?.invoke()
//    }
//
//    override fun start() {
//        logD(TAG, "start... state: $state")
//        try {
//            // check source
//            if (sourceQueue.isEmpty()) {
//                logD(TAG, "sourceQueue is empty, please set or add media source!")
//                return
//            }
//
//            // init
//            if (null == mediaPlayer) {
//                logD(TAG, "init player...")
//                mediaPlayer = MediaPlayer()
//                state = AudioPlayState.IDLE
//                mediaPlayer?.setWakeMode(context.applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
//                mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
//                mediaPlayer?.setOnPreparedListener(this)
//                mediaPlayer?.setOnCompletionListener(this)
//                mediaPlayer?.setOnErrorListener(this)
//                mediaPlayer?.setOnSeekCompleteListener(this)
//                mediaPlayer?.setOnBufferingUpdateListener(this)
//            }
//
//            // state
//            when (state) {
//                // 只有处于 IDLE 状态，才可以调用 setDataSource() ，否则会出现 IllegalStateException
//                AudioPlayState.IDLE -> {
//                    mediaPlayer?.setDataSource(sourceQueue[0].absolutePath)
//                    state = AudioPlayState.INITIALIZED
//                    prepare()
//                }
//                AudioPlayState.INITIALIZED -> {
//                }
//                AudioPlayState.PREPARING -> {
//                }
//                AudioPlayState.PREPARED -> {
//                }
//                AudioPlayState.PLAYING -> {
//                }
//                AudioPlayState.PAUSED -> {
//                }
//                // 如果处于 STOPPED 状态，重新 prepareAsync()
//                AudioPlayState.STOPPED -> {
//                    mediaPlayer?.prepareAsync()
//                    state = AudioPlayState.PREPARING
//                    onPreparing?.invoke()
//                }
//                AudioPlayState.RELEASED -> {
//                }
//                AudioPlayState.COMPLETED -> {
//                    mediaPlayer?.start()
//                    state = AudioPlayState.PLAYING
//                    onPlaying?.invoke()
//                    startSendingProgress()
//                }
//                // 如果处于 ERROR 状态，调用 reset() 重置状态后，再调用 setDataSource()
//                AudioPlayState.ERROR -> {
//                    mediaPlayer?.reset()
//                    state = AudioPlayState.IDLE
//                    mediaPlayer?.setDataSource(sourceQueue[0].absolutePath)
//                    state = AudioPlayState.INITIALIZED
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            state = AudioPlayState.ERROR
//            onError?.invoke("start error: " + e.message)
//        }
//    }
//
//    override fun pause() {
//        logD(TAG, "pause... state: $state")
//        if (state == AudioPlayState.PLAYING) {
//            mediaPlayer?.pause()
//            state = AudioPlayState.PAUSED
//            onPaused?.invoke()
//        }
//    }
//
//    override fun resume() {
//        logD(TAG, "resume... state: $state")
//        if (state == AudioPlayState.PAUSED) {
//            mediaPlayer?.start()
//            state = AudioPlayState.PLAYING
//            onPlaying?.invoke()
//            startSendingProgress()
//        }
//    }
//
//    override fun stop() {
//        logD(TAG, "stop... state: $state")
//        if (state == AudioPlayState.PREPARED || state == AudioPlayState.PLAYING || state == AudioPlayState.PAUSED) {
//            mediaPlayer?.stop()
//            state = AudioPlayState.STOPPED
//            onStopped?.invoke(null)
//        }
//    }
//
//    override fun release() {
//        logD(TAG, "release... state: $state")
//        if (state == AudioPlayState.IDLE || state == AudioPlayState.STOPPED || state == AudioPlayState.COMPLETED) {
//            mediaPlayer?.release()
//            mediaPlayer = null
//            state = AudioPlayState.RELEASED
//            onReleased?.invoke()
//        }
//    }
//
//    override fun seekTo(duration: Int) {
//        logD(TAG, "seekTo... state: $state")
//        if (state == AudioPlayState.PREPARED || state == AudioPlayState.PLAYING || state == AudioPlayState.PAUSED) {
//            mediaPlayer?.seekTo(duration)
//            state = AudioPlayState.PREPARING
//            onSeekTo?.invoke(currentPosition, duration)
//        }
//    }
//
//    override fun onPrepared(mp: MediaPlayer) {
//        logD(TAG, "onPrepared...")
//        try {
//            requestAudioFocus()
//
//            state = AudioPlayState.PREPARED
//            onPrepared?.invoke()
//
//            mp.start()
//            state = AudioPlayState.PLAYING
//            onPlaying?.invoke()
//            startSendingProgress()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            state = AudioPlayState.ERROR
//            onError?.invoke("prepared error: " + e.message)
//        }
//    }
//
//    /**
//     * 播放网络media的时候回调
//     */
//    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
//        logD(TAG, "onBufferingUpdate... percent: $percent")
//    }
//
//    override fun onCompletion(mp: MediaPlayer) {
//        logD(TAG, "onCompletion...")
//        state = AudioPlayState.COMPLETED
//        onCompleted?.invoke()
//    }
//
//    override fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean {
//        logD(TAG, "onError... what:$what, extra: $extra")
//        state = AudioPlayState.ERROR
//        onError?.invoke("$what $extra")
//        return false
//    }
//
//    override fun onAudioFocusChange(focusChange: Int) {
//        logD(TAG, "onAudioFocusChange...  focusChange: $focusChange")
//        when (focusChange) {
//            // 获取 audio focus
//            AudioManager.AUDIOFOCUS_GAIN -> {
//                resume()
//                mediaPlayer?.setVolume(1.0f, 1.0f)
//            }
//            // 失去 audio focus 很长一段时间，必须停止所有的 audio 播放，清理资源
//            AudioManager.AUDIOFOCUS_LOSS -> {
//                stop()
//                release()
//            }
//            // 暂时失去 audio focus，但是很快就会重新获得，在此状态应该暂停所有音频播放，但是不能清除资源
//            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> pause()
//            // 暂时失去 audio focus，但是允许持续播放音频(以很小的声音)，不需要完全停止播放。
//            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> mediaPlayer?.setVolume(0.1f, 0.1f)
//        }
//    }
//
//    override fun onSeekComplete(mp: MediaPlayer) {
//        logD(TAG, "onSeekComplete...")
//        mp.start()
//        state = AudioPlayState.PLAYING
//        onPlaying?.invoke()
//        startSendingProgress()
//    }
//
//    /**
//     * 播放进度
//     */
//    private fun startSendingProgress() {
//        if (state == AudioPlayState.PLAYING) {
//            onProgress?.invoke(currentPosition, duration)
//            handler.postDelayed({ startSendingProgress() }, 1000)
//            return
//        }
//        handler.removeCallbacksAndMessages(0)
//    }
//
//    /**
//     * 处理音频焦点。处理多个程序会来竞争音频输出设备
//     */
//    private fun requestAudioFocus() {
//        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
//                    .setOnAudioFocusChangeListener(this)
//                    .build()
//            audioFocusRequest.acceptsDelayedFocusGain()
//            audioManager.requestAudioFocus(audioFocusRequest)
//        } else {
//            audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
//        }
//    }
//
//    companion object {
//        private val TAG = CMediaPlayer::class.java.simpleName
//    }
//}