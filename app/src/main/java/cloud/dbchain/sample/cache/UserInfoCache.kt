package cloud.dbchain.sample.cache

import cloud.dbchain.sample.data.table.User
import com.gcigb.dbchain.util.json2Any

/**
 * @author: Xiao Bo
 * @date: 15/6/2021
 */
object UserInfoCache : BaseObjectCache<User>() {
    override val key: String
        get() = "key_userinfo"

    override fun json2T(valueJson: String): User {
        return valueJson.json2Any(User::class.java)
    }
}