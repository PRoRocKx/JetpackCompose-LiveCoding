package ru.eugene.rickandmorty.listsscreen.viewmodel.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.scope.Scope
import ru.eugene.rickandmorty.listsscreen.data.details.DetailsRepository
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteEntity
import ru.eugene.rickandmorty.listsscreen.data.favorite.RoomRepository
import ru.eugene.rickandmorty.listsscreen.entity.Entity
import timber.log.Timber

abstract class BaseDetailsViewModel<E : Entity, FE : FavoriteEntity> : ViewModel(),
    KoinScopeComponent {

    override val scope: Scope by lazy { createScope() }
    val details: LiveData<E>
        get() = mutableDetails
    val isLoading: LiveData<Boolean>
        get() = mutableIsLoading
    val isFavoriteClickable: LiveData<Boolean>
        get() = mutableIsFavoriteClickable

    protected abstract val repository: DetailsRepository<E>
    protected abstract val roomRepository: RoomRepository<FE>
    private val mutableDetails = MutableLiveData<E>()
    private val mutableIsLoading = MutableLiveData<Boolean>()
    private val mutableIsFavoriteClickable = MutableLiveData<Boolean>()
    private val disposable = CompositeDisposable()

    protected fun loadDetails(id: Int) {
        repository.getData(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                mutableIsLoading.value = true
            }
            .subscribeBy(
                onSuccess = {
                    mutableDetails.value = it
                    mutableIsLoading.value = false
                },
                onError = {
                    mutableIsLoading.value = false
                    Timber.e(it)
                }
            )
            .addTo(disposable)
    }

    protected fun changeFavoriteStatus(id: Int) {
        mutableIsFavoriteClickable.value = false
        roomRepository.exists(id)
            .subscribeOn(Schedulers.io())
            .flatMapCompletable { isExists ->
                if (isExists) {
                    deleteEntity(id)
                } else {
                    addEntity(id)
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = { mutableIsFavoriteClickable.value = true },
                onError = {
                    mutableIsFavoriteClickable.value = true
                    Timber.e(it)
                }
            )
            .addTo(disposable)
    }

    abstract fun addEntity(id: Int): Completable

    override fun onCleared() {
        disposable.dispose()
        scope.close()
        super.onCleared()
    }

    private fun deleteEntity(id: Int) = roomRepository.deleteEntity(id)
}