package cloud.dbchain.sample.ui.mnemonic

import android.text.TextUtils
import androidx.lifecycle.viewModelScope
import cloud.dbchain.sample.BaseApplication
import cloud.dbchain.sample.R
import dingshaoshuai.base.mvvm.BaseViewModel
import dingshaoshuai.base.mvvm.CallLiveData
import dingshaoshuai.base.mvvm.ObservableString
import cloud.dbchain.sample.cache.DBChainKeyCache
import cloud.dbchain.sample.cache.UserInfoCache
import cloud.dbchain.sample.data.TokenRepository
import cloud.dbchain.sample.data.UserRepository
import cloud.dbchain.sample.data.table.User
import com.gcigb.dbchain.mnemonic2list
import com.gcigb.dbchain.util.Wallet.importMnemonic
import com.gcigb.dbchain.withDBChainKey
import dingshaoshuai.function.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.ArrayList

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
        val mnemonicList = mnemonic2list(word)
        val checkMnemonic = checkMnemonic(mnemonicList)
        if (!checkMnemonic) {
            toast(R.string.memonic_invalid)
            return
        }
        val dbChainKey = importMnemonic(mnemonicList)
        // 设置 DBChainKey
        withDBChainKey(dbChainKey)
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
        val dbChainKey = com.gcigb.dbchain.generateMnemonic()
        mnemonic.set(dbChainKey.mnemonic)
        nikeName.set(null)
    }

    fun checkMnemonic(mnemonic: List<String>): Boolean {
        if (mnemonic.size != 12) return false
        val inputStream = BaseApplication.context.assets.open("bip39-wordlist.txt")
        val br = BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8))
        val wordList = ArrayList<String>(2048)
        var word: String?
        while (br.readLine().also { word = it } != null) {
            wordList.add(word!!)
        }
        br.close()
        mnemonic.forEach {
            val contains = wordList.contains(it)
            if (!contains) {
                return false
            }
        }
        return true
    }
}