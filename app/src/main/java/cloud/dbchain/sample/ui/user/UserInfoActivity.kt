package cloud.dbchain.sample.ui.user

import android.Manifest
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cloud.dbchain.sample.R
import dingshaoshuai.base.mvvm.BaseMvvmActivity
import cloud.dbchain.sample.databinding.ActivityUserInfoBinding
import cloud.dbchain.sample.util.IntentUtil
import com.gcigb.network.util.logI
import java.io.File

class UserInfoActivity : BaseMvvmActivity<ActivityUserInfoBinding, UserInfoViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_user_info

    override fun initContentView() {
        super.initContentView()
        binding.ivWoman.isSelected = true
    }

    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    override fun initCustom() {
        super.initCustom()
        initLauncher()
    }

    private fun initLauncher() {
        val pictureLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                it?.data?.let { intent ->
                    IntentUtil.getFilePathFromUri(this, intent)?.firstOrNull()?.let { path ->
                        viewModel.upload(File(path))
                    }
                }
            }
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    val intent = Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    pictureLauncher.launch(intent)
                }
            }
    }

    override fun initViewModel(): UserInfoViewModel {
        return ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(UserInfoViewModel::class.java)
    }

    override fun bindViewModel(viewModel: UserInfoViewModel) {
        binding.viewModel = viewModel
    }

    override fun initObserver() {
        super.initObserver()
        viewModel.isMan.observe(this, Observer {
            binding.ivMan.isSelected = it
            binding.ivWoman.isSelected = !it
        })
        viewModel.camearCallLiveData.observe(this, Observer {
            permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        })
    }


    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, UserInfoActivity::class.java))
        }
    }
}