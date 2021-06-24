package cloud.dbchain.sample.ui.mnemonic.login

import dingshaoshuai.base.mvvm.BaseViewModel
import dingshaoshuai.base.mvvm.CallLiveData
import dingshaoshuai.base.mvvm.ObservableString
import cloud.dbchain.sample.cache.DBChainKeyCache
import cloud.dbchain.sample.cache.UserInfoCache

/**
 * @author: Xiao Bo
 * @date: 11/6/2021
 */
class LoginViewModel : BaseViewModel() {

    val user = UserInfoCache.getValue()
    val avatarCid = ObservableString(user?.photo)
    val nikeName = ObservableString(user?.name)
    val mnemonic = ObservableString(DBChainKeyCache.getValue()?.mnemonic)

    val startMainCallLiveData = CallLiveData()
    val exitCallLiveData = CallLiveData()

    fun startMain() {
        startMainCallLiveData.call()
    }

    fun exit() {
        DBChainKeyCache.clear()
        UserInfoCache.clear()
        exitCallLiveData.call()
    }
}