package com.example.pokemonencyclopedia.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pokemonencyclopedia.localFragment.*

class ViewPagerAdapter(manager: FragmentActivity): FragmentStateAdapter(manager) {
    override fun getItemCount(): Int = 9

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> KantoFragment()
            1 -> SaintFragment()
            2 -> HoennFragment()
            3 -> ShinoFragment()
            4 -> HanaFragment()
            5 -> CarlosFragment()
            6 -> AlolaFragment()
            7 -> GalarFragment()
            8 -> HisuiFragment()
            else -> KantoFragment()
        }
    }
}