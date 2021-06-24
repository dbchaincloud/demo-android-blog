package cloud.dbchain.sample.binding

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.databinding.BindingAdapter
import cloud.dbchain.sample.R

/**
 * @author: Xiao Bo
 * @date: 16/6/2021
 */
@SuppressLint("SetTextI18n")
@BindingAdapter("binding:author", "binding:reply")
fun setReply(textView: TextView, author: String, reply: String) {
    val replyStr = textView.context.getString(R.string.reply)
    val content = "$author $replyStr $reply"

    val spannableString = SpannableString(content)
    val foregroundColorSpan = ForegroundColorSpan(Color.parseColor("#2E44FF"))
    // 有空格，所以要再 +1
    spannableString.setSpan(
        foregroundColorSpan,
        author.length,
        author.length + replyStr.length + 1,
        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
    )
    textView.text = spannableString
}