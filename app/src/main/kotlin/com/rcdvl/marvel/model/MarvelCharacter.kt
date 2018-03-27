package com.rcdvl.marvel.model

import java.io.Serializable
import java.util.*

/**
 * Created by renan on 3/17/16.
 */
data class MarvelCharacter(var id: Long, var name: String, var description: String,
                           var modified: String, var thumbnail: MarvelThumbnail,
                           var resourceURI: String, var comics: MarvelListWrapper,
                           var series: MarvelListWrapper, var stories: MarvelListWrapper,
                           var events: MarvelListWrapper, var urls: ArrayList<MarvelUrl>?) : Serializable