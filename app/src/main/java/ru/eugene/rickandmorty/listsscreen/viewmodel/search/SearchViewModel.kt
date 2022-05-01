package ru.eugene.rickandmorty.listsscreen.viewmodel.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import retrofit2.HttpException
import ru.eugene.rickandmorty.listsscreen.data.list.ListRepository
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteEntity
import ru.eugene.rickandmorty.listsscreen.entity.Entity
import ru.eugene.rickandmorty.listsscreen.extensions.parseError
import ru.eugene.rickandmorty.listsscreen.viewmodel.BaseViewModel
import ru.eugene.rickandmorty.listsscreen.viewmodel.FIRST_PAGE
import timber.log.Timber
import java.util.concurrent.TimeUnit

private const val TIME_OUT = 300L
private const val NOT_FOUND_CODE = 404

abstract class SearchViewModel<E : Entity, FE : FavoriteEntity> : BaseViewModel<E, FE>() {

    val blockedFavorites: LiveData<Set<Int>>
        get() = mutableBlockedFavorites
    val isFound: LiveData<Boolean>
        get() = mutableIsFound
    override val mutableData = MutableLiveData<List<E>>()
    override val mutableIsLoading = MutableLiveData<Boolean>()
    override val mutableNextPoint = MutableLiveData<Int?>()
    override val mutableErrorMessage = MutableLiveData<String>()
    protected abstract val repository: ListRepository<E>
    private val mutableCurrentName = MutableLiveData<String>()
    private val mutableIsFound = MutableLiveData<Boolean>()
    private lateinit var currentName: String
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

    protected fun searchFirst(subject: PublishSubject<String>) {
        subject.debounce(TIME_OUT, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .switchMapSingle { name ->
                currentName = name
                repository.searchData(FIRST_PAGE, name)
                    .doOnSubscribe { mutableIsLoading.postValue(true) }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { (dataList, next) ->
                    tempData = dataList
                    mutableData.value = tempData
                    mutableNextPoint.value = next
                    mutableIsLoading.value = false
                    mutableIsFound.value = true
                },
                onError = {
                    mutableIsLoading.value = false
                    if (it is HttpException && it.code() == NOT_FOUND_CODE) {
                        mutableIsFound.value = false
                    } else {
                        mutableErrorMessage.value = it.parseError()
                    }
                    Timber.e(it)
                }
            )
            .addTo(disposable)
    }

    protected fun searchNext() {
        nextPoint.value?.let { nextPage ->
            repository.searchData(nextPage, currentName)
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