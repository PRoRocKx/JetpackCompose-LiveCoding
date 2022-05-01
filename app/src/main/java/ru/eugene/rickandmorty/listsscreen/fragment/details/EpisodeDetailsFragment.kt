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
import ru.eugene.rickandmorty.listsscreen.viewmodel.details.EpisodeDetailsViewModel

const val EPISODE_ID = "episode_id"

class EpisodeDetailsFragment :
    BaseDetailsFragment<Character, CharacterListAdapter.CharacterViewHolder>(R.layout.fragment_episode),
    OnItemClickListener {

    override val adapter by lazy {
        CharacterListAdapter(
            R.layout.item_character_arrow,
            ::clickItem
        )
    }
    override val startMargin = R.dimen.character_list_divider_start_margin
    private val viewModel by viewModel<EpisodeDetailsViewModel>()

    private lateinit var parentLayout: ConstraintLayout
    private lateinit var headerTextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var codeDetailsTextView: TextView
    private lateinit var codeValueTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var favoriteDescriptionTextView: TextView

    override fun clickItem(id: Int) {
        findNavController().navigate(
            R.id.action_episodeDetailsFragment_to_characterDetailsFragment,
            bundleOf(CHARACTER_ID to id)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        if (savedInstanceState == null) {
            arguments?.getInt(EPISODE_ID)?.also { id ->
                viewModel.loadEpisodeDetails(id)
                favoriteDescriptionTextView.setOnClickListener {
                    viewModel.changeEpisodeFavoriteStatus(id)
                    favoriteDescriptionTextView.changeFavoriteStatus(
                        favoriteIconDrawable,
                        notFavoriteIconDrawable
                    )
                }
            } ?: findNavController().popBackStack()
        }
    }

    override fun observeToIsLoading(view: View) {
        viewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            (activity as? SupportManager)?.showProgressBar(isLoading)
            parentLayout.isVisible = !isLoading
        })
    }

    override fun observeToDetails(view: View) {
        viewModel.details.observe(viewLifecycleOwner, { episode ->
            adapter.submitList(episode.residents)
            headerTextView.isVisible = episode.residents.isNotEmpty()
            nameTextView.text = episode.name
            codeDetailsTextView.text = if (episode.numberOfEpisodeInfo.season != null) {
                view.context.getString(
                    R.string.numberEpisode,
                    episode.numberOfEpisodeInfo.season,
                    episode.numberOfEpisodeInfo.episode
                )
            } else {
                episode.numberOfEpisodeInfo.codeOfEpisode
            }
            codeValueTextView.text = episode.numberOfEpisodeInfo.codeOfEpisode
            dateTextView.text = episode.created
            favoriteDescriptionTextView.setFavoriteStatus(
                episode.isFavorite,
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
        headerTextView = view.findViewById(R.id.characterInEpisodeHeader)
        nameTextView = view.findViewById(R.id.name)
        codeDetailsTextView = view.findViewById(R.id.codeDetails)
        codeValueTextView = view.findViewById(R.id.codeValue)
        dateTextView = view.findViewById(R.id.dateValue)
        favoriteDescriptionTextView = view.findViewById(R.id.favoriteDescription)
    }
}