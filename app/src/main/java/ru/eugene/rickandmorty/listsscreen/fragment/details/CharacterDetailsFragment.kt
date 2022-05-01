package ru.eugene.rickandmorty.listsscreen.fragment.details

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.eugene.rickandmorty.R
import ru.eugene.rickandmorty.listsscreen.SupportManager
import ru.eugene.rickandmorty.listsscreen.entity.CharacterDetails
import ru.eugene.rickandmorty.listsscreen.entity.Episode
import ru.eugene.rickandmorty.listsscreen.extensions.SLASH
import ru.eugene.rickandmorty.listsscreen.extensions.changeFavoriteStatus
import ru.eugene.rickandmorty.listsscreen.extensions.getColorById
import ru.eugene.rickandmorty.listsscreen.extensions.setColor
import ru.eugene.rickandmorty.listsscreen.extensions.setFavoriteStatus
import ru.eugene.rickandmorty.listsscreen.recycler.*
import ru.eugene.rickandmorty.listsscreen.viewmodel.details.CharacterDetailsViewModel

const val CHARACTER_ID = "character_id"

class CharacterDetailsFragment :
    BaseDetailsFragment<Episode, EpisodeListAdapter.EpisodeViewHolder>(R.layout.fragment_character),
    OnItemClickListener {

    override val adapter by lazy {
        EpisodeListAdapter(
            R.layout.item_episode_character,
            ::clickItem
        )
    }
    override val startMargin = R.dimen.episode_list_divider_start_margin
    private val viewModel by viewModel<CharacterDetailsViewModel>()
    private val similarAdapter by lazy { SimilarAdapter(::clickCard) }

    private lateinit var parentLayout: ConstraintLayout
    private lateinit var headerTextView: TextView
    private lateinit var avatarImageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var statusTextView: TextView
    private lateinit var statusImageView: ImageView
    private lateinit var typeTextView: TextView
    private lateinit var genderTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var locationTextView: TextView
    private lateinit var locationLayout: LinearLayout
    private lateinit var similarHeaderTextView: TextView
    private lateinit var similarRecyclerView: RecyclerView
    private lateinit var favoriteDescriptionTextView: TextView

    override fun clickItem(id: Int) =
        findNavController().navigate(
            R.id.action_characterDetailsFragment_to_episodeDetailsFragment,
            bundleOf(EPISODE_ID to id)
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initRecyclerView(view)
        if (savedInstanceState == null) {
            arguments?.getInt(CHARACTER_ID)?.also { id ->
                viewModel.loadCharacterDetails(id)
                favoriteDescriptionTextView.setOnClickListener {
                    viewModel.changeCharacterFavoriteStatus(id)
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
        viewModel.details.observe(viewLifecycleOwner, { character ->
            adapter.submitList(character.episodes)
            similarAdapter.submitList(character.similarCharacters)
            similarHeaderTextView.isVisible = character.similarCharacters.isNotEmpty()
            similarRecyclerView.isVisible = character.similarCharacters.isNotEmpty()
            headerTextView.isVisible = character.episodes.isNotEmpty()
            Glide.with(requireContext()).load(character.image).into(avatarImageView)
            nameTextView.text = character.name
            context?.getColorById(character.status.color)
                ?.let { setStatus(it, character.status.description) }
            typeTextView.text = character.species
            genderTextView.text = character.gender
            dateTextView.text = character.created
            locationTextView.text = character.location.name
            locationLayout.also {
                character.location.url.isNotEmpty().also { isLocationExist ->
                    it.isVisible = isLocationExist
                    if (isLocationExist) {
                        it.setOnClickListener { onClickLocation(character) }
                    }
                }
            }
            favoriteDescriptionTextView.setFavoriteStatus(
                character.isFavorite,
                favoriteIconDrawable,
                notFavoriteIconDrawable
            )
        })
        viewModel.isFavoriteClickable.observe(viewLifecycleOwner, { isClickable ->
            favoriteDescriptionTextView.isClickable = isClickable
        })
    }

    private fun initRecyclerView(view: View) =
        similarRecyclerView.also {
            it.adapter = similarAdapter
            if (it.onFlingListener == null) {
                LinearSnapHelper().attachToRecyclerView(it)
            }
            it.addItemDecoration(
                CardItemDecorator(view.context.resources.getDimensionPixelSize(R.dimen.similar_card_offset))
            )
        }

    private fun clickCard(id: Int) =
        findNavController().navigate(
            R.id.action_characterDetailsFragment_self,
            bundleOf(CHARACTER_ID to id)
        )

    private fun setStatus(color: Int, statusDescription: String) {
        statusTextView.text = statusDescription
        statusTextView.setColor(color)
        statusImageView.setColor(color)
    }

    private fun onClickLocation(character: CharacterDetails) =
        findNavController().navigate(
            R.id.action_characterDetailsFragment_to_locationDetailsFragment,
            bundleOf(LOCATION_ID to character.location.url.substringAfterLast(SLASH).toInt())
        )

    private fun initViews(view: View) {
        parentLayout = view.findViewById(R.id.constraintLayout)
        headerTextView = view.findViewById(R.id.episodesHeader)
        avatarImageView = view.findViewById(R.id.avatar)
        nameTextView = view.findViewById(R.id.name)
        statusTextView = view.findViewById(R.id.statusDescription)
        statusImageView = view.findViewById(R.id.statusPoint)
        typeTextView = view.findViewById(R.id.typeValue)
        genderTextView = view.findViewById(R.id.genderValue)
        dateTextView = view.findViewById(R.id.dateValue)
        locationTextView = view.findViewById(R.id.locationValue)
        locationLayout = view.findViewById(R.id.locationLayout)
        similarHeaderTextView = view.findViewById(R.id.similarHeader)
        similarRecyclerView = view.findViewById(R.id.similarCharactersRecyclerView)
        favoriteDescriptionTextView = view.findViewById(R.id.favoriteDescription)
    }
}