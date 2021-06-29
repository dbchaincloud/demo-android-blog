package cloud.dbchain.sample.ktx

import cloud.dbchain.sample.R
import dingshaoshuai.function.toast

/**
 * @author: Xiao Bo
 * @date: 11/6/2021
 */

fun toastSuccess() {
    toast(R.string.common_success)
}

fun toastFailure() {
    toast(R.string.common_failure)
}