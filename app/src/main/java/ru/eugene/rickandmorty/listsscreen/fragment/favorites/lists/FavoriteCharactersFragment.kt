package ru.eugene.rickandmorty.listsscreen.fragment.favorites.lists

import androidx.annotation.DimenRes
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.eugene.rickandmorty.R
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteCharacter
import ru.eugene.rickandmorty.listsscreen.entity.Character
import ru.eugene.rickandmorty.listsscreen.fragment.details.CHARACTER_ID
import ru.eugene.rickandmorty.listsscreen.recycler.CharacterListAdapter
import ru.eugene.rickandmorty.listsscreen.viewmodel.favorites.FavoriteCharactersViewModel

class FavoriteCharactersFragment :
    FavoriteFragment<Character, FavoriteCharacter, CharacterListAdapter.CharacterViewHolder>(R.layout.fragment_list) {

    @DimenRes
    override val startMargin = R.dimen.character_list_divider_start_margin
    override val viewModel by viewModel<FavoriteCharactersViewModel>()
    override val adapter by lazy {
        CharacterListAdapter(
            R.layout.item_character,
            ::clickItem,
            ::onFavoriteIconClicked
        )
    }

    override fun loadFirstPage(isRefresh: Boolean) = viewModel.loadFirstCharacterList(isRefresh)

    override fun loadNextData() = viewModel.loadNextCharacterList()

    override fun onItemWithIdClicked(id: Int) {
        findNavController().navigate(
            R.id.action_favoriteFragment_to_characterDetailsFragment,
            bundleOf(CHARACTER_ID to id)
        )
        isOpenedDetails = true
    }
}