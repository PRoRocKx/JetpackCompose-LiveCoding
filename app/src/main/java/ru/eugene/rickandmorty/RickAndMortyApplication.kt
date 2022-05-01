package ru.eugene.rickandmorty

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.eugene.rickandmorty.di.*
import timber.log.Timber

const val CHANNEL_ID = "channel_01"

class RickAndMortyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initLogger()
        startKoin()
        createNotificationChannel()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun startKoin() {
        startKoin {
            androidContext(this@RickAndMortyApplication)
            modules(
                appModule,
                networkModule,
                databaseModule,
                favoriteResolverModule,
                listViewModelModule,
                searchViewModelModule,
                favoritesViewModelModule,
                detailsViewModelModule,
            )
        }
    }

    private fun createNotificationChannel() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationManager?.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    getString(R.string.notificationNameChannel),
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }
    }
}