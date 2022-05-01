package ru.eugene.rickandmorty.listsscreen

import com.google.android.material.bottomappbar.BottomAppBar

interface SupportManager {
    val bottomAppBar: BottomAppBar
    fun showToolbar(isShow: Boolean)
    fun showProgressBar(isShow: Boolean)
}