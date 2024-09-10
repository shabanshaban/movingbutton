package com.example.movingbutton.utils

import android.content.Context
import android.os.Vibrator

/**
 * Created by TheFinestArtist on 2/12/15.
 */
object VibrateUtil {
    private val mSingletonLock = Any()
    private var vibrator: Vibrator? = null

    private fun getInstance(context: Context?): Vibrator? {
        synchronized(mSingletonLock) {
            if (vibrator != null) return vibrator
            if (context != null) vibrator =
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            return vibrator
        }
    }

    @JvmStatic
    fun vibtate(context: Context?, vibrationDuration: Int) {
        if (vibrationDuration > 0) getInstance(context)!!
            .vibrate(vibrationDuration.toLong())
    }
}
