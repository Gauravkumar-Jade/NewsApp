package com.gaurav.newsapp.ui.activity

import android.view.View
import android.view.View.OnClickListener
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.gaurav.newsapp.adapter.ScreenSlideAdapter
import com.gaurav.newsapp.common.BaseActivity
import com.gaurav.newsapp.databinding.ActivityMainBinding
import com.gaurav.newsapp.ui.fragment.HomeFragment
import com.gaurav.newsapp.utils.AppHelper
import com.gaurav.newsapp.utils.launchActivity
import com.gaurav.newsapp.viewModel.NewsViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate), OnClickListener{


   lateinit var viewModel: NewsViewModel

   private var vpAdapter:ScreenSlideAdapter? =null

    override fun init() {
        super.init()
    }

    override fun setVariables() {
        super.setVariables()
        viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
        vpAdapter = ScreenSlideAdapter(this)
        binding.listener = this
    }

    override fun setUpViews() {
        super.setUpViews()
        setViewPager()
        setTabLayout()

    }

    private fun setViewPager() {
        binding.viewPager.adapter = vpAdapter
    }

    private fun setTabLayout() {
        val tabs = AppHelper.tab_list

        TabLayoutMediator(binding.tabLayout, binding.viewPager){tab, position->
            tab.text = tabs[position]
        }.attach()

    }

    override fun observeData() {
        super.observeData()

        binding.viewPager.registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val category = AppHelper.tab_list[position]
                viewModel.getNews(category)
                HomeFragment.category = category
            }
        })
    }

    override fun onClick(v: View?) {
        when(v){
            binding.btnSaved -> {
                launchActivity<SavedNewsActivity>()
            }
        }
    }
}