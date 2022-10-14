package com.geekbrains.materialyou.view.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.geekbrains.materialyou.R
import com.geekbrains.materialyou.databinding.FragmentViewBinding
import com.geekbrains.materialyou.model.PictureOfTheDayData
import com.geekbrains.materialyou.viewmodel.PictureOfTheDayViewModel
import java.time.LocalDate

class ViewFragment : Fragment() {

    companion object {
        val viewModel: PictureOfTheDayViewModel =
            ViewModelProvider.NewInstanceFactory().create(PictureOfTheDayViewModel::class.java)

//        @RequiresApi(Build.VERSION_CODES.O)
//        fun newInstance(num: Int): Fragment {
//            val yesterday = LocalDate.now().minusDays(1)
//            val beforeYesterday = LocalDate.now().minusDays(2)
//
//            when (num) {
//                1 -> { viewModel.pictureOfTheDay() }
//                2 -> { viewModel.pictureOfTheDayWithDate(yesterday) }
//                3 -> { viewModel.pictureOfTheDayWithDate(beforeYesterday) }
//            }
//            return ViewFragment()
//
//        }
    }

    private var _binding: FragmentViewBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getData().observe(viewLifecycleOwner) { renderData(it) }
        _binding = FragmentViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                binding.root.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    binding.imageView.setBackgroundResource(R.drawable.earth)
                } else {
                    binding.imageView.load(url) {
                        lifecycle(this@ViewFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                        crossfade(true)
                    }
                }
            }
            is PictureOfTheDayData.Loading -> {
                binding.root.findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
            }
            else -> {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}