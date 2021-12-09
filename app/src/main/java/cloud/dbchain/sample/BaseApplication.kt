package cloud.dbchain.sample

import android.app.Application
import android.content.Context
import cloud.dbchain.sample.ui.EmptyCallback
import cloud.dbchain.sample.ui.ErrorCallback
import cloud.dbchain.sample.ui.LoadingCallback
import cloud.dbchain.sample.util.LogImpl
import com.gcigb.dbchain.init
import dbchain.client.java.sm2.SM2Encrypt
import dingshaoshuai.baseext.PageStatus
import dingshaoshuai.function.Function

/**
 * @author: Xiao Bo
 * @date: 10/6/2021
 */
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        val appCode = "GGPJWXRSC6"
        val baseUrl = "https://controlpanel.dbchain.cloud/relay02/"
        val chainId = "testnet02"
        val debug = BuildConfig.DEBUG
        init(
            appCodeParameter = appCode,
            baseUrlParameter = baseUrl,
            chainIdParameter = chainId,
            dbChainEncryptParameter = SM2Encrypt(),
            iLogParameter = LogImpl(),
            isDebug = debug,
        )

        PageStatus.init(
            LoadingCallback(),
            EmptyCallback(),
            ErrorCallback(),
            LoadingCallback::class.java
        )
        Function.init(this)
    }

    companion object {
        lateinit var context: Context
    }
}