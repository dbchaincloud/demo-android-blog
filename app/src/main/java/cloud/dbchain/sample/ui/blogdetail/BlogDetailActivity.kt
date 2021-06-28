package cloud.dbchain.sample.ui.blogdetail

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.ViewOutlineProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cloud.dbchain.sample.R
import cloud.dbchain.sample.bean.BlogBundle
import cloud.dbchain.sample.databinding.ActivityBlogDetail2Binding
import cloud.dbchain.sample.util.KeyboardUtil
import cloud.dbchain.sample.views.LinearSpacesItemDecoration
import com.google.android.material.appbar.AppBarLayout
import com.qmuiteam.qmui.kotlin.dip
import dingshaoshuai.base.mvvm.BaseMvvmActivity

class BlogDetailActivity : BaseMvvmActivity<ActivityBlogDetail2Binding, BlogDetailViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_blog_detail2

    override val isFitsSystemWindows: Boolean
        get() = false

    private val adapter = CommentListAdapter()

    override fun initContentView() {
        super.initContentView()
        binding.swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#2E44FF"))
        binding.rvComment.let {
            it.layoutManager = LinearLayoutManager(this)
            it.addItemDecoration(
                LinearSpacesItemDecoration(
                    dip(16),
                    dip(16),
                    dip(12),
                    dip(20),
                    dip(150)
                )
            )
            it.adapter = this.adapter
        }
    }

    override fun initData() {
        super.initData()
        intent.getSerializableExtra(EXTRA_BLOG_BOUND)?.let {
            if (it is BlogBundle) {
                viewModel.blog.set(it.blog)
            }
        }
        viewModel.onRefresh()
    }

    override fun initViewModel(): BlogDetailViewModel {
        return ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(BlogDetailViewModel::class.java)
    }

    override fun bindViewModel(viewModel: BlogDetailViewModel) {
        binding.viewModel = viewModel
    }

    override fun initObserver() {
        super.initObserver()
        viewModel.discussBundleList.observe(this, Observer {
            adapter.dataList = it
        })
        viewModel.showKeyboardCallLiveData.observe(this, Observer {
            binding.etComment.requestFocus()
            KeyboardUtil.open(binding.etComment)
        })
        viewModel.hideboardCallLiveData.observe(this, Observer {
            KeyboardUtil.hide(binding.etComment)
        })
    }

    override fun initClickListener() {
        super.initClickListener()
        adapter.itemChildClickListener = viewModel.itemChildClickListener
    }

    companion object {
        const val EXTRA_BLOG_BOUND = "blogdetailactivity_extra_blog_bound"

        fun start(context: Context, blogBundle: BlogBundle) {
            val intent = Intent(context, BlogDetailActivity::class.java).apply {
                putExtra(EXTRA_BLOG_BOUND, blogBundle)
            }
            context.startActivity(intent)
        }
    }
}