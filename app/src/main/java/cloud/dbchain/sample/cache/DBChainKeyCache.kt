package cloud.dbchain.sample.cache

import com.gcigb.dbchain.DbChainKey
import dingshaoshuai.base.feature.json.JsonParseProxy

/**
 * @author: Xiao Bo
 * @date: 15/6/2021
 */
object DBChainKeyCache : BaseObjectCache<DbChainKey>() {
    override val key: String
        get() = "key_dbchainkey"

    override fun json2T(valueJson: String): DbChainKey {
        return JsonParseProxy.instance.toObject(valueJson, DbChainKey::class.java)
    }
}