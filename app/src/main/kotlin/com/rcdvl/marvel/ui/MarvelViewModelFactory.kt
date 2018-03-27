package com.rcdvl.marvel.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.rcdvl.marvel.di.AppModule
import com.rcdvl.marvel.networking.MarvelService
import com.rcdvl.marvel.ui.details.CharacterDetailsViewModel
import com.rcdvl.marvel.ui.list.CharacterListViewModel
import javax.inject.Inject
import javax.inject.Named


/**
 * Created by renan on 07/02/18.
 */
class MarvelViewModelFactory @Inject constructor(var marvelService: MarvelService,
                                                 @Named(AppModule.CHARACTER_RESOURCES_LINKS)
                                                 var resourcesLinks: Array<String>) :
        ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharacterListViewModel::class.java)) {
            return CharacterListViewModel(marvelService) as T
        } else if (modelClass.isAssignableFrom(CharacterDetailsViewModel::class.java)) {
            return CharacterDetailsViewModel(marvelService, resourcesLinks) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}