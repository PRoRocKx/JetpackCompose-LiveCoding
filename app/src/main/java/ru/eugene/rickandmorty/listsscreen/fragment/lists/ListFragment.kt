package ru.eugene.rickandmorty.listsscreen.fragment.lists

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.eugene.rickandmorty.R
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteEntity
import ru.eugene.rickandmorty.listsscreen.entity.Entity
import ru.eugene.rickandmorty.listsscreen.fragment.BaseFragment
import ru.eugene.rickandmorty.listsscreen.recycler.BaseListAdapter
import ru.eugene.rickandmorty.listsscreen.viewmodel.lists.ListViewModel

abstract class ListFragment<E : Entity, FE : FavoriteEntity, VH : BaseListAdapter.BaseViewHolder<E>>(
    @LayoutRes contentLayoutId: Int
) : BaseFragment<E, FE, VH>(contentLayoutId) {

    abstract val viewModel: ListViewModel<E, FE>
    protected var isOpenedDetails = false

    abstract fun loadFirstPage(isRefresh: Boolean)

    abstract fun navigateToSearchFragment()

    abstract fun observeToFavoriteClickable(
        lifecycleOwner: LifecycleOwner,
        observer: Observer<Set<Int>>
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSwipeRefreshLayout(view)
        observeToLiveData(viewModel)
        if ((savedInstanceState == null && viewModel.data.value == null) || isOpenedDetails) {
            loadFirstPage(false)
            isOpenedDetails = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.nav_search_menu, menu)
        menu.findItem(R.id.appSearchBar).setOnMenuItemClickListener {
            navigateToSearchFragment()
            true
        }
    }

    private fun initSwipeRefreshLayout(view: View) {
        view.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh).also {
            it.setProgressViewOffset(
                true,
                getDimenInPixels(view, R.dimen.start_offset_swipe_refresher),
                getDimenInPixels(view, R.dimen.end_offset_swipe_refresher)
            )
            it.setOnRefreshListener {
                loadFirstPage(true)
                it.isRefreshing = false
            }
        }
    }
}