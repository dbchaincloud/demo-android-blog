package cloud.dbchain.sample.ui.mnemonic

import android.text.TextUtils
import androidx.lifecycle.viewModelScope
import cloud.dbchain.sample.R
import dingshaoshuai.base.mvvm.BaseViewModel
import dingshaoshuai.base.mvvm.CallLiveData
import dingshaoshuai.base.mvvm.ObservableString
import cloud.dbchain.sample.cache.DBChainKeyCache
import cloud.dbchain.sample.cache.UserInfoCache
import cloud.dbchain.sample.data.TokenRepository
import cloud.dbchain.sample.data.UserRepository
import cloud.dbchain.sample.data.table.User
import dingshaoshuai.function.toast
import com.gcigb.dbchain.DBChain
import com.gcigb.dbchain.MnemonicClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: Xiao Bo
 * @date: 11/6/2021
 */
class MnemonicViewModel : BaseViewModel() {

    val mnemonic = ObservableString()

    val nikeName = ObservableString()

    val startMainCallLiveData = CallLiveData()

    // TODO 测试方便，暂时添加，后续删除
    init {
//        mnemonic.set("all all all all all all all all all all all all")
    }

    fun enterClick() {
        // 检查助记词是否合法
        val word = mnemonic.get()
        val checkMnemonic = MnemonicClient.checkMnemonic(word)
        if (!checkMnemonic) {
            toast(R.string.memonic_invalid)
            return
        }
        val dbChainKey = MnemonicClient.importMnemonic(MnemonicClient.mnemonic2list(word))
        // 设置 DBChainKey
        DBChain.withDBChainKey(dbChainKey)
        viewModelScope.launch(Dispatchers.Main) {
            loadingDialog(true)
            var user: User?
            val code = withContext(Dispatchers.IO) {
                // 查询是否已经存在昵称
                user = UserRepository.query(dbchain_key = dbChainKey.address, findLast = true)
                    ?.lastOrNull()
                if (user == null) {
                    val name = nikeName.get()
                    if (TextUtils.isEmpty(name)) {
                        return@withContext R.string.input_nikename
                    }
                    // 先请求积分（没有积分是不能写入数据的，首先得确保有积分）
                    val checkTokenAvailable = TokenRepository.checkToken(dbChainKey)
                    if (!checkTokenAvailable) {
                        return@withContext R.string.network_error
                    }
                    // 插入数据
                    user = User(name, dbchain_key = dbChainKey.address, sex = User.USER_SEX_MAN)
                    val insert =
                        UserRepository.insert(user!!)
                    if (insert) {
                        // 跳转主页
                        return@withContext 0
                    } else {
                        // 失败
                        return@withContext R.string.network_error
                    }
                } else {
                    // 跳转主页
                    return@withContext 0
                }
            }
            if (code == 0) {
                user?.let {
                    // 名称同步到 UI
                    nikeName.set(it.name)
                    UserInfoCache.setValue(it)
                }
                // 缓存在本地（此为 Demo ，实际项目中千万不能将助记词明文保存在本地）
                DBChainKeyCache.setValue(dbChainKey)
                startMainCallLiveData.call()
            } else {
                toast(code)
            }
            loadingDialog(false)
        }
    }

    fun generateMnemonic() {
        val dbChainKey = MnemonicClient.generateMnemonic()
        mnemonic.set(dbChainKey.mnemonic)
        nikeName.set(null)
    }
}