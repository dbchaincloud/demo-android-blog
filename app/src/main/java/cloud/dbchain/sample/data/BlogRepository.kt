package cloud.dbchain.sample.data

import android.util.SparseIntArray
import cloud.dbchain.sample.bean.BlogBundle
import cloud.dbchain.sample.data.table.Blog
import cloud.dbchain.sample.data.table.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author: Xiao Bo
 * @date: 15/6/2021
 */
object BlogRepository {

    suspend fun getBlogs(
        title: String? = null,
        body: String? = null,
        created_by: String? = null,
        findLast: Boolean = false
    ): List<BlogBundle> {
        return withContext(Dispatchers.IO) {
            val blogBoundList = mutableListOf<BlogBundle>()
            // 获取用户表数据
            val userMap = hashMapOf<String, User>().apply {
                UserRepository.query()?.forEach {
                    put(it.dbchain_key, it)
                }
            }
            // 获取评论表
            val discussMap = SparseIntArray().apply {
                DiscussRepository.query()?.forEach {
                    val key = it.blog_id.toInt()
                    val value = get(key)
                    put(key, value + 1)
                }
            }
            val blogBeanList = Blog.query(title, body, created_by, findLast)
            blogBeanList?.forEach {
                val author = userMap[it.created_by] ?: User()
                val blogBound = BlogBundle(it, author, discussMap[it.id.toInt()])
                blogBoundList.add(blogBound)
            }
            blogBoundList
        }
    }

    suspend fun getBlog(
        title: String? = null,
        body: String? = null,
        created_by: String? = null
    ): BlogBundle? {
        return withContext(Dispatchers.IO) {
            var blogBundle: BlogBundle? = null
            val blogBeanList = Blog.query(title, body, created_by, true)
            blogBeanList?.forEach {
                val author =
                    UserRepository.query(dbchain_key = it.created_by)?.lastOrNull() ?: User()
                blogBundle = BlogBundle(it, author, 0)
            }
            blogBundle
        }
    }

    suspend fun insert(blog: Blog): Boolean {
        return withContext(Dispatchers.IO) {
            Blog.insert(blog)
        }
    }
}