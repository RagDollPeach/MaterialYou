package com.geekbrains.materialyou.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.geekbrains.materialyou.R
import com.geekbrains.materialyou.databinding.FragmentSolarSystemBinding
import com.geekbrains.materialyou.view.navigation.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class SolarSystemFragment : Fragment() {

    private var _binding: FragmentSolarSystemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSolarSystemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitleTabLayout()
    }

    private fun setTitleTabLayout() {
        binding.viewPager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(binding.tableLayout, binding.viewPager,
            object : TabLayoutMediator.TabConfigurationStrategy {
                override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                    tab.text = when (position) {
                        0 -> resources.getString(R.string.today)
                        1 -> resources.getString(R.string.yesterday)
                        2 -> resources.getString(R.string.before_yesterday)
                        else -> ""
                    }
                }
            }).attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}