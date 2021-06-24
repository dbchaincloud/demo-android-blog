package cloud.dbchain.sample.ui.bloglist

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cloud.dbchain.sample.bean.BlogBundle
import cloud.dbchain.sample.data.BlogRepository
import dingshaoshuai.baseext.mvvm.BasePageViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author: Xiao Bo
 * @date: 15/6/2021
 */
class BloglistViewModel : BasePageViewModel() {

    val bloglistLiveData = MutableLiveData<List<BlogBundle>>()
    val refreshing = ObservableBoolean()

    fun onRefresh() {
        viewModelScope.launch(Dispatchers.Main) {
            refreshing.set(true)
            val blogs = getBlogs()
            if (blogs.isEmpty()) {
                showEmptyPage()
            } else {
                bloglistLiveData.value = blogs
            }
            refreshing.set(false)
        }
    }

    fun initData() {
        launchOnPageSwitch(
            { getBlogs() },
            { it?.isEmpty() ?: false },
            { it == null },
            { bloglistLiveData.value = it }
        )
    }

    private suspend fun getBlogs() = BlogRepository.getBlogs()

}