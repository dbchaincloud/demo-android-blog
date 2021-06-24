package cloud.dbchain.sample.cache

/**
 * @author: Xiao Bo
 * @date: 15/6/2021
 */
abstract class BaseCache<T> {

    protected abstract val key: String
    protected var _value: T? = null

    abstract fun setValue(value: T)

    abstract fun getValue(): T?

    abstract fun clear()
}