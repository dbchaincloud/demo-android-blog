package cloud.dbchain.sample.ui.user.main

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import cloud.dbchain.sample.bean.BlogBundle
import cloud.dbchain.sample.cache.UserInfoCache
import cloud.dbchain.sample.data.BlogRepository
import cloud.dbchain.sample.data.table.User
import com.gcigb.dbchain.dbChainKey
import dingshaoshuai.baseext.mvvm.BasePageViewModel

/**
 * @author: Xiao Bo
 * @date: 11/6/2021
 */
class UserMainViewModel : BasePageViewModel() {

    var user = ObservableField<User>(UserInfoCache.getValue())

    val bloglistLiveData = MutableLiveData<List<BlogBundle>>()

    fun getBlogList() {
        val block = suspend { BlogRepository.getBlogs(created_by = dbChainKey.address) }
        launchOnPageSwitch(
            block,
            { it?.isEmpty() ?: false },
            { it == null },
            { bloglistLiveData.value = it })
    }
}