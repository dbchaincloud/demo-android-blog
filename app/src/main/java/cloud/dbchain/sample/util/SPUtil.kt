package cloud.dbchain.sample.util

import android.content.Context
import android.content.SharedPreferences
import cloud.dbchain.sample.BaseApplication

/**
 * @author: Xiao Bo
 * @date: 15/6/2021
 */
object SPUtil {

    private var sharedPreferences: SharedPreferences =
        BaseApplication.context.getSharedPreferences("blog_data", Context.MODE_PRIVATE)

    fun setValue(key: String, value: Any) {
        val edit = sharedPreferences.edit()
        when (value) {
            is String -> edit.putString(key, value)
            is Boolean -> edit.putBoolean(key, value)
            is Int -> edit.putInt(key, value)
            else -> edit.putString(key, value.toString())
        }
        edit.apply()
    }

    fun <T> getValue(key: String, default: T): T {
        val value = when (default) {
            is String -> sharedPreferences.getString(key, default)
            is Boolean -> sharedPreferences.getBoolean(key, default)
            is Int -> sharedPreferences.getInt(key, default)
            else -> sharedPreferences.getString(key, default.toString())
        }
        return value as T
    }

    fun deleteKey(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
}