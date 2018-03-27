package com.rcdvl.marvel.model

import java.io.Serializable

/**
 * Created by renan on 12/5/17.
 */

data class MarvelResource(var id: Int, var title: String, var thumbnail: MarvelThumbnail?,
                          var type: String) : Serializable
