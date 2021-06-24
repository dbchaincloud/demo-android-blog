package cloud.dbchain.sample.ui.main

import dingshaoshuai.base.mvvm.BaseViewModel
import dingshaoshuai.base.mvvm.ObservableString
import cloud.dbchain.sample.cache.UserInfoCache

/**
 * @author: Xiao Bo
 * @date: 10/6/2021
 */
class MainViewModel : BaseViewModel() {

    val avatarCid = ObservableString(UserInfoCache.getValue()?.photo)
}