package ru.eugene.rickandmorty.listsscreen.viewmodel.favorites

import org.koin.core.component.inject
import ru.eugene.rickandmorty.listsscreen.data.favorite.CharacterRoomRepository
import ru.eugene.rickandmorty.listsscreen.data.favoritelists.FavoriteCharactersRepository
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteCharacter
import ru.eugene.rickandmorty.listsscreen.entity.Character

class FavoriteCharactersViewModel : FavoriteViewModel<Character, FavoriteCharacter>() {

    override val repository: FavoriteCharactersRepository by inject()

    override val roomRepository: CharacterRoomRepository by inject()

    override fun addEntity(id: Int, date: Long) =
        roomRepository.addEntity(FavoriteCharacter(id, date))

    override fun Character.setStatus(isFavorite: Boolean): Character =
        this.copy(isFavorite = isFavorite)

    fun loadFirstCharacterList(isRefresh: Boolean) = loadFirst(isRefresh)

    fun loadNextCharacterList() = loadNext()
}