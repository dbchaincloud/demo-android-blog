package cloud.dbchain.sample.ui.blogdetail

import android.text.TextUtils
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cloud.dbchain.sample.BaseApplication
import cloud.dbchain.sample.R
import cloud.dbchain.sample.bean.DiscussBundle
import cloud.dbchain.sample.data.DiscussRepository
import cloud.dbchain.sample.data.table.BlogBean
import cloud.dbchain.sample.data.table.Discuss
import dingshaoshuai.function.toast
import cloud.dbchain.sample.ktx.toastFailure
import dingshaoshuai.base.mvvm.BaseDatabindingAdapter
import dingshaoshuai.base.mvvm.BaseViewModel
import dingshaoshuai.base.mvvm.CallLiveData
import dingshaoshuai.base.mvvm.ObservableString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author: Xiao Bo
 * @date: 11/6/2021
 */
class BlogDetailViewModel : BaseViewModel() {

    val blog = ObservableField<BlogBean>()
    val commentHint =
        ObservableString(BaseApplication.context.getString(R.string.hint_comment))
    val commentText = ObservableString()
    val discussBundleList = MutableLiveData<List<DiscussBundle>>()
    val showKeyboardCallLiveData = CallLiveData()
    val hideboardCallLiveData = CallLiveData()
    private var commentDiscussId = ""
    var emptyComment = ObservableBoolean(true)
    val refreshing = ObservableBoolean()

    val itemChildClickListener =
        object : BaseDatabindingAdapter.ItemChildClickListener<DiscussBundle> {
            override fun onItemChildClick(view: View, data: DiscussBundle) {
                commentDiscussId = data.discussBean.id
                commentHint.set("${BaseApplication.context.getString(R.string.reply)}${data.author.name}")
                showKeyboardCallLiveData.call()
            }
        }

    fun commentClick() {
        val comment = commentText.get()
        if (TextUtils.isEmpty(comment)) {
            toast(R.string.input_comment)
            return
        }
        viewModelScope.launch(Dispatchers.Main) {
            loadingDialog(true)
            val blogId = blog.get()?.id ?: "-1"
            val discuss = Discuss(blogId, commentDiscussId, comment)
            val insert = DiscussRepository.insert(discuss)
            loadingDialog(false)
            if (insert) {
                onRefresh()
                // 收起键盘
                hideboardCallLiveData.call()
                // 输入框内容清空
                commentText.set(null)
                // 评论 id 也清空
                commentDiscussId = ""
                // 输入框 hint 返璞归真
                commentHint.set(BaseApplication.context.getString(R.string.hint_comment))
            } else {
                toastFailure()
            }
        }
    }

    fun onRefresh() {
        viewModelScope.launch(Dispatchers.Main) {
            refreshing.set(true)
            val id = blog.get()?.id ?: "-1"
            DiscussRepository.queryDiscussBundle(id)?.let {
                // 我们这里只有两层
                val value = DiscussRepository.parse2Layer(it)
                discussBundleList.value = value
            }
            emptyComment.set(discussBundleList.value?.size ?: 0 < 1)
            refreshing.set(false)
        }
    }

}