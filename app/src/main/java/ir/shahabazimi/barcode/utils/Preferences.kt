package ir.shahabazimi.barcode.utils

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class Preferences private constructor(ctx: Context) {
    private val masterKey =
        MasterKey.Builder(ctx).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
    private val sp = EncryptedSharedPreferences.create(
        ctx,
        "rodinia_preference",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )


    companion object {
        @Volatile
        private var instance: Preferences? = null
        operator fun invoke(ctx: Context): Preferences =
            instance ?: synchronized(this) {
                instance ?: Preferences(ctx).also { instance = it }
            }
    }

    fun clear() = sp.edit { clear() }

    fun getCameraPermission() = sp.getBoolean("cameraPermission", false)
    fun setCameraPermission() = sp.edit { putBoolean("cameraPermission", true) }

}
