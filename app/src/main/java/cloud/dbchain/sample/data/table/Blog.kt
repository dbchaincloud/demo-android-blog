package cloud.dbchain.sample.data.table

import cloud.dbchain.sample.data.base.BaseTable
import cloud.dbchain.sample.data.base.BaseTableBean
import com.gcigb.dbchain.util.toBaseQueryResult
import java.io.Serializable

class BlogBean(
    override var id: String = "",
    override var created_at: String = "",
    override var created_by: String = ""
) : Blog(), BaseTableBean

open class Blog(
    var title: String = "",
    var body: String = ""
) : Serializable {

    companion object : BaseTable<Blog, BlogBean>() {
        override fun content2list(content: String): List<BlogBean>? {
            val result = content.toBaseQueryResult(BlogBean::class.java)
            return result.result
        }

        override val tableName: String
            get() = "blogs"


        internal suspend fun query(
            title: String? = null,
            body: String? = null,
            created_by: String? = null,
            findLast: Boolean = false
        ): List<BlogBean>? {
            val queriedArray = createQueriedArray().order("id")
            title?.let { queriedArray.findEqual("title", it) }
            body?.let { queriedArray.findEqual("body", it) }
            created_by?.let { queriedArray.findEqual("created_by", it) }
            if (findLast) {
                queriedArray.findLast()
            }
            return query(queriedArray)
        }
    }

}

