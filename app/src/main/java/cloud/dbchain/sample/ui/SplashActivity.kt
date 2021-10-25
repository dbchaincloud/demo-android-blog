package cloud.dbchain.sample.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cloud.dbchain.sample.cache.DBChainKeyCache
import cloud.dbchain.sample.ui.mnemonic.MnemonicActivity
import cloud.dbchain.sample.ui.mnemonic.login.LoginActivity
import com.gcigb.dbchain.withDBChainKey

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dbChainKey = DBChainKeyCache.getValue()
        if (dbChainKey == null) {
            MnemonicActivity.start(this)
        } else {
            withDBChainKey(dbChainKey)
            LoginActivity.start(this)
        }
        finish()
    }
}