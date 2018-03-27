package com.rcdvl.marvel.ui.details

import com.rcdvl.marvel.model.MarvelResource
import com.rcdvl.marvel.networking.MarvelService
import com.rcdvl.marvel.ui.MarvelViewModel

/**
 * Created by renan on 18/02/2018.
 */
class CharacterResourceViewModel(marvelService: MarvelService,
                                 private val resourceType: String) :
        MarvelViewModel<MarvelResource>(marvelService) {

    fun loadMore(characterId: Long) {
        fireCall(marvelService.getCharacterResourceList(resourceType, characterId, offset, count))
    }
}