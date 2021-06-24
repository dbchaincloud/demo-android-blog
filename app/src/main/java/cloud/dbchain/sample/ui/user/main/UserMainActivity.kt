package cloud.dbchain.sample.ui.user.main

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewOutlineProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cloud.dbchain.sample.R
import cloud.dbchain.sample.bean.BlogBundle
import cloud.dbchain.sample.binding.setSex
import cloud.dbchain.sample.cache.UserInfoCache
import cloud.dbchain.sample.data.table.User
import cloud.dbchain.sample.databinding.ActivityUserMainBinding
import cloud.dbchain.sample.ui.PageMvvmActivity
import cloud.dbchain.sample.ui.blogdetail.BlogDetailActivity
import cloud.dbchain.sample.ui.bloglist.BloglistAdapter
import cloud.dbchain.sample.ui.user.UserInfoActivity
import cloud.dbchain.sample.views.LinearSpacesItemDecoration
import com.qmuiteam.qmui.kotlin.dip
import dingshaoshuai.base.mvvm.BaseDatabindingAdapter

class UserMainActivity : PageMvvmActivity<ActivityUserMainBinding, UserMainViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_user_main

    override val placeholderView: View
        get() = binding.rvBlog

    private val adapter = BloglistAdapter()

    override fun initContentView() {
        super.initContentView()
        // 去掉上划到顶部的阴影效果
        binding.appbarLayout.outlineProvider = null
        binding.toolbarLayout.outlineProvider = ViewOutlineProvider.BOUNDS
        binding.rvBlog.let {
            it.layoutManager = LinearLayoutManager(this)
            it.addItemDecoration(
                LinearSpacesItemDecoration(
                    left = dip(16),
                    right = dip(16),
                    space = dip(12),
                    firstTop = dip(20),
                    lastBottom = dip(150)
                )
            )
            it.adapter = this.adapter
        }
    }

    override fun initViewModel(): UserMainViewModel {
        return ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(UserMainViewModel::class.java)
    }

    override fun bindViewModel(viewModel: UserMainViewModel) {
        binding.viewModel = viewModel
        binding.listener = ClickListener()
    }

    override fun initObserver() {
        super.initObserver()
        viewModel.bloglistLiveData.observe(this, Observer {
            adapter.dataList = it
        })
        setSex(binding.ivSex, UserInfoCache.getValue()?.sex == User.USER_SEX_MAN)
        UserInfoCache.observer(this, Observer {
            viewModel.user.set(it)
            setSex(binding.ivSex, it.sex == User.USER_SEX_MAN)
        })
    }

    override fun initClickListener() {
        super.initClickListener()
        adapter.itemClickListener = object : BaseDatabindingAdapter.ItemClickListener<BlogBundle> {
            override fun onItemClick(data: BlogBundle) {
                BlogDetailActivity.start(this@UserMainActivity, data)
            }
        }
    }

    override fun initData() {
        super.initData()
        viewModel.getBlogList()
    }

    inner class ClickListener {

        fun backClick() {
            finish()
        }

        fun editClick() {
            UserInfoActivity.start(this@UserMainActivity)
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, UserMainActivity::class.java))
        }
    }

}