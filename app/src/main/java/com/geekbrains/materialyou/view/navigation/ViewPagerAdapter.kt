package com.geekbrains.materialyou.view.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.geekbrains.materialyou.view.fragments.ViewFragment


class ViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    @RequiresApi(Build.VERSION_CODES.O)
    private val fragments = mutableListOf(ViewFragment()
        ,ViewFragment(),ViewFragment())

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getItemCount(): Int {
        return fragments.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}