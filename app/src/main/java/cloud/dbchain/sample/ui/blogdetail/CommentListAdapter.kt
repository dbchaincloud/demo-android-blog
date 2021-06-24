package cloud.dbchain.sample.ui.blogdetail

import androidx.recyclerview.widget.LinearLayoutManager
import cloud.dbchain.sample.R
import cloud.dbchain.sample.bean.DiscussBundle
import cloud.dbchain.sample.databinding.ItemConmentBinding
import cloud.dbchain.sample.databinding.ItemConmentReplyBinding
import cloud.dbchain.sample.views.LinearSpacesItemDecoration
import com.qmuiteam.qmui.kotlin.dip
import dingshaoshuai.base.mvvm.BaseDatabindingAdapter

/**
 * @author: Xiao Bo
 * @date: 10/6/2021
 */
class CommentListAdapter : BaseDatabindingAdapter<DiscussBundle, ItemConmentBinding>() {

    var itemChildClickListener: ItemChildClickListener<DiscussBundle>? = null

    override val layoutId: Int
        get() = R.layout.item_conment

    override fun onBind(binding: ItemConmentBinding, data: DiscussBundle) {
        binding.discussBundle = data
        binding.childListener = itemChildClickListener
        binding.rvReply.let {
            it.layoutManager = LinearLayoutManager(it.context)
            val space = it.context.dip(22)
            if (binding.rvReply.itemDecorationCount == 0) {
                it.addItemDecoration(LinearSpacesItemDecoration(space = space, firstTop = space))
            }
            val replyListAdapter = ReplyListAdapter(itemChildClickListener).apply {
                dataList = data.repliedList
            }
            it.adapter = replyListAdapter
        }
    }

}

class ReplyListAdapter(private val itemChildClickListener: ItemChildClickListener<DiscussBundle>?) :
    BaseDatabindingAdapter<DiscussBundle, ItemConmentReplyBinding>() {

    override val layoutId: Int
        get() = R.layout.item_conment_reply

    override fun onBind(binding: ItemConmentReplyBinding, data: DiscussBundle) {
        binding.discussBundle = data
        binding.childListener = itemChildClickListener
    }

}