package ru.eugene.rickandmorty.listsscreen.pojo

private const val PAGE_PARAMETER = "page="
private const val AMPERSAND = '&'

data class Info(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
) {
    fun getNextPage(): Int? =
        next?.substringAfterLast(PAGE_PARAMETER)?.substringBefore(AMPERSAND)?.toIntOrNull()
}