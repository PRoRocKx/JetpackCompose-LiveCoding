package ru.eugene.rickandmorty.listsscreen.fragment.details

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.eugene.rickandmorty.R
import ru.eugene.rickandmorty.listsscreen.SupportManager
import ru.eugene.rickandmorty.listsscreen.entity.Character
import ru.eugene.rickandmorty.listsscreen.extensions.changeFavoriteStatus
import ru.eugene.rickandmorty.listsscreen.extensions.setFavoriteStatus
import ru.eugene.rickandmorty.listsscreen.recycler.CharacterListAdapter
import ru.eugene.rickandmorty.listsscreen.recycler.OnItemClickListener
import ru.eugene.rickandmorty.listsscreen.viewmodel.details.LocationDetailsViewModel

const val LOCATION_ID = "location_id"

class LocationDetailsFragment :
    BaseDetailsFragment<Character, CharacterListAdapter.CharacterViewHolder>(R.layout.fragment_location),
    OnItemClickListener {

    override val adapter by lazy {
        CharacterListAdapter(
            R.layout.item_character_arrow,
            ::clickItem
        )
    }
    override val startMargin = R.dimen.character_list_divider_start_margin
    private val viewModel by viewModel<LocationDetailsViewModel>()

    private lateinit var parentLayout: ConstraintLayout
    private lateinit var headerTextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var typeTextView: TextView
    private lateinit var dimensionTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var favoriteDescriptionTextView: TextView

    override fun clickItem(id: Int) =
        findNavController().navigate(
            R.id.action_locationDetailsFragment_to_characterDetailsFragment,
            bundleOf(CHARACTER_ID to id)
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        if (savedInstanceState == null) {
            arguments?.getInt(LOCATION_ID)?.also { id ->
                viewModel.loadLocationDetails(id)
                favoriteDescriptionTextView.setOnClickListener {
                    viewModel.changeLocationFavoriteStatus(id)
                    favoriteDescriptionTextView.changeFavoriteStatus(
                        favoriteIconDrawable,
                        notFavoriteIconDrawable
                    )
                }
            } ?: findNavController().popBackStack()
        }
    }

    override fun observeToIsLoading(view: View) =
        viewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            (activity as? SupportManager)?.showProgressBar(isLoading)
            parentLayout.isVisible = !isLoading
        })

    override fun observeToDetails(view: View) {
        viewModel.details.observe(viewLifecycleOwner, { location ->
            adapter.submitList(location.residents)
            headerTextView.isVisible = location.residents.isNotEmpty()
            nameTextView.text = location.name
            typeTextView.text = location.type
            dimensionTextView.text = location.dimension
            dateTextView.text = location.created
            favoriteDescriptionTextView.setFavoriteStatus(
                location.isFavorite,
                favoriteIconDrawable,
                notFavoriteIconDrawable
            )
        })
        viewModel.isFavoriteClickable.observe(viewLifecycleOwner, { isClickable ->
            favoriteDescriptionTextView.isClickable = isClickable
        })
    }

    private fun initViews(view: View) {
        parentLayout = view.findViewById(R.id.constraintLayout)
        headerTextView = view.findViewById(R.id.characterInLocationHeader)
        nameTextView = view.findViewById(R.id.name)
        typeTextView = view.findViewById(R.id.typeValue)
        dimensionTextView = view.findViewById(R.id.dimensionValue)
        dateTextView = view.findViewById(R.id.dateValue)
        favoriteDescriptionTextView = view.findViewById(R.id.favoriteDescription)
    }
}