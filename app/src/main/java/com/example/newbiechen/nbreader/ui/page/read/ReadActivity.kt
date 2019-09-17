package com.example.newbiechen.nbreader.ui.page.read

import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import com.example.newbiechen.nbreader.R
import com.example.newbiechen.nbreader.databinding.ActivityReadBinding
import com.example.newbiechen.nbreader.ui.component.book.BookManager
import com.example.newbiechen.nbreader.ui.component.extension.closeDrawer
import com.example.newbiechen.nbreader.ui.component.extension.isDrawerOpen
import com.example.newbiechen.nbreader.ui.component.extension.openDrawer
import com.example.newbiechen.nbreader.ui.component.widget.page.ReadMenuAction
import com.example.newbiechen.nbreader.uilts.SystemBarUtil
import com.youtubedl.ui.main.base.BaseBindingActivity
import javax.inject.Inject

/**
 *  author : newbiechen
 *  date : 2019-08-25 16:28
 *  description :书籍阅读页面
 */

class ReadActivity : BaseBindingActivity<ActivityReadBinding>(), View.OnClickListener {

    companion object {
        private const val TAG = "ReadActivity"
    }

    @Inject
    lateinit var mBookManager: BookManager

    private lateinit var mViewModel: ReadViewModel

    override fun initContentView(): Int = R.layout.activity_read

    override fun initView() {
        super.initView()
        mDataBinding.apply {
            // 初始化 toolbar
            supportActionBar(toolbar)
            // 设置颜色半透明
            overStatusBar(toolbar)
            // 隐藏系统状态栏
            hideSystemBar()
            // 初始化侧滑栏
            dlSlide.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

            // 允许手势关闭侧滑栏
            dlSlide.addDrawerListener(object : DrawerLayout.DrawerListener {
                override fun onDrawerStateChanged(newState: Int) {
                }

                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                }

                override fun onDrawerClosed(drawerView: View) {
                    dlSlide.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }

                override fun onDrawerOpened(drawerView: View) {
                    dlSlide.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }
            })

            // 初始化点击事件
            tvCategory.setOnClickListener(this@ReadActivity)
            tvNightMode.setOnClickListener(this@ReadActivity)
            tvSetting.setOnClickListener(this@ReadActivity)
            tvBright.setOnClickListener(this@ReadActivity)
            menuFrame.setOnClickListener(this@ReadActivity)

            // 添加页面事件回调
            pvBook.addPageActionListener {
                onPageAction(it)
            }

            // 初始化 BookManager
            mBookManager.initPageController(pvBook.getPageController())

            // 加载本地书籍
        }
    }

    private fun showSystemBar() {
        //显示
        SystemBarUtil.showUnStableStatusBar(this)
        // SystemBarUtil.showUnStableNavBar(this)
    }

    private fun hideSystemBar() {
        //隐藏
        SystemBarUtil.hideStableStatusBar(this)
        // SystemBarUtil.hideStableNavBar(this)
    }

    override fun processLogic() {
        super.processLogic()
        mViewModel = ViewModelProviders.of(this).get(ReadViewModel::class.java)
        // 需要对 viewmodel 做一次初始化操作
        mViewModel.init(this)
        mDataBinding.viewModel = mViewModel
    }

    private fun openBook() {
        // 根据 intent 获取 book 信息
        // 将 book 信息转换成 Book 对象
        // 判断该 Book 对象对应的文件是否存在
        // 等待 BookCursor Service 开启，并调用 BookManager 的 openBook()
    }

    override fun onBackPressed() {
        when {
            mDataBinding.dlSlide.isDrawerOpen() -> mDataBinding.dlSlide.closeDrawer()
            mViewModel.isShowMenu.get()!! -> mViewModel.isShowMenu.set(false)
            else -> super.onBackPressed()
        }
    }

    private fun toggleMenu() {
        mViewModel.apply {
            if (isShowMenu.get()!!) {
                hideSystemBar()
            } else {
                showSystemBar()
            }

            isShowMenu.set(!isShowMenu.get()!!)
        }
    }

    private fun toggleNightMode() {
        mViewModel.apply {
            isNightMode.set(!isNightMode.get()!!)
        }
    }

    override fun onClick(v: View?) {
        mViewModel.apply {
            when (v!!.id) {
                R.id.tv_night_mode -> {
                    toggleNightMode()
                }
                R.id.tv_category -> {
                    // 关闭菜单
                    isShowMenu.set(false)
                    // 打开抽屉
                    mDataBinding.dlSlide.openDrawer()
                }
                R.id.tv_setting -> {
                    // 创建 dialog
                    isShowMenu.set(false)
                    isShowSettingMenu.set(true)
                }
                R.id.tv_bright -> {
                    isShowMenu.set(false)
                    isShowBrightMenu.set(true)
                }
                R.id.menu_frame -> {
                    when {
                        isShowSettingMenu.get()!! -> isShowSettingMenu.set(false)
                        isShowBrightMenu.get()!! -> isShowBrightMenu.set(false)
                        else -> toggleMenu()
                    }
                }
            }
        }
    }

    private fun onPageAction(action: Any) {
        when (action) {
            is ReadMenuAction -> {
                toggleMenu()
            }
        }
    }
}