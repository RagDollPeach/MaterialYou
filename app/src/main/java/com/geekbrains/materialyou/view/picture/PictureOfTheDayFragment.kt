package com.geekbrains.materialyou.view.picture

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.geekbrains.materialyou.MainActivity
import com.geekbrains.materialyou.R
import com.geekbrains.materialyou.databinding.FragmentPictureOfTheDayBinding
import com.geekbrains.materialyou.model.PictureOfTheDayData
import com.geekbrains.materialyou.util.*
import com.geekbrains.materialyou.view.chips.ChipsFragment
import com.geekbrains.materialyou.viewmodel.PictureOfTheDayViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import java.time.LocalDate

class PictureOfTheDayFragment : Fragment() {

    private var _binding: FragmentPictureOfTheDayBinding? = null
    private val binding get() = _binding!!

    private val marsTheme = 0
    private val defaultTheme = 1
    private val titanTheme = 2

    private val today= 1
    private val yesterday = 2
    private val beforeYesterday = 3

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as MainActivity).setTheme(getAppTheme(R.style.MarsStyle))
        viewModel.getData().observe(viewLifecycleOwner) { renderData(it) }
        _binding = FragmentPictureOfTheDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))
        findInWiki()
        setBottomAppBar(view)
        switchPictures()
        initThemeChooser()
        setChipOfPicture()
    }

    private fun setChipOfPicture() {
        val sp = requireActivity().getSharedPreferences(PICTURE_SP_NAME, Context.MODE_PRIVATE)
        when (sp.getInt(PICTURE_SP_KEY, 1)) {
            today -> binding.todayChip.isChecked = true
            yesterday -> binding.yesterdayChip.isChecked = true
            beforeYesterday -> binding.beforeYesterdayChip.isChecked = true
        }
    }

    private fun setChipOfTheme() {
        val sp =
            requireActivity().getSharedPreferences(NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE)
        when (sp.getInt(APP_THEME, 0)) {
            marsTheme -> binding.marsChip.isChecked = true
            titanTheme -> binding.titanChip.isChecked = true
            defaultTheme -> binding.earthChip.isChecked = true
        }
    }

    private fun initThemeChooser() {
        setChipOfTheme()

        initChip(binding.marsChip, marsTheme)
        initChip(binding.earthChip, defaultTheme)
        initChip(binding.titanChip, titanTheme)
    }

    private fun initChip(button: View, codeStyle: Int) {
        button.setOnClickListener {
            setAppTheme(codeStyle)
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment())
                .commitNow()
        }
    }

    private fun getAppTheme(codeStyle: Int): Int {
        return codeStyleToStyleId(getCodeStyle(codeStyle))
    }

    private fun getCodeStyle(codeStyle: Int): Int {
        val sharedPref =
            requireActivity().getSharedPreferences(NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE)
        return sharedPref.getInt(APP_THEME, codeStyle)
    }

    private fun setAppTheme(codeStyle: Int) {
        val sharedPref =
            requireActivity().getSharedPreferences(NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putInt(APP_THEME, codeStyle)
        editor.apply()
    }

    private fun setStatusBarColor(codeStyle: Int) {
        when (codeStyle) {
            marsTheme -> requireActivity().window.statusBarColor =
                ContextCompat.getColor(requireContext(), R.color.mars_color)
            titanTheme -> requireActivity().window.statusBarColor =
                ContextCompat.getColor(requireContext(), R.color.titan_color)
            defaultTheme -> requireActivity().window.statusBarColor =
                ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        }
    }

    private fun codeStyleToStyleId(codeStyle: Int): Int {
        setStatusBarColor(codeStyle)

      return  when (codeStyle) {
            marsTheme -> R.style.MarsStyle
            defaultTheme ->  R.style.Theme_MaterialYou
            titanTheme ->  R.style.TitanStyle

          else -> marsTheme
      }
    }

    private fun findInWiki() {
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_fav -> toast("Favourite")
            R.id.app_bar_settings -> activity?.supportFragmentManager
                ?.beginTransaction()?.replace(R.id.container, ChipsFragment.newInstance())
                ?.addToBackStack(null)
                ?.commit()
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment().show(it.supportFragmentManager, "tag")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun switchPictures() {
        val pref = requireActivity().getSharedPreferences(PICTURE_SP_NAME, Context.MODE_PRIVATE)
        binding.dayChipGroup.setOnCheckedChangeListener { chipGroup, position ->
            chipGroup.findViewById<Chip>(position)?.let {
                when (it.id) {
                    R.id.today_chip -> {
                        viewModel.pictureOfTheDay()
                        pref.edit().putInt(PICTURE_SP_KEY ,1).apply()
                    }
                    R.id.yesterday_chip -> {
                        val yesterday = LocalDate.now().minusDays(1)
                        viewModel.pictureOfTheDayWithDate(yesterday)
                        pref.edit().putInt(PICTURE_SP_KEY ,2).apply()
                    }
                    R.id.before_yesterday_chip -> {
                        val beforeYesterday = LocalDate.now().minusDays(2)
                        viewModel.pictureOfTheDayWithDate(beforeYesterday)
                        pref.edit().putInt(PICTURE_SP_KEY ,3).apply()
                    }
                    else -> viewModel.pictureOfTheDay()
                }
            }
        }
    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                binding.root.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    showError("Link is empty")
                } else {
                    binding.imageView.load(url) {
                        lifecycle(this@PictureOfTheDayFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                        crossfade(true)
                    }
                    binding.root.findViewById<TextView>(R.id.bottomSheetDescriptionHeader)
                        .text = data.serverResponseData.title

                    binding.root.findViewById<TextView>(R.id.bottomSheetDescription)
                        .text = data.serverResponseData.explanation

                }
            }
            is PictureOfTheDayData.Loading -> {
                binding.root.findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
            }
            is PictureOfTheDayData.Error -> {
                showError(data.error.message)
                binding.root.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
            }
        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setBottomAppBar(view: View) {
        val context = activity as MainActivity
        context.setSupportActionBar(view.findViewById(R.id.bottom_app_bar))
        setHasOptionsMenu(true)
        binding.fab.setOnClickListener {
            if (isMain) {
                isMain = false
                binding.bottomAppBar.navigationIcon = null
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_back_fab))
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
                binding.imageView.load(resources.getDrawable(R.drawable.view))
            } else {
                isMain = true
                binding.bottomAppBar.navigationIcon =
                    ContextCompat.getDrawable(context, R.drawable.ic_hamburger_menu_bottom_bar)
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_plus_fab))
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
                unKnownMethod()
            }
        }
    }

    private fun unKnownMethod() {
        if (binding.yesterdayChip.isChecked) {
            binding.todayChip.isChecked = true
        } else if (binding.todayChip.isChecked) {
            binding.yesterdayChip.isChecked = true
        } else if (binding.beforeYesterdayChip.isChecked){
            binding.yesterdayChip.isChecked = true
        }
    }

    private fun showError(message: String?) {
        toast(message)
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
        private var isMain = true
    }
}
