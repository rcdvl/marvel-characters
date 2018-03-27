package com.rcdvl.marvel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.rcdvl.marvel.di.AppComponent
import com.rcdvl.marvel.di.AppModule
import com.rcdvl.marvel.di.DaggerAppComponent


/**
 * Created by renan on 10/16/17.
 */
class MarvelApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    fun isNetworkAvailable(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
}