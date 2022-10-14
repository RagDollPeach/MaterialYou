package com.geekbrains.materialyou.view.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.geekbrains.materialyou.R
import com.geekbrains.materialyou.databinding.FragmentViewPageBinding
import com.geekbrains.materialyou.view.fragments.EarthFragment
import com.geekbrains.materialyou.view.fragments.MarsFragment
import com.geekbrains.materialyou.view.fragments.PictureOfTheDayFragment
import com.geekbrains.materialyou.view.fragments.SolarSystemFragment


class ViewPageFragment : Fragment() {

    private var _binding: FragmentViewPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBottomNavigationMenu()
    }

    private fun initBottomNavigationMenu() {
        binding.naviView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.mars_menu -> {
                    switchFragment(MarsFragment(),R.id.container2); true
                }
                R.id.earth_menu -> {
                    switchFragment(EarthFragment(),R.id.container2); true
                }
                R.id.system_menu -> {
                    switchFragment(SolarSystemFragment(),R.id.container2); true
                }
                R.id.back_menu -> {
                    switchFragment(PictureOfTheDayFragment.newInstance(),R.id.container); true
                }
                else -> true
            }
        }
        binding.naviView.selectedItemId = R.id.system_menu
    }

    private fun switchFragment(fragment: Fragment, container: Int) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(container, fragment)
            .addToBackStack("")
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}