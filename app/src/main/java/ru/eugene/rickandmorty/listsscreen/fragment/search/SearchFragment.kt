package ru.eugene.rickandmorty.listsscreen.fragment.search

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.eugene.rickandmorty.R
import ru.eugene.rickandmorty.listsscreen.db.entity.FavoriteEntity
import ru.eugene.rickandmorty.listsscreen.entity.Entity
import ru.eugene.rickandmorty.listsscreen.fragment.BaseFragment
import ru.eugene.rickandmorty.listsscreen.recycler.BaseListAdapter
import ru.eugene.rickandmorty.listsscreen.viewmodel.BaseViewModel
import ru.eugene.rickandmorty.listsscreen.viewmodel.search.SearchViewModel

private const val SEARCH_VIEW_KEY = "searchViewKey"

abstract class SearchFragment<E : Entity, FE : FavoriteEntity, VH : BaseListAdapter.BaseViewHolder<E>>(
    @LayoutRes contentLayoutId: Int
) : BaseFragment<E, FE, VH>(contentLayoutId) {

    abstract val viewModel: SearchViewModel<E, FE>
    private lateinit var notFoundTextView: TextView
    private var searchView: SearchView? = null
    private var oldQuery: String? = null
    private val subject by lazy { PublishSubject.create<String>() }

    abstract fun searchDataByName(subject: PublishSubject<String>)

    abstract fun observeToFavoriteClickable(
        lifecycleOwner: LifecycleOwner,
        observer: Observer<Set<Int>>
    )

    @CallSuper
    override fun onItemWithIdClicked(id: Int) {
        (context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(
            view?.windowToken,
            0
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notFoundTextView = view.findViewById(R.id.notFoundTextView)
        setOnBackPressedListener()
        observeToLiveData(viewModel)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        oldQuery = savedInstanceState?.getString(SEARCH_VIEW_KEY)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SEARCH_VIEW_KEY, searchView?.query.toString())
        super.onSaveInstanceState(outState)
    }

    final override fun observeToLiveData(viewModel: BaseViewModel<E, FE>) {
        super.observeToLiveData(viewModel)
        this@SearchFragment.viewModel.isFound.observe(viewLifecycleOwner, { isFound ->
            notFoundTextView.isVisible = !isFound
            recyclerView.isVisible = isFound
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        searchView = menu.findItem(R.id.appSearchBar).actionView as? SearchView
        searchView?.let {
            it.isIconified = false
            it.setQuery(oldQuery, false)
            it.setInputListener()
        }
    }

    private fun setOnBackPressedListener() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        )
    }

    private fun SearchView.setInputListener() =
        this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    subject.onNext(newText)
                    searchDataByName(subject)
                }
                return false
            }
        })
}