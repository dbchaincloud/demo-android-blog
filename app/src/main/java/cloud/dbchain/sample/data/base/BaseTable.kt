package cloud.dbchain.sample.data.base

import com.gcigb.dbchain.*
import com.gcigb.dbchain.check.checkIdValid
import com.gcigb.dbchain.util.toMap


/**
 * @author: Xiao Bo
 * @date: 10/6/2021
 */
abstract class BaseTable<T, R : BaseTableBean> {

    abstract val tableName: String

    protected fun createQueriedArray() = QueriedArray(table = tableName)

    // 插入一条数据
    internal suspend fun insert(data: T): Boolean {
        data ?: return false
        return insertRow(tableName, data.toMap())
    }

    // 冻结一条数据
    internal suspend fun freeze(id: String): Boolean {
        if (!checkIdValid(id)) return false
        return freezeRow(tableName, id)
    }

    internal suspend fun update(id: String, newData: T): Boolean {
        if (!checkIdValid(id)) return false
        newData ?: return false
        val messageList = newMessageList()
        val owner = dbChainKey.address
        messageList.add(createFreezeMessage(tableName, id, owner))
        messageList.add(createInsertMessage(tableName, newData.toMap(), owner))
        return handleBatchMessage(messageList)
    }

    // 查询所有数据
    internal suspend fun query(queriedArray: QueriedArray): List<R>? {
        val content = querier(queriedArray).content ?: return null
        return content2list(content)
    }

    // 根据 id 查询一条数据
    internal suspend fun queryById(id: String): R? {
        if (!checkIdValid(id)) return null
        val queriedArray = createQueriedArray().findById(id).findLast()
        return query(queriedArray)?.lastOrNull()
    }

    // 查询该创建者创建的所有数据
    internal suspend fun queryByCreated(created_by: String): List<R>? {
        val queriedArray = createQueriedArray().findCreatedBy(created_by)
        return query(queriedArray)
    }

    internal abstract fun content2list(content: String): List<R>?
}