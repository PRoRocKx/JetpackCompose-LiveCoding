package ru.eugene.rickandmorty.listsscreen.fragment.search

import androidx.annotation.DimenRes
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import io.reactivex.rxjava3.subjects.PublishSubject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.eugene.rickandmorty.R
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteCharacter
import ru.eugene.rickandmorty.listsscreen.entity.Character
import ru.eugene.rickandmorty.listsscreen.fragment.details.CHARACTER_ID
import ru.eugene.rickandmorty.listsscreen.recycler.CharacterListAdapter
import ru.eugene.rickandmorty.listsscreen.viewmodel.search.CharacterSearchViewModel

class CharacterSearchFragment :
    SearchFragment<Character, FavoriteCharacter, CharacterListAdapter.CharacterViewHolder>(R.layout.fragment_search_list) {

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
    override val viewModel by viewModel<CharacterSearchViewModel>()

    override fun onFavoriteIconClicked(id: Int, position: Int) =
        viewModel.changeCharacterFavoriteStatus(id)

    override fun searchDataByName(subject: PublishSubject<String>) =
        viewModel.searchFirstPage(subject)

    override fun loadNextData() = viewModel.searchNextPage()

    override fun onItemWithIdClicked(id: Int) {
        super.onItemWithIdClicked(id)
        findNavController().navigate(
            R.id.action_characterSearchFragment_to_characterDetailsFragment,
            bundleOf(CHARACTER_ID to id)
        )
    }

    override fun observeToFavoriteClickable(
        lifecycleOwner: LifecycleOwner,
        observer: Observer<Set<Int>>
    ) = viewModel.blockedFavorites.observe(lifecycleOwner, observer)
}