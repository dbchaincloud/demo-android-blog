package cloud.dbchain.sample.ui.bloglist

import cloud.dbchain.sample.R
import dingshaoshuai.base.mvvm.BaseDatabindingAdapter
import cloud.dbchain.sample.bean.BlogBundle
import cloud.dbchain.sample.databinding.ItemBlogBinding

/**
 * @author: Xiao Bo
 * @date: 10/6/2021
 */
class BloglistAdapter : BaseDatabindingAdapter<BlogBundle, ItemBlogBinding>() {

    var itemClickListener: ItemClickListener<BlogBundle>? = null

    override val layoutId: Int
        get() = R.layout.item_blog

    override fun onBind(binding: ItemBlogBinding, data: BlogBundle) {
        binding.blogBound = data
        binding.itemClickListener = itemClickListener
    }


}