package cloud.dbchain.sample.data

import cloud.dbchain.sample.data.table.User
import cloud.dbchain.sample.data.table.UserBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author: Xiao Bo
 * @date: 11/6/2021
 */
object UserRepository {

    suspend fun query(
        name: String? = null,
        age: String? = null,
        dbchain_key: String? = null,
        sex: String? = null,
        photo: String? = null,
        motto: String? = null,
        findLast: Boolean = false
    ): List<UserBean>? {
        return withContext(Dispatchers.IO) {
            User.query(name, age, dbchain_key, sex, photo, motto, findLast)
        }
    }

    suspend fun insert(user: User): Boolean {
        return withContext(Dispatchers.IO) {
            User.insert(user)
        }
    }

    suspend fun update(id: String, user: User): Boolean {
        return withContext(Dispatchers.IO) {
            User.update(id, user)
        }
    }
}