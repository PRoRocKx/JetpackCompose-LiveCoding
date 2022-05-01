package ru.eugene.rickandmorty.listsscreen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.scope.Scope
import ru.eugene.rickandmorty.listsscreen.data.favorite.RoomRepository
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteEntity
import ru.eugene.rickandmorty.listsscreen.entity.Entity

const val FIRST_PAGE = 1

abstract class BaseViewModel<E : Entity, FE : FavoriteEntity> : ViewModel(), KoinScopeComponent {

    override val scope: Scope by lazy { createScope() }
    val data: LiveData<List<E>>
        get() = mutableData
    val isLoading: LiveData<Boolean>
        get() = mutableIsLoading
    val nextPoint: LiveData<Int?>
        get() = mutableNextPoint
    val errorMessage: LiveData<String>
        get() = mutableErrorMessage

    protected abstract val roomRepository: RoomRepository<FE>
    protected abstract val mutableData: MutableLiveData<List<E>>
    protected abstract val mutableIsLoading: MutableLiveData<Boolean>
    protected abstract val mutableNextPoint: MutableLiveData<Int?>
    protected abstract val mutableErrorMessage: MutableLiveData<String>
    protected val disposable = CompositeDisposable()
    protected var tempData = emptyList<E>()

    abstract fun E.setStatus(isFavorite: Boolean): E

    override fun onCleared() {
        disposable.dispose()
        scope.close()
        super.onCleared()
    }

    protected fun deleteEntity(id: Int) = roomRepository.deleteEntity(id)
}