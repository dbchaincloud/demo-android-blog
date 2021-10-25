package cloud.dbchain.sample.ui.user

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cloud.dbchain.sample.R
import dingshaoshuai.base.mvvm.BaseViewModel
import dingshaoshuai.base.mvvm.CallLiveData
import dingshaoshuai.base.mvvm.ObservableString
import cloud.dbchain.sample.cache.UserInfoCache
import cloud.dbchain.sample.data.UploadRepository
import cloud.dbchain.sample.data.UserRepository
import cloud.dbchain.sample.data.table.User
import dingshaoshuai.function.toast
import cloud.dbchain.sample.ktx.toastFailure
import com.gcigb.dbchain.dbChainKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

/**
 * @author: Xiao Bo
 * @date: 11/6/2021
 */
class UserInfoViewModel : BaseViewModel() {

    val user = UserInfoCache.getValue()
    val nikeName = ObservableString(user?.name)
    val age = ObservableString(user?.age)
    var sex = user?.sex ?: ""
    var isMan = MutableLiveData<Boolean>(user?.sex == User.USER_SEX_MAN)
    val motto = ObservableString(user?.motto)
    val camearCallLiveData = CallLiveData()
    val avatarCid = ObservableString(user?.photo)

    fun cameraClick() {
        camearCallLiveData.call()
    }

    fun upload(file: File) {
        viewModelScope.launch(Dispatchers.Main) {
            loadingDialog(true)
            val cid = UploadRepository.upload(file)
            if (TextUtils.isEmpty(cid)) {
                // 失败
                toastFailure()
            } else {
                // 成功
                avatarCid.set(cid)
            }
            loadingDialog(false)
        }
    }

    fun manClick() {
        isMan.value = true
        sex = User.USER_SEX_MAN
    }

    fun womanClick() {
        isMan.value = false
        sex = User.USER_SEX_WOMAN
    }

    fun saveClick() {
        viewModelScope.launch(Dispatchers.Main) {
            loadingDialog(true)
            // 昵称不可为空
            val nikeName = nikeName.get()
            if (TextUtils.isEmpty(nikeName)) {
                loadingDialog(false)
                toast(R.string.input_nikename)
                return@launch
            }
            val age = age.get()
            val motto = motto.get()
            // 查询用户在表中的 id
            val address = dbChainKey.address
            val oldUser = UserRepository.query(dbchain_key = address, findLast = true)?.lastOrNull()
            if (oldUser == null) {
                loadingDialog(false)
                toastFailure()
                return@launch
            }
            val user = User(
                name = nikeName, age = age, dbchain_key = address,
                sex = sex, photo = avatarCid.get(), motto = motto
            )
            val update = UserRepository.update(oldUser.id, user)
            if (update) {
                // 如果修改成功
                UserInfoCache.setValue(user)
                finishPage()
            } else {
                toastFailure()
            }

            loadingDialog(false)
        }
    }
}