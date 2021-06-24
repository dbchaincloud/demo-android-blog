package cloud.dbchain.sample.ui.mnemonic

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cloud.dbchain.sample.R
import dingshaoshuai.base.mvvm.BaseMvvmActivity
import cloud.dbchain.sample.databinding.ActivityMnemonicBinding
import cloud.dbchain.sample.ui.main.MainActivity


class MnemonicActivity : BaseMvvmActivity<ActivityMnemonicBinding, MnemonicViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_mnemonic

    override fun initViewModel(): MnemonicViewModel {
        return ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(MnemonicViewModel::class.java)
    }

    override fun bindViewModel(viewModel: MnemonicViewModel) {
        binding.viewModel = viewModel
    }

    override fun initObserver() {
        super.initObserver()
        viewModel.startMainCallLiveData.observe(this, Observer {
            MainActivity.start(this)
            finish()
        })
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MnemonicActivity::class.java))
        }
    }
}