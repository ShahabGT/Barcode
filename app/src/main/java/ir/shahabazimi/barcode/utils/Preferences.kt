package ir.shahabazimi.barcode.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit

class Preferences private constructor(ctx: Context) {

    private val sp = ctx.getSharedPreferences("barcode_preference", MODE_PRIVATE)

    companion object {
        @Volatile
        private var instance: Preferences? = null
        operator fun invoke(ctx: Context): Preferences =
            instance ?: synchronized(this) {
                instance ?: Preferences(ctx).also { instance = it }
            }
    }

    fun getCameraPermission() = sp.getBoolean("cameraPermission", false)
    fun setCameraPermission() = sp.edit { putBoolean("cameraPermission", true) }

}
