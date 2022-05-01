package ru.eugene.rickandmorty.listsscreen.fragment.favorites

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.eugene.rickandmorty.listsscreen.fragment.favorites.lists.FavoriteCharactersFragment
import ru.eugene.rickandmorty.listsscreen.fragment.favorites.lists.FavoriteEpisodesFragment
import ru.eugene.rickandmorty.listsscreen.fragment.favorites.lists.FavoriteLocationsFragment

private const val EXCEPTION_MESSAGE = "there is no such tab"
private const val FRAGMENTS_COUNT = 3

class FavoritePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = FRAGMENTS_COUNT

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> FavoriteCharactersFragment()
            1 -> FavoriteLocationsFragment()
            2 -> FavoriteEpisodesFragment()
            else -> throw NoSuchElementException(EXCEPTION_MESSAGE)
        }
}