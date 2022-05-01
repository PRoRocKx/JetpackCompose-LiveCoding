package ru.eugene.rickandmorty.listsscreen.data

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import org.koin.android.ext.android.inject
import ru.eugene.rickandmorty.listsscreen.db.RickAndMortyDatabase

const val FAVORITE_ID = "id"
const val FAVORITE_DATE = "date"

private const val AUTHORITY = "ru.eugene.rickandmorty.FavoriteProvider"

private const val CHARACTER_PATH = "characters"
private const val LOCATION_PATH = "locations"
private const val EPISODE_PATH = "episodes"
private const val EXISTS_CHARACTER_PATH = "characters/exists"
private const val EXISTS_LOCATION_PATH = "locations/exists"
private const val EXISTS_EPISODE_PATH = "episodes/exists"

private const val URI_PREFIX = "content://$AUTHORITY/"

val CHARACTER_CONTENT_URI: Uri = Uri.parse("$URI_PREFIX$CHARACTER_PATH")
val LOCATION_CONTENT_URI: Uri = Uri.parse("$URI_PREFIX$LOCATION_PATH")
val EPISODE_CONTENT_URI: Uri = Uri.parse("$URI_PREFIX$EPISODE_PATH")

val EXISTS_CHARACTER_CONTENT_URI: Uri = Uri.parse("$URI_PREFIX$EXISTS_CHARACTER_PATH")
val EXISTS_LOCATION_CONTENT_URI: Uri = Uri.parse("$URI_PREFIX$EXISTS_LOCATION_PATH")
val EXISTS_EPISODE_CONTENT_URI: Uri = Uri.parse("$URI_PREFIX$EXISTS_EPISODE_PATH")

private const val PREFIX_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
private const val PREFIX_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."

private const val CHARACTER_CONTENT_TYPE = "$PREFIX_CONTENT_TYPE$CHARACTER_PATH"
private const val LOCATION_CONTENT_TYPE = "$PREFIX_CONTENT_TYPE$AUTHORITY.$LOCATION_PATH"
private const val EPISODE_CONTENT_TYPE = "$PREFIX_CONTENT_TYPE$AUTHORITY.$EPISODE_PATH"

private const val CHARACTER_CONTENT_ITEM_TYPE = "$PREFIX_CONTENT_ITEM_TYPE$AUTHORITY.$CHARACTER_PATH"
private const val LOCATION_CONTENT_ITEM_TYPE = "$PREFIX_CONTENT_ITEM_TYPE$AUTHORITY.$LOCATION_PATH"
private const val EPISODE_CONTENT_ITEM_TYPE = "$PREFIX_CONTENT_ITEM_TYPE$AUTHORITY.$EPISODE_PATH"

private const val URI_CHARACTERS = 1
private const val URI_LOCATIONS = 2
private const val URI_EPISODES = 3

private const val URI_CHARACTERS_ID = 4
private const val URI_LOCATIONS_ID = 5
private const val URI_EPISODES_ID = 6

private const val URI_EXISTS_CHARACTERS_ID = 7
private const val URI_EXISTS_LOCATIONS_ID = 8
private const val URI_EXISTS_EPISODES_ID = 9

private var uriMatcher = UriMatcher(UriMatcher.NO_MATCH).also {
    it.addURI(AUTHORITY, CHARACTER_PATH, URI_CHARACTERS)
    it.addURI(AUTHORITY, LOCATION_PATH, URI_LOCATIONS)
    it.addURI(AUTHORITY, EPISODE_PATH, URI_EPISODES)
    it.addURI(AUTHORITY, "$CHARACTER_PATH/#", URI_CHARACTERS_ID)
    it.addURI(AUTHORITY, "$LOCATION_PATH/#", URI_LOCATIONS_ID)
    it.addURI(AUTHORITY, "$EPISODE_PATH/#", URI_EPISODES_ID)
    it.addURI(AUTHORITY, "$EXISTS_CHARACTER_PATH/#", URI_EXISTS_CHARACTERS_ID)
    it.addURI(AUTHORITY, "$EXISTS_LOCATION_PATH/#", URI_EXISTS_LOCATIONS_ID)
    it.addURI(AUTHORITY, "$EXISTS_EPISODE_PATH/#", URI_EXISTS_EPISODES_ID)
}

private const val WRONG_URI_EXCEPTION_MESSAGE = "Wrong URI:"
private const val VALUES_NOT_FOUND_EXCEPTION_MESSAGE = "Values not found"
private const val NOT_IMPLEMENTED_EXCEPTION_MESSAGE = "Not implemented"
private const val ARGUMENTS_NOT_FOUND_EXCEPTION_MESSAGE = "offset & limit not found"

class FavoriteProvider : ContentProvider() {

    private val database: RickAndMortyDatabase by inject()

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int =
        when (uriMatcher.match(uri)) {
            URI_CHARACTERS_ID -> database.characterDao()
                .deleteCharacter(uri.lastPathSegment.toString())
            URI_LOCATIONS_ID -> database.locationDao()
                .deleteLocation(uri.lastPathSegment.toString())
            URI_EPISODES_ID -> database.episodeDao().deleteEpisode(uri.lastPathSegment.toString())
            else -> throw IllegalArgumentException("$WRONG_URI_EXCEPTION_MESSAGE $uri")
        }

    override fun getType(uri: Uri): String? =
        when (uriMatcher.match(uri)) {
            URI_CHARACTERS -> CHARACTER_CONTENT_TYPE
            URI_LOCATIONS -> LOCATION_CONTENT_TYPE
            URI_EPISODES -> EPISODE_CONTENT_TYPE
            URI_CHARACTERS_ID, URI_EXISTS_CHARACTERS_ID -> CHARACTER_CONTENT_ITEM_TYPE
            URI_LOCATIONS_ID, URI_EXISTS_LOCATIONS_ID -> LOCATION_CONTENT_ITEM_TYPE
            URI_EPISODES_ID, URI_EXISTS_EPISODES_ID -> EPISODE_CONTENT_ITEM_TYPE
            else -> null
        }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        values?.getAsInteger(FAVORITE_ID)?.let { id ->
            return when (uriMatcher.match(uri)) {
                URI_CHARACTERS_ID -> {
                    database.characterDao().addCharacter(id, values.getAsLong(FAVORITE_DATE))
                    ContentUris.withAppendedId(CHARACTER_CONTENT_URI, id.toLong())
                }
                URI_LOCATIONS_ID -> {
                    database.locationDao().addLocation(id, values.getAsLong(FAVORITE_DATE))
                    ContentUris.withAppendedId(LOCATION_CONTENT_URI, id.toLong())
                }
                URI_EPISODES_ID -> {
                    database.episodeDao().addEpisode(id, values.getAsLong(FAVORITE_DATE))
                    ContentUris.withAppendedId(EPISODE_CONTENT_URI, id.toLong())
                }
                else -> throw IllegalArgumentException("$WRONG_URI_EXCEPTION_MESSAGE $uri")
            }
        } ?: throw IllegalArgumentException(VALUES_NOT_FOUND_EXCEPTION_MESSAGE)
    }

    override fun onCreate(): Boolean = true

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor =
        when (uriMatcher.match(uri)) {
            URI_CHARACTERS -> {
                if (selectionArgs == null) {
                    database.characterDao().getCount()
                } else {
                    selectionArgs.takeIf { it.size == 2 }?.let {
                        database.characterDao()
                            .getCharacters(offset = selectionArgs[0], limit = selectionArgs[1])
                    } ?: throw IllegalArgumentException(ARGUMENTS_NOT_FOUND_EXCEPTION_MESSAGE)
                }
            }
            URI_LOCATIONS -> {
                if (selectionArgs == null) {
                    database.locationDao().getCount()
                } else {
                    selectionArgs.takeIf { it.size == 2 }?.let {
                        database.locationDao()
                            .getLocations(offset = selectionArgs[0], limit = selectionArgs[1])
                    } ?: throw IllegalArgumentException(ARGUMENTS_NOT_FOUND_EXCEPTION_MESSAGE)
                }
            }
            URI_EPISODES -> {
                if (selectionArgs == null) {
                    database.episodeDao().getCount()
                } else {
                    selectionArgs.takeIf { it.size == 2 }?.let {
                        database.episodeDao()
                            .getEpisodes(offset = selectionArgs[0], limit = selectionArgs[1])
                    } ?: throw IllegalArgumentException(ARGUMENTS_NOT_FOUND_EXCEPTION_MESSAGE)
                }
            }
            URI_EXISTS_LOCATIONS_ID -> database.locationDao().exists(uri.lastPathSegment.toString())
            URI_EXISTS_CHARACTERS_ID -> database.characterDao()
                .exists(uri.lastPathSegment.toString())
            URI_EXISTS_EPISODES_ID -> database.episodeDao().exists(uri.lastPathSegment.toString())
            else -> throw IllegalArgumentException("$WRONG_URI_EXCEPTION_MESSAGE $uri")
        }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int = throw UnsupportedOperationException(NOT_IMPLEMENTED_EXCEPTION_MESSAGE)
}