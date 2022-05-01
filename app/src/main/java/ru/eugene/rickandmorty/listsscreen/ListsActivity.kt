package ru.eugene.rickandmorty.listsscreen

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.eugene.rickandmorty.R

private const val DELAY_BACK_PRESSED = 3000L

class ListsActivity : AppCompatActivity(), SupportManager {

    override val bottomAppBar: BottomAppBar by lazy { findViewById(R.id.bottomAppBar) }
    private lateinit var fab: FloatingActionButton
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var fragmentNavigator: FragmentNavigator
    private lateinit var favoriteItem: MenuItem
    private lateinit var toolbar: MaterialToolbar
    private lateinit var progressBar: ProgressBar
    private lateinit var appBarLayout: AppBarLayout
    private var isBackPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lists)
        appBarLayout = findViewById(R.id.appBarLayout)
        progressBar = findViewById(R.id.progressBar)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        initNavigation()
        favoriteItem = bottomNavigationView.menu.findItem(R.id.favorites)
        fab = findViewById(R.id.fab)
        fab.setOnClickListener { onFabClick() }
        switchBottomNavigation()
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.characters
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (bottomNavigationView.menu.findItem(R.id.favorites).isChecked) {
            fab.isSelected = true
        }
    }

    override fun finish() {
        if (isBackPressed) {
            super.finish()
            return
        }
        Toast.makeText(this, getString(R.string.clickBackToExitMessage), Toast.LENGTH_LONG).show()
        isBackPressed = true
        Handler(Looper.getMainLooper()).postDelayed({
            isBackPressed = false
        }, DELAY_BACK_PRESSED)
    }

    override fun showToolbar(isShow: Boolean) {
        appBarLayout.setExpanded(isShow)
        toolbar.isInvisible = !isShow
    }

    override fun showProgressBar(isShow: Boolean) {
        progressBar.isVisible = isShow
    }

    private fun switchBottomNavigation() =
        bottomNavigationView.setOnItemSelectedListener {
            hideSoftKeyboard()
            fab.isSelected = false
            fragmentNavigator.navigateByItemId(it.itemId)
            favoriteItem.isEnabled = false
            true
        }

    private fun onFabClick() {
        favoriteItem.isEnabled = true
        bottomNavigationView.selectedItemId = R.id.favorites
        fab.isSelected = true
    }

    private fun initNavigation() {
        (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment)
            .navController.also {
                val appBarConfiguration = AppBarConfiguration(
                    setOf(
                        R.id.characterListFragment,
                        R.id.locationListFragment,
                        R.id.favoriteFragment,
                        R.id.episodeListFragment
                    )
                )
                setupActionBarWithNavController(it, appBarConfiguration)
                it.addOnDestinationChangedListener { _, destination, _ ->
                    showNavigationIcon(
                        !appBarConfiguration.topLevelDestinations.contains(destination.id)
                    )
                    setOnClickNavigationIcon(it)
                }
                fragmentNavigator = FragmentNavigator(it)
                bottomNavigationView = findViewById(R.id.bottomNavigation)
                bottomNavigationView.setupWithNavController(it)
            }
    }

    private fun setOnClickNavigationIcon(navController: NavController) =
        toolbar.setNavigationOnClickListener {
            hideSoftKeyboard()
            navController.popBackStack()
        }

    private fun showNavigationIcon(isShow: Boolean) =
        if (isShow) {
            toolbar.setNavigationIcon(R.drawable.up)
        } else {
            toolbar.navigationIcon = null
        }

    private fun hideSoftKeyboard() =
        (this.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(
            this.currentFocus?.windowToken,
            0
        )
}