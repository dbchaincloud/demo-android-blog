package cloud.dbchain.sample.data.table

import cloud.dbchain.sample.R
import cloud.dbchain.sample.data.base.BaseTable
import cloud.dbchain.sample.data.base.BaseTableBean
import com.gcigb.dbchain.util.toBaseQueryResult
import java.io.Serializable

class UserBean(
    override var id: String = "",
    override var created_at: String = "",
    override var created_by: String = ""
) : User(), BaseTableBean

open class User(
    var name: String = "",
    var age: String = "",
    var dbchain_key: String = "",
    var sex: String = "",
    var photo: String = "",
    var motto: String = ""
) : Serializable {

    companion object : BaseTable<User, UserBean>() {
        const val USER_SEX_MAN = "0"
        const val USER_SEX_WOMAN = "1"
        const val DEFAULT_AVATAR = R.drawable.ic_avatar

        override fun content2list(content: String): List<UserBean>? {
            val result = content.toBaseQueryResult(UserBean::class.java)
            return result.result
        }

        override val tableName: String
            get() = "user"

        internal suspend fun query(
            name: String? = null,
            age: String? = null,
            dbchain_key: String? = null,
            sex: String? = null,
            photo: String? = null,
            motto: String? = null,
            findLast: Boolean = false
        ): List<UserBean>? {
            val queriedArray = createQueriedArray()
            name?.let { queriedArray.findEqual("name", it) }
            age?.let { queriedArray.findEqual("age", it) }
            dbchain_key?.let { queriedArray.findEqual("dbchain_key", it) }
            sex?.let { queriedArray.findEqual("sex", it) }
            photo?.let { queriedArray.findEqual("photo", it) }
            motto?.let { queriedArray.findEqual("motto", it) }
            if (findLast) {
                queriedArray.findLast()
            }
            return query(queriedArray)
        }

    }

}

