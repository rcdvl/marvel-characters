package com.rcdvl.marvel.ui.details

import android.util.Log
import androidx.lifecycle.ViewModel
import com.rcdvl.marvel.networking.MarvelService

/**
 * Created by renan on 10/9/17.
 */
class CharacterDetailsViewModel(private val marvelService: MarvelService,
                                resourcesLinks: Array<String>) : ViewModel() {

    val characterResourcesViewModels: Map<String, CharacterResourceViewModel>

    init {
        characterResourcesViewModels = mutableMapOf()
        resourcesLinks.forEach {
            characterResourcesViewModels[it] = CharacterResourceViewModel(marvelService, it)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(this.javaClass.simpleName, "onCleared()")
    }
}