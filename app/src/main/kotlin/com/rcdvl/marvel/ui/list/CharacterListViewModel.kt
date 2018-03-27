package com.rcdvl.marvel.ui.list

import com.rcdvl.marvel.model.MarvelCharacter
import com.rcdvl.marvel.networking.MarvelService
import com.rcdvl.marvel.ui.MarvelViewModel

/**
 * Created by renan on 10/9/17.
 */
class CharacterListViewModel(marvelService: MarvelService) :
        MarvelViewModel<MarvelCharacter>(marvelService) {

    private var lastSearchTerm = ""

    fun loadMoreCharacters(searchTerm: String? = null) {
        searchTerm?.let {
            if (it != lastSearchTerm) {
                lastSearchTerm = it
                reset()
            }

            fireCall(marvelService.getCharactersList(offset, count, searchTerm))
            return
        }

        fireCall(marvelService.getCharactersList(offset, count))
    }
}