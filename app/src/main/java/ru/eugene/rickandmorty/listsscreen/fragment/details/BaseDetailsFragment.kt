package ru.eugene.rickandmorty.listsscreen.fragment.details

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import ru.eugene.rickandmorty.R
import ru.eugene.rickandmorty.listsscreen.SupportManager
import ru.eugene.rickandmorty.listsscreen.entity.Entity
import ru.eugene.rickandmorty.listsscreen.extensions.getDrawableById
import ru.eugene.rickandmorty.listsscreen.recycler.BaseListAdapter
import ru.eugene.rickandmorty.listsscreen.recycler.ItemDecorator

abstract class BaseDetailsFragment<E : Entity, VH : BaseListAdapter.BaseViewHolder<E>>(
    @LayoutRes contentLayoutId: Int
) : Fragment(contentLayoutId) {

    abstract val adapter: BaseListAdapter<E, VH>
    abstract val startMargin: Int
    protected val favoriteIconDrawable by lazy {
        context?.getDrawableById(R.drawable.favorite_icon)!!
    }
    protected val notFavoriteIconDrawable by lazy {
        context?.getDrawableById(R.drawable.not_favorite_icon)!!
    }

    abstract fun observeToIsLoading(view: View)

    abstract fun observeToDetails(view: View)

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? SupportManager)?.showToolbar(true)
        initRecyclerView(view)
        setOnBackPressedListener()
        observeToLiveData(view)
    }

    @CallSuper
    override fun onStop() {
        (activity as? SupportManager)?.showProgressBar(false)
        super.onStop()
    }

    private fun initRecyclerView(view: View) =
        view.findViewById<RecyclerView>(R.id.recyclerView).also {
            it.adapter = adapter
            it.setItemDecorator(view)
        }

    private fun RecyclerView.setItemDecorator(view: View) =
        ContextCompat.getDrawable(context, R.drawable.divider)
            ?.also { dividerDrawable ->
                addItemDecoration(
                    ItemDecorator(
                        dividerDrawable,
                        view.context.resources.getDimensionPixelSize(startMargin)
                    )
                )
            }

    private fun setOnBackPressedListener() =
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        )

    private fun observeToLiveData(view: View) {
        observeToIsLoading(view)
        observeToDetails(view)
    }
}