package ru.eugene.rickandmorty.listsscreen.fragment.favorites.lists

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import ru.eugene.rickandmorty.R
import ru.eugene.rickandmorty.listsscreen.SupportManager
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteEntity
import ru.eugene.rickandmorty.listsscreen.entity.Entity
import ru.eugene.rickandmorty.listsscreen.extensions.getColorById
import ru.eugene.rickandmorty.listsscreen.fragment.BaseFragment
import ru.eugene.rickandmorty.listsscreen.recycler.BaseListAdapter
import ru.eugene.rickandmorty.listsscreen.viewmodel.favorites.FavoriteViewModel

abstract class FavoriteFragment<E : Entity, FE : FavoriteEntity, VH : BaseListAdapter.BaseViewHolder<E>>(
    @LayoutRes contentLayoutId: Int
) : BaseFragment<E, FE, VH>(contentLayoutId) {

    abstract val viewModel: FavoriteViewModel<E, FE>
    protected var isOpenedDetails = false
    private val yellow by lazy { requireContext().getColorById(R.color.bright_yellow) }

    abstract fun loadFirstPage(isRefresh: Boolean)

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

    @CallSuper
    override fun onFavoriteIconClicked(id: Int, position: Int) {
        val entity = adapter.currentList[position]
        viewModel.removeFromFavs(id)
        (activity as? SupportManager)?.bottomAppBar?.also {
            Snackbar.make(it, getString(R.string.snackBarText), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.snackBarActionText)) {
                    viewModel.returnToFavs(entity, position)
                }
                .setActionTextColor(yellow)
                .setAnchorView(it)
                .show()
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