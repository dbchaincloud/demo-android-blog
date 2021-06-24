package cloud.dbchain.sample.ui.bloglist

import android.graphics.Color
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cloud.dbchain.sample.R
import cloud.dbchain.sample.bean.BlogBundle
import cloud.dbchain.sample.databinding.FragmentBloglistBinding
import cloud.dbchain.sample.ui.PageMvvmFragment
import cloud.dbchain.sample.ui.blogdetail.BlogDetailActivity
import cloud.dbchain.sample.views.LinearSpacesItemDecoration
import com.qmuiteam.qmui.kotlin.dip
import dingshaoshuai.base.mvvm.BaseDatabindingAdapter

/**
 * @author: Xiao Bo
 * @date: 10/6/2021
 */
class BloglistFragment : PageMvvmFragment<FragmentBloglistBinding, BloglistViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_bloglist

    override val placeholderView: View
        get() = binding.swipeRefreshLayout

    private val adapter = BloglistAdapter()

    override fun initView() {
        binding.swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#2E44FF"))
        binding.rvBlog.let {
            it.layoutManager = LinearLayoutManager(activity)
            it.addItemDecoration(
                LinearSpacesItemDecoration(
                    left = dip(16),
                    right = dip(16),
                    space = dip(12),
                    lastBottom = dip(150)
                )
            )
            it.adapter = this.adapter
        }
    }

    override fun initViewModel(): BloglistViewModel {
        return ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(BloglistViewModel::class.java)
    }

    override fun bindViewModel(viewModel: BloglistViewModel) {
        binding.viewModel = viewModel
    }

    override fun initClickListener() {
        super.initClickListener()
        adapter.itemClickListener = object : BaseDatabindingAdapter.ItemClickListener<BlogBundle> {
            override fun onItemClick(data: BlogBundle) {
                mActivity?.let { activity ->
                    BlogDetailActivity.start(activity, data)
                }
            }
        }
    }

    override fun initObserver() {
        super.initObserver()
        viewModel.bloglistLiveData.observe(this, Observer {
            adapter.dataList = it
        })
    }

    override fun initData() {
        super.initData()
        viewModel.initData()
    }

    companion object {
        fun newInstance(): BloglistFragment {
            return BloglistFragment()
        }
    }


}