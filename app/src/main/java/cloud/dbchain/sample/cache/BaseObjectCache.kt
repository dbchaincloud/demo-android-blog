package cloud.dbchain.sample.cache

import android.text.TextUtils
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.gcigb.dbchain.ktx.toJsonSort
import dingshaoshuai.base.feature.cache.CacheProxy

/**
 * @author: Xiao Bo
 * @date: 15/6/2021
 */
abstract class BaseObjectCache<T> : BaseCache<T>() {

    abstract fun json2T(valueJson: String): T
    private val liveData by lazy { MutableLiveData<T>() }

    override fun setValue(value: T) {
        CacheProxy.instance.save(key, value!!.toJsonSort())
        _value = value
        liveData.value = _value
    }

    override fun getValue(): T? {
        if (_value != null) {
            return _value
        }
        val valueJson = CacheProxy.instance.get(key, "")
        _value = if (TextUtils.isEmpty(valueJson)) {
            null
        } else {
            json2T(valueJson)
        }
        return _value
    }

    override fun clear() {
        CacheProxy.instance.delete(key)
        _value = null
    }

    fun observer(owner: LifecycleOwner, observer: Observer<T>) {
        liveData.observe(owner, observer)
    }
}