package cloud.dbchain.sample.ui.mnemonic.login

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cloud.dbchain.sample.R
import cloud.dbchain.sample.databinding.ActivityLoginBinding
import cloud.dbchain.sample.ui.main.MainActivity
import cloud.dbchain.sample.ui.mnemonic.MnemonicActivity
import dingshaoshuai.base.mvvm.BaseMvvmActivity

class LoginActivity : BaseMvvmActivity<ActivityLoginBinding, LoginViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_login

    override fun initViewModel(): LoginViewModel {
        return ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(LoginViewModel::class.java)
    }

    override fun bindViewModel(viewModel: LoginViewModel) {
        binding.viewModel = viewModel
    }

    override fun initObserver() {
        super.initObserver()
        viewModel.startMainCallLiveData.observe(this, Observer {
            MainActivity.start(this)
            finish()
        })
        viewModel.exitCallLiveData.observe(this, Observer {
            MnemonicActivity.start(this)
            finish()
        })
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }
}