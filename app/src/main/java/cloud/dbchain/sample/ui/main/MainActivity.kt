package cloud.dbchain.sample.ui.main

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import cloud.dbchain.sample.BaseApplication
import cloud.dbchain.sample.R
import cloud.dbchain.sample.adapter.AdapterFragmentPager
import dingshaoshuai.base.mvvm.BaseMvvmActivity
import cloud.dbchain.sample.cache.UserInfoCache
import cloud.dbchain.sample.databinding.ActivityMainBinding
import cloud.dbchain.sample.ui.bloglist.BloglistFragment
import cloud.dbchain.sample.ui.edit.EditActivity
import cloud.dbchain.sample.ui.user.main.UserMainActivity
import com.google.android.material.tabs.TabLayout

class MainActivity : BaseMvvmActivity<ActivityMainBinding, MainViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_main

    private val tableNameArray =
        BaseApplication.context.resources.getStringArray(R.array.table_list)
    private val fragmentList = mutableListOf<Fragment>().apply {
        add(BloglistFragment.newInstance())
        // 仅供示例，最新与评价最多，排序逻辑就不写了
        add(BloglistFragment.newInstance())
        add(BloglistFragment.newInstance())
    }

    override fun initContentView() {
        super.initContentView()
        initTableLayout()
        initViewPager()
    }

    override fun initViewModel(): MainViewModel {
        return ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(MainViewModel::class.java)
    }

    override fun bindViewModel(viewModel: MainViewModel) {
        binding.viewModel = viewModel
        binding.listener = ClickListener()
    }

    override fun initObserver() {
        super.initObserver()
        UserInfoCache.observer(this, Observer {
            viewModel.avatarCid.set(it.photo)
        })
    }

    private fun initTableLayout() {
        tableNameArray.forEach {
            val view = View.inflate(this, R.layout.table, null).apply {
                if (this is AppCompatTextView) {
                    text = it
                }
            }
            val tab = binding.tableLayout.newTab().setCustomView(view)
            binding.tableLayout.addTab(tab)
        }
        binding.tableLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                selected(true, tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                selected(false, tab)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                selected(true, tab)
                binding.viewPager.currentItem = tab?.position ?: 0
            }

            private fun selected(selected: Boolean, tab: TabLayout.Tab?) {
                tab?.let {
                    val customView = it.customView
                    if (customView is AppCompatTextView) {
                        if (selected) {
                            customView.textSize = 24F
                            customView.paint.isFakeBoldText = true
                        } else {
                            customView.textSize = 20F
                            customView.paint.isFakeBoldText = false
                        }
                    }
                }
            }
        })
        binding.tableLayout.getTabAt(0)?.select()
    }

    private fun initViewPager() {
        binding.viewPager.adapter = AdapterFragmentPager(this, fragmentList)
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tableLayout.getTabAt(position)?.select()
            }
        })
    }

    inner class ClickListener {
        fun writeClick() {
            EditActivity.start(this@MainActivity)
        }

        fun avatarClick() {
            UserMainActivity.start(this@MainActivity)
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}