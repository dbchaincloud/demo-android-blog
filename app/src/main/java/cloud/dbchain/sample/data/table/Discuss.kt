package cloud.dbchain.sample.data.table

import cloud.dbchain.sample.data.base.BaseTable
import cloud.dbchain.sample.data.base.BaseTableBean
import com.gcigb.dbchain.util.toBaseQueryResult

class DiscussBean(
    override var id: String = "",
    override var created_at: String = "",
    override var created_by: String = ""
) : Discuss(), BaseTableBean

open class Discuss(
    var blog_id: String = "",
    var discuss_id: String = "",
    var text: String = ""
) {

    companion object : BaseTable<Discuss, DiscussBean>() {
        override fun content2list(content: String): List<DiscussBean>? {
            val result = content.toBaseQueryResult(DiscussBean::class.java)
            return result.result
        }

        override val tableName: String
            get() = "discuss"

        internal suspend fun query(
            blog_id: String? = null,
            discuss_id: String? = null,
            text: String? = null
        ): List<DiscussBean>? {
            val queriedArray = createQueriedArray()
            blog_id?.let { queriedArray.findEqual("blog_id", it) }
            discuss_id?.let { queriedArray.findEqual("discuss_id", it) }
            text?.let { queriedArray.findEqual("text", it) }
            return query(queriedArray)
        }
    }

}

