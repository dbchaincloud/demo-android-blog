package cloud.dbchain.sample.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @author: Xiao Bo
 * @date: 7/12/2020
 */
class AdapterFragmentPager(fragmentActivity: FragmentActivity, private val typeList: List<Fragment>) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return typeList[position]
    }

    override fun getItemCount(): Int {
        return typeList.size
    }

}