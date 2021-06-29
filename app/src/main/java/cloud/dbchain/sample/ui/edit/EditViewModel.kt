package cloud.dbchain.sample.ui.edit

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cloud.dbchain.sample.R
import cloud.dbchain.sample.bean.BlogBundle
import cloud.dbchain.sample.data.BlogRepository
import cloud.dbchain.sample.data.table.Blog
import dingshaoshuai.function.toast
import dingshaoshuai.base.mvvm.BaseViewModel
import dingshaoshuai.base.mvvm.ObservableString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author: Xiao Bo
 * @date: 11/6/2021
 */
class EditViewModel : BaseViewModel() {

    val title = ObservableString()
    val content = ObservableString()
    val startBlogDetailLiveData = MutableLiveData<BlogBundle>()

    fun publish() {
        val title = title.get()
        if (TextUtils.isEmpty(title)) {
            toast(R.string.input_title)
            return
        }
        val content = content.get()
        if (TextUtils.isEmpty(content)) {
            toast(R.string.input_content)
            return
        }
        viewModelScope.launch(Dispatchers.Main) {
            loadingDialog(true)
            val insert = BlogRepository.insert(Blog(title = title, body = content))
            if (insert) {
                val blogBundle = BlogRepository.getBlog(title = title, body = content)
                // 跳转文章详情
                startBlogDetailLiveData.value = blogBundle
            } else {
                toast(R.string.publish_success)
            }
            loadingDialog(false)
        }
    }
}