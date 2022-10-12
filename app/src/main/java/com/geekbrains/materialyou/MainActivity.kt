package com.geekbrains.materialyou

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.geekbrains.materialyou.databinding.ActivityMainBinding
import com.geekbrains.materialyou.view.navigation.ViewPageFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ViewPageFragment())
                .commitNow()
        }
    }
}