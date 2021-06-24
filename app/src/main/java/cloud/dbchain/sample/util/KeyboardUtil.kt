package cloud.dbchain.sample.util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * @author: Xiao Bo
 * @date: 16/6/2021
 */
object KeyboardUtil {

    fun open(editText: EditText) {
        val systemService = editText.context.getSystemService(Context.INPUT_METHOD_SERVICE)
        if (systemService != null && systemService is InputMethodManager) {
            systemService.showSoftInput(editText, 0)
        }
    }

    fun hide(editText: EditText) {
        val systemService = editText.context.getSystemService(Context.INPUT_METHOD_SERVICE)
        if (systemService != null && systemService is InputMethodManager) {
            systemService.hideSoftInputFromWindow(editText.windowToken, 0)
        }
    }
}