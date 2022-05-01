package ru.eugene.rickandmorty.listsscreen.extensions

import java.text.SimpleDateFormat
import java.util.*

private const val DATE_PATTERN = "dd.MM.yyyy"

fun Date.transformToString(): String =
    SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).format(this)