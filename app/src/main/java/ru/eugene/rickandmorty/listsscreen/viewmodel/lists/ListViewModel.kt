package ru.eugene.rickandmorty.listsscreen.viewmodel.lists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.eugene.rickandmorty.listsscreen.data.list.ListRepository
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteEntity
import ru.eugene.rickandmorty.listsscreen.entity.Entity
import ru.eugene.rickandmorty.listsscreen.extensions.parseError
import ru.eugene.rickandmorty.listsscreen.viewmodel.BaseViewModel
import ru.eugene.rickandmorty.listsscreen.viewmodel.FIRST_PAGE
import timber.log.Timber

abstract class ListViewModel<E : Entity, FE : FavoriteEntity> : BaseViewModel<E, FE>() {

    val blockedFavorites: LiveData<Set<Int>>
        get() = mutableBlockedFavorites
    override val mutableData = MutableLiveData<List<E>>()
    override val mutableIsLoading = MutableLiveData<Boolean>()
    override val mutableNextPoint = MutableLiveData<Int?>()
    override val mutableErrorMessage = MutableLiveData<String>()
    protected abstract val repository: ListRepository<E>
    private val mutableBlockedFavorites = MutableLiveData<Set<Int>>(emptySet())
    private var tempBlockedFavorites = emptySet<Int>()

    abstract fun addEntity(id: Int): Completable

    protected fun changeFavoriteStatus(id: Int) {
        tempBlockedFavorites = tempBlockedFavorites.plus(id)
        mutableBlockedFavorites.value = tempBlockedFavorites
        roomRepository.exists(id)
            .subscribeOn(Schedulers.io())
            .flatMapCompletable { isExists ->
                if (isExists) {
                    changeStatusInLiveData(id, false)
                    deleteEntity(id)
                } else {
                    changeStatusInLiveData(id, true)
                    addEntity(id)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    tempBlockedFavorites = tempBlockedFavorites.minus(id)
                    mutableBlockedFavorites.value = tempBlockedFavorites
                },
                onError = {
                    tempBlockedFavorites = tempBlockedFavorites.minus(id)
                    mutableBlockedFavorites.value = tempBlockedFavorites
                    Timber.e(it)
                }
            )
            .addTo(disposable)
    }

    protected fun loadFirst(isRefresh: Boolean) {
        repository.getData(FIRST_PAGE)
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
                    mutableNextPoint.value = next
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
            repository.getData(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = { (dataList, next) ->
                        tempData = tempData.plus(dataList)
                        mutableData.value = tempData
                        mutableNextPoint.value = next
                    },
                    onError = {
                        mutableErrorMessage.value = it.parseError()
                        Timber.e(it)
                    }
                )
                .addTo(disposable)
        }
    }

    private fun changeStatusInLiveData(id: Int, isFavorite: Boolean) =
        tempData.toMutableList().also { mutableList ->
            mutableList.indexOfFirst { it.id == id }.also { index ->
                mutableList[index] = mutableList[index].setStatus(isFavorite = isFavorite)
            }
            tempData = mutableList
            mutableData.postValue(tempData)
        }
}