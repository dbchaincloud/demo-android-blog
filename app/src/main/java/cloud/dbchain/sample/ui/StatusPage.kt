package cloud.dbchain.sample.ui

import android.content.Context
import android.view.View
import cloud.dbchain.sample.R
import com.kingja.loadsir.callback.Callback

/**
 * @author: Xiao Bo
 * @date: 18/6/2021
 */
class LoadingCallback : Callback() {
    override fun onCreateView(): Int = R.layout.loading_callback

    override fun onReloadEvent(context: Context?, view: View?) = true

}

class EmptyCallback : Callback() {
    override fun onCreateView(): Int = R.layout.empty_callback

    override fun onReloadEvent(context: Context?, view: View?) = false

}

class ErrorCallback : Callback() {
    override fun onCreateView(): Int = R.layout.error_callback

    override fun onReloadEvent(context: Context?, view: View?) = false

}