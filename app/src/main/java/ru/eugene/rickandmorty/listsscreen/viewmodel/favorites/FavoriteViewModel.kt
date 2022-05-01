package ru.eugene.rickandmorty.listsscreen.viewmodel.favorites

import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.eugene.rickandmorty.listsscreen.data.favoritelists.FavoriteRepository
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteEntity
import ru.eugene.rickandmorty.listsscreen.entity.Entity
import ru.eugene.rickandmorty.listsscreen.extensions.parseError
import ru.eugene.rickandmorty.listsscreen.viewmodel.BaseViewModel
import timber.log.Timber

private const val FIRST_OFFSET = 0

abstract class FavoriteViewModel<E : Entity, FE : FavoriteEntity> : BaseViewModel<E, FE>() {

    override val mutableData = MutableLiveData<List<E>>()
    override val mutableIsLoading = MutableLiveData<Boolean>()
    override val mutableErrorMessage = MutableLiveData<String>()
    override val mutableNextPoint = MutableLiveData<Int?>()
    protected abstract val repository: FavoriteRepository<E>
    private var tempNextPoint: Int? = null

    abstract fun addEntity(id: Int, date: Long): Completable

    fun removeFromFavs(id: Int) {
        tempData = tempData.minus(tempData.first { it.id == id} )
        mutableData.value = tempData
        tempNextPoint = tempNextPoint?.minus(1)
        mutableNextPoint.value = tempNextPoint
        roomRepository.deleteEntity(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = { Timber.e(it) }
            )
            .addTo(disposable)
    }

    fun returnToFavs(entity: E, position: Int) {
        tempData.toMutableList().also { mutableList ->
            mutableList.add(position, entity.setStatus(isFavorite = true))
            tempData = mutableList
        }
        mutableData.value = tempData
        tempNextPoint = tempNextPoint?.plus(1)
        mutableNextPoint.value = tempNextPoint
        addEntity(entity.id, entity.dateFavorite)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onError = { Timber.e(it) }
            )
            .addTo(disposable)
    }

    protected fun loadFirst(isRefresh: Boolean) {
        repository.getFavorites(FIRST_OFFSET)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                if (!isRefresh) {
                    mutableIsLoading.value = true
                }
            }
            .subscribeBy(
                onSuccess = { (dataList, next) ->
                    tempData = dataList
                    mutableData.value = tempData
                    tempNextPoint = next
                    mutableNextPoint.value = tempNextPoint
                    mutableIsLoading.value = false
                },
                onError = {
                    mutableIsLoading.value = false
                    mutableErrorMessage.value = it.parseError()
                    Timber.e(it)
                }
            )
            .addTo(disposable)
    }

    protected fun loadNext() {
        nextPoint.value?.let { page ->
            repository.getFavorites(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = { (dataList, next) ->
                        tempData = tempData.plus(dataList)
                        mutableData.value = tempData
                        tempNextPoint = next
                        mutableNextPoint.value = tempNextPoint
                    },
                    onError = {
                        mutableErrorMessage.value = it.parseError()
                        Timber.e(it)
                    }
                )
                .addTo(disposable)
        }
    }
}