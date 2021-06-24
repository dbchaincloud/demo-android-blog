package cloud.dbchain.sample.data

import android.text.TextUtils
import cloud.dbchain.sample.bean.DiscussBundle
import cloud.dbchain.sample.data.table.Discuss
import cloud.dbchain.sample.data.table.DiscussBean
import cloud.dbchain.sample.data.table.User
import cloud.dbchain.sample.data.table.UserBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author: Xiao Bo
 * @date: 15/6/2021
 */
object DiscussRepository {

    suspend fun query(
        blog_id: String? = null,
        discuss_id: String? = null,
        text: String? = null
    ): List<DiscussBean>? {
        return withContext(Dispatchers.IO) {
            Discuss.query(blog_id, discuss_id, text)
        }
    }

    suspend fun queryDiscussBundle(blog_id: String? = null): List<DiscussBundle>? {
        // 查询用户表（为了方便通过地址获取用户名称）
        val userMap = hashMapOf<String, UserBean>().apply {
            UserRepository.query()?.forEach {
                put(it.dbchain_key, it)
            }
        }
        // 查询出所有关于这条博客的评论（包括回复）
        val discussList = query(blog_id = blog_id)
        // 遍历所有评论
        val discussMap = hashMapOf<String, DiscussBean>().apply {
            discussList?.forEach {
                put(it.id, it)
            }
        }
        // 遍历出评论文章与回复评论的
        // 评论文章的
        val commentList = mutableListOf<DiscussBundle>()
        // 回复集合
        val repliedMap = hashMapOf<String, MutableList<DiscussBundle>>()
        discussList?.forEach {
            // 直接评论文章的
            if (TextUtils.isEmpty(it.discuss_id)) {
                commentList.add(DiscussBundle(it, userMap[it.created_by] ?: User()))
            } else {
                // 回复评论的
                // 被回复的者的地址
                val replied = discussMap[it.discuss_id]?.created_by
                // 组装这条回复的发布者和被回复者
                val discussBundle =
                    DiscussBundle(it, userMap[it.created_by] ?: User(), userMap[replied]?.name)
                val list = repliedMap[it.discuss_id]
                if (list == null) {
                    repliedMap[it.discuss_id] = mutableListOf(discussBundle)
                } else {
                    list.add(discussBundle)
                    repliedMap[it.discuss_id] = list
                }
            }
        }
        commentList.forEach {
            getByRecursion(repliedMap, it)
        }
        return commentList
    }

    // 递归遍历出这条评论的子评论
    private fun getByRecursion(
        map: Map<String, List<DiscussBundle>>,
        discussBundle: DiscussBundle
    ) {
        map[discussBundle.discussBean.id]?.forEach {
            val repliedList = discussBundle.repliedList
            if (repliedList == null) {
                discussBundle.repliedList = mutableListOf()
            }
            discussBundle.repliedList?.add(it)
            getByRecursion(map, it)
        }
    }

    fun parse2Layer(origin: List<DiscussBundle>): List<DiscussBundle> {
        origin.forEach { discussBundle ->
            val list = mutableListOf<DiscussBundle>()
            discussBundle.repliedList?.forEach { childDiscussBundle ->
                parse(childDiscussBundle, list)
            }
            discussBundle.repliedList?.addAll(list)
        }
        return origin
    }

    private fun parse(
        discussBundle: DiscussBundle,
        result: MutableList<DiscussBundle>
    ): List<DiscussBundle> {
        discussBundle.repliedList?.forEach {
            result.add(it)
            parse(it, result)
        }
        return result
    }

    suspend fun insert(discuss: Discuss): Boolean {
        return withContext(Dispatchers.IO) {
            Discuss.insert(discuss)
        }
    }
}