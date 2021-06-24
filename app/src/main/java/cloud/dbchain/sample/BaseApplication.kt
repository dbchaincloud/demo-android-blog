package cloud.dbchain.sample

import android.app.Application
import android.content.Context
import cloud.dbchain.sample.ui.EmptyCallback
import cloud.dbchain.sample.ui.ErrorCallback
import cloud.dbchain.sample.ui.LoadingCallback
import com.gcigb.dbchain.DBChain
import dingshaoshuai.baseext.PageStatus

/**
 * @author: Xiao Bo
 * @date: 10/6/2021
 */
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        val appCode = "BNQKVMVFGG"
        val baseUrl = "https://www.dbchain.cloud/relay/"
        val chainId = "testnet"
        val debug = BuildConfig.DEBUG
        DBChain.init(this, appCode, baseUrl, chainId, debug)

        PageStatus.init(
            LoadingCallback(),
            EmptyCallback(),
            ErrorCallback(),
            LoadingCallback::class.java
        )
    }

    companion object {
        lateinit var context: Context
    }
}