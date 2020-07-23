package com.mtjsoft.www.kotlinmvputils.base

import android.view.View
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.mtjsoft.www.kotlinmvputils.R
import kotlinx.android.synthetic.main.base_viewpager_tablayout_top.*


/**
 * BaseTabLayoutViewPagerTopActivity
 * 适用于界面顶部分类导航
 * @author mtj
 */

abstract class BaseTabLayoutViewPagerTopActivity
    : BaseTopViewActivity() {


    private var choosePosition = 0

    override fun initaddView(): View = inflateView(R.layout.base_viewpager_tablayout_top)

    override fun initBaseView() {
        initView()
        initViewInfo()
    }

    private fun initView() {
        vp_pager.adapter = object :
            FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            //此方法用来显示tab上的名字
            override fun getPageTitle(position: Int): CharSequence {
                return setTabLayoutTitle()[position % setTabLayoutTitle().size]
            }

            override fun getItem(position: Int): Fragment {
                return setFragments()[position]
            }

            override fun getCount(): Int {
                return setTabLayoutTitle().size
            }
        }
        vp_pager.offscreenPageLimit = setFragments().size
        tl_tab.setupWithViewPager(vp_pager)
        if (choosePosition >= 0 && choosePosition < (vp_pager.adapter as FragmentPagerAdapter).count) {
            vp_pager.currentItem = choosePosition
        }
    }

    /**
     * 设置vp_pager的选择页
     */
    fun setChoosePosition(position: Int) {
        choosePosition = position
    }

    protected abstract fun initViewInfo()

    protected abstract fun setTabLayoutTitle(): List<String>

    protected abstract fun setFragments(): List<Fragment>

    fun getTabLayout(): TabLayout {
        return tl_tab
    }

    fun getViewPager(): ViewPager {
        return vp_pager
    }

    fun getFragments(): List<Fragment> {
        return setFragments()
    }

    fun getTopTitles(): List<String> {
        return setTabLayoutTitle()
    }

    fun getRLayoutView(): RelativeLayout {
        return rl_layout
    }
}
