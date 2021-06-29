package cloud.dbchain.sample.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import cloud.dbchain.sample.R
import cloud.dbchain.sample.api.IPFS_URL
import dingshaoshuai.base.feature.image.ImageLoaderProxy

/**
 * @author: Xiao Bo
 * @date: 15/6/2021
 */

@BindingAdapter("binding:cid", "binding:placeholder")
fun setImage(imageView: ImageView, cid: String, placeholder: Int) {
    ImageLoaderProxy.instance.load(imageView, IPFS_URL + cid, placeholder)
}

@BindingAdapter("binding:isMan")
fun setSex(imageView: ImageView, isMan: Boolean) {
    ImageLoaderProxy.instance.load(
        imageView,
        if (isMan) R.drawable.ic_sex_man else R.drawable.ic_sex_woman
    )
}