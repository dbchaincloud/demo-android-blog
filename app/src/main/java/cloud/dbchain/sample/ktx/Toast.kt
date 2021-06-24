package cloud.dbchain.sample.ktx

import android.widget.Toast
import cloud.dbchain.sample.BaseApplication
import cloud.dbchain.sample.R

/**
 * @author: Xiao Bo
 * @date: 11/6/2021
 */

private var toast: Toast? = null

fun toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    toast?.cancel()
    toast = Toast.makeText(BaseApplication.context, msg, duration)
    toast?.show()
}

fun toast(strResId: Int, duration: Int = Toast.LENGTH_SHORT) {
    toast(BaseApplication.context.getString(strResId))
}

fun toastSuccess() {
    toast(R.string.common_success)
}

fun toastFailure() {
    toast(R.string.common_failure)
}