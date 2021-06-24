package cloud.dbchain.sample.views

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.qmuiteam.qmui.util.QMUIViewHelper
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButtonDrawable

/**
 * @author: Xiao Bo
 * @date: 10/6/2021
 */
class QMUIRoundConstraintLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        val bg = QMUIRoundButtonDrawable.fromAttributeSet(context, attrs, defStyleAttr)
        QMUIViewHelper.setBackgroundKeepingPadding(this, bg)
    }
}