package ru.eugene.rickandmorty.listsscreen.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.eugene.rickandmorty.R
import ru.eugene.rickandmorty.listsscreen.SupportManager
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteEntity
import ru.eugene.rickandmorty.listsscreen.entity.Entity
import ru.eugene.rickandmorty.listsscreen.recycler.BaseListAdapter
import ru.eugene.rickandmorty.listsscreen.recycler.ItemDecorator
import ru.eugene.rickandmorty.listsscreen.recycler.OnItemClickListener
import ru.eugene.rickandmorty.listsscreen.viewmodel.BaseViewModel

abstract class BaseFragment<E : Entity, FE : FavoriteEntity, VH : BaseListAdapter.BaseViewHolder<E>>(
    @LayoutRes contentLayoutId: Int
) : Fragment(contentLayoutId), OnItemClickListener {

    abstract val startMargin: Int
    abstract val adapter: BaseListAdapter<E, VH>
    protected lateinit var recyclerView: RecyclerView
    private var isLoading: Boolean = false

    abstract fun loadNextData()

    abstract fun onItemWithIdClicked(id: Int)

    abstract fun onFavoriteIconClicked(id: Int, position: Int)

    override fun clickItem(id: Int) = onItemWithIdClicked(id)

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? SupportManager)?.showToolbar(true)
        recyclerView = view.findViewById(R.id.recyclerView)
        initRecyclerView(view)
    }

    @CallSuper
    override fun onStop() {
        (activity as? SupportManager)?.showProgressBar(false)
        super.onStop()
    }

    @CallSuper
    protected open fun observeToLiveData(viewModel: BaseViewModel<E, FE>) {
        viewModel.also {
            it.data.observe(viewLifecycleOwner, { dataList ->
                adapter.submitList(dataList)
                isLoading = false
            })
            it.isLoading.observe(viewLifecycleOwner, { isLoading ->
                (activity as? SupportManager)?.showProgressBar(isLoading)
            })
            it.nextPoint.observe(viewLifecycleOwner, { nextPage ->
                val previousIsLoading = adapter.isLoading
                adapter.isLoading = nextPage != null
                if (nextPage == null && previousIsLoading) {
                    adapter.notifyItemRemoved(adapter.itemCount - 1)
                } else if (nextPage != null && !previousIsLoading) {
                    adapter.notifyItemInserted(adapter.itemCount)
                }
            })
            it.errorMessage.observe(viewLifecycleOwner, { errorMessage ->
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            })
        }
    }

    protected fun getDimenInPixels(view: View, marginRes: Int) =
        view.context.resources.getDimensionPixelSize(marginRes)

    private fun initRecyclerView(view: View) =
        recyclerView.also {
            it.adapter = adapter
            ContextCompat.getDrawable(it.context, R.drawable.divider)
                ?.also { dividerDrawable ->
                    it.addItemDecoration(
                        ItemDecorator(dividerDrawable, getDimenInPixels(view, startMargin))
                    )
                }
            setScrollListener(it)
        }

    private fun setScrollListener(recyclerView: RecyclerView) =
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                (recyclerView.layoutManager as? LinearLayoutManager)?.takeIf {
                    it.findLastCompletelyVisibleItemPosition() >= adapter.currentList.size - 1 && !isLoading
                }?.let {
                    isLoading = true
                    loadNextData()
                }
            }
        })
}