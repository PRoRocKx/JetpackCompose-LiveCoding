package ru.eugene.rickandmorty.listsscreen.fragment.lists

import androidx.annotation.DimenRes
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.eugene.rickandmorty.R
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteCharacter
import ru.eugene.rickandmorty.listsscreen.entity.Character
import ru.eugene.rickandmorty.listsscreen.fragment.details.CHARACTER_ID
import ru.eugene.rickandmorty.listsscreen.recycler.CharacterListAdapter
import ru.eugene.rickandmorty.listsscreen.viewmodel.lists.CharacterListViewModel

class CharacterListFragment :
    ListFragment<Character, FavoriteCharacter, CharacterListAdapter.CharacterViewHolder>(R.layout.fragment_list) {

    @DimenRes
    override val startMargin = R.dimen.character_list_divider_start_margin
    override val adapter by lazy {
        CharacterListAdapter(
            R.layout.item_character,
            ::clickItem,
            ::onFavoriteIconClicked,
            ::observeToFavoriteClickable
        )
    }
    override val viewModel by viewModel<CharacterListViewModel>()

    override fun onFavoriteIconClicked(id: Int, position: Int) =
        viewModel.changeCharacterFavoriteStatus(id)

    override fun loadFirstPage(isRefresh: Boolean) = viewModel.loadFirstCharacterList(isRefresh)

    override fun loadNextData() = viewModel.loadNextCharacterList()

    override fun navigateToSearchFragment() =
        findNavController().navigate(R.id.action_characterListFragment_to_searchFragment)

    override fun onItemWithIdClicked(id: Int) {
        isOpenedDetails = true
        findNavController().navigate(
            R.id.action_characterListFragment_to_characterDetailsFragment,
            bundleOf(CHARACTER_ID to id)
        )
    }

    override fun observeToFavoriteClickable(
        lifecycleOwner: LifecycleOwner,
        observer: Observer<Set<Int>>
    ) = viewModel.blockedFavorites.observe(lifecycleOwner, observer)
}