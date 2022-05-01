package ru.eugene.rickandmorty.listsscreen.viewmodel.details

import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.inject
import ru.eugene.rickandmorty.listsscreen.data.details.CharacterDetailsRepository
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteCharacter
import ru.eugene.rickandmorty.listsscreen.data.favorite.CharacterRoomRepository
import ru.eugene.rickandmorty.listsscreen.entity.CharacterDetails
import java.util.*

class CharacterDetailsViewModel : BaseDetailsViewModel<CharacterDetails, FavoriteCharacter>(), KoinScopeComponent {

    override val repository: CharacterDetailsRepository by inject()

    override val roomRepository: CharacterRoomRepository by inject()

    override fun addEntity(id: Int) = roomRepository.addEntity(FavoriteCharacter(id, Date().time))

    fun loadCharacterDetails(id: Int) = loadDetails(id)

    fun changeCharacterFavoriteStatus(id: Int) = changeFavoriteStatus(id)
}