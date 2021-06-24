package cloud.dbchain.sample.ui

import androidx.databinding.ViewDataBinding
import com.kingja.loadsir.callback.Callback
import dingshaoshuai.baseext.mvvm.BasePageMvvmFragment
import dingshaoshuai.baseext.mvvm.BasePageViewModel

/**
 * @author: Xiao Bo
 * @date: 18/6/2021
 */
abstract class PageMvvmFragment<T : ViewDataBinding, E : BasePageViewModel> :
    BasePageMvvmFragment<T, E>() {

    override val emptyPageCallbackClazz: Class<out Callback>
        get() = EmptyCallback::class.java
    override val errorPageCallbackClazz: Class<out Callback>
        get() = ErrorCallback::class.java
    override val loadingPageCallbackClazz: Class<out Callback>
        get() = LoadingCallback::class.java
}