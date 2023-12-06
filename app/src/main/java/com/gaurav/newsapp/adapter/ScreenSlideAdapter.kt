package com.gaurav.newsapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gaurav.newsapp.ui.fragment.HomeFragment
import com.gaurav.newsapp.utils.AppHelper

class ScreenSlideAdapter(fragmentActivity: FragmentActivity):FragmentStateAdapter(fragmentActivity) {


    override fun getItemCount(): Int {
        return AppHelper.tab_list.size
    }

    override fun createFragment(position: Int): Fragment {
       return HomeFragment()
    }
}