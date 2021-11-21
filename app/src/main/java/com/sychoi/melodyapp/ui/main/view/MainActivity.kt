package com.sychoi.melodyapp.ui.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.sychoi.melodyapp.R
import com.sychoi.melodyapp.databinding.ActivityMainBinding
import com.sychoi.melodyapp.ui.main.adapter.MainViewpagerAdapter
import com.sychoi.melodyapp.ui.main.fragment.ListFragment
import com.sychoi.melodyapp.ui.main.fragment.AnalysisFragment
import com.sychoi.melodyapp.ui.main.fragment.RecordFragment
import com.sychoi.melodyapp.ui.main.fragment.SettingFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val listFragment by lazy { ListFragment() }
    private val analysisFragment by lazy { AnalysisFragment() }
    private val recordFragment by lazy { RecordFragment() }
    private val settingFragment by lazy { SettingFragment() }
    private val fragments: List<Fragment> = listOf(listFragment, analysisFragment, recordFragment, settingFragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewPager()
        initNavigationBar()
    }

    private val pagerAdapter: MainViewpagerAdapter by lazy {
        MainViewpagerAdapter(this, fragments)
    }

    private fun initNavigationBar() {
        binding.navBottom.run {
            setOnItemSelectedListener {
                val page = when(it.itemId) {
                    R.id.nav_list -> 0
                    R.id.nav_analysis -> 1
                    R.id.nav_record -> 2
                    R.id.nav_setting -> 3
                    else -> 0
                }

                if (page != binding.vpMain.currentItem) {
                    binding.vpMain.currentItem = page
                }

                true
            }
            selectedItemId = R.id.nav_list
        }
    }

    private fun initViewPager() {
        binding.vpMain.run {
            adapter = pagerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    val navigation = when(position) {
                        0 -> R.id.nav_list
                        1 -> R.id.nav_analysis
                        2 -> R.id.nav_record
                        3 -> R.id.nav_setting
                        else -> R.id.nav_list
                    }
                    if (binding.navBottom.selectedItemId != navigation) {
                        binding.navBottom.selectedItemId = navigation
                    }
                }
            })
        }
    }
}