package ru.eugene.rickandmorty

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.request.RequestOptions
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.skydoves.landscapist.glide.GlideImage
import ru.eugene.rickandmorty.listsscreen.entity.Character

private object Helper {

    @Composable
    private fun Avatar(character: Character) {
        GlideImage(
            character.image,
            modifier = Modifier
                .size(40.dp),
            requestOptions = { RequestOptions.circleCropTransform() },
            previewPlaceholder = R.drawable.favorite_icon
        )
    }

    @Composable
    private fun LikeButton(character: Character, onLikeClick: (Int) -> Unit) {

        var startAnimation by remember { mutableStateOf(false) }

        val scale = remember {
            Animatable(initialValue = 1f)
        }
        if (startAnimation) {
            LaunchedEffect(startAnimation) {
                scale.animateTo(2f)
                scale.animateTo(1f)
                startAnimation = false
            }
        }

        val painter = if (character.isFavorite) {
            painterResource(id = R.drawable.favorite_icon)
        } else {
            painterResource(id = R.drawable.not_favorite_icon)
        }
        Image(
            painter = painter,
            contentDescription = "",
            modifier = Modifier
                .wrapContentWidth(Alignment.End)
                .padding(end = 16.dp)
                .scale(scale.value)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    startAnimation = true
                    onLikeClick(character.id)
                }
                .graphicsLayer(clip = true)
        )
    }

    @Composable
    fun CharacterListScreen(
        data: List<Character>?,
        isLoading: Boolean?,
        refresh: () -> Unit,
        onItemClick: (Int) -> Unit,
        onLikeClick: (Int) -> Unit
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(false),
            onRefresh = refresh,
        ) {
            if (isLoading == true || data == null) {
//                LoadingState()
            } else {
//                CharacterList(data = data, onItemClick, onLikeClick)
            }
        }
    }

    @Composable
    private fun CharacterList(
        data: List<Character>,
        onItemClick: (Int) -> Unit,
        onLikeClick: (Int) -> Unit
    ) {

        val state = rememberLazyListState()

        val needLoad by derivedStateOf {
            val lastVisibleIndex = state.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val lastIndex = state.layoutInfo.totalItemsCount - 1
            lastVisibleIndex >= lastIndex
        }

        LazyColumn(state = state) {
            items(data, key = { it.id }) {
                println(state)
//                CharacterItem(character = it, onItemClick, onLikeClick)
            }
        }
    }
}