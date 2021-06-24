package cloud.dbchain.sample.ui.edit

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cloud.dbchain.sample.R
import cloud.dbchain.sample.databinding.ActivityEditBinding
import cloud.dbchain.sample.ui.blogdetail.BlogDetailActivity
import dingshaoshuai.base.mvvm.BaseMvvmActivity

class EditActivity : BaseMvvmActivity<ActivityEditBinding, EditViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_edit

    override fun initViewModel(): EditViewModel {
        return ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(EditViewModel::class.java)
    }

    override fun bindViewModel(viewModel: EditViewModel) {
        binding.viewModel = viewModel
    }

    override fun initObserver() {
        super.initObserver()
        viewModel.startBlogDetailLiveData.observe(this, Observer {
            BlogDetailActivity.start(this, it)
            finish()
        })
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, EditActivity::class.java))
        }
    }

}