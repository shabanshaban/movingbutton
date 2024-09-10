package com.example.movingbutton.utils

import android.content.Context
import android.media.AudioManager

/**
 * Created by TheFinestArtist on 2/12/15.
 */
object AudioUtil {
    private val mSingletonLock = Any()
    private var audioManager: AudioManager? = null

    private fun getInstance(context: Context?): AudioManager? {
        synchronized(mSingletonLock) {
            if (audioManager != null) return audioManager
            if (context != null) audioManager =
                context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            return audioManager
        }
    }

    fun adjustMusicVolume(context: Context?, up: Boolean, showInterface: Boolean) {
        val direction = if (up) AudioManager.ADJUST_RAISE else AudioManager.ADJUST_LOWER
        val flag =
            AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE or (if (showInterface) AudioManager.FLAG_SHOW_UI else 0)
        getInstance(context)!!
            .adjustStreamVolume(AudioManager.STREAM_MUSIC, direction, flag)
    }

    @JvmStatic
    fun playKeyClickSound(context: Context?, volume: Int) {
        if (volume > 0) getInstance(context)!!
            .playSoundEffect(AudioManager.FX_KEY_CLICK, volume.toFloat() / 100.0f)
    }
}
