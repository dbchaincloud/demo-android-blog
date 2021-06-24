package cloud.dbchain.sample.bean

import cloud.dbchain.sample.data.table.DiscussBean
import cloud.dbchain.sample.data.table.User

/**
 * @author: Xiao Bo
 * @date: 15/6/2021
 */
class DiscussBundle(
    val discussBean: DiscussBean,
    val author: User,
    val replied: String? = null,
    var repliedList: MutableList<DiscussBundle>? = null
)