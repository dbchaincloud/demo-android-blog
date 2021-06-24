package cloud.dbchain.sample.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import cloud.dbchain.sample.R
import cloud.dbchain.sample.api.IPFS_URL
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * @author: Xiao Bo
 * @date: 15/6/2021
 */

@BindingAdapter("binding:cid", "binding:placeholder")
fun setImage(imageView: ImageView, cid: String, placeholder: Int) {
    val requestOptions = RequestOptions().error(placeholder).placeholder(placeholder)
    Glide.with(imageView).load(IPFS_URL + cid).apply(requestOptions).into(imageView)
}

@BindingAdapter("binding:isMan")
fun setSex(imageView: ImageView, isMan: Boolean) {
    Glide.with(imageView)
        .load(if (isMan) R.drawable.ic_sex_man else R.drawable.ic_sex_woman)
        .into(imageView)
}