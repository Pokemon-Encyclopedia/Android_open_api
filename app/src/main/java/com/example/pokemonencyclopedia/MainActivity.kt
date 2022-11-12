package com.example.pokemonencyclopedia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pokemonencyclopedia.adapter.ViewPagerAdapter
import com.example.pokemonencyclopedia.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private val fragmentTitleList = mutableListOf("관동","성도","호연","신오","하나","칼로스","알로라","가라르","히스이")
    private val fragmentIconList = mutableListOf(R.drawable.kanto,R.drawable.saint,R.drawable.hoenn,R.drawable.shino,R.drawable.hana,R.drawable.carlos,R.drawable.alola,R.drawable.galar,R.drawable.hisui)

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(binding.localTabLayout, binding.viewPager) { tab, pos ->
            tab.text = fragmentTitleList[pos]
            tab.setIcon(fragmentIconList[pos])
        }.attach()
    }
}