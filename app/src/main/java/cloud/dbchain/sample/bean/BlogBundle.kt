package cloud.dbchain.sample.bean

import cloud.dbchain.sample.data.table.BlogBean
import cloud.dbchain.sample.data.table.User
import java.io.Serializable

/**
 * @author: Xiao Bo
 * @date: 15/6/2021
 */
data class BlogBundle(
    val blog: BlogBean,
    val author: User,
    val commentCount: Int = 0
) : Serializable