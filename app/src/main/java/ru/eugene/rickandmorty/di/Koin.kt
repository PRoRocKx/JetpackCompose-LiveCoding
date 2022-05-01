package ru.eugene.rickandmorty.di

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.eugene.rickandmorty.listsscreen.data.list.CharacterListRepository
import ru.eugene.rickandmorty.listsscreen.data.list.EpisodeListRepository
import ru.eugene.rickandmorty.listsscreen.data.list.LocationListRepository
import ru.eugene.rickandmorty.listsscreen.viewmodel.lists.EpisodeListViewModel
import ru.eugene.rickandmorty.listsscreen.data.RickAndMortyApi
import ru.eugene.rickandmorty.listsscreen.data.details.CharacterDetailsRepository
import ru.eugene.rickandmorty.listsscreen.data.details.EpisodeDetailsRepository
import ru.eugene.rickandmorty.listsscreen.data.details.LocationDetailsRepository
import ru.eugene.rickandmorty.listsscreen.data.favorite.CharacterRoomRepository
import ru.eugene.rickandmorty.listsscreen.data.favorite.EpisodeRoomRepository
import ru.eugene.rickandmorty.listsscreen.data.resolvers.CharacterResolver
import ru.eugene.rickandmorty.listsscreen.data.resolvers.EpisodeResolver
import ru.eugene.rickandmorty.listsscreen.data.resolvers.LocationResolver
import ru.eugene.rickandmorty.listsscreen.data.favorite.LocationRoomRepository
import ru.eugene.rickandmorty.listsscreen.data.favoritelists.FavoriteCharactersRepository
import ru.eugene.rickandmorty.listsscreen.data.favoritelists.FavoriteEpisodesRepository
import ru.eugene.rickandmorty.listsscreen.data.favoritelists.FavoriteLocationsRepository
import ru.eugene.rickandmorty.listsscreen.db.RickAndMortyDatabase
import ru.eugene.rickandmorty.listsscreen.viewmodel.details.CharacterDetailsViewModel
import ru.eugene.rickandmorty.listsscreen.viewmodel.details.EpisodeDetailsViewModel
import ru.eugene.rickandmorty.listsscreen.viewmodel.details.LocationDetailsViewModel
import ru.eugene.rickandmorty.listsscreen.viewmodel.favorites.FavoriteCharactersViewModel
import ru.eugene.rickandmorty.listsscreen.viewmodel.favorites.FavoriteEpisodesViewModel
import ru.eugene.rickandmorty.listsscreen.viewmodel.favorites.FavoriteLocationsViewModel
import ru.eugene.rickandmorty.listsscreen.viewmodel.lists.CharacterListViewModel
import ru.eugene.rickandmorty.listsscreen.viewmodel.search.CharacterSearchViewModel
import ru.eugene.rickandmorty.listsscreen.viewmodel.search.EpisodeSearchViewModel
import ru.eugene.rickandmorty.listsscreen.viewmodel.lists.LocationListViewModel
import ru.eugene.rickandmorty.listsscreen.viewmodel.search.LocationSearchViewModel

private const val URL = "https://rickandmortyapi.com/api/"
private const val DATABASE_NAME = "favorite_database"

val appModule = module {
    single {
        androidApplication()
    }
}

val networkModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(URL)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RickAndMortyApi::class.java)
    }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
            get(),
            RickAndMortyDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}

val favoriteResolverModule = module {
    factory {
        CharacterResolver(get())
    }
    factory {
        LocationResolver(get())
    }
    factory {
        EpisodeResolver(get())
    }
}

val listViewModelModule = module {
    viewModel { CharacterListViewModel() }
    viewModel { LocationListViewModel() }
    viewModel { EpisodeListViewModel() }

    scope<CharacterListViewModel> {
        scoped { CharacterListRepository(get(), get()) }
        scoped { CharacterRoomRepository(get()) }
    }
    scope<LocationListViewModel> {
        scoped { LocationListRepository(get(), get()) }
        scoped { LocationRoomRepository(get()) }
    }
    scope<EpisodeListViewModel> {
        scoped { EpisodeListRepository(get(), get()) }
        scoped { EpisodeRoomRepository(get()) }
    }
}

val searchViewModelModule = module {
    viewModel { CharacterSearchViewModel() }
    viewModel { LocationSearchViewModel() }
    viewModel { EpisodeSearchViewModel() }

    scope<CharacterSearchViewModel> {
        scoped { CharacterListRepository(get(), get()) }
        scoped { CharacterRoomRepository(get()) }
    }
    scope<LocationSearchViewModel> {
        scoped { LocationListRepository(get(), get()) }
        scoped { LocationRoomRepository(get()) }
    }
    scope<EpisodeSearchViewModel> {
        scoped { EpisodeListRepository(get(), get()) }
        scoped { EpisodeRoomRepository(get()) }
    }
}

val detailsViewModelModule = module {
    viewModel { CharacterDetailsViewModel() }
    viewModel { LocationDetailsViewModel() }
    viewModel { EpisodeDetailsViewModel() }

    scope<CharacterDetailsViewModel> {
        scoped { CharacterDetailsRepository(get(), get()) }
        scoped { CharacterRoomRepository(get()) }
    }
    scope<LocationDetailsViewModel> {
        scoped { LocationDetailsRepository(get(), get()) }
        scoped { LocationRoomRepository(get()) }
    }
    scope<EpisodeDetailsViewModel> {
        scoped { EpisodeDetailsRepository(get(), get()) }
        scoped { EpisodeRoomRepository(get()) }
    }
}

val favoritesViewModelModule = module {
    viewModel { FavoriteCharactersViewModel() }
    viewModel { FavoriteLocationsViewModel() }
    viewModel { FavoriteEpisodesViewModel() }

    scope<FavoriteCharactersViewModel> {
        scoped { FavoriteCharactersRepository(get(), get()) }
        scoped { CharacterRoomRepository(get()) }
    }
    scope<FavoriteLocationsViewModel> {
        scoped { FavoriteLocationsRepository(get(), get()) }
        scoped { LocationRoomRepository(get()) }
    }
    scope<FavoriteEpisodesViewModel> {
        scoped { FavoriteEpisodesRepository(get(), get()) }
        scoped { EpisodeRoomRepository(get()) }
    }
}