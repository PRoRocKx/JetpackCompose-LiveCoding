package ru.eugene.rickandmorty.listsscreen.fragment.favorites

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.eugene.rickandmorty.R
import ru.eugene.rickandmorty.listsscreen.SupportManager

class FavoritesHostFragment : Fragment(R.layout.fragment_favorites) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? SupportManager)?.showToolbar(true)
        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.favoritesViewPager)
        viewPager.adapter = FavoritePagerAdapter(this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.characters)
                1 -> tab.text = getString(R.string.locations)
                2 -> tab.text = getString(R.string.episodes)
            }
        }.attach()
    }
}