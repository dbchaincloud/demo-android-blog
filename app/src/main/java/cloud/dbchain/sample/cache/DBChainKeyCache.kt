package cloud.dbchain.sample.cache

import com.gcigb.dbchain.DbChainKey
import com.gcigb.dbchain.util.json2Any

/**
 * @author: Xiao Bo
 * @date: 15/6/2021
 */
object DBChainKeyCache : BaseObjectCache<DbChainKey>() {
    override val key: String
        get() = "key_dbchainkey"

    override fun json2T(valueJson: String): DbChainKey {
        return valueJson.json2Any(DbChainKey::class.java)
    }
}