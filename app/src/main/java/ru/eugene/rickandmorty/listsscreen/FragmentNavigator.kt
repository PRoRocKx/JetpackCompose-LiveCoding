package ru.eugene.rickandmorty.listsscreen

import androidx.navigation.NavController
import ru.eugene.rickandmorty.R
import ru.eugene.rickandmorty.listsscreen.fragment.favorites.FavoritesHostFragmentDirections
import ru.eugene.rickandmorty.listsscreen.fragment.lists.CharacterListFragmentDirections
import ru.eugene.rickandmorty.listsscreen.fragment.lists.EpisodeListFragmentDirections
import ru.eugene.rickandmorty.listsscreen.fragment.lists.LocationListFragmentDirections

private const val EXCEPTION_MESSAGE = "there is no such menu item"

class FragmentNavigator(private val navController: NavController) {

    fun navigateByItemId(itemId: Int) {
        when (itemId) {
            R.id.characters -> navigateToCharacters()
            R.id.locations -> navigateToLocations()
            R.id.favorites -> navigateToFavorites()
            R.id.episodes -> navigateToEpisodes()
            else -> throw NoSuchElementException(EXCEPTION_MESSAGE)
        }
    }

    private fun navigateToCharacters() =
        navController.navigate(CharacterListFragmentDirections.actionGlobalCharacterListFragment())

    private fun navigateToLocations() =
        navController.navigate(LocationListFragmentDirections.actionGlobalLocationListFragment())

    private fun navigateToFavorites() =
        navController.navigate(FavoritesHostFragmentDirections.actionGlobalFavoriteFragment())

    private fun navigateToEpisodes() =
        navController.navigate(EpisodeListFragmentDirections.actionGlobalEpisodeListFragment())
}