package cloud.dbchain.sample.data

import com.gcigb.dbchain.DbChainKey
import com.gcigb.dbchain.checkTokenAvailable
import com.gcigb.dbchain.getToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author: Xiao Bo
 * @date: 11/6/2021
 */
object TokenRepository {

    suspend fun checkToken(dbChainKey: DbChainKey): Boolean {
        return withContext(Dispatchers.IO) {
            checkTokenAvailable(
                privateKeyBytes = dbChainKey.privateKeyBytes,
                publicKeyBytes33 = dbChainKey.publicKeyBytes33,
                address = dbChainKey.address
            )
        }
    }

    suspend fun getTokenNumber(address: String): Int {
        return withContext(Dispatchers.IO) {
            getToken(address)
        }
    }
}