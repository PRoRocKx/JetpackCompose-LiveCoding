package ru.eugene.rickandmorty.listsscreen.viewmodel.lists

import org.koin.core.component.inject
import ru.eugene.rickandmorty.listsscreen.data.list.CharacterListRepository
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteCharacter
import ru.eugene.rickandmorty.listsscreen.data.favorite.CharacterRoomRepository
import ru.eugene.rickandmorty.listsscreen.entity.Character
import java.util.*

class CharacterListViewModel : ListViewModel<Character, FavoriteCharacter>() {

    override val repository: CharacterListRepository by inject()

    override val roomRepository: CharacterRoomRepository by inject()

    override fun addEntity(id: Int) = roomRepository.addEntity(FavoriteCharacter(id, Date().time))

    override fun Character.setStatus(isFavorite: Boolean): Character =
        this.copy(isFavorite = isFavorite)

    fun loadFirstCharacterList(isRefresh: Boolean) = loadFirst(isRefresh)

    fun loadNextCharacterList() = loadNext()

    fun changeCharacterFavoriteStatus(id: Int) = changeFavoriteStatus(id)
}