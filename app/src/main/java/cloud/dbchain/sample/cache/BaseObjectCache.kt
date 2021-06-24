package cloud.dbchain.sample.cache

import android.text.TextUtils
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import cloud.dbchain.sample.util.SPUtil
import com.gcigb.dbchain.ktx.toJsonSort

/**
 * @author: Xiao Bo
 * @date: 15/6/2021
 */
abstract class BaseObjectCache<T> : BaseCache<T>() {

    abstract fun json2T(valueJson: String): T
    private val liveData by lazy { MutableLiveData<T>() }

    override fun setValue(value: T) {
        SPUtil.setValue(key, value!!.toJsonSort())
        _value = value
        liveData.value = _value
    }

    override fun getValue(): T? {
        if (_value != null) {
            return _value
        }
        val valueJson = SPUtil.getValue(key, "")
        _value = if (TextUtils.isEmpty(valueJson)) {
            null
        } else {
            json2T(valueJson)
        }
        return _value
    }

    override fun clear() {
        SPUtil.deleteKey(key)
        _value = null
    }

    fun observer(owner: LifecycleOwner, observer: Observer<T>) {
        liveData.observe(owner, observer)
    }
}